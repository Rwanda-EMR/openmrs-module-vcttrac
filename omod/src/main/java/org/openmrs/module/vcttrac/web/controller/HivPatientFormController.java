package org.openmrs.module.vcttrac.web.controller;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.module.vcttrac.util.VCTTracConstant;
import org.openmrs.module.vcttrac.util.VCTTracUtil;
import org.openmrs.web.WebConstants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by k-joseph on 18/08/2017.
 */
@Controller
public class HivPatientFormController {
    protected final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/module/vcttrac/hivPatientForm.form", method = RequestMethod.GET)
    public void renderPage(ModelMap model) {
        try {
            setupModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupModel(ModelMap model) throws Exception {
        model.addAttribute("educationLevels", VCTTracUtil.createCodedOptions(VCTTracConstant.EDUCATION_LEVEL));
        model.addAttribute("mainActivities", VCTTracUtil.createCodedOptions(VCTTracConstant.MAIN_ACTIVITY));
        model.addAttribute("civilStatus", VCTTracUtil.createCivilStatusOptions());
        model.addAttribute("todayDate", Context.getDateFormat().format(new Date()));
        model.addAttribute("location", Context.getLocationService().getLocation(VCTConfigurationUtil.getDefaultLocationId()));
        model.addAttribute("locationId", VCTConfigurationUtil.getDefaultLocationId());
        model.addAttribute("registrationEntryPoints", Context.getService(VCTModuleService.class).getAllRegistrationEntryPoints());
    }

    private boolean checkIfParameterValuesAreSet(HttpServletRequest request, List<String> params) {
        for(String p : params) {
            if(request.getParameterMap().get(p) != null && StringUtils.isBlank(((String[])request.getParameterMap().get(p))[0]))
                return false;
        }

        return true;
    }

    @RequestMapping(value = "/module/vcttrac/hivPatientForm.form", method = RequestMethod.POST)
    public String postForm(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        try {
            ClientOrPatientRegistration cOrpController = new ClientOrPatientRegistration();

            //TODO probably in future make these fields configurable
            if(!checkIfParameterValuesAreSet(request, Arrays.asList(new String[] {"nid", "codeClient", "birthdate", "familyName", "givenName", "gender", "hivTestDate"}))) {
                request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "NID, Client Code, Birthdate, HIVTestDate, Gender, Family & Given Names are required");
            } else {
                if(StringUtils.isNotBlank(request.getParameter("registrationDate")) && Context.getDateFormat().parse(request.getParameter("registrationDate")).after(new Date())) {
                    request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Registration Date can't be in the future!!!");
                } else {
                    Person p = cOrpController.saveVCTClient(request, true);

                    if (p != null)
                        request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "vcttrac.patient.hiv.saved");
                    setupModel(model);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, e.getMessage());
        }
        return "redirect:/module/vcttrac/hivPatientForm.form";
    }
}

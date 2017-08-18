package org.openmrs.module.vcttrac.web.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
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
import java.util.Date;

/**
 * Created by k-joseph on 18/08/2017.
 */
@Controller
public class HivPatientFormController {
    protected final Log log = LogFactory.getLog(getClass());

    ClientOrPatientRegistration cOrpController = new ClientOrPatientRegistration();
    @RequestMapping(value = "/module/vcttrac/hivPatientForm.form", method = RequestMethod.GET)
    public void renderPage(ModelMap model) {
        try {
            model.addAttribute("educationLevels", VCTTracUtil.createCodedOptions(VCTTracConstant.EDUCATION_LEVEL));
            model.addAttribute("mainActivities", VCTTracUtil.createCodedOptions(VCTTracConstant.MAIN_ACTIVITY));
            model.addAttribute("civilStatus", VCTTracUtil.createCivilStatusOptions());
            model.addAttribute("todayDate", Context.getDateFormat().format(new Date()));
            model.addAttribute("location", Context.getLocationService().getLocation(VCTConfigurationUtil.getDefaultLocationId()));
            model.addAttribute("locationId", VCTConfigurationUtil.getDefaultLocationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/module/vcttrac/hivPatientForm.form", method = RequestMethod.POST)
    public void postForm(ModelMap model, HttpServletRequest request, HttpServletResponse response) {
        try {
            Patient p = cOrpController.saveHIVPatient(request);

            if(p != null)
                request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "vcttrac.patient.hiv.saved");
            else
                request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, "Something went wrong, contact administration logs");
        } catch (ParseException e) {
            e.printStackTrace();
            request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, e.getMessage());
        }
    }
}

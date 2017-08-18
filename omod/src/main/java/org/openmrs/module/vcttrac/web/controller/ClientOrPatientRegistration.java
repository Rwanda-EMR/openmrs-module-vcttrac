package org.openmrs.module.vcttrac.web.controller;

import MoHOrderEntryBridge.MoHOrderEntryBridgeConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.openmrs.*;
import org.openmrs.api.*;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.util.MohTracConfigurationUtil;
import org.openmrs.module.vcttrac.VCTClient;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.module.vcttrac.util.VCTConfigurationUtil;
import org.openmrs.web.WebConstants;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import static org.openmrs.api.context.Context.getPatientService;

/**
 * Created by k-joseph on 18/08/2017.
 */
public class ClientOrPatientRegistration {
    /** Logger for this class and subclasses */
    protected final Log log = LogFactory.getLog(getClass());

    public static String PERSONATTRIBUTE_UUID_CONTACTPERSON_NAME = "99cc76cc-3914-11e7-a919-92ebcb67fe33";
    public static String PERSONATTRIBUTE_UUID_CONTACTPERSON_PHONENUMBER = "99cc76cc-3914-11e7-a919-92ebcb67fe44";

    /**
     * Auto generated method comment
     *
     * @param request
     * @return
     */
    private PersonAddress createPersonAddress(HttpServletRequest request, PersonAddress pAddress) {
        PersonAddress pa = (pAddress == null) ? new PersonAddress() : pAddress;

        try {
            if (request.getParameter("country") != null)
                pa.setCountry(request.getParameter("country").trim());
            pa.setStateProvince(request.getParameter("stateProvince").trim());
            pa.setCountyDistrict(request.getParameter("countyDistrict").trim());
            pa.setCityVillage(request.getParameter("cityVillage").trim());
            pa.setNeighborhoodCell(request.getParameter("neighborhoodCell").trim());
            pa.setAddress1(request.getParameter("address1").trim());

            if (pAddress == null) {
                pa.setCreator(Context.getAuthenticatedUser());
                pa.setDateCreated(new Date());
            }
        } catch (Exception ex) {
            log.error(">>>>VCT>>REGISTRATION>> Fail to create/update person_address : " + ex.getMessage());
            ex.printStackTrace();
            pa = null;
        }
        return pa;
    }

    /**
     * Auto generated method comment
     *
     * @param request
     * @param p
     */
    private void createPersonAttributes(HttpServletRequest request, Person p) {
        PersonService ps = Context.getPersonService();
        PersonAttribute pa_civilStatus = null, paCs = null, pa_educationLevel = null, paEl = null,
                pa_mainActivity = null, paMa = null;
        PersonAttribute phoneNumber = new PersonAttribute();
        PersonAttribute peerEducator = new PersonAttribute();
        PersonAttribute peerEducatorPhoneNumber = new PersonAttribute();
        PersonAttribute contactPerson = new PersonAttribute();
        PersonAttribute contactPersonPhoneNumber = new PersonAttribute();

        phoneNumber.setAttributeType(Context.getPersonService()
                .getPersonAttributeTypeByUuid(MoHOrderEntryBridgeConstants.PERSONATTRIBUTE_UUID_PHONENUMBER));
        phoneNumber.setValue(request.getParameter("phoneNumber"));
        peerEducator.setAttributeType(Context.getPersonService()
                .getPersonAttributeTypeByUuid(MoHOrderEntryBridgeConstants.PERSONATTRIBUTE_UUID_PEER_EDUCATOR_NAME));
        peerEducatorPhoneNumber.setAttributeType(Context.getPersonService().getPersonAttributeTypeByUuid(
                MoHOrderEntryBridgeConstants.PERSONATTRIBUTE_UUID_PEER_EDUCATOR_PHONENUMBER));
        peerEducator.setValue(request.getParameter("peerEducator"));
        peerEducatorPhoneNumber.setValue(request.getParameter("peerEducatorPhoneNumber"));
        contactPerson.setAttributeType(Context.getPersonService()
                .getPersonAttributeTypeByUuid(PERSONATTRIBUTE_UUID_CONTACTPERSON_NAME));
        contactPersonPhoneNumber.setAttributeType(Context.getPersonService().getPersonAttributeTypeByUuid(PERSONATTRIBUTE_UUID_CONTACTPERSON_PHONENUMBER));
        contactPerson.setValue(request.getParameter("contactPerson"));
        contactPersonPhoneNumber.setValue(request.getParameter("contactPersonPhoneNumber"));

        if (StringUtils.isNotBlank(phoneNumber.getValue())) {
            setBasicOpenMRSDataProperties(phoneNumber);
            p.addAttribute(phoneNumber);
        }
        if (StringUtils.isNotBlank(peerEducator.getValue())) {
            setBasicOpenMRSDataProperties(peerEducator);
            p.addAttribute(peerEducator);
        }
        if (StringUtils.isNotBlank(peerEducatorPhoneNumber.getValue())) {
            setBasicOpenMRSDataProperties(peerEducatorPhoneNumber);
            p.addAttribute(peerEducatorPhoneNumber);
        }
        if (StringUtils.isNotBlank(contactPerson.getValue())) {
            setBasicOpenMRSDataProperties(contactPerson);
            p.addAttribute(contactPerson);
        }
        if (StringUtils.isNotBlank(contactPersonPhoneNumber.getValue())) {
            setBasicOpenMRSDataProperties(contactPersonPhoneNumber);
            p.addAttribute(contactPersonPhoneNumber);
        }

        try {
            paCs = p.getAttribute(VCTConfigurationUtil.getCivilStatusAttributeTypeId());
            pa_civilStatus = (paCs == null) ? new PersonAttribute() : paCs;

            if (request.getParameter("civilStatus").compareToIgnoreCase("0") != 0) {
                if (null == paCs) {
                    pa_civilStatus = new PersonAttribute();
                    pa_civilStatus.setAttributeType(
                            ps.getPersonAttributeType(VCTConfigurationUtil.getCivilStatusAttributeTypeId()));
                    pa_civilStatus.setCreator(Context.getAuthenticatedUser());
                    pa_civilStatus.setDateCreated(new Date());
                    pa_civilStatus.setValue(request.getParameter("civilStatus"));
                    pa_civilStatus.setUuid(UUID.randomUUID().toString());

                    p.addAttribute(pa_civilStatus);
                } else {
                    pa_civilStatus.setValue(request.getParameter("civilStatus"));
                    pa_civilStatus.setDateChanged(new Date());
                    pa_civilStatus.setChangedBy(Context.getAuthenticatedUser());
                }

            }

            paEl = p.getAttribute(VCTConfigurationUtil.getEducationLevelAttributeTypeId());
            if (request.getParameter("educationLevel").compareToIgnoreCase("0") != 0) {
                if (paEl == null) {
                    pa_educationLevel = new PersonAttribute();
                    pa_educationLevel.setAttributeType(
                            ps.getPersonAttributeType(VCTConfigurationUtil.getEducationLevelAttributeTypeId()));
                    pa_educationLevel.setCreator(Context.getAuthenticatedUser());
                    pa_educationLevel.setDateCreated(new Date());
                    pa_educationLevel.setValue(request.getParameter("educationLevel"));
                    pa_educationLevel.setUuid(UUID.randomUUID().toString());

                    p.addAttribute(pa_educationLevel);
                } else {
                    pa_educationLevel = paEl;
                    pa_educationLevel.setValue(request.getParameter("educationLevel"));
                    pa_educationLevel.setDateChanged(new Date());
                    pa_educationLevel.setChangedBy(Context.getAuthenticatedUser());
                }
            }

            paMa = p.getAttribute(VCTConfigurationUtil.getMainActivityAttributeTypeId());
            pa_mainActivity = (null == paMa) ? new PersonAttribute() : paMa;

            if (request.getParameter("mainActivity").compareToIgnoreCase("0") != 0) {
                if (paMa == null) {
                    pa_mainActivity = new PersonAttribute();
                    pa_mainActivity.setAttributeType(
                            ps.getPersonAttributeType(VCTConfigurationUtil.getMainActivityAttributeTypeId()));
                    pa_mainActivity.setCreator(Context.getAuthenticatedUser());
                    pa_mainActivity.setDateCreated(new Date());
                    pa_mainActivity.setValue(request.getParameter("mainActivity"));
                    pa_mainActivity.setUuid(UUID.randomUUID().toString());

                    p.addAttribute(pa_mainActivity);
                } else {
                    pa_mainActivity.setValue(request.getParameter("mainActivity"));
                    pa_mainActivity.setDateChanged(new Date());
                    pa_mainActivity.setChangedBy(Context.getAuthenticatedUser());
                }
            }
        } catch (Exception ex) {
            log.error(">>>>VCT>>REGISTRATION>> Fail to create/update person_attributes : " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void setBasicOpenMRSDataProperties(OpenmrsData object) {
        object.setCreator(Context.getAuthenticatedUser());
        object.setDateCreated(new Date());
    }

    /**
     * Auto generated method comment
     *
     * @param request
     */
    public void saveVCTClient(HttpServletRequest request) {
        Person p;
        PersonService ps = Context.getPersonService();
        PersonName pn;
        DateFormat df = Context.getDateFormat();
        VCTClient client = new VCTClient();

        try {
            if (request.getParameter("clientId") != null) {
                client = Context.getService(VCTModuleService.class)
                        .getClientById(Integer.valueOf(request.getParameter("clientId")));
                client.getClient().getPersonName().setFamilyName(request.getParameter("familyName").trim());
                client.getClient().getPersonName().setGivenName(request.getParameter("givenName").trim());
                client.getClient().getPersonName().setMiddleName(request.getParameter("middleName").trim());
                client.getClient().setGender(request.getParameter("gender"));
                client.getClient().setBirthdate(df.parse(request.getParameter("birthdate")));

                // attributes
                createPersonAttributes(request, client.getClient());

                // address
                createPersonAddress(request, client.getClient().getPersonAddress());

                client.setRegistrationEntryPoint(request.getParameter("registrationEntryPoint"));
                if (request.getParameter("registrationDate") != null
                        && request.getParameter("registrationDate").trim().compareTo("") != 0) {
                    client.setDateOfRegistration(df.parse(request.getParameter("registrationDate")));
                }

                if (request.getParameter("location") != null
                        && request.getParameter("location").trim().compareTo("") != 0) {
                    client.setLocation(Context.getLocationService()
                            .getLocation(Integer.valueOf(request.getParameter("location"))));
                }

                if (request.getParameter("codeClient") != null
                        && request.getParameter("codeClient").trim().compareTo("") != 0) {
                    client.setCodeClient(request.getParameter("codeClient"));
                }
            } else {
                if (request.getParameter("existOrNew").compareTo("0") == 0) {
                    p = savePerson(request);

                    log.info(">>>>>>>VCT>>Client>>Registration>>Form>>>> Person created successfully !");
                } else
                    p = ps.getPerson(Integer.valueOf(request.getParameter("client")));

                client.setClient(p);
                client.setRegistrationEntryPoint(request.getParameter("registrationEntryPoint"));
                client.setCounselingObs(null);
                client.setResultObs(null);
                client.setCreatedBy(Context.getAuthenticatedUser());

                if (request.getParameter("registrationDate") != null
                        && request.getParameter("registrationDate").trim().compareTo("") != 0) {
                    client.setDateOfRegistration(df.parse(request.getParameter("registrationDate")));
                }

                if (request.getParameter("registrationDate_A") != null
                        && request.getParameter("registrationDate_A").trim().compareTo("") != 0) {
                    client.setDateOfRegistration(df.parse(request.getParameter("registrationDate_A")));
                }

                if (request.getParameter("location") != null
                        && request.getParameter("location").trim().compareTo("") != 0) {
                    client.setLocation(Context.getLocationService()
                            .getLocation(Integer.valueOf(request.getParameter("location"))));
                }

                if (request.getParameter("location_A") != null
                        && request.getParameter("location_A").trim().compareTo("") != 0) {
                    client.setLocation(Context.getLocationService()
                            .getLocation(Integer.valueOf(request.getParameter("location_A"))));
                }

                if (request.getParameter("codeClient") != null
                        && request.getParameter("codeClient").trim().compareTo("") != 0) {
                    client.setCodeClient(request.getParameter("codeClient"));
                }

                if (request.getParameter("codeClient_A") != null
                        && request.getParameter("codeClient_A").trim().compareTo("") != 0) {
                    client.setCodeClient(request.getParameter("codeClient_A"));
                }

                if (request.getParameter("nid") != null && request.getParameter("nid").trim().compareTo("") != 0) {
                    client.setNid(request.getParameter("nid"));
                }

                if (request.getParameter("input_nid") != null
                        && request.getParameter("input_nid").trim().compareTo("") != 0) {
                    // adding the NID to the client object
                    client.setNid(request.getParameter("input_nid"));

                    // creating NID patientIdentifier for the patient
                    PatientIdentifier pi = new PatientIdentifier();
                    pi.setCreator(Context.getAuthenticatedUser());
                    pi.setDateCreated(new Date());
                    pi.setIdentifier(request.getParameter("input_nid"));
                    pi.setIdentifierType(getPatientService()
                            .getPatientIdentifierType(VCTConfigurationUtil.getNIDIdentifierTypeId()));
                    pi.setLocation(
                            Context.getLocationService().getLocation(VCTConfigurationUtil.getDefaultLocationId()));

                    try {
                        Patient patient = getPatientService().getPatient(client.getClient().getPersonId());
                        patient.addIdentifier(pi);
                        getPatientService().savePatient(patient);
                    } catch (InvalidIdentifierFormatException iife) {
                        log.error(iife);
                        request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
                                "PatientIdentifier.error.formatInvalid");
                    } catch (IdentifierNotUniqueException inue) {
                        log.error(inue);
                        request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
                                "PatientIdentifier.error.notUnique");
                    } catch (DuplicateIdentifierException die) {
                        log.error(die);
                        request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
                                "PatientIdentifier.error.duplicate");
                    } catch (InsufficientIdentifiersException iie) {
                        log.error(iie);
                        request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
                                "PatientIdentifier.error.insufficientIdentifiers");
                    } catch (PatientIdentifierException pie) {
                        log.error(pie);
                        request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR,
                                "PatientIdentifier.error.general");
                    }
                }

                client.setDateCreated(new Date());
                log.info(">>>>>>>VCT>>Client>>Registration>>Form>>>> " + client.getDateOfRegistration());
            }
            VCTModuleService vms = Context.getService(VCTModuleService.class);
            vms.saveVCTClient(client);
            log.info(">>>>>>>VCT>>Client>>Registration>>Form>>>> Client created successfully !");

            request.getSession().setAttribute(WebConstants.OPENMRS_MSG_ATTR, "Form.saved");
        } catch (ConstraintViolationException cve) {
            // cseCaught = true;
            String msg = "The CODE CLIENT " + client.getCodeClient() + " is arleady in use.";
            request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, msg);
            cve.printStackTrace();
        } catch (Exception ex) {
            request.getSession().setAttribute(WebConstants.OPENMRS_ERROR_ATTR, Context.getMessageSourceService().getMessage("Form.not.saved") + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private Person savePerson(HttpServletRequest request) throws ParseException {
        Person p;
        PersonName pn;
        p = new Person();
        pn = new PersonName(request.getParameter("givenName").trim(),
                request.getParameter("middleName").trim(),
                request.getParameter("familyName").trim().toUpperCase());
        p.getNames().add(pn);
        p.setGender(request.getParameter("gender"));
        p.setBirthdate(Context.getDateFormat().parse(request.getParameter("birthdate")));

        // attributes
        createPersonAttributes(request, p);

        // address
        p.addAddress(createPersonAddress(request, null));

        // save the person
        Context.getPersonService().savePerson(p);
        return p;
    }

    public Patient saveHIVPatient(HttpServletRequest request) throws ParseException, Exception {
        Person p = savePerson(request);
        String hivConcept = Context.getAdministrationService().getGlobalProperty("reports.hivRapidTestConceptId");
        String hivPositiveConcept = Context.getAdministrationService().getGlobalProperty("rwandasphstudyreports.hivPositiveConceptId");
        Patient patient = null;

        if(p != null && StringUtils.isNotBlank(hivConcept) && StringUtils.isNotBlank(hivPositiveConcept)) {
            Context.getObsService().saveObs(createObs(Context.getConceptService().getConcept(Integer.parseInt(hivConcept)), Context.getConceptService().getConcept(Integer.parseInt(hivPositiveConcept)), new Date(), null), null);

            patient = Context.getPatientService().savePatient(new Patient(p));
            enrollPatientInProgram(patient, Context.getProgramWorkflowService().getProgram(MohTracConfigurationUtil.getHivProgramId()), new Date(), null);
        }
        return patient;
    }

    private Obs createObs(Concept concept, Object value, Date datetime, String accessionNumber) {
        Obs obs = null;

        if (concept != null) {
            obs = new Obs();
            obs.setConcept(concept);
            ConceptDatatype dt = obs.getConcept().getDatatype();
            if (dt.isNumeric()) {
                obs.setValueNumeric(Double.parseDouble(value.toString()));
            } else if (dt.isText()) {
                if (value instanceof Location) {
                    Location location = (Location) value;
                    obs.setValueText(location.getId().toString() + " - " + location.getName());
                } else if (value instanceof Person) {
                    Person person = (Person) value;
                    obs.setValueText(person.getId().toString() + " - " + person.getPersonName().toString());
                } else {
                    obs.setValueText(value.toString());
                }
            } else if (dt.isCoded()) {
                if (value instanceof Drug) {
                    obs.setValueDrug((Drug) value);
                    obs.setValueCoded(((Drug) value).getConcept());
                } else if (value instanceof ConceptName) {
                    obs.setValueCodedName((ConceptName) value);
                    obs.setValueCoded(obs.getValueCodedName().getConcept());
                } else if (value instanceof Concept) {
                    obs.setValueCoded((Concept) value);
                }
            } else if (dt.isBoolean()) {
                if (value != null) {
                    try {
                        obs.setValueAsString(value.toString());
                    } catch (ParseException e) {
                        throw new IllegalArgumentException("Unable to convert " + value + " to a Boolean Obs value", e);
                    }
                }
            } else if (ConceptDatatype.DATE.equals(dt.getHl7Abbreviation())
                    || ConceptDatatype.TIME.equals(dt.getHl7Abbreviation())
                    || ConceptDatatype.DATETIME.equals(dt.getHl7Abbreviation())) {
                Date date = (Date) value;
                obs.setValueDatetime(date);
            } else if ("ZZ".equals(dt.getHl7Abbreviation())) {
                // don't set a value
            } else {
                throw new IllegalArgumentException("concept datatype not yet implemented: " + dt.getName()
                        + " with Hl7 Abbreviation: " + dt.getHl7Abbreviation());
            }
            if (datetime != null)
                obs.setObsDatetime(datetime);
            if (accessionNumber != null)
                obs.setAccessionNumber(accessionNumber);
        }
        return obs;
    }

    private void enrollPatientInProgram(Patient patient, Program program, Date enrollmentDate, Date completionDate) {
        PatientProgram p = new PatientProgram();

        p.setPatient(patient);
        p.setProgram(program);
        p.setDateEnrolled(enrollmentDate);
        p.setDateCompleted(completionDate);
        p.setCreator(Context.getAuthenticatedUser());

        Context.getProgramWorkflowService().savePatientProgram(p);
    }
}

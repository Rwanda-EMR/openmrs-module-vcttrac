/**
 * 
 */
package org.openmrs.module.vcttrac.util;

import java.util.List;

import org.openmrs.GlobalProperty;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.util.MohTracConfigurationUtil;

/**
 * @author Yves G
 */
public class VCTConfigurationUtil {
	
	/**
	 * constructor
	 */
	private VCTConfigurationUtil() {
	}
	
	/**
	 * @return Number of entries to display per page, if not configured, 25 is the default.
	 * @throws Exception
	 */
	public static Integer getNumberOfRecordPerPage() throws Exception {
		return MohTracConfigurationUtil.getNumberOfRecordPerPage();
	}
	
	/**
	 * @return The configured default location id
	 * @throws Exception
	 */
	public static Integer getDefaultLocationId() throws Exception {
		return MohTracConfigurationUtil.getDefaultLocationId();
	}
	
	/**
	 * @return Returns true or false.<br/>
	 *         If the module is configured it returns true, otherwise it returns false.
	 */
	public static boolean isConfigured() throws Exception {
		return MohTracConfigurationUtil.isConfigured();
	}
	
	/**
	 * @return Returns the Hiv Program Id if it has been configured, otherwise, it will return -1.
	 */
	public static Integer getHivProgramId() throws Exception {
		return MohTracConfigurationUtil.getHivProgramId();
	}
	
	/**
	 * @return Returns the Pmtct Program Id if it has been configured, otherwise, it will return -1.
	 */
	public static Integer getPmtctProgramId() throws Exception {
		return MohTracConfigurationUtil.getPmtctProgramId();
	}
	
	/**
	 * @return The Civil Status attribute type id, in case it has not been configured, it will
	 *         return null.
	 */
	public static int getCivilStatusAttributeTypeId() throws Exception {
		return MohTracConfigurationUtil.getCivilStatusAttributeTypeId();
	}
	
	/**
	 * @return The Education Level attribute type id, in case it has not been configured, it will
	 *         return null.
	 */
	public static int getEducationLevelAttributeTypeId() throws Exception {
		return MohTracConfigurationUtil.getEducationLevelAttributeTypeId();
	}
	
	/**
	 * @return The Main Activity attribute type id, in case it has not been configured, it will
	 *         return null.
	 */
	public static int getMainActivityAttributeTypeId() throws Exception {
		return MohTracConfigurationUtil.getMainActivityAttributeTypeId();
	}
	
	/**
	 * @return The NID identifier type id, in case it has not been configured, it will return null.
	 */
	public static int getNIDIdentifierTypeId() throws Exception {
		return MohTracConfigurationUtil.getNIDIdentifierTypeId();
	}
	
	/**
	 * @return The TracNet identifier type id, in case it has not been configured, it will return
	 *         null.
	 */
	public static int getTRACNETIdentifierTypeId() throws Exception {
		return MohTracConfigurationUtil.getTracNetIdentifierTypeId();
	}
	
	/**
	 * @return The local health center identifier type id, in case it has not been configured, it
	 *         will return null.
	 */
	public static int getLocalHealthCenterIdentifierTypeId() throws Exception {
		return MohTracConfigurationUtil.getLocalHealthCenterIdentifierTypeId();
	}
	
	//-------------- VCT Global Properties -----------------
	
	/**
	 * @return Returns number.<br/>
	 *         If the module is configured it returns the Person relationshiptype for partners,
	 *         otherwise it returns null.
	 */
	public static Integer getPartnerRelationShipTypeId() throws Exception {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("vcttrac.relationshiptype.partners");
		
		return (gp != null && gp.getPropertyValue() != null && gp.getPropertyValue().trim().compareTo("") != 0) ? Integer.parseInt(gp.getPropertyValue()) : null;
	}
	
	//VctHivTestConstruct
	/**
	 * @return Returns an <i>int</i> representing the concept id of the HIV test construct. A
	 *         <i>null</i> value is returned if the globalproperty doesn't have a value
	 */
	public static int getVctHivTestConstructConceptId() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("vcttrac.htc.hivTestConstruct");
		return (gp != null) ? Integer.parseInt(gp.getPropertyValue()) : null;
	}
	
	/**
	 * @return Returns an <i>int</i> representing the concept id of the HIV test date. A <i>null</i>
	 *         value is returned if the globalproperty doesn't have a value
	 */
	public static int getHivTestDateConceptId() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("vcttrac.htc.hivTestDate");
		return (gp != null) ? Integer.parseInt(gp.getPropertyValue()) : null;
	}
	
	/**
	 * @return Returns an <i>int</i> representing the concept id of the result of HIV test. A
	 *         <i>null</i> value is returned if the globalproperty doesn't have a value
	 */
	public static int getResultOfHivTestConceptId() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("vcttrac.htc.resultOfHivTest");
		return (gp != null) ? Integer.parseInt(gp.getPropertyValue()) : null;
	}
	
	/**
	 * @return Returns an <i>int</i> representing the concept id of the date the result of HIV test
	 *         were received. A <i>null</i> value is returned if the globalproperty doesn't have a
	 *         value
	 */
	public static int getDateResultOfHivTestReceivedConceptId() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(
		    "vcttrac.htc.dateResultOfHivTestReceived");
		return (gp != null) ? Integer.parseInt(gp.getPropertyValue()) : null;
	}
	
	/**
	 * @return Returns a <i>list of glabal properties</i> members of the concept HIV test construct.
	 *         A <i>null</i> value is returned if the globalproperty doesn't have a value
	 */
	public static List<GlobalProperty> getHivTestConstructMembers() {
		List<GlobalProperty> gpList1 = null;
		gpList1 = Context.getAdministrationService().getGlobalPropertiesByPrefix("vcttrac.htc");
		for (GlobalProperty gp1 : Context.getAdministrationService().getGlobalPropertiesByPrefix("vcttrac.htc"))
			if (gp1.getProperty().compareToIgnoreCase("vcttrac.htc.hivTestConstruct") == 0)
				gpList1.remove(gp1);
		
		return gpList1;
	}
	
	/**
	 * @return Returns a <i>global property object</i> of the HIV test construct global property. A
	 *         <i>null</i> value is returned if the globalproperty doesn't have a value
	 */
	public static GlobalProperty getHivTestConstructGlobalPropertie() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("vcttrac.htc.hivTestConstruct");
		return gp;
	}
	
	//endOfVCTHivTestConstruct
	
	/**
	 * The concept_id of VCT Program Construct
	 * 
	 * @return vct program construct concept id
	 */
	public static int getVctProgramConstructConceptId() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("vcttrac.vpc.vctProgramConstruct");
		return (gp != null) ? Integer.parseInt(gp.getPropertyValue()) : null;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	public static int getWhyDidYouGetTestedConceptId() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("vcttrac.vpc.whyDidYouGetTested");
		return (gp != null) ? Integer.parseInt(gp.getPropertyValue()) : null;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	public static int getProgramThatOrderedTestConceptId() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("vcttrac.vpc.programThatOrderedTest");
		return (gp != null) ? Integer.parseInt(gp.getPropertyValue()) : null;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	public static int getHivTestingDoneConceptId() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("vcttrac.vpc.hivTestingDone");
		return (gp != null) ? Integer.parseInt(gp.getPropertyValue()) : null;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	public static int getNumberOfCondomsTakenConceptId() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("vcttrac.vpc.numberOfCondomTaken");
		return (gp != null) ? Integer.parseInt(gp.getPropertyValue()) : null;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	public static int getClinicalImpressionCommentConceptId() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject(
		    "vcttrac.vpc.clinicalImpressionComment");
		return (gp != null) ? Integer.parseInt(gp.getPropertyValue()) : null;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	public static List<GlobalProperty> getVctProgramConstructMembers() {
		List<GlobalProperty> gpList = Context.getAdministrationService().getGlobalPropertiesByPrefix("vcttrac.vpc");
		for (GlobalProperty gp : gpList)
			if (gp.getProperty().compareToIgnoreCase("vcttrac.vpc.vctProgramConstruct") == 0)
				gpList.remove(gp);
		
		return gpList;
	}
	
	/**
	 * Auto generated method comment
	 * 
	 * @return
	 */
	public static GlobalProperty getVctProgramConstructGlobalPropertie() {
		GlobalProperty gp = Context.getAdministrationService().getGlobalPropertyObject("vcttrac.vpc.vctProgramConstruct");
		return gp;
	}
	
	//----------------end global properties-------------------
}

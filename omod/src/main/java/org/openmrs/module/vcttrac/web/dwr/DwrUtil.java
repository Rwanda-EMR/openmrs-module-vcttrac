package org.openmrs.module.vcttrac.web.dwr;

import java.text.SimpleDateFormat;
import java.util.List;

import org.openmrs.Patient;
import org.openmrs.Person;
import org.openmrs.api.context.Context;
import org.openmrs.module.mohtracportal.util.MohTracUtil;
import org.openmrs.module.vcttrac.service.VCTModuleService;
import org.openmrs.web.dwr.PersonListItem;

public class DwrUtil {
	
	public String getPatientListInTable(String searchString, String id, int counseled) {
		PersonListItem ret = null;
		
		VCTModuleService service = Context.getService(VCTModuleService.class);
		
		List<Person> persons = service.getPeople(searchString, false, (counseled==1));
		
		StringBuilder sb = new StringBuilder("");
		sb.append("<table class='openmrsSearchTable' cellpadding=2 cellspacing=0 style='width:100%; font-size:0.8em'>");
		sb.append("<tr><td colspan='9' style='text-align:right; font-style:italic;'>"
		        + MohTracUtil.getMessage("vcttrac.result.for", null) + " &quot;" + searchString + "&quot;: "
		        + persons.size() + " " + MohTracUtil.getMessage("vcttrac.client", null) + "</tr>");
		sb.append("<tr class='oddRow'><th>#</th><th>" + MohTracUtil.getMessage("Patient.identifier", null) + "</th><th>"
		        + MohTracUtil.getMessage("PersonName.givenName", null) + "</th><th>"
		        + MohTracUtil.getMessage("PersonName.middleName", null) + "</th><th>"
		        + MohTracUtil.getMessage("PersonName.familyName", null) + "</th><th>"
		        + MohTracUtil.getMessage("vcttrac.age", null) + "</th><th>"
		        + MohTracUtil.getMessage("vcttrac.person.gender", null) + "</th><th></th><th>"
		        + MohTracUtil.getMessage("vcttrac.person.birthdate", null) + "</th></tr>");
		int i = 0;
		for (Person ps : persons) {
			i++;
			ret = new PersonListItem();
			ret.setPersonId(ps.getPersonId());
			ret.setGivenName(ps.getGivenName());
			ret.setMiddleName(ps.getMiddleName());
			ret.setFamilyName(ps.getFamilyName());
			ret.setGender(ps.getGender());
			ret.setBirthdate(ps.getBirthdate());
			ret.setBirthdateEstimated(ps.getBirthdateEstimated());
			String identifier = "";
			identifier = (ps.isPatient()) ? ((Patient) ps).getPatientIdentifier().getIdentifier() : "";
			
			String name = ((ret.getGivenName() != null) ? ret.getGivenName().trim() : "") + "&nbsp;"
			        + ((ret.getMiddleName() != null) ? ret.getMiddleName().trim() : "") + "&nbsp;"
			        + ((ret.getFamilyName() != null) ? ret.getFamilyName().trim() : "");
			
			sb.append("<tr onclick=personValues('" + ret.getPersonId() + "','" + name.replace(" ", "&nbsp;") + "','" + id + "') class='searchRow "
			        + ((i % 2 == 0) ? "oddRow" : "") + "'>");
			sb.append("<td class='searchIndex'>" + i + ".</td>");
			sb.append("<td class='patientIdentifier'>" + identifier + "</td>");
			sb.append("<td>" + ret.getGivenName() + "</td>");
			sb.append("<td>" + ret.getMiddleName() + "</td>");
			sb.append("<td>" + ret.getFamilyName() + "</td>");
			sb.append("<td style='text-align:center'>" + ((ps.getAge().intValue()>=1)?ps.getAge():"<1") + "</td>");
			if (ret.getGender().trim().compareToIgnoreCase("f") == 0)
				sb.append("<td style='text-align:center'><img src='../../images/female.gif'/></td>");
			else
				sb.append("<td style='text-align:center'><img src='../../images/male.gif'/></td>");
			sb.append("<td>" + ((ret.getBirthdateEstimated()) ? "&asymp;" : "") + "</td>");
			sb.append("<td>" + new SimpleDateFormat("dd-MMM-yyyy").format(ret.getBirthdate()) + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		
		return sb.toString();
	}
	
}

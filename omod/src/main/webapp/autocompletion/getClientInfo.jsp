
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="org.openmrs.module.vcttrac.util.VCTTracConstant"%>
<%@page import="org.openmrs.module.vcttrac.util.VCTTracUtil"%>
<%@page import="org.openmrs.PatientIdentifier"%>
<%@page import="org.openmrs.Location"%>
<%@page import="org.openmrs.PatientIdentifierType"%>
<%@page import="org.openmrs.ConceptAnswer"%>
<%@page import="org.openmrs.api.context.Context"%>
<%@page import="org.openmrs.Obs"%>
<%@page import="org.openmrs.module.vcttrac.VCTClient"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page
	import="org.openmrs.module.vcttrac.util.VCTTracAutocompleteUtils"%>
<%
	VCTTracAutocompleteUtils db = new VCTTracAutocompleteUtils();

	String query = request.getParameter("q");

	VCTClient client = db.getClientInfosForReceptionOfResult(query);
	Obs resultOfHivTest = null;
	for (Obs o : client.getResultObs().getGroupMembers()) {
		if (o.getConcept().getConceptId() == VCTConfigurationUtil.getResultOfHivTestConceptId())
			resultOfHivTest = o;
	}

	String value = ((resultOfHivTest != null) ? resultOfHivTest
			.getValueAsString(Context.getLocale()) : "");
	int hivTestResultId = ((resultOfHivTest != null) ? resultOfHivTest
			.getValueCoded().getConceptId() : 0);
	String cssClass = (hivTestResultId==VCTTracConstant.POSITIVE_CID) ? "obsValuePositive"
			: "obsValue";

	out.println("<div class='generatedClientInfo'><table style='width: 70%'><tr>");

	out.println("<td style='50%'><a href='vctClientDashboard.form?clientId="
					+ client.getClient().getPersonId()
					+ "'>"
					+ client.getClient().getPersonName()
					+ "</a></td><td style='20%'>"
					+ ((client.getClient().getGender()
							.compareToIgnoreCase("F") == 0) ? VCTTracUtil.getMessage("Patient.gender.female",null)
							: VCTTracUtil.getMessage("Patient.gender.male",null))
					+ " "
					+ ((client.getClient().getAge()<1)?"<1":client.getClient().getAge())
					+ "yrs</td><td style='30%'><span class='"
					+ cssClass + "'>" + value + "</span></td>");
	
	out.println("</tr></table></div>");
	
	int index=0;
	PatientIdentifier identifier=null;
	
	out.println("<input type='hidden' name='hivStatus' value='"+((hivTestResultId==VCTTracConstant.POSITIVE_CID)?1:0)+"'/>");
	
	if(hivTestResultId==VCTTracConstant.POSITIVE_CID){
		//client possible decision
		out.println("<div class='generatedClientInfo' id='divClientDecision'><table style='width: 70%'>");
		
		out.println("<tr><td><input type='radio' onclick='changeFormView(this.value)' name='clientDecision' id='clientEnrolled' value='1' checked='checked'/></td><td><label for='clientEnrolled'>"+VCTTracUtil.getMessage("vcttrac.result.enrolled",null)+"</label></td>");
		out.println("<td><input type='radio' onclick='changeFormView(this.value)' name='clientDecision' id='transferred' value='0'/></td><td><label for='transferred'>"+VCTTracUtil.getMessage("vcttrac.result.transferred",null)+"</label></td>");
		out.println("<td><input type='radio' onclick='changeFormView(this.value)' name='clientDecision' id='clientRefusedEnrollHivProgram' value='2'/></td><td><label for='clientRefusedEnrollHivProgram'>"+VCTTracUtil.getMessage("vcttrac.result.refusedToJoinProgram",null)+"</label></td></tr>");
		
		out.println("</table></div>");
		//end client possible decision
		
		//decision #1: transfer to another location
		out.println("<div id='transferTo_Id' style='display:none' class='generatedClientInfo'><table style='width: 70%'>");
		
		out.println("<tr><th>"+VCTTracUtil.getMessage("vcttrac.result.transferToLocation",null)+"</th>");
		out.println("<td><select name='location' id='locationId'><option value=''>--</option>");
		
		for(Location loc:Context.getLocationService().getAllLocations())
			out.println("<option value='"+loc.getDisplayString()+"'>"+loc.getDisplayString()+"</option>");
		
		out.println("</select></td><td><span id='locationError'></span></td></tr></table></div>");
		//end decision #1
		
		//decision #2: Join the program
		out.println("<div id='enroll_Id' class='generatedClientInfo'><table style='width: 70%'>");
		
		out.println("<tr><th>"+VCTTracUtil.getMessage("PatientIdentifier.identifierType",null)+"</th><th>"+VCTTracUtil.getMessage("PatientIdentifier.identifier",null)+"</th><td></td><th>"+VCTTracUtil.getMessage("PatientIdentifier.location.identifier",null)+"</th><td></td></tr>");
		int numberOfIdentifierTypeRequired=0, numberOfIdentifierType=0;
		for(PatientIdentifierType pi:Context.getPatientService().getPatientIdentifierTypes()){
			numberOfIdentifierType++;
			if(pi.getRequired() || (Context.getPatientService().getPatientIdentifierTypes().size()==numberOfIdentifierType && numberOfIdentifierTypeRequired==0)){
				numberOfIdentifierTypeRequired++;
				out.println("<tr>");
				out.println("<td><input type='hidden' name='identifierType_"+index+"' id='identifierTypeId_"+index+"' value='"+pi.getPatientIdentifierTypeId()+"'/>"+ pi.getName()+"</td>");
				if(!client.getClient().isPatient()){
					out.println("<td><input type='text' size='30' name='identifier_"+index+"' id='identifierId_"+index+"'/></td><td><span id='identifierError_"+index+"'></span></td>");
					out.println("<td><select name='identifierLocation_"+index+"' id='identifierLocationId_"+index+"'><option value=''>--</option>");
						for(Location loc:Context.getLocationService().getAllLocations())
							out.println("<option value='"+loc.getLocationId()+"' "+((loc.getLocationId().intValue()==VCTConfigurationUtil.getDefaultLocationId().intValue())?"selected='selected'":"")+">"+loc.getDisplayString()+"</option>");
					out.println("</select></td><td><span id='identifierLocationError_"+index+"'></span></td>");
				}else{
					identifier=Context.getPatientService().getPatient(client.getClient().getPersonId()).getPatientIdentifier(pi.getPatientIdentifierTypeId());
					if(identifier==null || identifier.getLocation().getDisplayString().trim().compareTo("")==0){
						out.println("<td><input type='text' size='30' name='identifier_"+index+"' id='identifierId_"+index+"'/></td><td><span id='identifierError_"+index+"'></span></td>");
						out.println("<td><select name='identifierLocation_"+index+"' id='identifierLocationId_"+index+"'><option value=''>--</option>");
							for(Location loc:Context.getLocationService().getAllLocations())
								out.println("<option value='"+loc.getLocationId()+"' "+((loc.getLocationId().intValue()==VCTConfigurationUtil.getDefaultLocationId().intValue())?"selected='selected'":"")+">"+loc.getDisplayString()+"</option>");
						out.println("</select></td><td><span id='identifierLocationError_"+index+"'></span></td>");
					}else{
						out.println("<td>"+((identifier!=null)?identifier.getIdentifier():"-")+"</td><td></td>");
						out.println("<td>"+((identifier!=null)?identifier.getLocation().getDisplayString():"-")+"</td><td></td>");
					}
				}
				out.println("</tr>");
				index+=1;
			}
		}
		out.println("</table></div>");
		
		out.println("<div id='visitSection_Id' class='generatedClientInfo'><table>");
	
		out.println("<tr><td>"+VCTTracUtil.getMessage("Summary.returnVisitDate",null)+"</td><td><input name='nextVisitDate' id='nextVisitDateId' type='text' size='11' onclick='showCalendar(this)' value=''/><span id='nextVisitDateError'></span></td></tr>");
		
		out.println("</table></div>");
		//end decision #2
		
	}
%>


<%@page import="org.openmrs.module.vcttrac.util.VCTConfigurationUtil"%><script>
	function changeFormView(value) {
		if (value == 0) {
			$("#transferTo_Id").show();
			$("#enroll_Id").hide();
			$("#visitSection_Id").hide();
		} else {
			$("#enroll_Id").hide();
			$("#visitSection_Id").hide();
			$("#transferTo_Id").hide();
		}
	}
</script>
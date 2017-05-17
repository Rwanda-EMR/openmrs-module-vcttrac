<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="View VCT Home Page" otherwise="/login.htm" redirect="/module/vcttrac/vctHome.htm" />

	<div style="text-align: center;">
		
		<openmrs:hasPrivilege privilege="Manage VCT/PIT Clients registration">
			<div class="menuGroup"><spring:message code="vcttrac.home.titleHome"/></div>
			
			<div class="menuLinkGroup">
				<select name="registrationEntryPoint" onchange="location = this.options[this.selectedIndex].value;">
					<option value="">Select Registration Entry Point</option>
					<option value="vctPreRegistrationCheckup.htm?type=VCT">VCT</option>
					<option value="vctPreRegistrationCheckup.htm?type=PIT">PIT</option>
					<option value="vctPreRegistrationCheckup.htm?type=MALE_CIRCUMCISION">Male Circumcision</option>
					<option value="vctPreRegistrationCheckup.htm?type=POST_EXPOSURE">Post exposure</option>
					<option value="vctPreRegistrationCheckup.htm?type=OTHER">Other</option>
				</select>
			</div>
		</openmrs:hasPrivilege>
		
		<openmrs:hasPrivilege privilege="Manage Counseling of VCT/PIT Clients">
			<div class="menuGroup"><spring:message code="vcttrac.home.counseling"/></div>
			
			<div class="menuLinkGroup">
				<a href="preCounseling.form"><div class="menuLink"><spring:message code="vcttrac.home.clientCounseling"/></div></a>
			</div>
		</openmrs:hasPrivilege>
		
		<openmrs:hasPrivilege privilege="Add VCT Client test result,Edit VCT Client test result,Manage Counseling of VCT/PIT Clients">
			<div class="menuGroup"><spring:message code="vcttrac.home.result"/></div>
			
			<div class="menuLinkGroup">
				<table>
					<tr>
						<openmrs:hasPrivilege privilege="Add VCT Client test result,Edit VCT Client test result">
							<td><a href="vctClientTest.list?page=1"><div class="menuLink"><spring:message code="vcttrac.home.test"/></div></a></td>
							<td><a href="vctClientResults.form"><div class="menuLink"><spring:message code="vcttrac.home.recording"/></div></a></td>
						</openmrs:hasPrivilege>
					</tr>
					<tr>
						<openmrs:hasPrivilege privilege="Manage Counseling of VCT/PIT Clients"><td colspan="2"><a href="vctResultReception.form"><div class="menuLink"><spring:message code="vcttrac.home.reception"/></div></a></td></openmrs:hasPrivilege>
					</tr>
				</table>
			</div>
		</openmrs:hasPrivilege>
		
		<openmrs:hasPrivilege privilege="Manage VCT Clients program enrollment">
			<div class="menuGroup"><spring:message code="vcttrac.home.program.enrollment"/></div>
			
			<div class="menuLinkGroup">
				<a href="hivProgramEnrollment.list?page=1"><div class="menuLink"><spring:message code="vcttrac.home.program.enroll"/></div></a>
			</div>
		</openmrs:hasPrivilege>
		
	</div>
	
<script>

	jQuery(document).ready(function(){
		jQuery("#vct").hover(function(){
			jQuery("#vct").addClass("menuLinkSelected");
			jQuery("#pit").removeClass("menuLinkSelected");
			jQuery("#newLinks").html("<a href='vctRegistration.form?type=vct&select=new'><div class='menuLinkA'><spring:message code='vcttrac.home.newclient'/></div></a>");
			jQuery("#selectLinks").html("<a href='vctRegistration.form?type=vct&select=choose'><div class='menuLinkA'><spring:message code='vcttrac.home.existingclient'/></div></a>");
		});

		jQuery("#pit").hover(function(){
			jQuery("#pit").addClass("menuLinkSelected");
			jQuery("#vct").removeClass("menuLinkSelected");
			jQuery("#newLinks").html("<a href='vctRegistration.form?type=pit&select=new'><div class='menuLinkA'><spring:message code='vcttrac.home.newclient'/></div></a>");
			jQuery("#selectLinks").html("<a href='vctRegistration.form?type=pit&select=choose'><div class='menuLinkA'><spring:message code='vcttrac.home.existingclient'/></div></a>");
		});
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
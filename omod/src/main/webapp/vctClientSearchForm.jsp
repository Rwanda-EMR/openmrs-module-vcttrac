<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/vcttrac/scripts/popup.js" />
<openmrs:htmlInclude file="/moduleResources/vcttrac/popup.css" />

<script src='<%= request.getContextPath()%>/dwr/interface/VCT_DWRUtil.js'></script>

<script type="text/javascript">
	function patientListInTable(item,id){
			if (item.value != null && item.value.length > 2){
				VCT_DWRUtil.getPatientListInTable(item.value,id,1, function(ret){
	
					var box = document.getElementById("resultOfSearch");
					box.innerHTML = ret
						+"<br/><openmrs:hasPrivilege privilege='Create new Client'>"
						+"<div style='text-align: left; margin-left: auto; margin-right; padding: 5px 2px;'><spring:message code='vcttrac.home.newclient'/> "
						+"<select onchange='location = this.options[this.selectedIndex].value;'><option value=''>Select Registration Entry Point</option><option value='vctPreRegistrationCheckup.htm?type=VCT'>VCT</option>"
						+ "<option value='vctPreRegistrationCheckup.htm?type=PIT'>PIT</option><option value='vctPreRegistrationCheckup.htm?type=MALE_CIRCUMCISION'>Male Circumcision</option>"
						+ "<option value='vctPreRegistrationCheckup.htm?type=POST_EXPOSURE'>Post exposure</option><option value='vctPreRegistrationCheckup.htm?type=OTHER'>Other</option></select>"
						+"</div></openmrs:hasPrivilege>";
				}); 
			}
		 }
	
	function personValues(personId,personName,id){
		window.location.href='vctClientDashboard.form?clientId='+personId;
	}
</script>

<openmrs:require privilege="View VCT Client Dashboard" otherwise="/login.htm" redirect="/module/vcttrac/findClient.htm" />

<h2><spring:message code="vcttrac.search.client"/></h2>
<br/>

<b class="boxHeader"><spring:message code="vcttrac.registration.findClient"/></b>
<div class="box">
	<table>
		<tr>
			<td><spring:message code="vcttrac.registration.clientName"/>/<spring:message code="vcttrac.registration.codeclient"/></td>
			<td><input type="text" name='n_1' id='n_1' style="width:25em" autocomplete="off" onkeyup='patientListInTable(this,1,1);'/></td>
		</tr>
	</table>
	
	<div id='resultOfSearch' style="background: whitesmoke; max-height: 400px; font-size:1em;"></div>
		
</div>

<script type="text/javascript">
	jQuery(document).ready(function(){
		jQuery("#n_1").focus();
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
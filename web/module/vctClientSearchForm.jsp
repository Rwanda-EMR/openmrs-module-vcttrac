<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/popup.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/popup.css" />

<script src='<%= request.getContextPath()%>/dwr/interface/VCT_DWRUtil.js'></script>

<script type="text/javascript">
	function patientListInTable(item,id){
			if (item.value != null && item.value.length > 2){
				VCT_DWRUtil.getPatientListInTable(item.value,id,1, function(ret){
	
					var box = document.getElementById("resultOfSearch");
					box.innerHTML = ret
						+"<br/><openmrs:hasPrivilege privilege='Create new Client'>"
						+"<div style='text-align: left; margin-left: auto; margin-right; padding: 5px 2px;'>[<spring:message code='@MODULE_ID@.home.newclient'/>"
						+" <a href='vctPreRegistrationCheckup.htm?type=vct'  title='<spring:message code='@MODULE_ID@.home.vct'/>'>"
						+"<spring:message code='@MODULE_ID@.vct'/></a> <spring:message code='@MODULE_ID@.or'/>"
						+" <a href='vctPreRegistrationCheckup.htm?type=pit' title='<spring:message code='@MODULE_ID@.home.pit'/>'><spring:message code='@MODULE_ID@.pit'/>"
						+"</a>]</div></openmrs:hasPrivilege>";
				}); 
			}
		 }
	
	function personValues(personId,personName,id){
		window.location.href='vctClientDashboard.form?clientId='+personId;
	}
</script>

<openmrs:require privilege="View VCT Client Dashboard" otherwise="/login.htm" redirect="/module/@MODULE_ID@/findClient.htm" />

<h2><spring:message code="@MODULE_ID@.search.client"/></h2>
<br/>

<b class="boxHeader"><spring:message code="@MODULE_ID@.registration.findClient"/></b>
<div class="box">
	<table>
		<tr>
			<td><spring:message code="@MODULE_ID@.registration.clientName"/>/<spring:message code="@MODULE_ID@.registration.codeclient"/></td>
			<td><input type="text" style="width:25em" autocomplete="off" value="" onKeyUp='javascript:patientListInTable(this,1,1);' name='n_1' id='n_1'/></td>
		</tr>
	</table>
	
	<div id='resultOfSearch' style="background: whitesmoke; max-height: 400px; font-size:1em;"></div>
		
</div>

<script type="text/javascript">
	$(document).ready(function(){
		$("#n_1").focus();
	});
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
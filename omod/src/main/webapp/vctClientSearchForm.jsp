<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/popup.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/popup.css" />


<openmrs:require privilege="View VCT Client Dashboard" otherwise="/login.htm" redirect="/module/@MODULE_ID@/findClient.htm" />

<h2><spring:message code="@MODULE_ID@.search.client"/></h2>
<br/>

<b class="boxHeader"><spring:message code="@MODULE_ID@.registration.findClient"/></b>
<div class="box">
	<table>
		<tr>
			<td><spring:message code="@MODULE_ID@.registration.clientName"/>/<spring:message code="@MODULE_ID@.registration.codeclient"/></td>
			<td><input type="text" name='n_1' id='n_1' style="width:25em" autocomplete="off" onkeyup='VCT_DWRUtil.patientListInTable(this,1,1);'/></td>
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
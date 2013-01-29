<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/popup.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/popup.css" />

<openmrs:require privilege="Manage Counseling of VCT/PIT Clients" otherwise="/login.htm" redirect="/module/@MODULE_ID@/counseling.form" />

<script src='<%= request.getContextPath()%>/dwr/interface/VCT_DWRUtil.js'></script>

<script type="text/javascript">
	function patientListInTable(item,id,counseled){
			if (item.value != null && item.value.length > 2){
				VCT_DWRUtil.getPatientListInTable(item.value,id,counseled, function(ret){
	
					var box = document.getElementById("resultOfSearch");
					box.innerHTML = ret;
	
				}); 
			}
		 }
	
	function personValues(identifier,personName,id){
		document.getElementById("personId_"+id).value=identifier;
		document.getElementById("personName_"+id).innerHTML=personName;
		distroyDiv(document.getElementById("n_"+id));
	}
</script>

<style>
	.disabledField{
		cursor: pointer;
		background: #E6E6E6;
	}
</style>

<script type="text/javascript">
	var $j = jQuery.noConflict();
</script>

<h2><spring:message code="@MODULE_ID@.counseling.step2.title"/></h2>

<form method="post" action="saveCounseling.htm">

<c:if test="${pci.counselingTypeId==1}">
	<c:set var="counselingType"><spring:message code="@MODULE_ID@.dashboard.typeofcounseling.individuel"/></c:set>
</c:if>
<c:if test="${pci.counselingTypeId==2}">
	<c:set var="counselingType"><spring:message code="@MODULE_ID@.dashboard.typeofcounseling.couple"/></c:set>
</c:if>

<b class="boxHeader"><spring:message code="@MODULE_ID@.counseling"/> : ${counselingType}</b>
<div class="box">
	<table>
		<tr>
			<td><input type="hidden" value="${pci.counselingTypeId}" id="nbrOfPerson"/></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td><spring:message code="Encounter.datetime" /></td>
			<td> : <b>${pci.encounterDate}</b><input name="encounterDate" value="${pci.encounterDate}" size="11" type="hidden"/></td>
			<td></td>
		</tr>
		<tr>
			<td><spring:message code="Encounter.location" /></td>
			<td> : <b>${vcttag:locationName(pci.locationId)}</b><input name="location" value="${pci.locationId}" size="5" type="hidden"/></td>
			<td></td>
		</tr>
		<tr>
			<td><spring:message code="Encounter.provider" /></td>
			<td> : <b>${providerName}</b><input name="provider" value="${pci.providerId}" size="5" type="hidden"/></td>
			<td></td>
		</tr>
		<tr>
			<td><spring:message code="@MODULE_ID@.dashboard.typeofcounseling" /></td>
			<td> : <b>${counselingType}</b><input name="counselingType" value="${pci.counselingTypeId}" size="5" type="hidden"/></td>
			<td></td>
		</tr>
	</table>
</div>
<br/>

<b class="boxHeader"><spring:message code="@MODULE_ID@.counseling" /> : ${counselingType}</b>
<div class="box">

	<div id="errorDivId" style="margin-bottom: 5px;"></div>
	<div class="pp_container" id="popup"></div>
	<table id="list_data">
		<tr>
			<th class="columnHeader">#.</th>
			<th class="columnHeader"><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span><spring:message code="@MODULE_ID@.registration.clientName"/></th>
			<th class="columnHeader"><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span><spring:message code="@MODULE_ID@.counseling.reasonTested"/></th>
			<th class="columnHeader"><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span><spring:message code="@MODULE_ID@.counseling.programOrderedTest"/></th>
			<th class="columnHeader"><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span><spring:message code="@MODULE_ID@.counseling.comment"/></th>
		</tr>
		<c:forEach items="${personIds}" var="id" varStatus="status">
			<tr class="${status.count%2!=0?'even':''}">
				<td class="rowValue" style="text-align: center;">${status.count}.</td>
				<td class="rowValue" style="width: 250px;">
				
					<input type="hidden" id="personId_${id}" name="personId_${id}"/><span id="personName_${id}"></span><input class="smallButton" type='button' value='Select' name="popup_${id}" onclick="createPopUp(this,'<spring:message code='vcttrac.registration.findClient'/>','<spring:message code='vcttrac.registration.clientName'/>/<spring:message code='vcttrac.registration.codeclient'/>');" id="bt_popup_${id}"/>
				
					<span id="patientError_${id}"></span>
				</td>
				<td class="rowValue"><select name="whyTesting_${id}" id="whyTestingId_${id}">
						<option value="0">--</option>
						<c:forEach items="${reasonOfHivTest}" var="reason">
							<option value="${reason.key}">${reason.value}</option>
						</c:forEach>
					</select>
					<span id="whyTestingError_${id}"></span>
				</td>
				<td class="rowValue"><select name="refereDuService_${id}" id="refereDuServiceId_${id}">
						<option value="0">--</option>
						<c:forEach items="${programOrderers}" var="program">
							<option value="${program.key}">${program.value}</option>
						</c:forEach>
					</select>
					<span id="refereDuServiceError_${id}"></span>
				</td>
				<td class="rowValue"><textarea title="" onblur="setTitle(this);" name="comment_${id}" cols="60" rows="2"></textarea></td>
			</tr>
		</c:forEach>
	</table>
</div>
<br/>
<div style="width: 99%; margin: auto;"><input style="min-width: 150px;" name="btSave" id="btSaveId" value="<spring:message code="general.save"/>" type="button"/></div>

<c:remove var="counselingType"/>

</form>

<script type="text/javascript">

	function setTitle(obj){
		obj.title=obj.value;
	}
	
	function showHideClientCode(obj){
		if(true==obj.checked)
			enableField(obj.id);
		else 
			disableField(obj.id);
	}	
	
	function enableField(val){
		var field="codeClientId_"+val;
		document.getElementById(field).type="text";
	}
	
	function disableField(val){
		var field="codeClientId_"+val;
		document.getElementById(field).value="";
		document.getElementById(field).type="hidden";
	}

	function validateFields(){
		var valid=true;
		var max=parseInt($j("#nbrOfPerson").val());
		for(var i=1;i<=max;i++){
			
			if(document.getElementsByName("personId_"+i)[0].value==''){
				$j("#patientError_"+i).html("*");
				$j("#patientError_"+i).addClass("error");
				valid=false;
			} else {
				$j("#patientError_"+i).html("");
				$j("#patientError_"+i).removeClass("error");
			}

			if($j("#whyTestingId_"+i).val()=='0'){
				$j("#whyTestingError_"+i).html("*");
				$j("#whyTestingError_"+i).addClass("error");
				valid=false;
			} else {
				$j("#whyTestingError_"+i).html("");
				$j("#whyTestingError_"+i).removeClass("error");
			}

			if($j("#refereDuServiceId_"+i).val()=='0'){
				$j("#refereDuServiceError_"+i).html("*");
				$j("#refereDuServiceError_"+i).addClass("error");
				valid=false;
			} else {
				$j("#refereDuServiceError_"+i).html("");
				$j("#refereDuServiceError_"+i).removeClass("error");
			}

			if(!valid){
				$j("#errorDivId").html("<spring:message code='@MODULE_ID@.fillbeforesubmit'/>");
				$j("#errorDivId").addClass("error");
			} else {
				$j("#errorDivId").html("");
				$j("#errorDivId").removeClass("error");
			}
		}
		return valid;
	}
	
	$j(document).ready( function() {
		$j("#btSaveId").click(function(){
			if(validateFields()){
				if(confirm("<spring:message code='@MODULE_ID@.surewanttosave'/>"))
					this.form.submit();
			}
		});
	});

</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
	
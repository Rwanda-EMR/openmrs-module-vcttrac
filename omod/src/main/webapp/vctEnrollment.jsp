<%@ include file="template/localHeader.jsp"%>

<!-- <openmrs:htmlInclude file="/moduleResources/vcttrac/theme/demos.css" /> -->

<openmrs:htmlInclude file="/moduleResources/vcttrac/jquery.autocomplete.css" />

<openmrs:require privilege="Manage VCT Clients program enrollment" otherwise="/login.htm" redirect="/module/vcttrac/hivProgramEnrollment.list?page=1" />

<style>
	#divClientDecision{
		display: none;
	}
</style>
<input type="hidden" id="currentCodeTest" value=""/>
<input type="hidden" id="currentGender" value=""/>
<div id="list_container" style="width: 95%">
	
	<div id="list_title">
		<div class="list_title_msg">${title}</div>
		<div class="list_title_bts">
			<openmrs:hasPrivilege privilege="Export Collective Patient Data">
				<form style="display: inline;" action="hivProgramEnrollment.list?page=1&export=csv" method="post">
					<input type="submit" class="list_exportBt" title="<spring:message code="vcttrac.tablelist.exportToCSV"/>" value="<spring:message code="vcttrac.tablelist.CSV"/>"/>
				</form>
			</openmrs:hasPrivilege>			
		</div>
		<div style="clear:both;"></div>
	</div>

	<table id="list_data">
		<tr>
			<th class="columnHeader"><spring:message code="vcttrac.export.column.registrationdate"/></th>
			<th class="columnHeader"><spring:message code="vcttrac.export.column.number"/></th>
			<th class="columnHeader"><spring:message code="vcttrac.export.column.clientcode"/></th>
			<openmrs:hasPrivilege privilege="View Patient Names">
				<th class="columnHeader"><spring:message code="vcttrac.export.column.names"/></th>
			</openmrs:hasPrivilege>
			<th class="columnHeader"><spring:message code="vcttrac.person.gender"/></th>
			<th class="columnHeader"><spring:message code="vcttrac.age"/></th>
			<openmrs:hasPrivilege privilege="View VCT Client result">
				<th class="columnHeader"><spring:message code="vcttrac.result.resulthivtest"/></th>
				<th class="columnHeader"></th>
			</openmrs:hasPrivilege>
		</tr>
		<c:if test="${empty clients}">
			<tr>
				<td colspan="7" style="text-align: center;"><spring:message code="vcttrac.tablelist.empty"/></td>
			</tr>
		</c:if>
		<c:set value="0" var="index"/>
		<c:forEach items="${clients}" var="client" varStatus="status">
			<tr class="">
				<c:choose>
				  <c:when test="${client.dateOfRegistration == currentDate}">
				   	<td class="rowValue" <c:if test="${index%2!=0}">style="background-color: whitesmoke;"</c:if>><c:if test="${client.dateOfRegistration!=currentDate}"><openmrs:formatDate date="${client.dateOfRegistration}" type="medium"/><c:set value="${client.dateOfRegistration}" var="currentDate"/></c:if></td>
				  </c:when>
				  <c:otherwise>
				  	<c:set value="${index+1}" var="index"/>
				   	<td class="rowValue" style="border-top: 1px solid cadetblue; <c:if test="${index%2!=0}">background-color: whitesmoke;</c:if>"><c:if test="${client.dateOfRegistration!=currentDate}"><openmrs:formatDate date="${client.dateOfRegistration}" type="medium"/><c:set value="${client.dateOfRegistration}" var="currentDate"/></c:if></td>
				  </c:otherwise>
				</c:choose>
				<!-- <td class="rowValue"><openmrs:formatDate date="${client.dateOfRegistration}" type="medium" /></td> -->
				<td class="rowValue ${status.count%2!=0?'even':''}">${((param.page-1)*pageSize)+status.count}.</td>
				<td class="rowValue ${status.count%2!=0?'even':''}">${client.codeClient}</td>
				<openmrs:hasPrivilege privilege="View Patient Names">
					<td class="rowValue ${status.count%2!=0?'even':''}">${client.client.personName}</td>
				</openmrs:hasPrivilege>
				<td class="rowValue ${status.count%2!=0?'even':''}"><img border="0"
					src="<c:if test="${client.client.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${client.client.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
				<td class="rowValue ${status.count%2!=0?'even':''}">${(client.client.age<1)?'<1':client.client.age} <spring:message code="vcttrac.dashboard.yrs"/></td>
				<openmrs:hasPrivilege privilege="View VCT Client result">
					<td class="rowValue ${status.count%2!=0?'even':''}"><span class="${vcttag:convsetObsValueByConcept(client.resultObs, resultOfHivTestId)==positiveString?'lastObsValuePositive':'lastObsValue'}">${vcttag:convsetObsValueByConcept(client.resultObs, resultOfHivTestId)}</span></td>
					<td class="rowValue ${status.count%2!=0?'even':''}"><input type="hidden" id="${status.count}" value="${client.codeTest}"/><input onclick="setCodeTestAndShowDialog(${status.count},'${client.client.gender}');jQuery('#current-client-id').val(${client.tracVctClientId});" type="submit" value="<spring:message code='vcttrac.enrollment.enroll'/>"/></td>
				</openmrs:hasPrivilege>
			</tr>
		</c:forEach>
	</table>
	<div id="list_footer">
		<div class="list_footer_info">${pageInfos}</div>
		<div class="list_footer_pages">		
			<table>
				<tr>
					<c:if test="${prevPage!=-1}">
						<td width="100px" class="" style="padding:1px 2px 1px 2px; vertical-align: text-top;">
							<a href="hivProgramEnrollment.list?page=1"><div class="list_pageNumber" style="text-align: center;">|&lt; <spring:message code="vcttrac.navigation.first"/></div></a>
						</td>
						<td width="100px" class="" style="padding:1px 2px 1px 2px; vertical-align: text-top;"><a href="hivProgramEnrollment.list?page=${prevPage}">
							<div class="list_pageNumber" style="text-align: center;">&lt;&lt; <spring:message code="vcttrac.navigation.previous"/></div></a>
						</td>
					</c:if>
					<c:if test="${nextPage!=-1}">
						<td width="100px" class="" style="padding:1px 2px 1px 2px; vertical-align: text-top;">
							<a href="hivProgramEnrollment.list?page=${nextPage}"><div class="list_pageNumber" style="text-align: center;"><spring:message code="vcttrac.navigation.next"/> &gt;&gt;</div></a>
						</td>
						<td width="100px" class="" style="padding:1px 2px 1px 2px; vertical-align: text-top;">
							<a href="hivProgramEnrollment.list?page=${lastPage}"><div class="list_pageNumber" style="text-align: center;"><spring:message code="vcttrac.navigation.last"/> |&gt;</div></a>
						</td>
					</c:if>
				</tr>
			</table>		
		</div>
		<div style="clear:both"></div>
	</div>
</div>

<div id="divDlg"></div>
<div id="dlgCtnt" style="display: none;"></div>
<a style="display: none;" href="#" id="load">Load</a>
<input type="hidden" id="locsList" value="<select name='encounterLocation' id='location'><c:forEach items='${locations}' var='loc'><option value='${loc.key}' <c:if test="${loc.key==defaultLocationId}">selected=selected</c:if>>${loc.value}</option></c:forEach></select>"/>
<input type="hidden" id="provsList" value="<select name='provider' id='provider'><c:forEach items='${providers}' var='prov'><option value='${prov.key}' <c:if test="${prov.key==defaultProviderId}">selected=selected</c:if>>${prov.value}</option></c:forEach></select>"/>

<script type="text/javascript">

function setCodeTestAndShowDialog(id,gender){
	distroyResultDiv();
	jQuery("#currentCodeTest").val(jQuery("#"+id).val());
	jQuery("#currentGender").val(gender);
	getClientInfo();
	showDialog();
}

function showDialog(){
	jQuery("#divDlg").html("<div id='dialog' style='font-size: 0.9em;' title='<spring:message code='vcttrac.enrollment.hiv'/>'><p><div id='result'></div></p></div>");
	jQuery("#dialog").dialog({
		zIndex: 980,
		bgiframe: true,
		height: 390,
		width: 808,
		modal: true
	});	
}

function distroyResultDiv(){
	while(document.getElementById("dialog")){
		var DIVtoRemove = document.getElementById("dialog");
		DIVtoRemove.parentNode.removeChild(DIVtoRemove);
	}
}

jQuery("#load").click(function(){
	var url='autocompletion/getClientInfo.htm?q='+jQuery("#currentCodeTest").val();
	jQuery("#currentGender").val((jQuery('#currentGender').val()=='F')?"<input type='checkbox' name='enroll_in_pmtct' id='enroll_in_pmtct'/><label for='enroll_in_pmtct'><spring:message code='vcttrac.enroll.program.pmtct'/></label>":"");

	jQuery.get(url, function(data) {
		  jQuery('#result').html("<div id='errorDivId' style='margin-bottom: 5px;'></div><form id='formEnrollment' action='hivProgramEnrollment.list?page=1&code="+jQuery("#currentCodeTest").val()+"' method='post'>"+data
				  +"<div class='generatedClientInfo'><table>"
				  +"<tr><td><spring:message code='vcttrac.enrollment.date'/></td><td><input name='enrollmentDate' id='enrollmentDate' type='text' size='11' onclick='showCalendar(this)' value=''/><span id='enrollmentDateError'></span></td><td></td><td>"+jQuery("#currentGender").val()+"</td></tr>"
				  +"<tr><td><spring:message code='Encounter.provider' /></td><td>"+jQuery("#provsList").val()+"</td><td><span id='providerError'></span></td><td></td></tr>"
				  +"<tr><td><spring:message code='Encounter.location' /></td><td>"+jQuery("#locsList").val()+"</td><td><span id='locationError'></span></td><td></td></tr></table></div>"
				  +"<br/><input type='button' onclick='submitForm();' value='<spring:message code='vcttrac.enrollment.enroll'/>'/></form>");
		  jQuery('#result').addClass("clientInfo");
	});				
});

function submitForm(){
	if(validateFields()){
		if(confirm("<spring:message code='vcttrac.surewanttosave'/>"))
			document.getElementById("formEnrollment").submit();
	}
}

function getClientInfo(){
	jQuery("#load").click();	
}

function validateFields(){
	var valid=true;
	if(jQuery("#enrollmentDate").val()==''){
		jQuery("#enrollmentDateError").html("*");
		jQuery("#enrollmentDateError").addClass("error");
		valid=false;
	}  else {
		jQuery("#enrollmentDateError").html("");
		jQuery("#enrollmentDateError").removeClass("error");
	}

	if(jQuery("#nextVisitDateId").val()==''){
		jQuery("#nextVisitDateError").html("*");
		jQuery("#nextVisitDateError").addClass("error");
		valid=false;
	} else {
		jQuery("#nextVisitDateError").html("");
		jQuery("#nextVisitDateError").removeClass("error");
	}

	var cont=true;
	var index=0;
	while(cont){

		if(cont && document.getElementById("identifierTypeId_"+index)!=null){
			if(jQuery("#identifierId_"+index).val()==''){
				jQuery("#identifierError_"+index).html("*");
				jQuery("#identifierError_"+index).addClass("error");
				valid=false;
			} else {
				jQuery("#identifierError_"+index).html("");
				jQuery("#identifierError_"+index).removeClass("error");
			}

			if(null!=document.getElementById("identifierLocationId_"+index) && document.getElementById("identifierLocationId_"+index).value==''){
				jQuery("#identifierLocationError_"+index).html("*");
				jQuery("#identifierLocationError_"+index).addClass("error");
				valid=false;
			} else {
				jQuery("#identifierLocationError_"+index).html("");
				jQuery("#identifierLocationError_"+index).removeClass("error");
			}
		} else{
			cont=false;
		}
		index++;
	}

	if(!valid){
		jQuery("#errorDivId").html("<spring:message code='vcttrac.fillbeforesubmit'/>");
		jQuery("#errorDivId").addClass("error");
	} else {
		jQuery("#errorDivId").html("");
		jQuery("#errorDivId").removeClass("error");
	}

	return valid;
}

</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
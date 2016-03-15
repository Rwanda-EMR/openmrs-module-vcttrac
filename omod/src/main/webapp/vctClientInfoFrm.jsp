<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/jquery.bgiframe.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/ui/ui.core.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/ui/ui.dialog.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/ui/ui.draggable.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/ui/ui.resizable.js" />

<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/theme/ui.all.css" />

<openmrs:require privilege="Edit VCT Client test result" otherwise="/login.htm" redirect="/module/@MODULE_ID@/vctClientTest.list?page=1" />

<script type="text/javascript">
var $j = jQuery.noConflict();
</script>

<!-- <div style="width: 74%; float: right; border: 1px solid #8FABC7; margin-left: 5px; -moz-border-radius: 3px; padding: 5px;"> -->
	<div id="list_container" style="width: 97%">
	
	<div id="list_title">
		<div class="list_title_msg">${title}</div>
		<div class="list_title_bts">
			<openmrs:hasPrivilege privilege="Export Collective Patient Data">
				<form style="display: inline;" action="vctClientTest.list?page=1&export=csv" method="post">
					<input type="submit" class="list_exportBt" title="<spring:message code="vcttrac.tablelist.exportToCSV"/>" value="<spring:message code="vcttrac.tablelist.CSV"/>"/>
				</form>
			</openmrs:hasPrivilege>				
		</div>
		<div style="clear:both;"></div>
	</div>

	<table id="list_data">
		<tr>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.export.column.registrationdate"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.export.column.number"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.export.column.clientcode"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.result.testcode"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.export.column.gender"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.age"/></th>
			<openmrs:hasPrivilege privilege="View VCT Client result">
				<th class="columnHeader"><spring:message code="@MODULE_ID@.result.resulthivtest"/></th>
				<th class="columnHeader"></th>
			</openmrs:hasPrivilege>
			<!-- <th class="columnHeader"></th> -->
		</tr>
		<c:if test="${empty clients}">
			<tr>
				<td colspan="8" style="text-align: center;"><spring:message code="vcttrac.tablelist.empty"/></td>
			</tr>
		</c:if>		
		<c:set value="0" var="index"/>
		<c:forEach items="${clients}" var="client" varStatus="status">
			<tr>
				<c:choose>
				  <c:when test="${client.dateOfRegistration == currentDate}">
				   	<td class="rowValue" <c:if test="${index%2!=0}">style="background-color: whitesmoke;"</c:if>><c:if test="${client.dateOfRegistration!=currentDate}"><openmrs:formatDate date="${client.dateOfRegistration}" type="medium"/><c:set value="${client.dateOfRegistration}" var="currentDate"/></c:if></td>
				  </c:when>
				  <c:otherwise>
				  	<c:set value="${index+1}" var="index"/>
				   	<td class="rowValue" style="border-top: 1px solid cadetblue; <c:if test="${index%2!=0}">background-color: whitesmoke;</c:if>"><c:if test="${client.dateOfRegistration!=currentDate}"><openmrs:formatDate date="${client.dateOfRegistration}" type="medium"/><c:set value="${client.dateOfRegistration}" var="currentDate"/></c:if></td>
				  </c:otherwise>
				</c:choose>
				<td class="rowValue ${status.count%2!=0?'even':''}">${((param.page-1)*pageSize)+status.count}.</td>
				<td class="rowValue ${status.count%2!=0?'even':''}">${client.codeClient}</td>
				<td class="rowValue ${status.count%2!=0?'even':''}"><c:if test="${client.codeTest ne null}"><img title="<spring:message code="@MODULE_ID@.result.clientcode.edit.title"/>" border="0" src="<openmrs:contextPath/>/images/edit.gif" onclick="editCodeTest(${client.tracVctClientId},'${client.codeTest}');"/>&nbsp;${client.codeTest}</c:if><c:if test="${client.codeTest==null}"><span class="lastObsValue">-</span></c:if></td>
				<td class="rowValue ${status.count%2!=0?'even':''}"><img border="0"
					src="<c:if test="${client.client.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${client.client.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
				<td class="rowValue ${status.count%2!=0?'even':''}">${(client.client.age<1)?'<1':client.client.age} <spring:message code="@MODULE_ID@.dashboard.yrs"/></td>
				<openmrs:hasPrivilege privilege="View VCT Client result">
					<td class="rowValue ${status.count%2!=0?'even':''}">
						<span class="${vcttag:convsetObsValueByConcept(client.resultObs, resultOfHivTest)==positiveString?'lastObsValuePositive':'lastObsValue'}">${vcttag:convsetObsValueByConcept(client.resultObs, resultOfHivTest)}</span>
						<c:if test="${client.resultObs ne null}">&nbsp;<spring:message code="@MODULE_ID@.date.on"/>&nbsp;<openmrs:formatDate date="${client.resultObs.obsDatetime}" type="medium" /></c:if>
					</td>
					<td class="rowValue ${status.count%2!=0?'even':''}"><c:if test="${client.codeTest==null}"><input type="button" onclick="addCodeTest(${client.tracVctClientId},'${client.codeClient}', '<openmrs:formatDate date='${client.dateOfRegistration}' type='textbox'/>');" value="<spring:message code="@MODULE_ID@.test.add"/>"/></c:if>
						<c:if test="${client.codeTest ne null && vcttag:convsetObsValueByConcept(client.resultObs, resultOfHivTest)=='-'}"><a href="vctClientResults.form?tcode=${client.codeTest}"><spring:message code="@MODULE_ID@.result.add"/></a></c:if>
					</td>
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
							<a href="vctClientTest.list?page=1"><div class="list_pageNumber" style="text-align: center;">|&lt; <spring:message code="@MODULE_ID@.navigation.first"/></div></a>
						</td>
						<td width="100px" class="" style="padding:1px 2px 1px 2px; vertical-align: text-top;"><a href="vctClientTest.list?page=${prevPage}">
							<div class="list_pageNumber" style="text-align: center;">&lt;&lt; <spring:message code="@MODULE_ID@.navigation.previous"/></div></a>
						</td>
					</c:if>
					<td>
						<span class="list_pageNumberCurrent">${param.page}</span>
					</td>
					<c:if test="${nextPage!=-1}">
						<td width="100px" class="" style="padding:1px 2px 1px 2px; vertical-align: text-top;">
							<a href="vctClientTest.list?page=${nextPage}"><div class="list_pageNumber" style="text-align: center;"><spring:message code="@MODULE_ID@.navigation.next"/> &gt;&gt;</div></a>
						</td>
						<td width="100px" class="" style="padding:1px 2px 1px 2px; vertical-align: text-top;">
							<a href="vctClientTest.list?page=${lastPage}"><div class="list_pageNumber" style="text-align: center;"><spring:message code="@MODULE_ID@.navigation.last"/> &gt;|</div></a>
						</td>
					</c:if>
				</tr>
			</table>		
		</div>
		<div style="clear:both"></div>
	</div>
</div>

<!-- </div>

<div style="clear: both;"></div> -->

<div id="divDlg"></div>

<script type="text/javascript">

	function submitForm(){
		if(validateFormFields()){
			if(confirm("<spring:message code='@MODULE_ID@.surewanttosave'/>"))
				document.getElementById("formNewTest").submit();
		}
	}

	function submitFormEditClientTestCode(){
		if(validateEditFormFields()){
			if(confirm("<spring:message code='@MODULE_ID@.surewanttosave'/>"))
				document.getElementById("formEditTestCode").submit();
		}
	}
	
	function showDialog(){
		$j("#dialog").dialog({
			zIndex: 980,
			bgiframe: true,
			height: 190,
			width: 545,
			modal: true
		});	
	}

	function initDialogContent(id,codeClient,dateOfRegistration){
		$j("#divDlg").html("<div id='dialog' style='font-size: 0.9em;' title='<spring:message code='@MODULE_ID@.tablelist.newClientTest'/>'><p><div><div id='errorDivId' style='margin-bottom: 5px;'></div><form id='formNewTest' action='vctClientTest.list?page=1&save' method='post'>"
			+"<table><tr><td></td><td></td><td><input type='hidden' id='clientId' name='clientId' value='"+id+"'/></td><td></td></tr>"
			+"<tr><td><spring:message code='@MODULE_ID@.registration.codeclient'/></td><td><span class='displayHelp'><img border='0' src='<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif' title='<spring:message code='@MODULE_ID@.help'/>'/></span></td><td><input readonly='readonly' type='text' name='clientCode' id='clientCodeId' value='"+codeClient+"'/></td><td><span id='clientCodeError'></span></td></tr>"
			+"<tr><td><spring:message code='@MODULE_ID@.result.clientcode'/></td><td><span class='displayHelp'><img border='0' src='<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif' title='<spring:message code='@MODULE_ID@.help'/>'/></span></td><td><input type='text' name='testCode' id='testCodeId' value='"+codeClient+"'/></td><td><span id='testCodeError'></span></td></tr>"
			+"<tr><td><spring:message code='Encounter.datetime'/></td><td><span class='displayHelp'><img border='0' src='<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif' title='<spring:message code='@MODULE_ID@.help'/>'/></span></td><td><input type='text' name='obsDate' id='obsDateId' value='"+dateOfRegistration+"' size='11' onclick='showCalendar(this);'/></td><td><span id='obsDateError'></span></td></tr>"
			+"<tr><td></td><td></td><td><input type='button' id='btSave' value='<spring:message code='general.save' />' onclick='submitForm();'/></td></tr></table></form></div></p></div>");
	}

	function editClientCodeTest(id,clientTestCode){
		$j("#divDlg").html("<div id='dialog' style='font-size: 0.9em;' title='<spring:message code='@MODULE_ID@.tablelist.editClientTest'/>'><p><div><div id='errorDivId' style='margin-bottom: 5px;'></div><form id='formEditTestCode' action='vctClientTest.list?page=1&edit' method='post'>"
			+"<table><tr><td></td><td></td><td><input type='hidden' id='clientCodeId' name='clientCode' value='"+id+"'/></td><td></td></tr>"
			+"<tr><td><spring:message code='@MODULE_ID@.result.clientcode'/></td><td><span class='displayHelp'><img border='0' src='<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif' title='<spring:message code='@MODULE_ID@.help'/>'/></span></td><td><input type='text' name='testCode' id='testCodeId' value='"+clientTestCode+"'/></td><td><span id='testCodeError'></span></td></tr>"
			+"<tr><td></td><td></td><td><input type='button' id='btSave' value='<spring:message code='general.save' />' onclick='submitFormEditClientTestCode();'/></td></tr></table></form></div></p></div>");
	}

	function addCodeTest(id,codeClient,dateOfRegistration){
		initDialogContent(id,codeClient,dateOfRegistration);
		showDialog();
		document.getElementById("testCodeId").focus();
	}

	function editCodeTest(id,clientTestCode){
		editClientCodeTest(id,clientTestCode);
		showDialog();
		document.getElementById("testCodeId").focus();
	}

	function validateFormFields(){
		var valid=true;
		if($j("#testCodeId").val()==''){
			$j("#testCodeError").html("*");
			$j("#testCodeError").addClass("error");
			valid=false;
		} else {
			$j("#testCodeError").html("");
			$j("#testCodeError").removeClass("error");
		}

		if($j("#obsDateId").val()==''){
			$j("#obsDateError").html("*");
			$j("#obsDateError").addClass("error");
			valid=false;
		} else {
			$j("#obsDateError").html("");
			$j("#obsDateError").removeClass("error");
		}

		if($j("#clientCodeId").val()==''){
			$j("#clientCodeError").html("*");
			$j("#clientCodeError").addClass("error");
			valid=false;
		} else {
			$j("#clientCodeError").html("");
			$j("#clientCodeError").removeClass("error");
		}

		if(!valid){
			$j("#errorDivId").html("<spring:message code='@MODULE_ID@.fillbeforesubmit'/>");
			$j("#errorDivId").addClass("error");
		} else {
			$j("#errorDivId").html("");
			$j("#errorDivId").removeClass("error");
		}
		
		return valid;
	}

	function validateEditFormFields(){
		var valid=true;
		if($j("#testCodeId").val()==''){
			$j("#testCodeError").html("*");
			$j("#testCodeError").addClass("error");
			valid=false;
		} else {
			$j("#testCodeError").html("");
			$j("#testCodeError").removeClass("error");
		}

		if(!valid){
			$j("#errorDivId").html("<spring:message code='@MODULE_ID@.fillbeforesubmit'/>");
			$j("#errorDivId").addClass("error");
		} else {
			$j("#errorDivId").html("");
			$j("#errorDivId").removeClass("error");
		}
		
		return valid;	
	}
	
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
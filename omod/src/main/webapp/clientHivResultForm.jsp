<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/jquery.bgiframe.js" />

<!-- <openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/theme/demos.css" /> -->

<%@ taglib prefix="springform" uri="/WEB-INF/taglibs/spring-form.tld" %>

<openmrs:require privilege="Edit VCT Client test result" otherwise="/login.htm" redirect="/module/@MODULE_ID@/vctClientResults.form" />

<style>
	input {
    	font-size: 100%;
    }
</style>

<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/jquery.autocomplete.js" />


<div style="width: 90%; margin-left: auto; margin-right: auto;">

<h2><spring:message code="@MODULE_ID@.home.result"/></h2>

<span style="position: absolute; margin-top: -28px; margin-right: 5px; right: 75px; font-style: italic; font-size: 10px;">
	<form action="vctClientResults.form" method="get">
		<table>
			<tr>
				<td><spring:message code="@MODULE_ID@.result.clientcode"/></td>
				<td><input type="text" name="testCode" value="${param.testCode}"/></td>
				<td><input type="submit" value="Search"/></td>
			</tr>
		</table>
	</form>
</span>

<div class="left">
	<b class="boxHeader"><spring:message code="@MODULE_ID@.result.testcode"/></b>
	<div class="box">
		<c:forEach items="${clientCodes}" var="code" varStatus="status">
			<span title="${code}" id="clientCode_${status.count}" onclick="changeValue(this);" class="clientCode <c:if test="${param.tcode!=code}">highLight</c:if>">${code}</span>
		</c:forEach>
		<c:if test="${empty clientCodes}"><i><spring:message code="@MODULE_ID@.result.noclientcodefound"/></i></c:if>
	</div>
</div>

<div class="right">
	<b class="boxHeader"><spring:message code="@MODULE_ID@.result.resulthivtest"/></b>
	<springform:form cssClass="box" commandName="result" method="post">
		<table>
			<tr>
				<td><spring:message code="Encounter.datetime" /></td>
				<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
				<td><spring:bind path="dateOfResult"><input id="encounterDate" name="${status.expression}" size="11" type="text" onclick="showCalendar(this)" value="${status.value}"/></spring:bind></td>
				<td><form:errors cssClass="error" path="dateOfResult" /></td>
			</tr>
			<tr>
				<td><spring:message code="Encounter.location" /></td>
				<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
				<td><spring:bind path="location"><openmrs_tag:locationField formFieldName="${status.expression}" initialValue="${status.value}" /></spring:bind></td>
				<td><form:errors cssClass="error" path="location" /></td>
			</tr>
			<tr>
				<td><spring:message code="@MODULE_ID@.result.clientcode"/></td>
				<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
				<td><spring:bind path="codeTest"><input readonly="readonly" type="text" id="clientCode" name="${status.expression}" value="${status.value}"/></spring:bind>
					<c:if test="${hivTestResultObsId!=0}"><!-- ${param.testCode!=null} -->
						<openmrs:hasPrivilege privilege="Edit VCT HIV Test Code">
							<input class="list_exportBt" type="button" onclick="editCodeTest('${param.testCode}');" value="<spring:message code='@MODULE_ID@.result.testcode.edit'/>"/>
						</openmrs:hasPrivilege>
					</c:if>
				</td>
				<td><form:errors cssClass="error" path="codeTest" /></td>
			</tr>
			<tr>
				<td><spring:message code="@MODULE_ID@.result.resulthivtest"/></td>
				<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
				<td><spring:bind path="hivTestResult"><select name="${status.expression}">
					<option value="0">--</option>
					<c:forEach items="${resultOfHivTestOptions}" var="option">
						<option value="${option.key}" <c:if test="${option.key==status.value}">selected='selected'</c:if>>${option.value}</option>
					</c:forEach>
				</select></spring:bind>
				</td>
				<td><form:errors cssClass="error" path="hivTestResult" /></td>
			</tr>
			<c:if test="${hivTestResultObsId!=0}"><!-- ${param.testCode!=null} -->
				<tr>
					<td><spring:message code="general.createdBy"/></td>
					<td></td>
					<td><spring:bind path="createdBy">${vcttag:personName(status.value)}</spring:bind>&nbsp;-&nbsp;<spring:bind path="dateCreated">${status.value}</spring:bind></td>
					<td></td>
				</tr>				
			</c:if>
			<c:if test="${hivTestResultObsId==0}"><!--${!resultReceived} , ${param.testCode==null} -->
				<tr>
					<td></td>
					<td></td>
					<td><input type="button" id="btSave" value="<spring:message code="general.save"/>"/></td>
					<td></td>
				</tr>
			</c:if>
		</table>	
	</springform:form>

</div>

<div style="clear: both;">
</div>

</div>

<div id="divDlg"></div>

	<script>
		jQuery(document).ready(function(){
			jQuery("#clientCode").autocomplete("autocompletion/getClientCode.htm");
			jQuery("#btSave").click(function(){
				if(confirm("<spring:message code='@MODULE_ID@.surewanttosave'/>"))
					this.form.submit();
			});
							
		});

		function changeValue(obj){
			var counter=1;
			while(document.getElementById("clientCode_"+counter)!=null){
				if(counter==parseInt(obj.id.substring(11)))
					jQuery("#clientCode_"+counter).removeClass("highLight");
				else jQuery("#clientCode_"+counter).addClass("highLight");
				counter++;
			}
			document.getElementById("clientCode").value=obj.title;
			var date=new Date();
			document.getElementById("encounterDate").value=date.getDate()+"/"+(date.getMonth()+1)+"/"+(date.getYear()+1900);
		}

		function showDialog(){
			jQuery("#dialog").dialog({
				zIndex: 980,
				bgiframe: true,
				height: 150,
				width: 545,
				modal: true
			});	
		}

		function initDialogContent(testCode){
			jQuery("#divDlg").html("<div id='dialog' style='font-size: 0.9em;' title='<spring:message code='@MODULE_ID@.result.testcode.edit'/>'><p><div><div id='errorDivId' style='margin-bottom: 5px;'></div><form id='formEditTest' action='vctClientResults.form?testCode="+testCode+"&edit' method='post'>"
				+"<table><tr><td></td><td><input type='hidden' id='testCodeToChangeId' name='testCodeToChange' value='"+testCode+"'/></td><td></td></tr>"
				+"<tr><td><spring:message code='@MODULE_ID@.result.clientcode'/></td><td><input type='text' name='editedTestCode' id='editedTestCodeId' value='"+testCode+"'/></td><td><span id='editedTestCodeError'></span></td></tr>"
				+"<tr><td></td><td><input type='button' id='btSave' value='<spring:message code='general.save' />' onclick='submitForm();'/></td></tr></table></form></div></p></div>");
		}

		function editCodeTest(testCode){
			initDialogContent(testCode);
			showDialog();
		}

		function submitForm(){
			if(validateFormFields()){
				if(confirm("<spring:message code='@MODULE_ID@.surewanttosave'/>"))
					document.getElementById("formEditTest").submit();
			}
		}

		function validateFormFields(){
			var valid=true;
			if(jQuery("#editedTestCodeId").val()==''){
				jQuery("#editedTestCodeError").html("*");
				jQuery("#editedTestCodeError").addClass("error");
				valid=false;
			} else {
				jQuery("#editedTestCodeError").html("");
				jQuery("#editedTestCodeError").removeClass("error");
			}

			if(!valid){
				jQuery("#errorDivId").html("<spring:message code='@MODULE_ID@.fillbeforesubmit'/>");
				jQuery("#errorDivId").addClass("error");
			} else {
				jQuery("#errorDivId").html("");
				jQuery("#errorDivId").removeClass("error");
			}
			
			return valid;
		}
		
	</script>
	
<%@ include file="/WEB-INF/template/footer.jsp"%>
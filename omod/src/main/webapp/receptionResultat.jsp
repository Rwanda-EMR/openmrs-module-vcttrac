<%@ include file="template/localHeader.jsp"%>
<openmrs:require privilege="Manage Counseling of VCT/PIT Clients" otherwise="/login.htm" redirect="/module/vcttrac/vctResultReception.form" />

	<style>
		input {
	    	font-size: 100%;
	    }
	    
	    #enroll_Id, #visitSection_Id{
			display: none;
		}
	</style>
	
<div style="width: 90%; margin-left: auto; margin-right: auto;">

<h2><spring:message code="vcttrac.result.receptionofresult"/></h2>

<div class="left">
	<b class="boxHeader"><spring:message code="vcttrac.result.testcode"/></b>
	<div class="box">
		<c:forEach items="${clientCodes}" var="code" varStatus="status">
			<span title="${code}" id="clientCode_${status.count}" onclick="changeValue(this);" class="clientCode highLight">${code}</span>
		</c:forEach>
		<c:if test="${empty clientCodes}"><i><spring:message code="vcttrac.result.noclientcodefound"/></i></c:if>
	</div>
</div>

<div class="right">
	<b class="boxHeader"><spring:message code="vcttrac.result.receptionofresult"/></b>
	<form class="box" action="vctResultReception.form?save" method="post">
		<div id="errorDivId" style="margin-bottom: 5px;"></div>
		<div id="result"></div>
		<table>
			<tr>
				<td><spring:message code="vcttrac.result.clientcode"/></td>
				<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span>
				</td>
				<td><input readonly="readonly" type="text" id="clientCode" name="clientCode" /></td>
				<td><span id="clientCodeError"></span></td>
			</tr>
			<tr>
				<td><spring:message code="vcttrac.result.datetestresultreceived"/></td>
				<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span>
				</td>
				<td><input id="dateHivTestRsltRcvd" name="dateHivTestResultReceived" size="11" type="text" onclick="showCalendar(this)" value=""/></td>
				<td><span id="dateHivTestRsltRcvdError"></span></td>
			</tr>
			<tr>
				<td><spring:message code="vcttrac.counseling.numberOfCondoms"/></td>
				<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
				<td><select name="numberOfCondom">
						<option value="0">--</option>
						<c:forEach items="4,8,12,16,20,24,28,32,36,40,44,48,52,56,60,64,68,72,76,80,84,88,92,96,100" var="nbr">
							<option value="${nbr}">${nbr}</option>
						</c:forEach>
					</select></td>
			</tr>
			<tr>
				<td></td>
				<td></td>
				<td><input type="button" id="btSave" value="<spring:message code="general.save"/>" disabled/></td>
			</tr>
		</table>	
	</form>	
</div>

<div style="clear: both;"></div>

<a style="display: none;" href="#" id="load">Load</a>

</div>
	<script>
		jQuery(document).ready(function(){
			jQuery("#clientCode").autocomplete("autocompletion/getClientCodeForResult.htm");
			jQuery("#btSave").click(function(){
				if(validateFields()){
					if(confirm("<spring:message code='vcttrac.surewanttosave'/>"))
						this.form.submit();
				}
			});
			jQuery("#load").click(function(){
				jQuery("#btSave").attr("disabled", true);
				var url='autocompletion/getClientInfo.htm?q='+jQuery("#clientCode").val();
				jQuery.get(url, function(data) {
					  jQuery('#result').html(data);
					  jQuery('#result').addClass("clientInfo");
					  jQuery("#btSave").attr("disabled", false);
				});
			});
		});

		function changeValue(obj){
			clearErrors();
			
			var counter=1;
			while(document.getElementById("clientCode_"+counter)!=null){
				if(counter==parseInt(obj.id.substring(11)))
					jQuery("#clientCode_"+counter).removeClass("highLight");
				else jQuery("#clientCode_"+counter).addClass("highLight");
				counter++;
			}
			
			document.getElementById("clientCode").value=obj.title;
			var date=new Date();
			document.getElementById("dateHivTestRsltRcvd").value=date.getDate()+"/"+(date.getMonth()+1)+"/"+(date.getYear()+1900);
			jQuery("#load").click();
		}

		function validateFields(){
			var valid=true;
			if(jQuery("#clientCode").val()==''){
				jQuery("#clientCodeError").html("*");
				jQuery("#clientCodeError").addClass("error");
				valid=false;
			} else {
				jQuery("#clientCodeError").html("");
				jQuery("#clientCodeError").removeClass("error");
			}

			if(jQuery("#dateHivTestRsltRcvd").val()==''){
				jQuery("#dateHivTestRsltRcvdError").html("*");
				jQuery("#dateHivTestRsltRcvdError").addClass("error");
				valid=false;
			} else {
				jQuery("#dateHivTestRsltRcvdError").html("");
				jQuery("#dateHivTestRsltRcvdError").removeClass("error");
			}

			if(document.getElementById("transferred")){
				if(document.getElementById("transferred").checked){
					//if(document.getElementById("nextVisitDateId")!=null){
						if(jQuery("#locationId").val()==''){
							jQuery("#locationError").html("*");
							jQuery("#locationError").addClass("error");
							valid=false;
						} else {
							jQuery("#locationError").html("");
							jQuery("#locationError").removeClass("error");
						}
					//}
				}
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

		function clearErrors(){
			jQuery("#clientCodeError").html("");
			jQuery("#clientCodeError").removeClass("error");

			jQuery("#dateHivTestRsltRcvdError").html("");
			jQuery("#dateHivTestRsltRcvdError").removeClass("error");
			
			jQuery("#errorDivId").html("");
			jQuery("#errorDivId").removeClass("error");
		}
		
	</script>
	
<%@ include file="/WEB-INF/template/footer.jsp"%>
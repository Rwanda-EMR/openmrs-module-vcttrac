<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="Manage Counseling of VCT/PIT Clients" otherwise="/login.htm" redirect="/module/vcttrac/preCounseling.form" />

<script type="text/javascript">
	var $j = jQuery.noConflict();
</script>

<h2><spring:message code="vcttrac.counseling.step1.title"/></h2>

<b class="boxHeader"><spring:message code="vcttrac.counseling.step1.preliminaryinfo"/></b>
<form method="post" action="counseling.form" class="box">
	<div id="errorDivId" style="margin-bottom: 5px;"></div>
	<table>
		<tr>
			<td><spring:message code="Encounter.datetime" /></td>
			<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span>
			</td>
			<td><input name="encounterDate" id="encounterDateId" size="11" type="text" onclick="showCalendar(this)" value="${ci.encounterDate}"/></td>
			<td><span id="encounterDateError"></span></td>
		</tr>
		<tr>
			<td><spring:message code="Encounter.location" /></td>
			<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span>
			</td>
			<td><openmrs_tag:locationField formFieldName="location" initialValue="${ci.locationId}" /></td>
			<td><span id="locationError"></span></td>
		</tr>
		<tr>
			<td><spring:message code="Encounter.provider" /></td>
			<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span>
			</td>
			<td><openmrs_tag:userField roles="Provider" formFieldName="provider" initialValue="${ci.providerId}" /></td>
			<td><span id="providerError"></span></td>
		</tr>
		<tr>
			<td><spring:message code="vcttrac.dashboard.typeofcounseling" /></td>
			<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span>
			</td>
			<td>
				<input checked="checked" type="radio" name="counselingType" value="1" id="individual"/><label for="individual"><spring:message code="vcttrac.dashboard.typeofcounseling.individuel" /></label>
				<input type="radio" name="counselingType" value="2" id="couple"/><label for="couple"><spring:message code="vcttrac.dashboard.typeofcounseling.couple" /></label>
			</td>
			<td><span id="counselingTypeError"></span></td>
		</tr>
		<tr>
			<td></td>
			<td></td>
			<td><input name="btNext" id="btNextId" value="<spring:message code='vcttrac.counseling.step.next'/> &gt;&gt;" type="button"/></td>
			<td></td>
		</tr>
	</table>
</form>

<script type="text/javascript">

	$j(document).ready( function() {
		$j("#btNextId").click(function(){
			if(validateFields())
				this.form.submit();
		});
	});

	function validateFields(){
		var valid=true;
		if($j("#individual").is(':checked')==false && $j("#couple").is(':checked')==false){
			$j("#counselingTypeError").html("*");
			$j("#counselingTypeError").addClass("error");
			valid=false;
		} else {
			$j("#counselingTypeError").html("");
			$j("#counselingTypeError").removeClass("error");
		}

		if($j("#encounterDateId").val()==""){
			$j("#encounterDateError").html("*");
			$j("#encounterDateError").addClass("error");
			valid=false;
		} else {
			$j("#encounterDateError").html("");
			$j("#encounterDateError").removeClass("error");
		}

		if(document.getElementsByName("location")[0].value==''){
			$j("#locationError").html("*");
			$j("#locationError").addClass("error");
			valid=false;
		} else {
			$j("#locationError").html("");
			$j("#locationError").removeClass("error");
		}

		if(document.getElementsByName("provider")[0].value==''){
			$j("#providerError").html("*");
			$j("#providerError").addClass("error");
			valid=false;
		} else {
			$j("#providerError").html("");
			$j("#providerError").removeClass("error");
		}

		if(!valid){
			$j("#errorDivId").html("<spring:message code='vcttrac.fillbeforesubmit'/>");
			$j("#errorDivId").addClass("error");
		} else {
			$j("#errorDivId").html("");
			$j("#errorDivId").removeClass("error");
		}
		
		return valid;
	}

</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
	
<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="Manage VCT/PIT Clients registration" otherwise="/login.htm" redirect="/module/@MODULE_ID@/vctHome.htm" />

<script type="text/javascript">
	var $j = jQuery.noConflict();
</script>

<div style="width: 90%; margin-left: auto; margin-right: auto;">

<h2>
	<c:if test="${param.edit eq null}"><spring:message code="@MODULE_ID@.registration.title"/> : ${fn:toUpperCase(param.type)}</c:if>
	<c:if test="${param.edit ne null}"><spring:message code="@MODULE_ID@.registration.vieweditregistration"/></c:if>
</h2>

<!-- Open edit/view form in case the user want to do so -->
<c:if test="${param.edit ne null}">
	<%@ include file="template/viewEditRegistration.jsp"%>
</c:if>

<c:if test="${param.select=='new'}">
	<form action="vctRegistration.form?save=true" method="post">
		<div id="divNew">
			<b class="boxHeader"><spring:message code="@MODULE_ID@.registration.clientName"/></b>
			<div class="box">
				
				<div id="errorDivNewId" style="margin-bottom: 5px;"></div>
				<div>
					<div style="float: left; width: 45%;">
						<table>
							<tr>
								<td><input type="hidden" name="vctOrPit" value="${param.type}"/></td>
								<td></td>
								<td><input type="hidden" id="existOrNewId" name="existOrNew" value="<c:if test="${param.select=='new'}">0</c:if><c:if test="${param.select=='choose'}">1</c:if>"/></td>
								<td></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.registration.nid"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input readonly="readonly" type="text" name="nid" value="${nid}" size="40"/></td>
								<td></td>
							</tr>
							<tr>
								<td><spring:message code="PersonName.familyName"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="30" name="familyName" id="familyNameId" style="text-transform: uppercase;"/></td>
								<td><span id="familyNameError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="PersonName.middleName"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="30" name="middleName"/></td>
								<td></td>
							</tr>
							<tr>
								<td><spring:message code="PersonName.givenName"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="30" name="givenName" id="givenNameId"/></td>
								<td><span id="givenNameError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.person.gender"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="radio" name="gender" id="gender-F" value="F"/><label for="gender-F"><spring:message code="@MODULE_ID@.person.female"/></label>
									<input type="radio" name="gender" id="gender-M" value="M"/><label for="gender-M"><spring:message code="@MODULE_ID@.person.male"/></label>
								</td>
								<td><span id="genderError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.person.birthdate"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="11" name="birthdate" id="birthdateId" onclick="showCalendar(this)"/></td>
								<td><span id="birthdateError"></span></td>
							</tr>
						</table>
					</div>
					
					<div style="float: right; width: 45%;">
						<table>
							<tr>
								<td><spring:message code="@MODULE_ID@.date"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="11" name="registrationDate" id="registrationDateId" onclick="showCalendar(this)" value="${todayDate}"/></td>
								<td><span id="registrationDateError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="Encounter.location"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><openmrs_tag:locationField formFieldName="location" initialValue="${locationId}" /></td>
								<td><span id="locationError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.registration.codeclient"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="18" name="codeClient" id="codeClientId" autocomplete="off"/></td>
								<td><span id="codeClientError"></span></td>
							</tr>
						</table>
					</div>
					<div style="clear: both;"></div>
				</div>
			</div><br/>
			
			<b class="boxHeader"><spring:message code="@MODULE_ID@.registration.clientInformation"/></b>
			<div class="box">
				<div>
					<div style="float: left; width: 45%;">
						<table>
							<spring:nestedPath path="location">
								<openmrs:portlet url="addressLayout" id="addressPortlet" size="full" parameters="layoutShowTable=false|layoutShowExtended=false|layoutShowErrors=false" />
							</spring:nestedPath>
						</table>
					</div>
					<div style="float: right; width: 45%;">
						<table>
							<tr>
								<td><spring:message code="@MODULE_ID@.registration.CivilStatus"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><select name="civilStatus" id="civilStatusId"><option value="0">--</option>
									<c:forEach items="${civilStatus}" var="cs">
										<option value="${cs.key}">${cs.value}</option>
									</c:forEach>
								</select></td>
								<td><span id="civilStatusError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.registration.educationLevel"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><select name="educationLevel" id="educationLevelId"><option value="0">--</option>
									<c:forEach items="${educationLevels}" var="el">
										<option value="${el.key}">${el.value}</option>
									</c:forEach>
								</select></td>
								<td><span id="educationLevelError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.registration.mainActivity"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><select name="mainActivity" id="mainActivityId"><option value="0">--</option>
									<c:forEach items="${mainActivities}" var="ma">
										<option value="${ma.key}">${ma.value}</option>
									</c:forEach>
								</select></td>
								<td><span id="mainActivityError"></span></td>
							</tr>
						</table>
					</div>
					<div style="clear: both;"></div>
				</div>
				
			</div><br/>
			
			&nbsp;&nbsp;<input style="min-width: 100px;" type="button" id="btSave" value="<spring:message code="general.save"/>"/>
	
		</div>	
	</form>
</c:if>

<c:if test="${param.select=='choose'}">

	<form action="vctRegistration.form?save=true" method="post">
		<div id="divChoose">
		
		<b class="boxHeader"><spring:message code="@MODULE_ID@.registration.title"/></b>
			<div class="box">
			<div id="errorDivChooseId" style="margin-bottom: 5px;"></div>
			<div id="noNIDError" style="margin-bottom: 5px;"></div>
				<table>
					<tr>
						<td><input type="hidden" name="vctOrPit" value="${param.type}"/></td>
						<td></td>
						<td><input type="hidden" name="existOrNew" value="<c:if test="${param.select=='new'}">0</c:if><c:if test="${param.select=='choose'}">1</c:if>"/></td>
						<td></td>
					</tr>
					<tr>
						<td>National ID</td>
						<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
						<td>
							<c:if test="${param.nid!=null}"><b style="font-size: 18px;">${param.nid}</b></c:if>
							<c:if test="${param.nid==null}"><input autocomplete="off" type="text" name="input_nid" id="input_nid" size="40" /></c:if>
						</td>
						<td><c:if test="${param.nid!=null}"><input type="hidden" name="nid" value="${param.nid}"/></c:if></td>
					</tr>
					<tr>
						<td><spring:message code="Person.names"/></td>
						<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
						<td><b>${vcttag:personName(param.clientId)}</b></td>
						<td><input type="hidden" name="client" value="${param.clientId}"/></td>
					</tr>
					<tr>
						<td><spring:message code="Encounter.location"/></td>
						<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
						<td><openmrs_tag:locationField formFieldName="location_A" initialValue="${locationId}" /></td>
						<td><span id="location_AError"></span></td>
					</tr>
					<tr>
						<td><spring:message code="@MODULE_ID@.date"/></td>
						<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
						<td><input type="text" id="registrationDate_AId" name="registrationDate_A" size="11" onclick="showCalendar(this)" value="${todayDate}"/></td>
						<td><span id="registrationDate_AError"></span></td>
					</tr>
					<tr>
						<td><spring:message code="@MODULE_ID@.registration.codeclient"/></td>
						<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/help.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
						<td><input type="text" id="codeClient_AId" name="codeClient_A" size="18" autocomplete="off"/></td>
						<td><span id="codeClient_AError"></span></td>
					</tr>
					<tr>
						<td></td>
						<td></td>
						<td><input style="min-width: 100px;" value="<spring:message code="general.save"/>" type="button" id="btSave2" <c:if test="${param.nid==null}">disabled="disabled"</c:if>/></td>
						<td></td>
					</tr>
				</table>
			</div><br/>
	
		</div>
	</form>

</c:if>

</div>

<script type="text/javascript">

	$j(document).ready( function() {
		
		$j("#btSave").click(function(){
			if(validateNewFormFields()){
				if(confirm("<spring:message code='@MODULE_ID@.surewanttosave'/>"))
					this.form.submit();
			}
		});
		
		$j("#btSave2").click(function(){
			if(validateExistingFormFields()){
				if(confirm("<spring:message code='@MODULE_ID@.surewanttosave'/>"))
					this.form.submit();
			}
		});

		if($j("#existOrNewId").val()=="0")
			$j("#familyNameId").focus();
		else $j("#input_nid").focus();
		
		// NID validation and formatting
		$j("#input_nid")
				.keyup(
						function(e) {
							var val = $j("#input_nid").val();
							var currentValue = $j.trim(val);

							if (currentValue.length != 21) {
								$j("#btSave2").attr("disabled",
										"true");
							} else {
								var nid=$j("#input_nid").val();
								formatNID(nid);
								if ($j("#input_nid").val().length == 21) {
									$j("#btSave2").removeAttr('disabled');
									$j("#btSave2").focus();
								}
							}

							if (currentValue.length == 18) {
								if (e.which != 8
										&& e.which != 0
										&& (e.which < 48 || e.which > 57)) {
									e.preventDefault();
								} else
									$j("#input_nid").val(
											currentValue + " ");
							} else if (currentValue.length == 16) {
								if (e.which != 8
										&& e.which != 0
										&& (e.which < 48 || e.which > 57)) {
									e.preventDefault();
								} else
									$j("#input_nid").val(
											currentValue + " ");
							} else if (currentValue.length == 8) {
								if (e.which != 8
										&& e.which != 0
										&& (e.which < 48 || e.which > 57)) {
									e.preventDefault();
								} else
									$j("#input_nid").val(
											currentValue + " ");
							} else if (currentValue.length == 6) {
								if (e.which != 8
										&& e.which != 0
										&& (e.which < 48 || e.which > 57)) {
									e.preventDefault();
								} else
									$j("#input_nid").val(
											currentValue + " ");
							} else if (currentValue.length == 1) {
								if (e.which != 8
										&& e.which != 0
										&& (e.which < 48 || e.which > 57)) {
									e.preventDefault();
								} else
									$j("#input_nid").val(
											currentValue + " ");
							}
						});

		$j("#input_nid").keydown(
				function(e) {
					if (e.which != 8 && e.which != 0
							&& (e.which < 48 || e.which > 57)) {
						e.preventDefault();
					} else {
						var val = $j("#input_nid").val();
						var currentValue = $j.trim(val);

						if (currentValue.length >= 21) {
							if (e.which != 8 && e.which != 0) {
								e.preventDefault();
							}
						} else if (currentValue.length == 21) {
							if (e.which != 8 && e.which != 0) {
								e.preventDefault();
							}
						}
					}
				});

		$j("#btSave2")
				.click(
						function() {
							if ($j("#input_nid").val() != "") {
								$j("#noNIDError").html("");
								$j("#noNIDError").removeClass(
										"error");
							} else {
								$j("#noNIDError")
										.html(
												"<spring:message code='@MODULE_ID@.error.noNID'/>");
								$j("#noNIDError").addClass(
										"error");
							}
						});
		
	});

	function formatNID(nid) {
		var currentValue = $j.trim(nid);
		var validNID="";
		for(var i=0;i<currentValue.length;i++){
			if(currentValue.charAt(i)!=' ')
				validNID+=currentValue.charAt(i);
			switch(validNID.length){
				case 21:$j("#input_nid").val(validNID);return;break;
				case 18:validNID+=' ';break;
				case 16:validNID+=' ';break;
				case 8:validNID+=' ';break;
				case 6:validNID+=' ';break;
				case 1:validNID+=' ';break;
			}
			
		}
		$j("#input_nid").val(validNID);
	}
	
	function validateExistingFormFields(){
		var valid=true;
		if($j("#codeClient_AId").val()==''){
			$j("#codeClient_AError").html("*");
			$j("#codeClient_AError").addClass("error");
			valid=false;
		} else {
			$j("#codeClient_AError").html("");
			$j("#codeClient_AError").removeClass("error");
		}
		
		if($j("#registrationDate_AId").val()==''){
			$j("#registrationDate_AError").html("*");
			$j("#registrationDate_AError").addClass("error");
			valid=false;
		} else {
			$j("#registrationDate_AError").html("");
			$j("#registrationDate_AError").removeClass("error");
		}

		if(document.getElementsByName("location_A")[0].value==''){
			$j("#location_AError").html("*");
			$j("#location_AError").addClass("error");
			valid=false;
		} else {
			$j("#location_AError").html("");
			$j("#location_AError").removeClass("error");
		}

		if(document.getElementsByName("client")[0].value==''){
			$j("#clientError").html("*");
			$j("#clientError").addClass("error");
			valid=false;
		} else {
			$j("#clientError").html("");
			$j("#clientError").removeClass("error");
		}

		if(!valid){
			$j("#errorDivChooseId").html("<spring:message code='@MODULE_ID@.fillbeforesubmit'/>");
			$j("#errorDivChooseId").addClass("error");
		} else {
			$j("#errorDivChooseId").html("");
			$j("#errorDivChooseId").removeClass("error");
		}
		
		return valid;
	}

	function validateNewFormFields(){
		var valid=true;
		if($j("#codeClientId").val()==''){
			$j("#codeClientError").html("*");
			$j("#codeClientError").addClass("error");
			valid=false;
		} else {
			$j("#codeClientError").html("");
			$j("#codeClientError").removeClass("error");
		}
		
		if($j("#registrationDateId").val()==''){
			$j("#registrationDateError").html("*");
			$j("#registrationDateError").addClass("error");
			valid=false;
		} else {
			$j("#registrationDateError").html("");
			$j("#registrationDateError").removeClass("error");
		}

		if(document.getElementsByName("location")[0].value==''){
			$j("#locationError").html("*");
			$j("#locationError").addClass("error");
			valid=false;
		} else {
			$j("#locationError").html("");
			$j("#locationError").removeClass("error");
		}

		if($j("#givenNameId").val()==''){
			$j("#givenNameError").html("*");
			$j("#givenNameError").addClass("error");
			valid=false;
		} else {
			$j("#givenNameError").html("");
			$j("#givenNameError").removeClass("error");
		}

		if($j("#familyNameId").val()==''){
			$j("#familyNameError").html("*");
			$j("#familyNameError").addClass("error");
			valid=false;
		} else {
			$j("#familyNameError").html("");
			$j("#familyNameError").removeClass("error");
		}

		if($j("#birthdateId").val()==''){
			$j("#birthdateError").html("*");
			$j("#birthdateError").addClass("error");
			valid=false;
		} else {
			$j("#birthdateError").html("");
			$j("#birthdateError").removeClass("error");
		}
		
		if($j("#gender-M").is(':checked')==false && $j("#gender-F").is(':checked')==false){
			$j("#genderError").html("*");
			$j("#genderError").addClass("error");
			valid=false;
		} else {
			$j("#genderError").html("");
			$j("#genderError").removeClass("error");
		}

		if(!valid){
			$j("#errorDivNewId").html("<spring:message code='@MODULE_ID@.fillbeforesubmit'/>");
			$j("#errorDivNewId").addClass("error");
		} else {
			$j("#errorDivNewId").html("");
			$j("#errorDivNewId").removeClass("error");
		}
		
		return valid;
	}

</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
	
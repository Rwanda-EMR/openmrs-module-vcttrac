
	<form action="vctRegistration.form?save=true" method="post">
		<div id="divNew">
			<b class="boxHeader"><spring:message code="@MODULE_ID@.registration.clientName"/></b>
			<div class="box">
				
				<div id="errorDivNewId" style="margin-bottom: 5px;"></div>
				<div>
					<div style="float: left; width: 45%;">
						<table>
							<tr>
								<td><input type="hidden" id="clientId" name="clientId" value="${clientId}"/></td>
								<td></td>
								<td><input type="hidden" id="existOrNewId" name="existOrNew" value="1"/></td>
								<td></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.registration.nid"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input readonly="readonly" type="text" name="nid" value="${nid}" size="40"/></td>
								<td></td>
							</tr>
							<tr>
								<td><spring:message code="PersonName.familyName"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="30" name="familyName" value="${familyName}" id="familyNameId" style="text-transform: uppercase;"/></td>
								<td><span id="familyNameError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="PersonName.middleName"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="30" name="middleName" value="${middleName}"/></td>
								<td></td>
							</tr>
							<tr>
								<td><spring:message code="PersonName.givenName"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="30" name="givenName" value="${givenName}" id="givenNameId"/></td>
								<td><span id="givenNameError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.person.gender"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="radio" name="gender" <c:if test="${gender=='F'}">checked='checked'</c:if> id="gender-F" value="F"/><label for="gender-F"><spring:message code="@MODULE_ID@.person.female"/></label>
									<input type="radio" name="gender" <c:if test="${gender=='M'}">checked='checked'</c:if> id="gender-M" value="M"/><label for="gender-M"><spring:message code="@MODULE_ID@.person.male"/></label>
								</td>
								<td><span id="genderError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.person.birthdate"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="11" name="birthdate" value="${birthdate}" id="birthdateId" onclick="showCalendar(this)"/></td>
								<td><span id="birthdateError"></span></td>
							</tr>
						</table>
					</div>
					
					<div style="float: right; width: 45%;">
						<table>
							<tr>
								<td><spring:message code="@MODULE_ID@.date"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="11" name="registrationDate" id="registrationDateId" onclick="showCalendar(this)" value="${todayDate}"/></td>
								<td><span id="registrationDateError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="Encounter.location"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><openmrs_tag:locationField formFieldName="location" initialValue="${locationId}" /></td>
								<td><span id="locationError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.registration.codeclient"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><input type="text" size="18" name="codeClient" id="codeClientId" value="${param.codeClient}" autocomplete="off"/></td>
								<td><span id="codeClientError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.registration.vctorpit"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><select name="vctOrPit">
										<option value="vct" <c:if test="${type=='vct'}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.vct"/></option>
										<option value="pit" <c:if test="${type=='pit'}">selected='selected'</c:if>><spring:message code="@MODULE_ID@.pit"/></option>
									</select>
								</td>
								<td></td>
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
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><select name="civilStatus" id="civilStatusId"><option value="0">--</option>
									<c:forEach items="${civilStatus}" var="cs">
										<option <c:if test="${fn:toUpperCase(currentCivilStatus)==fn:toUpperCase(cs.value)}">selected='selected'</c:if> value="${cs.key}">${cs.value}</option>
									</c:forEach>
								</select></td>
								<td><span id="civilStatusError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.registration.educationLevel"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><select name="educationLevel" id="educationLevelId"><option value="0">--</option>
									<c:forEach items="${educationLevels}" var="el">
										<option <c:if test="${fn:toUpperCase(currentEducationLevel)==fn:toUpperCase(el.value)}">selected='selected'</c:if> value="${el.key}">${el.value}</option>
									</c:forEach>
								</select></td>
								<td><span id="educationLevelError"></span></td>
							</tr>
							<tr>
								<td><spring:message code="@MODULE_ID@.registration.mainActivity"/></td>
								<td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/images/info.gif" title="<spring:message code="@MODULE_ID@.help"/>"/></span></td>
								<td><select name="mainActivity" id="mainActivityId"><option value="0">--</option>
									<c:forEach items="${mainActivities}" var="ma">
										<option <c:if test="${fn:toUpperCase(currentMainActivity)==fn:toUpperCase(ma.value)}">selected='selected'</c:if> value="${ma.key}">${ma.value}</option>
									</c:forEach>
								</select></td>
								<td><span id="mainActivityError"></span></td>
							</tr>
						</table>
					</div>
					<div style="clear: both;"></div>
				</div>
				
			</div><br/>
			
			&nbsp;&nbsp;<input style="min-width: 100px;" type="button" id="btSave" value="<spring:message code="@MODULE_ID@.general.modification.save"/>"/>
	
		</div>	
	</form>
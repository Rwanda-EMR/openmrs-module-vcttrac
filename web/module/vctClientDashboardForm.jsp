<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="View VCT Client Dashboard" otherwise="/login.htm" redirect="/module/@MODULE_ID@/vctClientDashboard.form" />

<c:if test="${client==null}">
	<br/><span class="error"><spring:message code="@MODULE_ID@.dashboard.noclientfound"/> ${param.clientId}</span><br/><br/>
</c:if>

<c:if test="${client!=null}">
<div class="hdClientSummary">
	<div style="float: left; margin-right: 8px;">
		<img title="${client.personName}" width=105px;" border="1px solid #BBBBBB;" src="<c:if test="${client.gender=='F'}"><openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/patient_F.gif</c:if><c:if test="${client.gender=='M'}"><openmrs:contextPath/>/moduleResources/@MODULE_ID@/images/patient_M.gif</c:if>" />
	</div>
	
	<div style="float: left; width: 625px;">
		<h2 <c:if test="${isAPatient}"> class="hdDashboard" title="<spring:message code='patientDashboard.viewDashboard'/>" onclick="window.location.href='<openmrs:contextPath/>/patientDashboard.form?patientId=${client.personId}'" </c:if>>${client.personName}</h2>
		<hr style="size: 1px;"/>
		
		<div>
			<div style="float: left;">				
				<table>
					<tr>
						<td><spring:message code="Patient.gender"/></td>
						<td>: <b><c:if test="${client.gender=='F'}"><spring:message code="@MODULE_ID@.person.female"/></c:if><c:if test="${client.gender=='M'}"><spring:message code="@MODULE_ID@.person.male"/></c:if></b></td>
					</tr>
					<tr>
						<td><spring:message code="Person.age"/></td>
						<td>: <b>${client.age}<spring:message code="@MODULE_ID@.dashboard.years"/> &nbsp;( <openmrs:formatDate date="${client.birthdate}" type="medium" /> )</b></td>
					</tr>
				</table>
			</div>
			<div style="float: right;">
				<table>
					<c:forEach items="${clientIdentifiers}" var="clientIdentifier">
						<tr>
							<td>${clientIdentifier.identifierType.name}</td>
							<td>: <b>${clientIdentifier.identifier}</b></td>
						</tr>
						<c:if test="${nidIdentifierTypeId==clientIdentifier.identifierType.patientIdentifierTypeId}"><c:set var="found" value="1"/></c:if>
					</c:forEach>
						<c:if test="${found!=1}">
							<tr>
								<td>${NID_title}</td>
								<td>: <b>${clientNID}</a></td>
							</tr>
						</c:if>
				</table>
			</div>
			<div style="clear: both;"></div>
		</div>
		<br/>
		<div>
			<div style="float: left; width: 48%; overflow: hidden;">
				<table>
					<tr>
						<td><spring:message code="PersonAttributeType.CivilStatus"/></td>
						<td>: <b>${vcttag:personAttribute(client,civilStatusAttibuteTypeId)}</b></td>
					</tr>
					<tr>
						<td><spring:message code="@MODULE_ID@.registration.educationLevel"/></td>
						<td>: <b>${vcttag:personAttribute(client,educationLevelAttibuteTypeId)}</b></td>
					</tr>
					<tr>
						<td><spring:message code="@MODULE_ID@.registration.mainActivity"/></td>
						<td>: <b>${vcttag:personAttribute(client,mainActivityAttibuteTypeId)}</b></td>
					</tr>
				</table>
			</div>
			<div style="float: right; width: 48%; overflow: hidden;">
				<vct_tag:personAddress personAddress="${client.personAddress}"/>
			</div>
			<div style="clear: both;"></div>
		</div>
			
	</div>
	<div style="clear: both;"></div>
</div>

<c:if test="${empty clientVisits}">
	<div class="hdClientSummary" style="text-align: center;"><spring:message code="@MODULE_ID@.dashboard.nocounselingfound"/></div>
</c:if>

<c:forEach items="${clientVisits}" var="visit" varStatus="status">

	<div class="hdClientSummary">
		<div class="boxHeader" style="-moz-border-radius: 5px; margin-top: 4px; font-weight: bold;">
			<div style="float: left; width: 70%;">
				<spring:message code="@MODULE_ID@.registration.reference"/> : <c:if test="${!visit.vctOrPit}"><a href="#"><spring:message code="@MODULE_ID@.vct"/></a></c:if><c:if test="${visit.vctOrPit}"><a href="#"><spring:message code="@MODULE_ID@.pit"/></a></c:if> <spring:message code="@MODULE_ID@.on"/> <a href="#"><openmrs:formatDate date="${visit.dateOfRegistration}" type="medium" /></a> @ <a href="#">${visit.location}</a> <spring:message code="@MODULE_ID@.by"/> <a href="#">${visit.createdBy.person.personName}</a> <c:if test="${visit.changedBy ne null}">/ <a href="#">${visit.changedBy.person.personName}</a></c:if>
			</div>
			<div style="float: right; width: 25%; text-align: right;">
				<spring:message code="@MODULE_ID@.registration.codeclient"/> : <a href="vctRegistration.form?codeClient=${visit.codeClient}&edit=true">${visit.codeClient}</a>
			</div>
			<div style="clear: both;"></div>
		</div>
		
		<div class="obsDetailsDiv">
			<div style="float: left;">
				<table>
					<tr>
						<td><c:if test="${visit.typeOfCounseling!=null}"><spring:message code="@MODULE_ID@.dashboard.typeofcounseling"/></c:if></td>
						<td><c:if test="${visit.typeOfCounseling==1}">: <b><spring:message code="@MODULE_ID@.dashboard.typeofcounseling.individuel"/></b></c:if>
							<c:if test="${visit.typeOfCounseling==2}">: <b><spring:message code="@MODULE_ID@.dashboard.typeofcounseling.couple"/></b> [ <a href="vctClientDashboard.form?clientId=${visit.partner.personId}"><spring:message code="@MODULE_ID@.dashboard.viewpartner"/></a> ]</c:if>
						</td>
					</tr>
					<c:forEach items="${visit.counselingObs.groupMembers}" var="obs">
						<tr>
							<td>${obs.concept.name}</td>
							<td>: <b><openmrs:format obsValue="${obs}" /></b></td>
						</tr>
					</c:forEach>
					<c:if test="${visit.counselingObs ne null}">
						<tr>
							<td colspan="2">
								<hr style="border-color: #BBBBBB; border-width: thin;"/>
								<spring:message code="@MODULE_ID@.counseling.counseled.by"/> <a href="#">${visit.counselingObs.creator.personName}</a> <spring:message code="@MODULE_ID@.on"/> <a href="#"><openmrs:formatDate date="${visit.counselingObs.obsDatetime}" type="medium" /></a> @ <a href="#">${visit.counselingObs.location}</a>
							</td>
						</tr>
					</c:if>
					<c:if test="${visit.counselingObs eq null}">
						<tr>
							<td colspan="2">
								<h3><spring:message code="@MODULE_ID@.dashboard.typeofcounseling.notcounseled"/></h3>
							</td>
						</tr>
					</c:if>
				</table>
			</div>
			<div style="float: right;">
				<table>
					<tr>
						<td>
							<img border="0" <c:if test="${visit.codeTest!=null}">src="<openmrs:contextPath/>/images/checkmark.png" title="<spring:message code="@MODULE_ID@.dashboard.clienttested"/>"</c:if><c:if test="${visit.codeTest==null}">src="<openmrs:contextPath/>/images/delete.gif" title="<spring:message code="@MODULE_ID@.dashboard.clientNoTtested"/>"</c:if> />
						</td>
						<td><spring:message code="@MODULE_ID@.dashboard.tested"/></td>
					</tr>
					<!-- <c:if test="${visit.codeTest!=null}">
						<tr>
							<td colspan="2"><a href="#">..</a></td>
						</tr>
					</c:if> -->
				</table>
			</div>
			<div style="clear: both;"></div>
		</div><br/>
		
		<c:if test="${visit.resultObs!=null}">
			<div class="obsDetailsDiv" style="border-top: 1px solid #BBBBBB;">
				<div style="float: left;"><spring:message code="@MODULE_ID@.dashboard.resultavailable"/> : <a href="#"><openmrs:formatDate date="${visit.resultObs.obsDatetime}" type="medium" /></a> @ <a href="#" title="<spring:message code="@MODULE_ID@.dashboard.viewallresult"/> @ ${visit.resultObs.location}">${visit.resultObs.location}</a></div>
				<div style="float: right;">
					<openmrs:hasPrivilege privilege="View VCT Client result">
						<span id="viewHide_${status.count}">[<a id="viewResult_${status.count}" onclick="showResult(this);" name="${status.count}" title="<spring:message code="@MODULE_ID@.dashboard.viewhivresult"/>" href="#"> <spring:message code="@MODULE_ID@.dashboard.showresult"/> </a>]</span>
						<div class="resultDiv" style="display: none;" id="hivResult_${status.count}">
							<table>
								<c:if test="${visit.codeTest!=null}">
									<tr>
										<td><spring:message code="@MODULE_ID@.result.clientcode"/></td>
										<td>: <a href="vctClientResults.form?testCode=${visit.codeTest}">${visit.codeTest}</a></td>
									</tr>
								</c:if>
								<c:forEach items="${visit.resultObs.groupMembers}" var="obs">
									<tr>
										<td>${obs.concept.name}</td>
										<td>: <b><openmrs:format obsValue="${obs}" /></b></td>
									</tr>
								</c:forEach>
							</table>
						</div>
					</openmrs:hasPrivilege>
				</div>
				<div style="clear: both;"></div>
			</div>
		</c:if>
		
		<c:if test="${vcttag:convsetObsValueByConcept(visit.resultObs,dateResultOfHivTestReceived)!='-'}">
			<b class="boxHeader" style="-moz-border-radius: 5px; margin-top: 4px;">
				<spring:message code="@MODULE_ID@.dashboard.clientgotresulton"/> <a href="#">${vcttag:convsetObsValueByConcept(visit.resultObs,dateResultOfHivTestReceived)}</a>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<openmrs:hasPrivilege privilege="View VCT Client result">
						<c:if test="${visit.clientDecision ne null}">/&nbsp;&nbsp;<spring:message code="@MODULE_ID@.result.clientDecision"/> : </c:if>
						<c:if test="${visit.clientDecision==1}"><a href="#"><spring:message code="@MODULE_ID@.result.enrolled"/></a></c:if>
						<c:if test="${visit.clientDecision==0}"><a href="#"><spring:message code="@MODULE_ID@.result.transferred"/></a></c:if>
						<c:if test="${visit.clientDecision==2}"><a href="#"><spring:message code="@MODULE_ID@.result.refusedToJoinProgram"/></a></c:if>
					</openmrs:hasPrivilege>
				
			</b>
		</c:if>
		
	</div>

</c:forEach>

<script>

		function showResult(obj){
			var id="#viewHide_"+obj.name;
			var hivTestResultDivId="#hivResult_"+obj.name;
			$(id).html("[<a id='hideResult_"+obj.name+"' onclick='hideResult(this);' name="+obj.name+" title='<spring:message code='@MODULE_ID@.dashboard.hidehivresult'/>' href='#'> <spring:message code='@MODULE_ID@.dashboard.hideresult'/> <a/>]");
			$(hivTestResultDivId).show(500);
		}

		function hideResult(obj){
			var id="#viewHide_"+obj.name;
			var hivTestResultDivId="#hivResult_"+obj.name;
			$(id).html("[<a id='viewResult_"+obj.name+"' onclick='showResult(this);' name="+obj.name+" title='<spring:message code='@MODULE_ID@.dashboard.viewhivresult'/>' href='#'> <spring:message code='@MODULE_ID@.dashboard.showresult'/> <a/>]");
			$(hivTestResultDivId).hide(500);
		}

</script>

</c:if>

<%@ include file="/WEB-INF/template/footer.jsp"%>
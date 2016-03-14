<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="Manage Counseling of VCT/PIT Clients" otherwise="/login.htm" redirect="/module/@MODULE_ID@/preCounseling.form" />

<h2><spring:message code="@MODULE_ID@.counseling.step3.title"/></h2>

<!-- <a href="preCounseling.form"><spring:message code="@MODULE_ID@.counseling.step3.newcounseling"/></a>
<br/><br/> -->

<c:if test="${pci.counselingTypeId==1}">
	<c:set var="counselingType"><spring:message code="@MODULE_ID@.dashboard.typeofcounseling.individuel" /></c:set>
</c:if>
<c:if test="${pci.counselingTypeId==2}">
	<c:set var="counselingType"><spring:message code="@MODULE_ID@.dashboard.typeofcounseling.couple" /></c:set>
</c:if>


<b class="boxHeader"><spring:message code="@MODULE_ID@.counseling" /> : ${counselingType}</b>
<div class="box">
	<table>
		<tr>
			<td><spring:message code="Encounter.datetime" /></td>
			<td> : <b>${pci.encounterDate}</b></td>
		</tr>
		<tr>
			<td><spring:message code="Encounter.location" /></td>
			<td> : <b>${vcttag:locationName(pci.locationId)}</b></td>
		</tr>
		<tr>
			<td><spring:message code="Encounter.provider" /></td>
			<td> : <b>${vcttag:personName(pci.providerId)}</b></td>
		</tr>
		<tr>
			<td><spring:message code="@MODULE_ID@.dashboard.typeofcounseling"/></td>
			<td> : <b>${counselingType}</b></td>
		</tr>
	</table>
</div>

<br/><br/>
<b class="boxHeader"><spring:message code="@MODULE_ID@.counseling"/> : ${counselingType}</b>
<div class="box">
	<table id="list_data">
		<tr>
			<th class="columnHeader">#.</th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.registration.clientName"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.counseling.reasonTested"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.counseling.programOrderedTest"/></th>
			<!-- <th class="columnHeader" style="color:red;">Quel handicap ?</th> 
			<th class="columnHeader"><spring:message code="@MODULE_ID@.dashboard.tested"/> ?</th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.result.clientcode"/></th>
			<c:if test="${pci.counselingTypeId==2}">
				<th class="columnHeader"><spring:message code="@MODULE_ID@.counseling.clientcodepartner"/></th>
			</c:if>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.counseling.numberOfCondoms"/></th> -->
			<th class="columnHeader"><spring:message code="@MODULE_ID@.counseling.comment"/></th>
		</tr>
		<c:forEach items="${ciList}" var="ci" varStatus="status">
			<tr class="${status.count%2!=0?'even':''}">
				<td class="rowValue" style="text-align: center;">${status.count}.</td>
				<td class="rowValue" style="width: 250px;"><a href="vctClientDashboard.form?clientId=${ci.patientId}">${vcttag:personName(ci.patientId)}<a/></td>
				<td class="rowValue"><b>${vcttag:conceptName(ci.whyTesting)}<b/></td>
				<td class="rowValue"><b>${vcttag:conceptName(ci.refereDuService)}<b/></td>
				<!-- <td class="rowValue"><b>${ci.whatHandicap}<b/></td>
				<td class="rowValue" style="text-align: center;"><img border="0"
						src="<c:if test="${ci.hivTested==1}"><openmrs:contextPath/>/images/checkmark.png</c:if><c:if test="${ci.hivTested==0}"><openmrs:contextPath/>/images/delete.gif</c:if>" />
				</td>
				<td class="rowValue"><b>${ci.codeClient}<b/></td>
				<c:if test="${pci.counselingTypeId==2}">
					<td class="rowValue"><b>${ci.codeDuPartenaire}</b></td>
				</c:if>
				<td class="rowValue" style="text-align: center;"><b>${ci.numberOfCondomTaken}<b/></td> -->
				<td class="rowValue"><b>${ci.clinicalComment}<b/></td>
			</tr>
		</c:forEach>
	</table>
</div>

<c:remove var="counselingType"/>

<br/>

<%@ include file="/WEB-INF/template/footer.jsp"%>
<%@ include file="template/overviewHeader.jsp"%>

<div id="list_container" style="width: 99%; padding: 5px;">

	<div class="list_title">
		<div class="list_title_msg">${parameterDescription}</div>
		<div class="list_title_bts">
		<form style="display: inline;" action="vctClients.list?exportFormat=csv${parameters}" method="post">
				<input type="submit" class="list_exportBt" title="<spring:message code="vcttrac.tablelist.exportToCSV"/>" value="<spring:message code="vcttrac.tablelist.CSV"/>"/>
			</form>				
		</div>
		<div style="clear:both;"></div>
	</div>
	
	<div style="width: 100%; overflow: scroll;">
		<table class="list_data">
			<tr>
				<th class="columnHeader" rowspan="2"><spring:message code="vcttrac.export.column.registrationdate"/></th>
				<th class="columnHeader" rowspan="2"><spring:message code="vcttrac.export.column.number"/></th>
				<th class="columnHeader" rowspan="2"><spring:message code="vcttrac.registration.codeclient"/></th>
				<th class="columnHeader" rowspan="2"><spring:message code="Person.names"/></th>
				<th class="columnHeader" rowspan="2"><spring:message code="vcttrac.age"/></th>
				<th class="columnHeader" rowspan="2"><spring:message code="vcttrac.person.gender"/></th>
				<th class="columnHeader" colspan="3" style="text-align: center;"><spring:message code="vcttrac.person.attributes"/></th>
				<th class="columnHeader" style="text-align: center;"><spring:message code="vcttrac.registration.clientAddress"/></th>
				<th class="columnHeader" colspan="2" style="text-align: center;"><spring:message code="vcttrac.counseling"/></th>
				<th class="columnHeader" colspan="2" style="text-align: center;"><spring:message code="vcttrac.hivtest"/></th>
			</tr>
			<tr>
				<th class="columnHeader"><spring:message code="vcttrac.registration.CivilStatus"/></th>
				<th class="columnHeader"><spring:message code="vcttrac.registration.mainActivity"/></th>
				<th class="columnHeader"><spring:message code="vcttrac.registration.educationLevel"/></th>
				<th class="columnHeader"><spring:message code="vcttrac.fullAddress"/></th>
				<th class="columnHeader"><spring:message code="vcttrac.dashboard.typeofcounseling"/></th>
				<th class="columnHeader"><spring:message code="vcttrac.counseling.reasonTested"/> ?</th>
				<th class="columnHeader"><spring:message code="vcttrac.result"/></th>
				<th class="columnHeader"><spring:message code="vcttrac.result.dateOfReception"/></th>
			</tr>
			<c:if test="${empty clients}">
				<tr>
					<td colspan="14" style="text-align: center;"><spring:message code="vcttrac.tablelist.empty"/></td>
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
					<td class="rowValue ${status.count%2!=0?'even':''}">${status.count}.</td>
					<td class="rowValue ${status.count%2!=0?'even':''}">${client.codeClient}</td>
					<td class="rowValue ${status.count%2!=0?'even':''}"><a href="vctClientDashboard.form?clientId=${client.client.personId}">${client.client.personName}</a></td>
					<td class="rowValue ${status.count%2!=0?'even':''}">${(client.client.age<1)?'<1':client.client.age}</td>
					<td class="rowValue ${status.count%2!=0?'even':''}" style="text-align: center;"><img border="0"
						src="<c:if test="${client.client.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${client.client.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
					<td class="rowValue ${status.count%2!=0?'even':''}">${vcttag:personAttribute(client.client,civilStatusAttributeTypeId)}</td>
					<td class="rowValue ${status.count%2!=0?'even':''}">${vcttag:personAttribute(client.client,mainActivityAttributeTypeId)}</td>
					<td class="rowValue ${status.count%2!=0?'even':''}">${vcttag:personAttribute(client.client,educationLevelAttributeTypeId)}</td>
					<td class="rowValue ${status.count%2!=0?'even':''}">${vcttag:fullPersonAddress(client.client)}</td>
					<td class="rowValue ${status.count%2!=0?'even':''}"><c:if test="${client.typeOfCounseling==1}">Individuel</c:if><c:if test="${client.typeOfCounseling==2}">Couple</c:if></td>
					<td class="rowValue ${status.count%2!=0?'even':''}">${vcttag:convsetObsValueByConcept(client.counselingObs, whyDidYouGetTestedConceptId)}</td>
					<td class="rowValue ${status.count%2!=0?'even':''}"><span class="${vcttag:convsetObsValueByConcept(client.resultObs, resultOfHivTestConceptId)==positive?'lastObsValuePositive':'lastObsValue'}">${vcttag:convsetObsValueByConcept(client.resultObs, resultOfHivTestConceptId)}</span></td>
					<td class="rowValue ${status.count%2!=0?'even':''}">${vcttag:convsetObsValueByConcept(client.resultObs, dateResultOfHivTestedReceivedConceptId)}</td>
				</tr>
			</c:forEach>
		</table>
	</div>	
	
	<div class="list_footer">
		&nbsp;&nbsp;
	</div>
</div>



<%@ include file="/WEB-INF/template/footer.jsp"%>
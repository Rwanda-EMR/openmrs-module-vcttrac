<%@ include file="/WEB-INF/template/include.jsp" %>
<%@ attribute name="personAddress" required="true" type="org.openmrs.PersonAddress" %>

<table>
	<c:if test="${personAddress.country!=''}">
		<tr>
			<td><spring:message code="@MODULE_ID@.registration.country"/></td>
			<td>: <b>${personAddress.country}</b></td>
		</tr>
	</c:if>
	<c:if test="${personAddress.stateProvince!=''}">
		<tr>
			<td><spring:message code="@MODULE_ID@.registration.province"/></td>
			<td>: <b>${personAddress.stateProvince}</b></td>
		</tr>
	</c:if>
	<c:if test="${personAddress.countyDistrict!=''}">
		<tr>
			<td><spring:message code="@MODULE_ID@.registration.district"/></td>
			<td>: <b>${personAddress.countyDistrict}</b></td>
		</tr>
	</c:if>
	<c:if test="${personAddress.cityVillage!=''}">
		<tr>
			<td><spring:message code="@MODULE_ID@.registration.sector"/></td>
			<td>: <b>${personAddress.cityVillage}</b></td>
		</tr>
	</c:if>
	<c:if test="${personAddress.neighborhoodCell!=''}">
		<tr>
			<td><spring:message code="@MODULE_ID@.registration.cell"/></td>
			<td>: <b>${personAddress.neighborhoodCell}</b></td>
		</tr>
	</c:if>
	<c:if test="${personAddress.address1!=''}">
		<tr>
			<td><spring:message code="@MODULE_ID@.registration.umudugudu"/></td>
			<td>: <b>${personAddress.address1}</b></td>
		</tr>
	</c:if>
</table>
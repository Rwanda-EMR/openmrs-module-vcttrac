<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>

<openmrs:require privilege="Manage VCT configurations" otherwise="/login.htm" redirect="/module/vcttrac/vctConfigurations.htm" />

<script type="text/javascript">
	var $j = jQuery.noConflict();
</script>

<%@ include file="template/configHeader.jsp"%>

<form action="vctConfigurations.htm?save=true" method="post" style="width:90%; margin:6px auto;">
	<h4><spring:message code="vcttrac.configuration.title"/></h4>

	<div style="border: 1px solid red; padding: 3px; -moz-border-radius: 3px; font-size: 0.8em;">
		<spring:message code="vcttrac.configuration.note"/>
	<br/>
	
	<!-- Relationship type -->
	
	<b class="boxHeader"><spring:message code="vcttrac.configuration.relationship"/></b>
	<div class="box">
		<table class="configTable">
			<tr>
				<td class="configDescription">${vcttag:relationShipTypeNameById(gp_relationShipType.propertyValue)}</td>
				<td class="configSelect"><select name="relationShipType">
						<c:forEach items="${relationShipTypes}" var="relationShipType">
							<option value="${relationShipType.relationshipTypeId}" <c:if test="${relationShipType.relationshipTypeId==relationShipTypeId}">selected='selected'</c:if>>${relationShipType.aIsToB} - ${relationShipType.bIsToA}</option>
						</c:forEach>
					</select></td>
			</tr>
			<tr>
				<td colspan="2" class="configComment">${gp_relationShipType.description}</td>					
			</tr>
			<!-- end vct program construct -->
		</table>
	</div>
	<br/>
	
	<!-- End Relationship type -->
	
	<b class="boxHeader"><spring:message code="vcttrac.configuration.concepts"/></b>
	<div class="box">
		<table class="configTable">
			<!-- vct program construct -->
			<tr>
				<td class="configDescription">${vcttag:conceptNameByIdAsString(gp_vctProgramConstruct.propertyValue)}</td>
				<td class="configSelect"><openmrs:fieldGen formFieldName="${vcttag:gpParser(gp_vctProgramConstruct.property)}" type="org.openmrs.Concept" val="${vcttag:checkIfConceptExistByIdAsString(gp_vctProgramConstruct.propertyValue)}"/></td>
			</tr>
			<tr>
				<td colspan="2" class="configComment">${gp_vctProgramConstruct.description}</td>					
			</tr>
			<c:forEach items="${gp_vpcMembers}" var="member" varStatus="status">
				<tr class="<c:if test="${(status.count+1)%2==0}">even</c:if>">
					<td class="configDescription">${vcttag:conceptNameByIdAsString(member.propertyValue)}</td>
					<td class="configSelect"><openmrs:fieldGen formFieldName="${vcttag:gpParser(member.property)}" type="org.openmrs.Concept" val="${vcttag:checkIfConceptExistByIdAsString(member.propertyValue)}"/></td>
				</tr>
				<tr class="<c:if test="${(status.count+1)%2==0}">even</c:if>">
					<td colspan="2" class="configComment">${member.description}</td>					
				</tr>
			</c:forEach>
			<!-- end vct program construct -->
			
			<!-- hiv test construct -->
			<tr class="<c:if test="${(status.count+2)%2==0}">even</c:if>">
				<td class="configDescription">${vcttag:conceptNameByIdAsString(gp_hivTestConstruct.propertyValue)}</td>
				<td class="configSelect"><openmrs:fieldGen formFieldName="${vcttag:gpParser(gp_hivTestConstruct.property)}" type="org.openmrs.Concept" val="${vcttag:checkIfConceptExistByIdAsString(gp_hivTestConstruct.propertyValue)}"/></td>
			</tr>
			<tr class="<c:if test="${(status.count+2)%2==0}">even</c:if>">
				<td colspan="2" class="configComment">${gp_hivTestConstruct.description}</td>					
			</tr>
			<c:forEach items="${gp_htcMembers}" var="member" varStatus="counter">
				<tr class="<c:if test="${(status.count+2+counter.count)%2==0}">even</c:if>">
					<td class="configDescription">${vcttag:conceptNameByIdAsString(member.propertyValue)}</td>
					<td class="configSelect"><openmrs:fieldGen formFieldName="${vcttag:gpParser(member.property)}" type="org.openmrs.Concept" val="${vcttag:checkIfConceptExistByIdAsString(member.propertyValue)}"/></td>
				</tr>
				<tr class="<c:if test="${(status.count+2+counter.count)%2==0}">even</c:if>">
					<td colspan="2" class="configComment">${member.description}</td>					
				</tr>
			</c:forEach>
			<!-- end hiv test construct -->
		</table>
	</div>
	<br/>
	
	<span title="${vctConfigured.description}"><input <c:if test="${vctConfigured.propertyValue=='true' || vctConfigured.propertyValue=='TRUE'}">checked='checked'</c:if> type="checkbox" name="config_chkbx" value="1"/><spring:message code="vcttrac.configuration.configuredproperly"/></span>
		
	<br/><br/>
	<input type="button" value="<spring:message code="vcttrac.configuration.save"/>" id="btSave"/>

</form>

<script>

$j(document).ready( function() {
	$j("#btSave").click(function(){
		if(confirm("<spring:message code='vcttrac.surewanttosave'/>"))
			this.form.submit();
		});
});

</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
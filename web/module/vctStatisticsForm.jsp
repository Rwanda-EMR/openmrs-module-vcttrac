<%@ include file="template/localHeader.jsp"%>
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/jquery.treeview.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/scripts/jquery.cookie.js" />
<openmrs:htmlInclude file="/moduleResources/@MODULE_ID@/jquery.treeview.css" />

<openmrs:require privilege="Manage VCT Report customizations" otherwise="/login.htm" redirect="/module/@MODULE_ID@/vctStatistics.htm?page=1" />

<c:if test="${param.page eq null || param.page==''}">
	<c:redirect url="/module/@MODULE_ID@/vctStatistics.htm?page=1"/>
</c:if>

<h2><spring:message code="@MODULE_ID@.statistic.description"/></h2>

<div>
	<%@ include file="template/statisticParameterHeader.jsp"%>
</div>

<div style="width: 94%; border: 1px solid #8FABC7; margin: auto; -moz-border-radius: 3px; padding: 5px;">
	<div id="list_container" style="width: 99%">
	<div id="list_title">
		<div class="list_title_msg"><span title="${searchResultDescription}">${title}</span></div>
		<div class="list_title_bts">
		<openmrs:hasPrivilege privilege="Export Collective Patient Data">
			<form style="display: inline;" action="vctStatistics.htm?page=${param.page}${parameters}&export=csv" method="post">
				<input type="submit" class="list_exportBt" title="<spring:message code="vcttrac.tablelist.exportToCSV"/>" value="<spring:message code="vcttrac.tablelist.CSV"/>"/>
			</form>	
		</openmrs:hasPrivilege>			
		</div>
		<div style="clear:both;"></div>
	</div>
	<c:set var="columns" value="9" scope="page"/>
	<table id="list_data">
		<tr>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.registration.date"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.export.column.number"/></th>
			<openmrs:hasPrivilege privilege="View Patient Names">
				<th class="columnHeader"><spring:message code="@MODULE_ID@.export.column.names"/></th>
			</openmrs:hasPrivilege>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.person.gender"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.age"/></th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.statistic.counselingdone"/> ?</th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.dashboard.tested"/> ?</th>
			<th class="columnHeader"><spring:message code="@MODULE_ID@.statistic.resultavailable"/> ?</th>
			<!-- <c:if test="${param.gotresult=='true'}"><th class="columnHeader"><spring:message code="@MODULE_ID@.statistic.receptiondone"/> ?</th></c:if> -->
			<openmrs:hasPrivilege privilege="View VCT Client result">
				<th class="columnHeader"><spring:message code="@MODULE_ID@.home.result"/></th>
			</openmrs:hasPrivilege>
		</tr>
		<c:if test="${empty clients}">
			<tr>
				<td colspan="${columns}" style="text-align: center;"><spring:message code="vcttrac.tablelist.empty"/></td>
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
				<td class="rowValue ${status.count%2!=0?'even':''}">${((param.page-1)*pageSize)+status.count}.</td>
				<openmrs:hasPrivilege privilege="View Patient Names">
					<td class="rowValue ${status.count%2!=0?'even':''}"><a href="vctClientDashboard.form?clientId=${client.client.personId}">${client.client.personName}</a></td>
				</openmrs:hasPrivilege>
				<td class="rowValue ${status.count%2!=0?'even':''}" style="text-align: center;"><img border="0"
					src="<c:if test="${client.client.gender=='F'}"><openmrs:contextPath/>/images/female.gif</c:if><c:if test="${client.client.gender=='M'}"><openmrs:contextPath/>/images/male.gif</c:if>" /></td>
				<td class="rowValue ${status.count%2!=0?'even':''}">${(client.client.age<1)?'<1':client.client.age} <spring:message code="@MODULE_ID@.dashboard.yrs"/></td>
				<td class="rowValue ${status.count%2!=0?'even':''}" style="text-align: center;"><img border="0"
					src="<c:if test="${client.counselingObs ne null}"><openmrs:contextPath/>/images/checkmark.png</c:if><c:if test="${client.counselingObs==null}"><openmrs:contextPath/>/images/delete.gif</c:if>" />
					<c:if test="${client.counselingObs ne null}">&nbsp;<spring:message code="@MODULE_ID@.date.on"/>&nbsp;<openmrs:formatDate date="${client.counselingObs.obsDatetime}" type="medium" /></c:if>
				</td>
				<td class="rowValue ${status.count%2!=0?'even':''}" style="text-align: center;">
					<c:if test="${client.codeTest ne null}"><img border="0" src="<openmrs:contextPath/>/images/checkmark.png" /></c:if>
					<c:if test="${client.codeTest eq null}"><img border="0" src="<openmrs:contextPath/>/images/delete.gif" /></c:if>
				
					<!-- <c:if test="${vcttag:convsetObsValueByConcept(client.counselingObs, hivTestingDoneConceptId)=='true'}"><img border="0" src="<openmrs:contextPath/>/images/checkmark.png" /></c:if>
					<c:if test="${vcttag:convsetObsValueByConcept(client.counselingObs, hivTestingDoneConceptId)!='true'}"><img border="0" src="<openmrs:contextPath/>/images/delete.gif" /></c:if> -->
				</td>
				<td class="rowValue ${status.count%2!=0?'even':''}" style="text-align: center;">
					<img border="0" src="<c:if test="${client.resultObs ne null}"><openmrs:contextPath/>/images/checkmark.png</c:if><c:if test="${client.resultObs==null}"><openmrs:contextPath/>/images/delete.gif</c:if>" />
					<c:if test="${client.resultObs ne null}">&nbsp;<spring:message code="@MODULE_ID@.date.on"/>&nbsp;<openmrs:formatDate date="${client.resultObs.obsDatetime}" type="medium" /></c:if>
				</td>
				<openmrs:hasPrivilege privilege="View VCT Client result">
					<td class="rowValue ${status.count%2!=0?'even':''}"><span class="${vcttag:convsetObsValueByConcept(client.resultObs, resultOfHivTestConceptId)==positiveString?'lastObsValuePositive':'lastObsValue'}">${vcttag:convsetObsValueByConcept(client.resultObs, resultOfHivTestConceptId)}</span>
					</td>
				</openmrs:hasPrivilege>
			</tr>
		</c:forEach>
	</table>
	<div id="list_footer">
		<div class="list_footer_info">${pageInfos}</div>
		<div class="list_footer_pages">		
			<table>
				<tr>
					<c:if test="${prevPage!=-1}">
						<td width="100px" class="" style="padding:1px 2px 1px 2px; vertical-align: text-top;">
							<a href="vctStatistics.htm?page=1${parameters}"><div class="list_pageNumber" style="text-align: center;">|&lt; <spring:message code="@MODULE_ID@.navigation.first"/></div></a>
						</td>
						<td width="100px" class="" style="padding:1px 2px 1px 2px; vertical-align: text-top;"><a href="vctStatistics.htm?page=${prevPage}${parameters}">
							<div class="list_pageNumber" style="text-align: center;">&lt;&lt; <spring:message code="@MODULE_ID@.navigation.previous"/></div></a>
						</td>
					</c:if>
					<td>
						<span class="list_pageNumberCurrent">${param.page}</span>
					</td>
					<c:if test="${nextPage!=-1}">
						<td width="100px" class="" style="padding:1px 2px 1px 2px; vertical-align: text-top;">
							<a href="vctStatistics.htm?page=${nextPage}${parameters}"><div class="list_pageNumber" style="text-align: center;"><spring:message code="@MODULE_ID@.navigation.next"/> &gt;&gt;</div></a>
						</td>
						<td width="100px" class="" style="padding:1px 2px 1px 2px; vertical-align: text-top;">
							<a href="vctStatistics.htm?page=${lastPage}${parameters}"><div class="list_pageNumber" style="text-align: center;"><spring:message code="@MODULE_ID@.navigation.last"/> &gt;|</div></a>
						</td>
					</c:if>
				</tr>
			</table>		
		</div>
		<div style="clear: both"></div>
	</div>
</div>

</div>


<script>

$(document).ready( function() {

});

</script>


<%@ include file="/WEB-INF/template/footer.jsp"%>
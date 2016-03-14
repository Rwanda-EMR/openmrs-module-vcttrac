<%@ include file="localHeader.jsp"%>

<ul id="menu">
	<li class="first<c:if test='<%= request.getRequestURI().contains("vctOverview") %>'> active</c:if>">
		<a href="vctOverview.htm"><spring:message code="@MODULE_ID@.overview"/></a>
	</li>
	
	<li class="<c:if test='<%= request.getRequestURI().contains("vctTracnetIndicators") %>'>active</c:if>">
		<a href="vctTracnetIndecators.list"><spring:message code="@MODULE_ID@.tracnetIndicators"/></a>
	</li>
	
	<c:if test='<%= request.getRequestURI().contains("vctClientListing") %>'>
		<li class="active">
			<a href="vctClients.list?page=1${parameters}">${title}</a>
		</li>
	</c:if>
	
</ul>
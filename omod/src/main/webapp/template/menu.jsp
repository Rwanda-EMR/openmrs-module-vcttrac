<ul id="menuList" class="treeview-red">
	<li><span><a class="link_page" href="vctStatistics.htm?type=0&page=1"><spring:message code="@MODULE_ID@.statistic.allclient"/></a></span></li>
	
	<li><span><spring:message code="@MODULE_ID@.dashboard.typeofcounseling"/></span>
		<ul>
			<li><span><a class="link_page" href="vctStatistics.htm?type=6&counselingType=1&page=1"><spring:message code="@MODULE_ID@.dashboard.typeofcounseling.individuel"/></a></span></li>
			<li><span><a class="link_page" href="vctStatistics.htm?type=6&counselingType=2&page=1"><spring:message code="@MODULE_ID@.dashboard.typeofcounseling.couple"/></a></span></li>
			<li><span><a class="link_page" href="vctStatistics.htm?type=6&counselingType=3&page=1"><spring:message code="@MODULE_ID@.dashboard.typeofcounseling.notcounseled"/></a></span></li>
		</ul>
	</li>
	
	<li><span><spring:message code="@MODULE_ID@.registration.CivilStatus"/></span>
		<ul>
			<c:forEach items="${civilStatus}" var="cs">
				<li><span><a class="link_page" href="vctStatistics.htm?type=3&attributeTypeId=${csTypeId}&value=${cs.key}&page=1">${cs.value}</a></span></li>
			</c:forEach>					
		</ul>
	</li>
	
	<li><span><spring:message code="@MODULE_ID@.registration.educationLevel"/></span>
		<ul>
			<c:forEach items="${educationLevels}" var="el">
				<li><span><a class="link_page" href="vctStatistics.htm?type=3&attributeTypeId=${elTypeId}&value=${el.key}&page=1">${el.value}</a></span></li>
			</c:forEach>					
		</ul>
	</li>
	
	<li><span><spring:message code="@MODULE_ID@.registration.mainActivity"/></span>
		<ul>
			<c:forEach items="${mainActivities}" var="ma">
				<li><span><a class="link_page" href="vctStatistics.htm?type=3&attributeTypeId=${maTypeId}&value=${ma.key}&page=1">${ma.value}</a></span></li>
			</c:forEach>					
		</ul>
	</li>
	
	<li><span><spring:message code="@MODULE_ID@.hivtest"/></span>
		<ul>
			<li><span><a class="link_page" href="vctStatistics.htm?type=4&tested=yes&page=1"><spring:message code="@MODULE_ID@.dashboard.clienttested"/></a></span></li>
			<li><span><a class="link_page" href="vctStatistics.htm?type=4&tested=no&page=1"><spring:message code="@MODULE_ID@.dashboard.clientNoTtested"/></a></span></li>
		</ul>
	</li>
	
	<li><span><spring:message code="@MODULE_ID@.counseling.reason"/></span>
		<ul>
			<c:forEach items="${whyGetTestedConceptOptions}" var="why">
				<li><span><a class="link_page" href="vctStatistics.htm?type=5&conceptId=${whyGetTestedConceptId}&value=${why.key}&page=1&gotresult=false">${why.value}</a></span></li>
			</c:forEach>					
		</ul>
	</li>
	
	<li><span><spring:message code="@MODULE_ID@.counseling.programOrderedTest"/></span>
		<ul>
			<c:forEach items="${programOrdererConceptOptions}" var="program">
				<li><span><a class="link_page" href="vctStatistics.htm?type=5&conceptId=${programOrdererConceptId}&value=${program.key}&page=1&gotresult=false">${program.value}</a></span></li>
			</c:forEach>					
		</ul>
	</li>
	</ul>
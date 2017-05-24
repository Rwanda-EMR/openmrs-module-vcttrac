<%@ taglib prefix="vct_tag" tagdir="/WEB-INF/tags/module/vcttrac" %>
<%@ taglib prefix="vcttag" uri="/WEB-INF/view/module/vcttrac/taglibs/vcttag.tld" %>
<openmrs:htmlInclude file="/moduleResources/vcttrac/vctstyle.css" />

<style>
	#vctHeaderDiv{
		text-align: left; 
		border-bottom: 2px solid #8FABC7;
		color: #8FABC7;
		margin-left: 5px;
		padding-bottom: 5px;
		cursor: pointer;
	}
</style>

<h2 id="vctHeaderDiv" onclick="window.location.href='vctHome.htm';" title="<spring:message code="vcttrac.goHome"/>"><spring:message code="vcttrac.title.description"/></h2>
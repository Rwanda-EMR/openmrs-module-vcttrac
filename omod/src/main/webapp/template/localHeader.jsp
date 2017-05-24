<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:htmlInclude file="/scripts/calendar/calendar.js" />
<%@ taglib prefix="vcttag" uri="/WEB-INF/view/module/vcttrac/taglibs/vcttag.tld" %>
<%@ taglib prefix="vct_tag" tagdir="/WEB-INF/tags/module/vcttrac" %>

<openmrs:htmlInclude file="/moduleResources/vcttrac/listingstyle.css" />
<openmrs:htmlInclude file="/moduleResources/vcttrac/vctstyle.css" />

<vct_tag:checkConfigurations/>

<style>
	#vctHeaderDiv{
		text-align: left; 
		border-bottom: 2px solid #8FABC7;
		color: #8FABC7;
		margin-left: 5px;
		padding-bottom: 5px;
		cursor: pointer;
	}
	
	.statDiv{
		right: 5px;
		margin-right: 5px;
		margin-top: -31px;
		margin-left: 5px;	
		position: absolute;	
	}
	
	.statDiv span{
		color: #8FABC7;
		cursor: pointer;
		padding: 3px;
		border: 1px solid #8FABC7;
		-moz-border-radius: 3px 3px 0px 0px;
	}
	
	.statDiv span:hover{
		background: #8FABC7;
		color: #ffffff;
	}
</style>

<h2 id="vctHeaderDiv" onclick="window.location.href='vctHome.htm';" title="<spring:message code="vcttrac.goHome"/>"><spring:message code="vcttrac.title.description"/></h2>
<div style="width: 100%; text-align: right;">
	<span class="statDiv">
		<table>
			<tr>
				<openmrs:hasPrivilege privilege="View VCT Client Dashboard">
					<td><span title="<spring:message code="vcttrac.home.searchClient"/>" onclick="window.location.href='vctClientSearch.htm';"><spring:message code="vcttrac.home.searchClient"/></span></td>
				</openmrs:hasPrivilege>
				<openmrs:hasPrivilege privilege="Manage VCT Report customizations">
					<td><span title="<spring:message code="vcttrac.statistic.description"/>" onclick="window.location.href='vctStatistics.htm?page=1';"><spring:message code="vcttrac.statistic.title"/></span></td>
				</openmrs:hasPrivilege>
			</tr>
		</table>
	</span>
</div>
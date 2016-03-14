<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c-rt.tld" %>
<%@tag import="org.openmrs.api.context.Context"%>
<%@tag import="org.openmrs.module.vcttrac.util.VCTConfigurationUtil"%>

<%
	if(Context.getAuthenticatedUser()==null)
		response.sendRedirect(request.getContextPath()+"/login.htm");

	request.getSession().setAttribute("vctConfigured", Context.getAdministrationService().getGlobalPropertyObject("vcttrac.configured"));
	request.getSession().setAttribute("mohtracmodulesConfigured", VCTConfigurationUtil.isConfigured());
%>


<c:if test="${mohtracmodulesConfigured==false}">
	<c:redirect url="/module/mohtracportal/configuration.form"/>
</c:if>

<c:if test="${vctConfigured.propertyValue=='false' || vctConfigured.propertyValue=='FALSE'}">
	<c:redirect url="/module/vcttrac/vctConfigurations.htm"/>
</c:if>
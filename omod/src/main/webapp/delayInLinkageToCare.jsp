<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="template/localHeader.jsp"%>

<h1><spring:message code="vcttrac.delayInLinkageToCare"/></h1>

<input type="button" value="Print" style="float: left;">
<table>
    <thead>
    <tr>
        <th>clientId</th>
        <th>clientName</th>
        <th>sex</th>
        <th>birthDate</th>
        <th>dateTestedForHIV</th>
        <th>telephone</th>
        <th>address</th>
        <th>peerEducator</th>
        <th>peerEducatorTelephone</th>
    </tr>
    </thead>
    <tbody>
        <c:forEach items="${delayedInLinkageClients}" var="client">
            <tr>
                <td>${client.clientId}</td>
                <td>${client.clientName}</td>
                <td>${client.sex}</td>
                <td>${client.birthDate}</td>
                <td>${client.dateTestedForHIV}</td>
                <td>${client.telephone}</td>
                <td>${client.address}</td>
                <td>${client.peerEducator}</td>
                <td>${client.peerEducatorTelephone}</td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="/WEB-INF/template/footer.jsp"%>
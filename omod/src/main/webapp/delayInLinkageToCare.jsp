<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<openmrs:htmlInclude file="/moduleResources/vcttrac/scripts/jquery.tabletoCSV.modified.js" />

<input type="button" value="Print" style="float: right;" onclick="printData()"><button id="export" data-export="export" style="float: right;">CSV</button>
<h2><spring:message code="vcttrac.delayInLinkageToCare"/></h2>

<script type="application/javascript">
    function printData() {
        var divToPrint=document.getElementById("printTable");
        newWin= window.open("");
        newWin.document.write(divToPrint.outerHTML);
        newWin.print();
        newWin.close();
    }

    jQuery(function() {
        jQuery("#export").click(function() {
            jQuery("#printTable").tableToCSV(jQuery("h2").text());
        });
    });

</script>

<table id="printTable">
    <thead>
    <tr class="evenRow">
        <th>Client Id</th>
        <th>Client Name</th>
        <th>Sex</th>
        <th>BirthDate</th>
        <th>HIV Test Date</th>
        <th>Telephone</th>
        <th>Address</th>
        <th>Peer Educator</th>
        <th>Peer Educator Telephone</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${delayedInLinkageClients}" var="client">
        <tr class="evenRow">
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
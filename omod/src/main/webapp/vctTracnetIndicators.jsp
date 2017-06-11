<%@ include file="template/overviewHeader.jsp"%>

<div style="margin: 5px;">
	<div style="float:left; width: 25%">
		<form action="vctTracnetIndecators.list" method="post" class="displayListOption">
			
			<table>
				<tr>
					<td><spring:message code="vcttrac.period"/>&nbsp;<spring:message code="vcttrac.from"/></td>
					<td><input type="text" name="dateFrom" size="11" value="${from}" onclick="showCalendar(this);"/></td>
				</tr>
				<tr>
					<td><spring:message code="vcttrac.to"/></td>
					<td><input type="text" name="dateTo" size="11" value="${to}" onclick="showCalendar(this);"/></td>
				</tr>
				<tr>
					<td><spring:message code="Encounter.location" /></td>
					<td><openmrs_tag:locationField formFieldName="location" initialValue="${defaultLoc}" /></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="<spring:message code="vcttrac.tablelist.refresh"/>"/></td>
				</tr>
			</table>
			
		</form>
	</div>
	
	<div style="float:right; width: 70%">
		<div class="list_container" style="width: 90%">
			<c:forEach items="${registrationEntryPoints}" var="entryPoint">
				<div class="list_title">
					<div class="list_title_msg">${entryPoint.displayName} <spring:message code="vcttrac.title.indicators"/></div>
					<div class="list_title_bts">
						<form style="display: inline;" action="vctTracnetIndecators.list?exportFormat=csv&location=${defaultLoc}&dateFrom=${from}&dateTo=${to}" method="post">
							<input type="submit" class="list_exportBt" title="<spring:message code="vcttrac.tablelist.exportToCSV"/>" value="<spring:message code="vcttrac.tablelist.CSV"/>"/>
						</form>				
					</div>
					<div style="clear:both;"></div>
				</div>
					
				<table class="list_data">
					<tr>
						<td class="columnHeader boldTitle" rowspan="2"><spring:message code="vcttrac.tracnet.criteria"/></td>
						<td class="columnHeader centered boldTitle" colspan="2"><spring:message code="vcttrac.age"/>: &lt;15</td>
						<td class="columnHeader centered boldTitle" colspan="2"><spring:message code="vcttrac.age"/>: 15-24</td>
						<td class="columnHeader centered boldTitle" colspan="2"><spring:message code="vcttrac.age"/>: 25+</td>
					</tr>
					<tr>
						<td class="columnHeader centered"><img border="0" src="<openmrs:contextPath/>/images/female.gif"/></td>
						<td class="columnHeader centered"><img border="0" src="<openmrs:contextPath/>/images/male.gif"/></td>
						<td class="columnHeader centered"><img border="0" src="<openmrs:contextPath/>/images/female.gif"/></td>
						<td class="columnHeader centered"><img border="0" src="<openmrs:contextPath/>/images/male.gif"/></td>
						<td class="columnHeader centered"><img border="0" src="<openmrs:contextPath/>/images/female.gif"/></td>
						<td class="columnHeader centered"><img border="0" src="<openmrs:contextPath/>/images/male.gif"/></td>
					</tr>
					<tr>
						<td class="rowValue"><spring:message code="vcttrac.tracnet.criteria.numberOfNewClientsCounseledAndTestedForHiv"/></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=f&type=${entryPoint.name}&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,entryPoint.name,0,15,'f')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=m&type=${entryPoint.name}&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,entryPoint.name,0,15,'m')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=f&type=${entryPoint.name}&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,0,15,25,'f')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=m&type=${entryPoint.name}&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,0,15,25,'m')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=f&type=${entryPoint.name}&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,0,25,0,'f')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=m&type=${entryPoint.name}&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,0,25,0,'m')}</a></td>
					</tr>
					<tr class="even">
						<td class="rowValue"><spring:message code="vcttrac.tracnet.criteria.numberOfNewClientsTestedAndReceivedResults"/></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=f&type=${entryPoint.name}&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,entryPoint.name,0,15,'f')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=m&type=${entryPoint.name}&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,entryPoint.name,0,15,'m')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=f&type=${entryPoint.name}&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,0,15,25,'f')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=m&type=${entryPoint.name}&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,0,15,25,'m')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=f&type=${entryPoint.name}&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,0,25,0,'f')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=m&type=${entryPoint.name}&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,0,25,0,'m')}</a></td>
					</tr>
					<tr>
						<td class="rowValue"><spring:message code="vcttrac.tracnet.criteria.numberOfHivPositiveClients"/></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=f&type=${entryPoint}&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,entryPoint,0,15,'f')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=m&type=${entryPoint}&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,entryPoint,0,15,'m')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=f&type=${entryPoint}&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,0,15,25,'f')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=m&type=${entryPoint}&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,0,15,25,'m')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=f&type=${entryPoint}&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,0,25,0,'f')}</a></td>
						<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=m&type=${entryPoint}&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,0,25,0,'m')}</a></td>
					</tr>
				</table>
			</c:forEach>
		</div>
		<br/><br/>
		
		<div class="list_container" style="width: 90%">
			<div class="list_title">
				<div class="list_title_msg"><spring:message code="vcttrac.tracnet.couple.indicators"/></div>
				<div class="list_title_bts">
					&nbsp; &nbsp;			
				</div>
				<div style="clear:both;"></div>
			</div>
				
			<table class="list_data">
				<tr>
					<td class="columnHeader boldTitle" rowspan="2"><spring:message code="vcttrac.tracnet.criteria"/></td>
					<td class="columnHeader centered boldTitle" rowspan="2"><spring:message code="vcttrac.tracnet.couple.counseled"/></td>
					<td class="columnHeader centered boldTitle" colspan="3"><spring:message code="vcttrac.tracnet.couple.tested"/></td>
					<!-- <td class="columnHeader centered boldTitle" colspan="4"><spring:message code="vcttrac.tracnet.couple.tested.receivedresult"/></td>
					<td class="columnHeader centered boldTitle" colspan="3"><spring:message code="vcttrac.tracnet.criteria.hivtestresult"/></td> -->
				</tr>
				<tr>
					<td class="columnHeader centered"><spring:message code="vcttrac.tracnet.couple.all"/></td>
					<td class="columnHeader centered"><spring:message code="vcttrac.tracnet.couple.oneoftwo"/> (*)</td>
					<td class="columnHeader centered"><spring:message code="vcttrac.tracnet.couple.none"/></td>
				</tr>
				
				<tr>
					<td class="rowValue"><spring:message code="vcttrac.tracnet.criteria.numberOfCouples"/></td>
					<td class="rowValue centered"><a href="#">${vcttag:couplesCounseled(from,to,defaultLoc)}</a></td>
					<td class="rowValue centered"><a href="#">${vcttag:couplesCounseledAndTested(from,to,defaultLoc,2)}</a></td>
					<td class="rowValue centered"><a href="#">${vcttag:couplesCounseledAndTested(from,to,defaultLoc,1)}</a></td>
					<td class="rowValue centered"><a href="#">${vcttag:couplesCounseledAndTested(from,to,defaultLoc,0)}</a></td>
				</tr>
				
			</table>
			
			<div class="list_footer">
				&nbsp;&nbsp;
			</div>
			
		</div><br/>
		
		
		
	</div>
	
	<div style="clear: both;"></div>
</div>

<%@ include file="/WEB-INF/template/footer.jsp"%>
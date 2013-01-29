<%@ include file="template/overviewHeader.jsp"%>

<div style="margin: 5px;">
	<div style="float:left; width: 25%">
		<form action="vctTracnetIndecators.list" method="post" class="displayListOption">
			
			<table>
				<tr>
					<td><spring:message code="@MODULE_ID@.period"/>&nbsp;<spring:message code="@MODULE_ID@.from"/></td>
					<td><input type="text" name="dateFrom" size="11" value="${from}" onclick="showCalendar(this);"/></td>
				</tr>
				<tr>
					<td><spring:message code="@MODULE_ID@.to"/></td>
					<td><input type="text" name="dateTo" size="11" value="${to}" onclick="showCalendar(this);"/></td>
				</tr>
				<tr>
					<td><spring:message code="Encounter.location" /></td>
					<td><openmrs_tag:locationField formFieldName="location" initialValue="${defaultLoc}" /></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="submit" value="<spring:message code="@MODULE_ID@.tablelist.refresh"/>"/></td>
				</tr>
			</table>
			
		</form>
	</div>
	
	<div style="float:right; width: 70%">
		<div class="list_container" style="width: 90%">
			<div class="list_title">
				<div class="list_title_msg"><spring:message code="@MODULE_ID@.tracnet.vct.indicators"/></div>
				<div class="list_title_bts">
					<form style="display: inline;" action="vctTracnetIndecators.list?exportFormat=csv&location=${defaultLoc}&dateFrom=${from}&dateTo=${to}" method="post">
						<input type="submit" class="list_exportBt" title="<spring:message code="@MODULE_ID@.tablelist.exportToCSV"/>" value="<spring:message code="@MODULE_ID@.tablelist.CSV"/>"/>
					</form>				
				</div>
				<div style="clear:both;"></div>
			</div>
				
			<table class="list_data">
				<tr>
					<td class="columnHeader boldTitle" rowspan="2"><spring:message code="@MODULE_ID@.tracnet.criteria"/></td>
					<td class="columnHeader centered boldTitle" colspan="2"><spring:message code="@MODULE_ID@.age"/>: &lt;15</td>
					<td class="columnHeader centered boldTitle" colspan="2"><spring:message code="@MODULE_ID@.age"/>: 15-24</td>
					<td class="columnHeader centered boldTitle" colspan="2"><spring:message code="@MODULE_ID@.age"/>: 25+</td>
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
					<td class="rowValue"><spring:message code="@MODULE_ID@.tracnet.criteria.numberOfNewClientsCounseledAndTestedForHiv"/></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=f&type=0&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,0,0,15,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=m&type=0&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,0,0,15,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=f&type=0&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,0,15,25,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=m&type=0&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,0,15,25,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=f&type=0&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,0,25,0,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=m&type=0&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,0,25,0,'m')}</a></td>
				</tr>
				<tr class="even">
					<td class="rowValue"><spring:message code="@MODULE_ID@.tracnet.criteria.numberOfNewClientsTestedAndReceivedResults"/></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=f&type=0&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,0,0,15,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=m&type=0&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,0,0,15,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=f&type=0&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,0,15,25,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=m&type=0&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,0,15,25,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=f&type=0&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,0,25,0,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=m&type=0&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,0,25,0,'m')}</a></td>
				</tr>
				<tr>
					<td class="rowValue"><spring:message code="@MODULE_ID@.tracnet.criteria.numberOfHivPositiveClients"/></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=f&type=0&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,0,0,15,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=m&type=0&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,0,0,15,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=f&type=0&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,0,15,25,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=m&type=0&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,0,15,25,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=f&type=0&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,0,25,0,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=m&type=0&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,0,25,0,'m')}</a></td>
				</tr>
				
				<!-- <tr>
					<th class="rowValue inlineTitle" colspan="7"><spring:message code="@MODULE_ID@.tracnet.couple.indicators"/></th>
				</tr>
				<tr>
					<td class="rowValue"><spring:message code="@MODULE_ID@.tracnet.criteria.numberOfCouplesCounseledAndTested"/></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&type=2&criteria=cct">${vcttag:couplesCounseledAndTested(from,to,defaultLoc,1)}</a></td>
					<td class="rowValue centered" colspan="5"></td>
				</tr>
				<tr class="even">
					<td class="rowValue"><spring:message code="@MODULE_ID@.tracnet.criteria.numberOfDiscordantCouples"/></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&type=2&criteria=dc">${vcttag:discordantCouples(from,to,defaultLoc)}</a></td>
					<td class="rowValue centered" colspan="5"></td>
				</tr> -->
				
			</table>
			
			<div class="list_footer">
				&nbsp;&nbsp;
			</div>
			
		</div>
		<br/><br/>
		
		<div class="list_container" style="width: 90%">
			<div class="list_title">
				<div class="list_title_msg"><spring:message code="@MODULE_ID@.tracnet.pit.indicators"/></div>
				<div class="list_title_bts">
					&nbsp;&nbsp;			
				</div>
				<div style="clear:both;"></div>
			</div>
				
			<table class="list_data">
				<tr>
					<td class="columnHeader boldTitle" rowspan="2"><spring:message code="@MODULE_ID@.tracnet.criteria"/></td>
					<td class="columnHeader centered boldTitle" colspan="2"><spring:message code="@MODULE_ID@.age"/>: &lt;15</td>
					<td class="columnHeader centered boldTitle" colspan="2"><spring:message code="@MODULE_ID@.age"/>: 15-24</td>
					<td class="columnHeader centered boldTitle" colspan="2"><spring:message code="@MODULE_ID@.age"/>: 25+</td>
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
					<td class="rowValue"><spring:message code="@MODULE_ID@.tracnet.criteria.numberOfNewClientsCounseledAndTestedForHiv"/></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=f&type=1&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,1,0,15,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=m&type=1&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,1,0,15,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=f&type=1&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,1,15,25,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=m&type=1&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,1,15,25,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=f&type=1&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,1,25,0,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=m&type=1&criteria=cct">${vcttag:newClientCounseledAndTested(from,to,defaultLoc,1,25,0,'m')}</a></td>
				</tr>
				<tr class="even">
					<td class="rowValue"><spring:message code="@MODULE_ID@.tracnet.criteria.numberOfNewClientsTestedAndReceivedResults"/></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=f&type=1&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,1,0,15,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=m&type=1&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,1,0,15,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=f&type=1&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,1,15,25,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=m&type=1&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,1,15,25,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=f&type=1&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,1,25,0,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=m&type=1&criteria=ctrr">${vcttag:newClientTestedAndReceivedResults(from,to,defaultLoc,1,25,0,'m')}</a></td>
				</tr>
				<tr>
					<td class="rowValue"><spring:message code="@MODULE_ID@.tracnet.criteria.numberOfHivPositiveClients"/></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=f&type=1&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,1,0,15,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=0&maxAge=15&gender=m&type=1&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,1,0,15,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=f&type=1&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,1,15,25,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=15&maxAge=25&gender=m&type=1&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,1,15,25,'m')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=f&type=1&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,1,25,0,'f')}</a></td>
					<td class="rowValue centered"><a href="vctClients.list?page=1&dateFrom=${from}&dateTo=${to}&location=${defaultLoc}&minAge=25&gender=m&type=1&criteria=hivpositive">${vcttag:hivPositiveClients(from,to,defaultLoc,1,25,0,'m')}</a></td>
				</tr>
			</table>
			
			<div class="list_footer">
				&nbsp;&nbsp;
			</div>
			
		</div>
		<br/><br/>
		
		<div class="list_container" style="width: 90%">
			<div class="list_title">
				<div class="list_title_msg"><spring:message code="@MODULE_ID@.tracnet.couple.indicators"/></div>
				<div class="list_title_bts">
					&nbsp; &nbsp;			
				</div>
				<div style="clear:both;"></div>
			</div>
				
			<table class="list_data">
				<tr>
					<td class="columnHeader boldTitle" rowspan="2"><spring:message code="@MODULE_ID@.tracnet.criteria"/></td>
					<td class="columnHeader centered boldTitle" rowspan="2"><spring:message code="@MODULE_ID@.tracnet.couple.counseled"/></td>
					<td class="columnHeader centered boldTitle" colspan="3"><spring:message code="@MODULE_ID@.tracnet.couple.tested"/></td>
					<!-- <td class="columnHeader centered boldTitle" colspan="4"><spring:message code="@MODULE_ID@.tracnet.couple.tested.receivedresult"/></td>
					<td class="columnHeader centered boldTitle" colspan="3"><spring:message code="@MODULE_ID@.tracnet.criteria.hivtestresult"/></td> -->
				</tr>
				<tr>
					<td class="columnHeader centered"><spring:message code="@MODULE_ID@.tracnet.couple.all"/></td>
					<td class="columnHeader centered"><spring:message code="@MODULE_ID@.tracnet.couple.oneoftwo"/> (*)</td>
					<td class="columnHeader centered"><spring:message code="@MODULE_ID@.tracnet.couple.none"/></td>
					<!-- <td class="columnHeader centered">All</td>
					<td class="columnHeader centered">1 of 2</td>
					<td class="columnHeader centered">(*)</td>
					<td class="columnHeader centered">None</td>
					<td class="columnHeader centered"><spring:message code="@MODULE_ID@.tracnet.criteria.negativeCouples"/></td>
					<td class="columnHeader centered"><spring:message code="@MODULE_ID@.tracnet.criteria.discordantCouples"/></td>
					<td class="columnHeader centered"><spring:message code="@MODULE_ID@.tracnet.criteria.positiveCouples"/></td> -->
				</tr>
				
				<tr>
					<td class="rowValue"><spring:message code="@MODULE_ID@.tracnet.criteria.numberOfCouples"/></td>
					<td class="rowValue centered"><a href="#">${vcttag:couplesCounseled(from,to,defaultLoc)}</a></td>
					<td class="rowValue centered"><a href="#">${vcttag:couplesCounseledAndTested(from,to,defaultLoc,2)}</a></td>
					<td class="rowValue centered"><a href="#">${vcttag:couplesCounseledAndTested(from,to,defaultLoc,1)}</a></td>
					<td class="rowValue centered"><a href="#">${vcttag:couplesCounseledAndTested(from,to,defaultLoc,0)}</a></td>
					<!-- <td class="rowValue centered"><a href="#">-</a></td>
					<td class="rowValue centered"><a href="#">-</a></td>
					<td class="rowValue centered"><a href="#">-</a></td>
					<td class="rowValue centered"><a href="#">-</a></td>
					<td class="rowValue centered"><a href="#">-</a></td>
					<td class="rowValue centered"><a href="#">-</a></td> -->
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
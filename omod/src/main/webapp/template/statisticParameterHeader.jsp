<div>
<form action="vctStatistics.htm?page=1" method="post" id="form_params">
	<div class="displayListOption">
		<table style="width: 100%;">
			<tr>
				<td style="vertical-align: text-top; width: 33%;">
					<span style="margin-left: 5px; display: inline;">
						<table>
							<tr>
								<td><spring:message code="Encounter.location"/></td>
								<td><openmrs_tag:locationField formFieldName="location" initialValue="${param.location}" /></td>
							</tr>
							<tr>
								<td><spring:message code="vcttrac.registration.reference"/></td>
								<td><select name="reference" id="reference_id">
									<option value="2">--</option>
									<option value="0" <c:if test="${param.reference==0}">selected='selected'</c:if>><spring:message code="vcttrac.vct"/></option>
									<option value="1" <c:if test="${param.reference==1}">selected='selected'</c:if>><spring:message code="vcttrac.pit"/></option>
								</select></td>
							</tr>
							<tr>
								<td><spring:message code="vcttrac.export.column.registrationdate"/></td>
								<td><spring:message code="vcttrac.from"/> <input value="${param.dateFrom}" type="text" name="dateFrom" size="11" onclick="showCalendar(this);"/> <spring:message code="vcttrac.to"/> <input value="${param.dateTo}" type="text" name="dateTo" size="11" onclick="showCalendar(this);"/></td>
							</tr>
						</table>
					</span>
				</td>
				<td style="width: 33%;">
					<span style="margin-left: 5px; display: inline;">
						<table>
							<tr>
								<td><spring:message code="vcttrac.person.gender"/></td>
								<td><select name="gender">
									<option value="">--</option>
									<option value="f" <c:if test="${param.gender=='f'}">selected='selected'</c:if>><spring:message code="vcttrac.person.female"/></option>
									<option value="m" <c:if test="${param.gender=='m'}">selected='selected'</c:if>><spring:message code="vcttrac.person.male"/></option>
								</select></td>
							</tr>
							<tr>
								<td><spring:message code="vcttrac.age"/></td>
								<td><spring:message code="vcttrac.between"/> <input value="${param.minAge}" type="text" name="minAge" size="3" style="text-align: right;"/> <spring:message code="vcttrac.dashboard.years"/> <spring:message code="vcttrac.and"/> <input value="${param.maxAge}" type="text" name="maxAge" size="3" style="text-align: right;"/> <spring:message code="vcttrac.dashboard.years"/></td>
							</tr>
						</table>
					</span>
				</td>
				
				<td style="width: 33%;">
					<span style="margin-left: 25px; display: inline;">
						<table>
							<tr>
								<td><spring:message code="vcttrac.registration.CivilStatus"/></td>
								<td><select name="civilStatus">
									<option value="">--</option>
									<c:forEach items="${civilStatus}" var="cs">
										<option value="${cs.key}" <c:if test="${param.civilStatus==cs.key}">selected='selected'</c:if>>${cs.value}</option>
									</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td><spring:message code="vcttrac.registration.educationLevel"/></td>
								<td><select name="educationLevel">
									<option value="">--</option>
									<c:forEach items="${educationLevels}" var="el">
										<option value="${el.key}" <c:if test="${param.educationLevel==el.key}">selected='selected'</c:if>>${el.value}</option>
									</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td><spring:message code="vcttrac.registration.mainActivity"/></td>
								<td><select name="mainActivity">
									<option value="">--</option>
									<c:forEach items="${mainActivities}" var="ma">
										<option value="${ma.key}" <c:if test="${param.mainActivity==ma.key}">selected='selected'</c:if>>${ma.value}</option>
									</c:forEach>
								</select></td>
							</tr>
						</table>
					</span>
				</td>
			</tr>
		</table>
	</div>
	
	<div class="displayListOption">
		<table style="width: 100%;">
			<tr>
				<td style="vertical-align: text-top; width: 45%;">
					<span style="margin-left: 5px; display: inline;">
						<table>
							<tr>
								<td><spring:message code="vcttrac.dashboard.typeofcounseling"/></td>
								<td><select name="counselingType">
									<option value="">--</option>
									<option value="1" <c:if test="${param.counselingType==1}">selected='selected'</c:if>><spring:message code="vcttrac.dashboard.typeofcounseling.individuel"/></option>
									<option value="2" <c:if test="${param.counselingType==2}">selected='selected'</c:if>><spring:message code="vcttrac.dashboard.typeofcounseling.couple"/></option>
									<option value="3" <c:if test="${param.counselingType==3}">selected='selected'</c:if>><spring:message code="vcttrac.dashboard.typeofcounseling.notcounseled"/></option>
								</select></td>
							</tr>
							<tr>
								<td><spring:message code="vcttrac.counseling.programOrderedTest"/></td>
								<td><select name="program">
									<option value="">--</option>
									<c:forEach items="${programOrdererConceptOptions}" var="program">
										<option value="${program.key}" <c:if test="${param.program==program.key}">selected='selected'</c:if>>${program.value}</option>
									</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td><spring:message code="vcttrac.counseling.reason"/></td>
								<td><select name="whyTested">
									<option value="">--</option>
									<c:forEach items="${whyGetTestedConceptOptions}" var="why">
										<option value="${why.key}" <c:if test="${param.whyTested==why.key}">selected='selected'</c:if>>${why.value}</option>
									</c:forEach>
								</select></td>
							</tr>
						</table>
					</span>
				</td>
				
				<td style="width: 45%;"><span style="margin-left: 25px; display: inline;">
						<table>
							<tr>
								<td><spring:message code="vcttrac.dashboard.tested"/> ?</td>
								<td><select name="tested">
									<option value="">--</option>
									<option value="yes" <c:if test="${param.tested=='yes'}">selected='selected'</c:if>><spring:message code="vcttrac.export.column.yes"/></option>
									<option value="no" <c:if test="${param.tested=='no'}">selected='selected'</c:if>><spring:message code="vcttrac.export.column.no"/></option>
								</select></td>
							</tr>
							<openmrs:hasPrivilege privilege="View VCT Client result">
								<tr>
									<td><spring:message code="vcttrac.result.resulthivtest"/></td>
									<td><select name="testResult">
										<option value="">--</option>
										<c:forEach items="${resultOfHivTestConceptOptions}" var="rslt">
											<option value="${rslt.key}" <c:if test="${param.testResult==rslt.key}">selected='selected'</c:if>>${rslt.value}</option>
										</c:forEach>
									</select></td>
								</tr>
							</openmrs:hasPrivilege>
							<tr>
								<td><spring:message code="vcttrac.client"/></td>
								<td><select name="gotResult">
									<option value="">--</option>
									<option value="1" <c:if test="${param.gotResult=='1'}">selected='selected'</c:if>><spring:message code="vcttrac.result.received"/></option>
									<option value="0" <c:if test="${param.gotResult=='0'}">selected='selected'</c:if>><spring:message code="vcttrac.result.waiting"/></option>
								</select></td>
							</tr>
						</table>
					</span>
				</td>
			</tr>
		</table>
	</div>
	
	<input class="list_exportBt" style="min-width: 100px;" type="submit" value="<spring:message code="vcttrac.tablelist.refresh"/>"/>
	<br/><br/>
</form>
	
</div>
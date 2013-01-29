<%@ include file="template/localHeader.jsp"%>

<div style="margin-top: 70px;">

	<div id="divNID" style=" width: 50%; margin-left: auto; margin-right: auto;">
		<form action="vctPreRegistrationCheckup.htm?type=${param.type}"
			method="get">
			<fieldset style="-moz-border-radius: 3px;">
				<legend><b>&nbsp;<spring:message
					code="@MODULE_ID@.preregistration.findclintusingnid" />&nbsp;</b></legend>
				<table>
					<tr>
						<td colspan="2">
						<div id="noNIDError"></div>
						</td>
						<td><input type="hidden" name="type" value="${param.type}" /></td>
					</tr>
					<tr>
						<td><spring:message code="@MODULE_ID@.preregistration.enter.nid"/></td>
						<td><input autocomplete="off" type="text" name="nid" id="nid" size="40" /></td>
						<td><span id="spanId"><input type="submit" disabled="disabled" id="btSubmit"
							value="<spring:message code="@MODULE_ID@.preregistration.find"/>"
							style="min-width: 100px;" /></span></td>
					</tr>
					<tr>
						<td></td>
						<td><span id="idFormat">9 9999 9 9999999 9 99</span></td>
						<td></td>
					</tr>
				</table>
			</fieldset>
		</form>
		&nbsp;<a href="#" id="linkOtherIds"><spring:message code="@MODULE_ID@.preregistration.search.medicalrecord"/></a>
	</div>
	
	<div id="divOtherID" style="display: none; width: 50%; margin-left: auto; margin-right: auto;">
		<form action="vctPreRegistrationCheckup.htm?type=${param.type}"
			method="get">
			<fieldset style="-moz-border-radius: 3px;">
				<legend><b>&nbsp;<spring:message code="@MODULE_ID@.preregistration.findclintusingid" />&nbsp;</b></legend>
				<table>
					<tr>
						<td colspan="2">
						<div id="noNIDError"></div>
						</td>
						<td><input type="hidden" name="type" value="${param.type}" /></td>
					</tr>
					<tr>
						<td>&nbsp;<b><spring:message code="@MODULE_ID@.preregistration.identifier.type.choice" /></b></td>
						<td>&nbsp;<b><spring:message code="@MODULE_ID@.preregistration.identifier.location" /></b></td>
						<td>&nbsp;<b><spring:message code="@MODULE_ID@.preregistration.identifier" /></b></td>
						<td></td>
					</tr>
					<tr>
						<td>
							<select name="idType" id="idType">
								<c:forEach items="${patientIdentifierTypes}" var="pit">
									<c:if test="${nationalIdType!=pit.key}"><option value="${pit.key}">${pit.value}</option></c:if>
								</c:forEach>
							</select>
						</td>
						<td>
							<select name="identifierLocation" id="identifierLocation">
								<c:forEach items="${locations}" var="loc">
									<option value="${loc.key}" <c:if test="${loc.key==defaultLocationId}">selected='selected'</c:if>>${loc.value}</option>
								</c:forEach>
							</select>
						</td>
						<td><input autocomplete="off" type="text" name="ptIdentifier" id="ptIdentifierId" size="30" /></td>
						<td><input type="submit" id="btSubmitOther"
							value="<spring:message code="@MODULE_ID@.preregistration.find"/>"
							style="min-width: 100px;" /></td>
					</tr>
				</table>
			</fieldset>
		</form>
		&nbsp;<a href="#" id="linkNId"><spring:message code="@MODULE_ID@.preregistration.search.nid"/></a>
	</div>
	

</div>

<script type="text/javascript">
	$(document)
			.ready(
					function() {
						$("#linkNId").click(function(){
							$("#divOtherID").hide();
							$("#divNID").show();
						});

						$("#linkOtherIds").click(function(){
							$("#divNID").hide();
							$("#divOtherID").show();
						});
						
						$("#nid").focus();

						$("#nid")
								.keyup(
										function(e) {
											var val = $("#nid").val();
											var currentValue = $.trim(val);

											if (currentValue.length != 21) {
												$("#btSubmit").attr("disabled",
														"true");
											} else {
												var nid=$("#nid").val();
												formatNID(nid);
												if ($("#nid").val().length == 21) {
													$("#btSubmit").removeAttr('disabled');
													$("#btSubmit").focus();
												}
											}

											if (currentValue.length == 18) {
												if (e.which != 8
														&& e.which != 0
														&& (e.which < 48 || e.which > 57)) {
													e.preventDefault();
												} else
													$("#nid").val(
															currentValue + " ");
											} else if (currentValue.length == 16) {
												if (e.which != 8
														&& e.which != 0
														&& (e.which < 48 || e.which > 57)) {
													e.preventDefault();
												} else
													$("#nid").val(
															currentValue + " ");
											} else if (currentValue.length == 8) {
												if (e.which != 8
														&& e.which != 0
														&& (e.which < 48 || e.which > 57)) {
													e.preventDefault();
												} else
													$("#nid").val(
															currentValue + " ");
											} else if (currentValue.length == 6) {
												if (e.which != 8
														&& e.which != 0
														&& (e.which < 48 || e.which > 57)) {
													e.preventDefault();
												} else
													$("#nid").val(
															currentValue + " ");
											} else if (currentValue.length == 1) {
												if (e.which != 8
														&& e.which != 0
														&& (e.which < 48 || e.which > 57)) {
													e.preventDefault();
												} else
													$("#nid").val(
															currentValue + " ");
											}
										});

						$("#nid").keydown(
								function(e) {
									if (e.which != 8 && e.which != 0
											&& (e.which < 48 || e.which > 57)) {
										e.preventDefault();
									} else {
										var val = $("#nid").val();
										var currentValue = $.trim(val);

										if (currentValue.length >= 21) {
											if (e.which != 8 && e.which != 0) {
												e.preventDefault();
											}
										} else if (currentValue.length == 21) {
											if (e.which != 8 && e.which != 0) {
												e.preventDefault();
											}
										}
									}
								});

						$("#btSubmit")
								.click(
										function() {
											if ($("#nid").val() != "") {
												$("#noNIDError").html("");
												$("#noNIDError").removeClass(
														"error");
												this.form.submit();
											} else {
												$("#noNIDError")
														.html(
																"<spring:message code='@MODULE_ID@.error.noNID'/>");
												$("#noNIDError").addClass(
														"error");
											}
										});

					});
	
	function formatNID(nid) {
		var currentValue = $.trim(nid);
		var validNID="";
		for(var i=0;i<currentValue.length;i++){
			if(currentValue.charAt(i)!=' ')
				validNID+=currentValue.charAt(i);
			switch(validNID.length){
				case 21:$("#nid").val(validNID);return;break;
				case 18:validNID+=' ';break;
				case 16:validNID+=' ';break;
				case 8:validNID+=' ';break;
				case 6:validNID+=' ';break;
				case 1:validNID+=' ';break;
			}
			
		}
		$("#nid").val(validNID);
	}
</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>
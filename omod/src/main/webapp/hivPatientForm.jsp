<%@ include file="template/localHeader.jsp"%>

<openmrs:require privilege="Manage VCT/PIT Clients registration" otherwise="/login.htm" redirect="/module/vcttrac/vctHome.htm" />

<script type="text/javascript">
    var $j = jQuery.noConflict();
</script>

<div style="width: 90%; margin-left: auto; margin-right: auto;">

    <h2>
        <spring:message code="mohtracportal.welcome.createPatient"/>
    </h2>

    <form method="post">
        <b class="boxHeader"><spring:message code="vcttrac.registration.clientName"/></b>
        <div class="box">
            <div id="errorDivNewId" style="margin-bottom: 5px;"></div>
            <div style="float: left; width: 45%;">
                <table>
                    <tr>
                        <td><input type="hidden" name="registrationEntryPoint" value="TODO_reerer"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.registration.nid"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><input type="text" name="nid" value="" size="40"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><spring:message code="PersonName.familyName"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><input type="text" size="30" name="familyName" id="familyNameId" style="text-transform: uppercase;"/></td>
                        <td><span id="familyNameError"></span></td>
                    </tr>
                    <tr>
                        <td><spring:message code="PersonName.middleName"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><input type="text" size="30" name="middleName"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><spring:message code="PersonName.givenName"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><input type="text" size="30" name="givenName" id="givenNameId"/></td>
                        <td><span id="givenNameError"></span></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.person.gender"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><input type="radio" name="gender" id="gender-F" value="F"/><label for="gender-F"><spring:message code="vcttrac.person.female"/></label>
                            <input type="radio" name="gender" id="gender-M" value="M"/><label for="gender-M"><spring:message code="vcttrac.person.male"/></label>
                        </td>
                        <td><span id="genderError"></span></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.person.birthdate"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><input type="text" size="11" name="birthdate" id="birthdateId" onclick="showCalendar(this)"/></td>
                        <td><span id="birthdateError"></span></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.person.phoneNumber"/></td>
                        <td></td>
                        <td><input type="text" name="phoneNumber"/></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.person.contactPerson"/></td>
                        <td></td>
                        <td><input type="text" name="contactPerson"/></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.person.contactPersonPhoneNumber"/></td>
                        <td></td>
                        <td><input type="text" name="contactPersonPhoneNumber"/></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.person.peerEducator"/></td>
                        <td></td>
                        <td><input type="text" name="peerEducator"/></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.person.peerEducatorPhoneNumber"/></td>
                        <td></td>
                        <td><input type="text" name="peerEducatorPhoneNumber"/></td>
                    </tr>
                </table>
            </div>
            <div style="float: right; width: 45%;">
                <table>
                    <tr>
                        <td>Registration <spring:message code="vcttrac.date"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><input type="text" size="11" name="registrationDate" id="registrationDateId" onclick="showCalendar(this)" value="${todayDate}"/></td>
                        <td><span id="registrationDateError"></span></td>
                    </tr>
                    <tr>
                        <td>HIV Test <spring:message code="vcttrac.date"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><input type="text" size="11" name="hivTestDate" onclick="showCalendar(this)" value="${todayDate}"/></td>
                        <td></td>
                    </tr>
                    <tr>
                        <td><spring:message code="Encounter.location"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><openmrs_tag:locationField formFieldName="location" initialValue="${locationId}"/></td>
                        <td><span id="locationError"></span></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.registration.codeclient"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><input type="text" size="18" name="codeClient" id="codeClientId" autocomplete="off"/></td>
                        <td><span id="codeClientError"></span></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.regEntryPoint"/></td>
                        <td></td>
                        <td>
                            <select name="reference" id="reference_id">
                                <c:forEach items="${registrationEntryPoints}" var="entryPoint">
                                    <option value="${entryPoint.name}">${entryPoint.displayName}</option>
                                </c:forEach>
                            </select>
                        </td>
                        <td></td>
                    </tr>
                </table>
            </div>
            <div style="clear: both;"></div>
        </div>
        <br/>
        <b class="boxHeader"><spring:message code="vcttrac.registration.clientInformation"/></b>
        <div class="box">
            <div style="float: left; width: 45%;">
                <table>
                    <spring:nestedPath path="location">
                        <openmrs:portlet url="addressLayout" id="addressPortlet" size="full" parameters="layoutShowTable=false|layoutShowExtended=false|layoutShowErrors=false" />
                    </spring:nestedPath>
                </table>
            </div>
            <div style="float: right; width: 45%;">
                <table>
                    <tr>
                        <td><spring:message code="vcttrac.registration.CivilStatus"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><select name="civilStatus" id="civilStatusId"><option value="0">--</option>
                            <c:forEach items="${civilStatus}" var="cs">
                                <option value="${cs.key}">${cs.value}</option>
                            </c:forEach>
                        </select></td>
                        <td><span id="civilStatusError"></span></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.registration.educationLevel"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><select name="educationLevel" id="educationLevelId"><option value="0">--</option>
                            <c:forEach items="${educationLevels}" var="el">
                                <option value="${el.key}">${el.value}</option>
                            </c:forEach>
                        </select></td>
                        <td><span id="educationLevelError"></span></td>
                    </tr>
                    <tr>
                        <td><spring:message code="vcttrac.registration.mainActivity"/></td>
                        <td><span class="displayHelp"><img border="0" src="<openmrs:contextPath/>/moduleResources/vcttrac/images/help.gif" title="<spring:message code="vcttrac.help"/>"/></span></td>
                        <td><select name="mainActivity" id="mainActivityId"><option value="0">--</option>
                            <c:forEach items="${mainActivities}" var="ma">
                                <option value="${ma.key}">${ma.value}</option>
                            </c:forEach>
                        </select></td>
                        <td><span id="mainActivityError"></span></td>
                    </tr>
                </table>
            </div>
        </div>
        <div style="clear: both;"></div>

        <br/>

        &nbsp;&nbsp;<input style="min-width: 100px;" type="submit" id="btSave" value="<spring:message code="general.save"/>"/>

    </form>
</div>

<script type="text/javascript">

    $j(document).ready( function() {

        if($j("#existOrNewId").val()=="0")
            $j("#familyNameId").focus();
        else $j("#input_nid").focus();

        // NID validation and formatting
        $j("#input_nid")
                .keyup(
                        function(e) {
                            var val = $j("#input_nid").val();
                            var currentValue = $j.trim(val);

                            if (currentValue.length != 21) {
                                $j("#btSave2").attr("disabled",
                                        "true");
                            } else {
                                var nid=$j("#input_nid").val();
                                formatNID(nid);
                                if ($j("#input_nid").val().length == 21) {
                                    $j("#btSave2").removeAttr('disabled');
                                    $j("#btSave2").focus();
                                }
                            }

                            if (currentValue.length == 18) {
                                if (e.which != 8
                                        && e.which != 0
                                        && (e.which < 48 || e.which > 57)) {
                                    e.preventDefault();
                                } else
                                    $j("#input_nid").val(
                                            currentValue + " ");
                            } else if (currentValue.length == 16) {
                                if (e.which != 8
                                        && e.which != 0
                                        && (e.which < 48 || e.which > 57)) {
                                    e.preventDefault();
                                } else
                                    $j("#input_nid").val(
                                            currentValue + " ");
                            } else if (currentValue.length == 8) {
                                if (e.which != 8
                                        && e.which != 0
                                        && (e.which < 48 || e.which > 57)) {
                                    e.preventDefault();
                                } else
                                    $j("#input_nid").val(
                                            currentValue + " ");
                            } else if (currentValue.length == 6) {
                                if (e.which != 8
                                        && e.which != 0
                                        && (e.which < 48 || e.which > 57)) {
                                    e.preventDefault();
                                } else
                                    $j("#input_nid").val(
                                            currentValue + " ");
                            } else if (currentValue.length == 1) {
                                if (e.which != 8
                                        && e.which != 0
                                        && (e.which < 48 || e.which > 57)) {
                                    e.preventDefault();
                                } else
                                    $j("#input_nid").val(
                                            currentValue + " ");
                            }
                        });

        $j("#input_nid").keydown(
                function(e) {
                    if (e.which != 8 && e.which != 0
                            && (e.which < 48 || e.which > 57)) {
                        e.preventDefault();
                    } else {
                        var val = $j("#input_nid").val();
                        var currentValue = $j.trim(val);

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

        $j("#btSave")
                .click(
                        function() {
                            if ($j("#input_nid").val() != "") {
                                $j("#noNIDError").html("");
                                $j("#noNIDError").removeClass(
                                        "error");
                            } else {
                                $j("#noNIDError")
                                        .html(
                                                "<spring:message code='vcttrac.error.noNID'/>");
                                $j("#noNIDError").addClass(
                                        "error");
                                if(validateNewFormFields()){
                                    if(confirm("<spring:message code='vcttrac.surewanttosave'/>"))
                                        this.form.submit();
                                }
                            }
                        });

    });

    function formatNID(nid) {
        var currentValue = $j.trim(nid);
        var validNID="";
        for(var i=0;i<currentValue.length;i++){
            if(currentValue.charAt(i)!=' ')
                validNID+=currentValue.charAt(i);
            switch(validNID.length){
                case 21:$j("#input_nid").val(validNID);return;break;
                case 18:validNID+=' ';break;
                case 16:validNID+=' ';break;
                case 8:validNID+=' ';break;
                case 6:validNID+=' ';break;
                case 1:validNID+=' ';break;
            }

        }
        $j("#input_nid").val(validNID);
    }

    function validateExistingFormFields(){
        var valid=true;
        if($j("#codeClient_AId").val()==''){
            $j("#codeClient_AError").html("*");
            $j("#codeClient_AError").addClass("error");
            valid=false;
        } else {
            $j("#codeClient_AError").html("");
            $j("#codeClient_AError").removeClass("error");
        }

        if($j("#registrationDate_AId").val()==''){
            $j("#registrationDate_AError").html("*");
            $j("#registrationDate_AError").addClass("error");
            valid=false;
        } else {
            $j("#registrationDate_AError").html("");
            $j("#registrationDate_AError").removeClass("error");
        }

        if(document.getElementsByName("location_A")[0].value==''){
            $j("#location_AError").html("*");
            $j("#location_AError").addClass("error");
            valid=false;
        } else {
            $j("#location_AError").html("");
            $j("#location_AError").removeClass("error");
        }

        if(document.getElementsByName("client")[0].value==''){
            $j("#clientError").html("*");
            $j("#clientError").addClass("error");
            valid=false;
        } else {
            $j("#clientError").html("");
            $j("#clientError").removeClass("error");
        }

        if(!valid){
            $j("#errorDivChooseId").html("<spring:message code='vcttrac.fillbeforesubmit'/>");
            $j("#errorDivChooseId").addClass("error");
        } else {
            $j("#errorDivChooseId").html("");
            $j("#errorDivChooseId").removeClass("error");
        }

        return valid;
    }

    function validateNewFormFields(){
        var valid=true;
        if($j("#codeClientId").val()==''){
            $j("#codeClientError").html("*");
            $j("#codeClientError").addClass("error");
            valid=false;
        } else {
            $j("#codeClientError").html("");
            $j("#codeClientError").removeClass("error");
        }

        if($j("#registrationDateId").val()==''){
            $j("#registrationDateError").html("*");
            $j("#registrationDateError").addClass("error");
            valid=false;
        } else {
            $j("#registrationDateError").html("");
            $j("#registrationDateError").removeClass("error");
        }

        if(document.getElementsByName("location")[0].value==''){
            $j("#locationError").html("*");
            $j("#locationError").addClass("error");
            valid=false;
        } else {
            $j("#locationError").html("");
            $j("#locationError").removeClass("error");
        }

        if($j("#givenNameId").val()==''){
            $j("#givenNameError").html("*");
            $j("#givenNameError").addClass("error");
            valid=false;
        } else {
            $j("#givenNameError").html("");
            $j("#givenNameError").removeClass("error");
        }

        if($j("#familyNameId").val()==''){
            $j("#familyNameError").html("*");
            $j("#familyNameError").addClass("error");
            valid=false;
        } else {
            $j("#familyNameError").html("");
            $j("#familyNameError").removeClass("error");
        }

        if($j("#birthdateId").val()==''){
            $j("#birthdateError").html("*");
            $j("#birthdateError").addClass("error");
            valid=false;
        } else {
            $j("#birthdateError").html("");
            $j("#birthdateError").removeClass("error");
        }

        if($j("#gender-M").is(':checked')==false && $j("#gender-F").is(':checked')==false){
            $j("#genderError").html("*");
            $j("#genderError").addClass("error");
            valid=false;
        } else {
            $j("#genderError").html("");
            $j("#genderError").removeClass("error");
        }

        if(!valid){
            $j("#errorDivNewId").html("<spring:message code='vcttrac.fillbeforesubmit'/>");
            $j("#errorDivNewId").addClass("error");
        } else {
            $j("#errorDivNewId").html("");
            $j("#errorDivNewId").removeClass("error");
        }

        return valid;
    }

</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>

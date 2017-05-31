package org.openmrs.module.vcttrac;

import java.text.SimpleDateFormat;

/**
 * Kind as a way to produce a similar VCT client who haven't linked to care, this object contains final UI consumable information about {@link org.openmrs.module.vcttrac.VCTClient}
 *
 * Created by k-joseph on 31/05/2017.
 */
public class VCTClientReport {
    public SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy");
    private Integer clientId;
    private String clientName;
    private String sex;
    private String birthDate;
    private String dateTestedForHIV;
    private Integer daysSinceHIVToDiagnosis;
    private String telephone;
    private String address;
    private String peerEducator;
    private String peerEducatorTelephone;

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getDateTestedForHIV() {
        return dateTestedForHIV;
    }

    public void setDateTestedForHIV(String dateTestedForHIV) {
        this.dateTestedForHIV = dateTestedForHIV;
    }

    public Integer getDaysSinceHIVToDiagnosis() {
        return daysSinceHIVToDiagnosis;
    }

    public void setDaysSinceHIVToDiagnosis(Integer daysSinceHIVToDiagnosis) {
        this.daysSinceHIVToDiagnosis = daysSinceHIVToDiagnosis;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPeerEducator() {
        return peerEducator;
    }

    public void setPeerEducator(String peerEducator) {
        this.peerEducator = peerEducator;
    }

    public String getPeerEducatorTelephone() {
        return peerEducatorTelephone;
    }

    public void setPeerEducatorTelephone(String peerEducatorTelephone) {
        this.peerEducatorTelephone = peerEducatorTelephone;
    }
}

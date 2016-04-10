package com.microideation.app.keywordpro.dictionary;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sandheepgr on 17/12/15.
 */
public class InboundSMSRequest {

    private String mobile;

    private String smsText;

    private String key;

    private String subject;

    private String accessCode;

    private String transId;

    private HashMap<String,String> metaParams;

    private List<String> subjectFields;

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSmsText() {
        return smsText;
    }

    public void setSmsText(String smsText) {
        this.smsText = smsText;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getSubjectFields() {
        return subjectFields;
    }

    public void setSubjectFields(List<String> subjectFields) {
        this.subjectFields = subjectFields;
    }

    public HashMap<String, String> getMetaParams() {
        return metaParams;
    }

    public void setMetaParams(HashMap<String, String> metaParams) {
        this.metaParams = metaParams;
    }

    @Override
    public String toString() {
        return "InboundSMSRequest{" +
                "mobile='" + mobile + '\'' +
                ", smsText='" + smsText + '\'' +
                ", key='" + key + '\'' +
                ", subject='" + subject + '\'' +
                ", accessCode='" + accessCode + '\'' +
                ", transId='" + transId + '\'' +
                ", metaParams=" + metaParams +
                ", subjectFields=" + subjectFields +
                '}';
    }
}

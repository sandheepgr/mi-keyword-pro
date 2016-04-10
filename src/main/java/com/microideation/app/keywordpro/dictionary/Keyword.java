package com.microideation.app.keywordpro.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by sandheepgr on 10/12/15.
 */
public class Keyword {

    private String key;

    private String fieldSeparator;

    private Integer minFieldCount = 0;

    private String apiMethod;

    private String apiUrl;

    private List<String> alias = new ArrayList<>(0);

    private List<KeywordSubjectField> keywordSubjectFields;

    private Map<String,String> apiMappings;

    public Integer getMinFieldCount() {
        return minFieldCount;
    }

    public void setMinFieldCount(Integer minFieldCount) {
        this.minFieldCount = minFieldCount;
    }

    public Map<String, String> getApiMappings() {
        return apiMappings;
    }

    public void setApiMappings(Map<String, String> apiMappings) {
        this.apiMappings = apiMappings;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public List<String> getAlias() {
        return alias;
    }

    public void setAlias(List<String> alias) {
        this.alias = alias;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getFieldSeparator() {
        return fieldSeparator;
    }

    public void setFieldSeparator(String fieldSeparator) {
        this.fieldSeparator = fieldSeparator;
    }

    public List<KeywordSubjectField> getKeywordSubjectFields() {
        return keywordSubjectFields;
    }

    public void setKeywordSubjectFields(List<KeywordSubjectField> keywordSubjectFields) {
        this.keywordSubjectFields = keywordSubjectFields;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "key='" + key + '\'' +
                ", fieldSeparator='" + fieldSeparator + '\'' +
                ", minFieldCount=" + minFieldCount +
                ", apiMethod='" + apiMethod + '\'' +
                ", apiUrl='" + apiUrl + '\'' +
                ", alias=" + alias +
                ", keywordSubjectFields=" + keywordSubjectFields +
                ", apiMappings=" + apiMappings +
                '}';
    }
}

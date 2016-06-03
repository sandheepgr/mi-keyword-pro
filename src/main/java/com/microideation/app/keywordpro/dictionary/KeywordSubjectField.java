package com.microideation.app.keywordpro.dictionary;

import java.util.List;

/**
 * Created by sandheepgr on 17/12/15.
 */
public class KeywordSubjectField {

    private boolean mandatory  = false;

    private Integer length = 0;

    private Integer index;

    private String validation = "";

    private List<String> stripCharacters;


    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public List<String> getStripCharacters() {
        return stripCharacters;
    }

    public void setStripCharacters(List<String> stripCharacters) {
        this.stripCharacters = stripCharacters;
    }

    @Override
    public String toString() {
        return "KeywordSubjectField{" +
                "mandatory=" + mandatory +
                ", length=" + length +
                ", index=" + index +
                ", validation='" + validation + '\'' +
                ", stripCharacters=" + stripCharacters +
                '}';
    }
}

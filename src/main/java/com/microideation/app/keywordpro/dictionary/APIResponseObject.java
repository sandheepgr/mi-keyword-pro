package com.microideation.app.keywordpro.dictionary;


import java.util.HashMap;

/**
 * Created by sandheepgr on 17/2/14.
 */
public class APIResponseObject extends HashMap {

    public APIResponseObject() {
        super();

        setData("");
        setStatus(APIResponseStatus.success);
    }


    public void setData(Object object) {

        put("data",object);

    }

    public void setErrorCode(APIErrorCode code) {

        put("errorcode",code);

    }


    public void setStatus(APIResponseStatus status) {

        put("status",status);

    }


    public void setErrorDesc(String errordesc) {

        put("errordesc",errordesc);

    }

    public void setPageNumber(int pageNumber) {

        put("pagenumber",Integer.toString(pageNumber));

    }

    public void setTotalPages(int totalPages) {

        put("totalpages",Integer.toString(totalPages));

    }

    public void setTotalElements(Long totalElements) {

        put("totalelements",totalElements.toString());

    }

}

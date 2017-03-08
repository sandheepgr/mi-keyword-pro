package com.microideation.app.keywordpro.service;

import java.util.Map;

/**
 * Created by sandheepgr on 28/9/14.
 */
public interface APIService {

    public String placeRestGetAPICall(String url, Map<String, String> variables, Map<String, String> headers);
    public String placeRestPostAPICall(String url, Map<String, String> params, Map<String, String> headers);
    public String placeRestJSONPostAPICall(String url, String json, Map<String, String> headers);
    public String placeRestPostQueryStringAPICall(String url, Map<String, String> variables, Map<String, String> headers);


}

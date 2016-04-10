package com.microideation.app.keywordpro.service;

import java.util.Map;

/**
 * Created by sandheepgr on 28/9/14.
 */
public interface APIService {

    public String placeRestGetAPICall(String url, Map<String, String> variables);
    public String placeRestPostAPICall(String url, Map<String, String> params);
    public String placeRestJSONPostAPICall(String url, String json);
    public String placeRestPostQueryStringAPICall(String url, Map<String, String> variables);


}

package com.microideation.app.keywordpro.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.microideation.app.keywordpro.dictionary.APIResponseObject;
import com.microideation.app.keywordpro.service.APIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


import java.util.ArrayList;
import java.util.Map;

/**
 * Created by sandheepgr on 28/9/14.
 */
@Service
public class APIServiceImpl implements APIService {

    @Autowired
    private ObjectMapper mapper;

    // Create the logger instance
    private static Logger log = LoggerFactory.getLogger(APIServiceImpl.class);

    // Autowire the rest template
    @Autowired
    private RestTemplate miKeywordProRestTemplate;

    @Override
    public String placeRestGetAPICall(String url, Map<String, String> variables, Map<String, String> headers) {

        // Create the response object
        APIResponseObject retData = new APIResponseObject();

        // Create a HttpHeader indicating to use the ipbased auth
        HttpHeaders httpHeaders = getHeaders(headers);

        // Create the HttpEntity
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);

        // Call the entity method
        entity = miKeywordProRestTemplate.exchange(url, org.springframework.http.HttpMethod.GET,entity,String.class,variables);


        // Check if the entity is not null and statusCode is 200
        if ( entity == null ) {

            // Return null
            return null;

        } else  {

            return entity.getBody().toString();

        }

    }

    @Override
    public String placeRestPostAPICall(String url, Map<String, String> params, Map<String, String> headers) {


        MultiValueMap<String,String> postParams = new LinkedMultiValueMap<String,String>(0);

        // Set the params
        postParams.setAll(params);

        // Create the response object
        APIResponseObject retData = new APIResponseObject();

        // Cer
        HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();

        ArrayList<HttpMessageConverter<?>> messageConverters = new ArrayList<>(0);

        messageConverters.add(formHttpMessageConverter);
        messageConverters.add(stringHttpMessageConverter);


        miKeywordProRestTemplate.setMessageConverters(messageConverters);

        // Create a HttpHeader indicating to use the ipbased auth
        HttpHeaders httpHeaders = getHeaders(headers);

        // Create the HttpEntity
        HttpEntity<?> requestEntity =
                new HttpEntity<MultiValueMap<String,String>>(postParams, httpHeaders);

        // Call the entity method
        ResponseEntity responseEntity = miKeywordProRestTemplate.exchange(url, org.springframework.http.HttpMethod.POST,requestEntity,String.class);


        // Check if the responseEntity is not null
        if ( responseEntity == null ) {

            return null;

        } else {

            return responseEntity.getBody().toString();

        }

    }

    @Override
    public String placeRestJSONPostAPICall(String url, String json, Map<String, String> headers) {

        // Create the response object
        APIResponseObject retData = new APIResponseObject();

        // Cer
        HttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        HttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();

        ArrayList<HttpMessageConverter<?>> messageConverters = new ArrayList<>(0);

        messageConverters.add(formHttpMessageConverter);
        messageConverters.add(stringHttpMessageConverter);


        miKeywordProRestTemplate.setMessageConverters(messageConverters);

        // Create a HttpHeader indicating to use the ipbased auth
        HttpHeaders httpHeaders = getHeaders(headers);

        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<String>(json,httpHeaders);

        // Call the entity method
        ResponseEntity responseEntity = miKeywordProRestTemplate.exchange(url, org.springframework.http.HttpMethod.POST,entity,String.class);


        // Check if the responseEntity is not null
        if ( responseEntity == null ) {

            return null;

        } else {

            return responseEntity.getBody().toString();

        }

    }

    @Override
    public String placeRestPostQueryStringAPICall(String url, Map<String, String> variables, Map<String, String> headers) {

        // Create the response object
        APIResponseObject retData = new APIResponseObject();

        // Create a HttpHeader indicating to use the ipbased auth
        HttpHeaders httpHeaders = getHeaders(headers);

        // Create the HttpEntity
        HttpEntity<String> entity = new HttpEntity<String>(httpHeaders);

        // Call the entity method
        entity = miKeywordProRestTemplate.exchange(url, HttpMethod.POST,entity,String.class,variables);


        // Check if the entity is not null and statusCode is 200
        if ( entity == null ) {

            // REturn null
            return null;

        } else  {

            return entity.getBody().toString();

        }

    }


    /**
     * Method to get the httpHeaders object from the list of the header map passed
     *
     * @param headers : The Map of headers in the form <Key,Value>
     * @return        : Empty HttpHeaders object if headers is null or empty
     *                  Return the HttpHeaders object with the header name and value corresponding
     *                  to the headers <key,value>
     */
    private HttpHeaders getHeaders(Map<String,String> headers) {

        // Create the headers object
        HttpHeaders httpHeaders = new HttpHeaders();

        // Check if the headers field is null
        if ( headers == null || headers.size() == 0)  return httpHeaders;

        // Iterate through the headers and then set the values
        for (Map.Entry<String ,String> entry: headers.entrySet()) {

            // Add to the httpHeaders
            httpHeaders.add(entry.getKey(),entry.getValue());

        }

        // return the headers
        return httpHeaders;
    }
}

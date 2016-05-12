package com.microideation.app.keywordpro.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microideation.app.keywordpro.dictionary.InboundSMSKeywordStatus;
import com.microideation.app.keywordpro.dictionary.Keyword;
import com.microideation.app.keywordpro.service.APIService;
import com.microideation.app.keywordpro.service.InboundKeywordService;
import com.microideation.app.keywordpro.dictionary.InboundSMSRequest;
import com.microideation.app.keywordpro.dictionary.KeywordSubjectField;
import com.microideation.app.keywordpro.util.KeywordConfigurationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by sandheepgr on 17/12/15.
 */
@Service
public class InboundKeywordServiceImpl implements InboundKeywordService {

    @Resource
    private Map<String,Keyword> keywordMap;

    @Resource
    private List<String> keywordList;

    @Autowired
    private APIService apiService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private KeywordConfigurationUtil keywordConfigurationUtil;

    // Start the logger
    private static Logger log = LoggerFactory.getLogger(InboundKeywordServiceImpl.class);


    /**
     * The base method that starts the parsing and processing of the inbound SMS
     * based on the subject
     *
     * @param mobile        : The sender mobile
     * @param subject       : The subject content including the keyword
     * @param accessCode    : The Access code for the incoming SMS
     *
     * @return              : Return the status based on the process
     *                        PROCESSED if the sms was processed successfully
     *                        INVALID_KEYWORD : if the keyword is not able to be mapped
     *                        INVALID_FORMAT  : if the format of the fields is not proper for the keyword
     */
    public int processInboundSMS(String mobile, String subject, String accessCode) {

        // Get the request
        InboundSMSRequest inboundSMSRequest = getInboundSMSRequest(mobile,subject,accessCode);

        // get the keyword for the SMS
        Keyword keyword = getConfigForKey(subject);

        // If the keyword is null, then return invalid keyword status
        if ( keyword == null ) {

            return InboundSMSKeywordStatus.INVALID_KEYWORD;

        }

        // Enrich,
        inboundSMSRequest = enrichInboundSMSFields(keyword,inboundSMSRequest);

        // Check the validity
        boolean isValid = isInboundRequestValid(keyword,inboundSMSRequest);

        // If the request is not valid, then return the control
        if ( !isValid ) {

            // Log the infor
            log.info("processInboundSMS : Validation is not valid : " + keyword);

            // Return the control
            return InboundSMSKeywordStatus.INVALID_FORMAT;

        }

        // Call the api call
        String response = placeAPICall(keyword,inboundSMSRequest);

        // Log the response
        log.info("processInboundSMS : Response : "  + response + " : Keyword : " + keyword);

        // finally return as processed
        return InboundSMSKeywordStatus.PROCESSED;

    }


    /**
     * Method to get the Configuration for the given key
     * This will search on the sorted list and return the first matching item
     *
     * @param keyword : The keyword string to be searched
     *
     * @return        : Return the Keyword object if found
     *                  Return null if not found
     */
    private Keyword getConfigForKey(String keyword) {

        // Get the key
        String key = keywordConfigurationUtil.getMatchingKey(keywordList,keyword);

        // Get the keyword
        Keyword keywordObj = keywordMap.get(key);

        // If the keyword is not found, then return null
        if(keywordObj == null){

            return null;
        }

        // Set the key field in the keywordOb as the key that matched ( this is to include alias as key)
        keywordObj.setKey(key);

        // Return the keyword object
        return keywordObj;

    }


    /**
     * Function to get the InboundSMSRequest object from the params
     *
     * @param mobile        : The sender mobile
     * @param subject       : The subject content including the keyword
     * @param accessCode    : The Access code for the incoming SMS
     *
     * @return              : The InboundSMSREquest object based on the fields.
     */
    private InboundSMSRequest getInboundSMSRequest(String mobile, String subject,String accessCode) {

        // Create the object
        InboundSMSRequest inboundSMSRequest = new InboundSMSRequest();

        // Set the fields
        inboundSMSRequest.setMobile(mobile);
        inboundSMSRequest.setSmsText(subject);
        inboundSMSRequest.setAccessCode(accessCode);
        inboundSMSRequest.setTransId(keywordConfigurationUtil.extractTransId(accessCode));

        // Return the object
        return inboundSMSRequest;

    }


    /**
     * Function to set the meta data fields of the InboundSMSRequest based on the subject
     *
     * @param keyword           : The identified keyword object
     * @param inboundSMSRequest : The InboundSMSRequest object to be updated with
     * @return                  : The InboundSMSREquest after updating the details
     */
    private InboundSMSRequest enrichInboundSMSFields(Keyword keyword, InboundSMSRequest inboundSMSRequest) {

        // Store the text
        String text = inboundSMSRequest.getSmsText();

        // Remove the key part from the object
        String key = keyword.getKey();

        // set the text
        text=text.substring(key.length()).trim();

        // Split the fields
        String subjectFields[] = text.split(keyword.getFieldSeparator());



        // Set the fields
        inboundSMSRequest.setKey(key);
        inboundSMSRequest.setSubject(text);
        inboundSMSRequest.setSubjectFields(Arrays.asList(subjectFields));

        // Set the metapara
        inboundSMSRequest = updateMetaParams(keyword,inboundSMSRequest);

        // Return the object
        return inboundSMSRequest;

    }


    /**
     * Function to set the metadata params to InboundSMSREquest object
     * The metadata params are the ones starting with the #
     *
     * @param keyword           : The Keyword object identified
     * @param inboundSMSRequest : The InboundSMSRequest object containing the request.
     *
     * @return                  : The InboundSMSREquest after updating the details
     */
    private InboundSMSRequest updateMetaParams(Keyword keyword, InboundSMSRequest inboundSMSRequest) {

        // Create the HashMap
        HashMap<String,String> metaParams = new HashMap<String,String>();

        // Add the fields
        metaParams.put("#mobile",inboundSMSRequest.getMobile());
        metaParams.put("#accesscode",inboundSMSRequest.getAccessCode());
        metaParams.put("#transid",inboundSMSRequest.getTransId());


        // Declare the index to 1
        int index = 1;

        // Add the subject fields
        for( String field : inboundSMSRequest.getSubjectFields() ) {

            // Set the param fields as param + index
            metaParams.put("#param" +index++,field);


        }

        // Set the metaparams in the inboundSMSReqest
        inboundSMSRequest.setMetaParams(metaParams);

        // Return the object
        return inboundSMSRequest;

    }


    /**
     * Function to validate the incoming request based on the rules in the Keyword object and
     * the InboundSMSRequest of the incoming data
     *
     * @param keyword   :  The Keyword object identified
     * @param request   : The InboundSMSRequest object containing the request.
     *
     * @return          : return true if all the fields in the subject are valid based on the rules
     *                    return false is atleast one field is not valid
     */
    private boolean isInboundRequestValid(Keyword keyword,InboundSMSRequest request) {

        // Get the subject field config from the keyword
        List<KeywordSubjectField> keywordSubjectFields = keyword.getKeywordSubjectFields();

        // Store the fields
        List<String> subjectFields = request.getSubjectFields();

        // Check if the totalFields is specified
        if ( keyword.getMinFieldCount() != null && keyword.getMinFieldCount() != 0 ) {

            // If the size of the subject fields is less thatn the total fields expected by the
            // program, then return false.
            if ( subjectFields.size() < keyword.getMinFieldCount() ) {

                // Log the information
                log.info("The subjects fields are less than total fields");

                // Return false
                return false;

            }

        }


        // If there are no explicit configuration, return true
        if ( (keywordSubjectFields == null || keywordSubjectFields.isEmpty()) ||
                (subjectFields == null || subjectFields.isEmpty() )
                ) {

            // Log the information
            log.info("No field config to validate");

            //return true
            return true;

        }


        // Iterate through the keywordSubjectFields
        for ( KeywordSubjectField keywordSubjectField :  keywordSubjectFields ) {

            // Check if the index is there for the request
            //
            // Compare with size + 1 as config index starting with 1
            if ( keywordSubjectField.getIndex() > (subjectFields.size()) ) {

                // Log the information
                log.info("No field for given index, continuing");

                // Continue;
                continue;

            }


            // Check if the field is valid
            boolean isValid = validateSubjectField(keywordSubjectField,subjectFields.get(keywordSubjectField.getIndex() -1));

            // Log the information
            log.info("Validity of the field : "+ keywordSubjectField + " valid : " + isValid);

            // If not valid, return false
            if ( !isValid ) {

                return false;

            }

        }

        // Log the info
        log.info("Field validity passed");

        // finally return true
        return true;

    }


    /**
     * Method to validate the subject field based on the rules of the field
     *
     * @param keywordSubjectField  : The object containing the rules for the specified field
     * @param field                : The value fo the field
     *
     *
     * @return                     : return true if the field is valid based on the settings
     *                               return false otherwise
     */
    private boolean validateSubjectField(KeywordSubjectField keywordSubjectField, String field) {

        // Check if the field is mandatory
        if ( keywordSubjectField.isMandatory() && (field == null || field.equals("")) ) {

            return false;

        }


        // Check if the field size is as specified
        if ( field.length() > keywordSubjectField.getLength()  ) {

            return false;

        }


        // If there is validation specified, then we need to run the regex and check
        if ( !keywordSubjectField.getValidation().equals("") ) {

            // Create the pattern
            Pattern r = Pattern.compile(keywordSubjectField.getValidation());

            /// create a matcher
            Matcher m = r.matcher(field);

            // If the match is not there, return false
            if ( !m.find()  ) {

                return false;

            }


        }



        // finally return true
        return true;

    }


    /**
     * Method to place the API call based on the configuration in the keyword and the params
     * identified from the request
     *
     * @param keyword           : The keyword object to be used for getting the configuration
     * @param inboundSMSRequest : The inbound request fields
     *
     * @return                  : Return the response given by the api
     *                            Return null if the API call failed.
     */
    private String placeAPICall(Keyword keyword, InboundSMSRequest inboundSMSRequest ) {

        // Read the url
        // Create a new string object to not update the url in the keyword
        String url = new String(keyword.getApiUrl());

        // Get the url after replacing the meta param place holders
        url = replaceUrlMetaParams(url,keyword,inboundSMSRequest);

        // Get the mappings
        HashMap<String,String> mappings = createAPICallMapping(keyword,inboundSMSRequest);

        // Log the mappings
        log.info("placeAPICall :  " + keyword + " : Mappings :  " + mappings);

        // Variable holding the response
        String response = "";

        // Switch the method and the place the call
        switch( keyword.getApiMethod().toLowerCase() ) {

            case "post":

                response = apiService.placeRestPostAPICall(url,mappings);
                break;

            case "get":

                response = apiService.placeRestGetAPICall(url, mappings);
                break;

            case "post-query":

                response = apiService.placeRestPostQueryStringAPICall(url, mappings);
                break;

            case "post-json":

                try {

                    response = apiService.placeRestJSONPostAPICall(url, mapper.writeValueAsString(mappings));

                } catch (JsonProcessingException e) {

                    // Log the error
                    log.error("placeAPICall : Error during json parsing : " + e.getMessage());

                    // Set the response to null
                    response = null;

                } catch (IOException e) {

                    // Log the error
                    log.error("placeAPICall : Error during json parsing : " + e.getMessage());

                    // Set the response to null
                    response = null;
                }

                break;

        }

        // Return the response
        return response;

    }


    /**
     * Function to replace the metaparams specified in the url of the item
     *
     * @param url               : The url to be updated
     * @param keyword           : The Keyword object containing the configuration
     * @param inboundSMSRequest : The inboundSMSRequest object with the meta params
     * @return
     */
    private String replaceUrlMetaParams(String url, Keyword keyword, InboundSMSRequest inboundSMSRequest) {

        // Iterate through the metaparam names and check if the url contains the metaparam key
        // If the key is present, then replace it with the value from the param.
        for ( Map.Entry<String,String> entry : inboundSMSRequest.getMetaParams().entrySet() ) {

            // chcek if the url contains the #param
            url = url.replace(entry.getKey(),entry.getValue());

        }

        // return the url
        return url;

    }


    /**
     * Method to create the hash params mapping based on the keyword and the inboundSMSRequest object
     *
     * @param keyword           : Keyword object with the names of the fields
     * @param inboundSMSRequest : InboundSMSRequest object containing the values for the fields
     *
     * @return                  : HashMap with keys as the name of the params and values as the corresponding value from
     *                            the InboundSMSRequest object
     */
    private HashMap<String,String> createAPICallMapping(Keyword keyword, InboundSMSRequest inboundSMSRequest) {

        // Create the Hashmap that will hold the details
        HashMap<String,String> mappings = new HashMap<>(0);

        //check whether api mappings is null
        if(keyword.getApiMappings() == null){

            //return empty mappings
            return mappings;

        }

        // Iterate throught the mappings
        for(Map.Entry<String,String> entry : keyword.getApiMappings().entrySet() ) {

            if ( entry.getValue().startsWith("#") ) {

                // Add the value to the mappings
                mappings.put(entry.getKey(),inboundSMSRequest.getMetaParams().get(entry.getValue()));


            } else {

                // Put the constant value
                mappings.put(entry.getKey(), entry.getValue());

            }

        }


        // Return the mappings
        return mappings;

    }

}


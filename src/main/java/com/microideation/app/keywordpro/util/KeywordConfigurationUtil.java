package com.microideation.app.keywordpro.util;



import com.microideation.app.keywordpro.dictionary.Keyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by sandheepgr on 17/12/15.
 */
@Component
public class KeywordConfigurationUtil {


    @Autowired
    private ResourceLoader resourceLoader;

    // Start the logger
    private static Logger log = LoggerFactory.getLogger(KeywordConfigurationUtil.class);


    /**
     * Function to get the keywordpro as a map
     * The key would be the keywordpro string and the value the Keyword object
     *
     * @return      : The HashMap object with key as the keywordpro string and the value the Keyword object
     */
    public HashMap<String,Keyword> getKeywordMap(String filePath) {

        // Get the list
        List<Keyword> keywordList = readKeywordConfiguration(filePath);

        // If the list is null
        if ( keywordList == null ) {

            // Log the error
            log.error("No keywordpro list found");

            // return null
            return null;

        }


        // Get the map
        HashMap<String,Keyword> keywordMap = convertKeywordListToMap(keywordList);

        // Return the map
        return keywordMap;


    }


    /**
     * Method to read the yaml file and create Keyword objects based on the configurations
     *
     * @return  : The List of Keyword objects if the file was read and parsed successfully
     *            null if not able to parse or the file is not found
     */
    private List<Keyword> readKeywordConfiguration(String filePath) {

        // Instantiate the Yaml
        Yaml yaml = new Yaml();


        try {

            // Read the data
            HashMap<String,List<Keyword>> data = (HashMap<String, List<Keyword>>) yaml.load(new FileReader(new File(filePath)));

            // Read the list
            List<Keyword> keywordList = data.get("keywords");

            // Return the list
            return keywordList;

        } catch (IOException e) {

            // Log the error
            log.info("Error reading keywordpro configuration : " + e.getMessage());

            // return null
            return null;

        }


    }


    /**
     * This method converts the List of keywords in to a map with the key as the value as the object
     * This will repeat the same Keyword object with a different alias and if there are duplicate keys,
     * this will report an error.
     *
     * @param keywordList    : The List<keywordpro> object containing the keywords
     * @return               : he HashMap object with key as the keywordpro string and the value the Keyword object
     */
    private HashMap<String,Keyword> convertKeywordListToMap(List<Keyword> keywordList) {

        // Create the map to hold the data
        HashMap<String,Keyword> keywordMap = new HashMap<>(0);

        // Iterate through the list and then convert to Map
        for ( Keyword keyword : keywordList) {

            // Get the key
            String key = keyword.getKey();

            // Get the list of aliases
            List<String> aliases = keyword.getAlias();

            // If the aliases is null, then create one
            if ( aliases == null ) {

                aliases = new ArrayList<>(0);

            }

            // Add the key to the aliases to be added
            aliases.add(key);

            // Iterate through the aliases and add the same
            for ( String alias : aliases ) {

                // Check if the key is already present
                if ( keywordMap.containsKey(alias.toLowerCase()) ) {

                    // Log the error
                    log.error("Duplicate keywordpro/alias configuration : " + alias);

                    // return null
                    return null;


                }

                // Add the entry with the key as the alias and the value as the keywordpro
                keywordMap.put(alias.toLowerCase(),keyword);

            }

        }

        // Return the map
        return keywordMap;

    }


    /**
     * Get the list of Keyword objects ordered by the key
     * The ordering is based on the text length.
     * For eg: To give precedence to entries like 'PIN SET' , 'PIN',
     * If we are checking purely based on the first word, this would not be possible.
     *
     * @param keywordMap    : The HashMap object with key as the keywordpro string and the value the Keyword object
     * @return              : The List with the Keyword object with the longest text on top
     */
    public List<String> orderedKeywords(Map<String,Keyword> keywordMap) {

        // Create the list holding the keys
        List<String> keys = new ArrayList<>(0);

        // get the keys of the hashmap
        keys.addAll(keywordMap.keySet());

        // sort the collections
        // We need to have the longest key at the top
        Collections.sort(keys,new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if ( o1.length() == o2.length() ) {
                    return 0;
                } else if ( o1.length() < o2.length() ) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        // Return the keys
        return keys;

    }


    /**
     * Method to get the configuration corresponding to a key
     * The method will check in the List object that is ordered so that the longest matching keywordpro
     * would be processed first
     *
     * @param keys      : The key objects
     * @param subject   : The subject line to be compared with
     * @return          : The ke
     */
    public String getMatchingKey(List<String> keys,String subject) {

        // Convert to lower case
        String compare = subject.toLowerCase();

        // Iterate through the list and get the first matching one
        for (String key : keys ) {

            // Check if the key is the starting of the subject
            if ( compare.startsWith(key) ) {

                // Check if the key is valid
                if(isKeywordValid(compare,key)){

                    return key;

                } else {

                    return null;
                }

            }

        }

        // If nothing matches, then return null
        return null;

    }


    /**
     * Method to check if the keyword is valid after removing the subject part
     * If the subject after removing the current key does not start with the empty string,
     * it means that the key is no matching for the keyword in the inbound request
     *
     * @param compare   : The subject field to be compared
     * @param key       : The currenct key being checked
     *
     * @return          : false if the compare string still has got content after removing the key and is
     *                    not starting with blank space
     *                    true  otherwise
     */
    private boolean isKeywordValid(String compare, String key) {

        //get the length of key
        int length = key.length();

        //get the part of the message excluding keyword
        String nonKeywordPart = compare.substring(length);

        //if non keyword part is not empty and not starts with blank space ,
        //the keyword can be considered as invalid
        if(nonKeywordPart.length()>0 && !nonKeywordPart.startsWith(" ")){

            return false;
        }

        //return true
        return true;
    }


    /**
     * Method to extract the trans id from the accesscode
     * This is the text after the first 4 digits in the access code.
     * if its not present, we return 0
     *
     * @param accessCode        : The accesscode string
     * @return                  : Return o if there is nothing after the 4 digits
     *                            Return the text after the first 4 digits
     */
    public String extractTransId(String accessCode) {

        // If the accessCode is null or empty of less than 4 characters, then we return 0
        if ( accessCode == null || accessCode.equals("") || accessCode.length() <= 4 ) {

            return "0";

        }

        // Extract the part of access code after the first 4 characters
        return accessCode.substring(4);

    }

}

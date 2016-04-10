package com.microideation.app.keywordpro.config;


import com.microideation.app.keywordpro.dictionary.Keyword;
import com.microideation.app.keywordpro.util.KeywordConfigurationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

/**
 * Created by sandheepgr on 10/12/15.
 */
@Configuration
@ComponentScan(basePackages = {"com.microideation.app.keywordpro.service"})
public class KeywordConfiguration {

    @Autowired
    private KeywordConfigurationUtil keywordConfigurationUtil;

    @Value("${mi.keywordpro.file}")
    private String filePath;

    @Bean
    public Map<String,Keyword> keywordMap() {

        // Create the keywordpro map
        Map<String,Keyword> keywordMap =  keywordConfigurationUtil.getKeywordMap(filePath);

        // return the map
        return keywordMap;

    }


    @Bean
    public List<String> keywordList() {

        // Create the keywordpro map
        Map<String,Keyword> keywordMap =  keywordConfigurationUtil.getKeywordMap(filePath);

        // return the list
        return keywordConfigurationUtil.orderedKeywords(keywordMap);

    }
}

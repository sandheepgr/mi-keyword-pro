package com.microideation.app.keywordpro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Created by sandheepgr on 8/3/17.
 */
@Configuration
public class APIConfiguration {

    @Bean(name = "miKeywordProRestTemplate")
    public RestTemplate miKeywordProRestTemplate() {

        return new RestTemplate();

    }

}

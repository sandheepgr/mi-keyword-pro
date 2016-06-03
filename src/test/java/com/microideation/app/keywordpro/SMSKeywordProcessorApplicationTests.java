package com.microideation.app.keywordpro;

import com.microideation.app.keywordpro.SMSKeywordProcessorApplication;
import com.microideation.app.keywordpro.service.InboundKeywordService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SMSKeywordProcessorApplication.class)
@WebIntegrationTest
public class SMSKeywordProcessorApplicationTests {

    @Autowired
    private InboundKeywordService inboundKeywordService;

    @Test
    public void testKeyword() {

        inboundKeywordService.processInboundSMS("9538828853","REWARDS +919538828853/test","200");

    }
}

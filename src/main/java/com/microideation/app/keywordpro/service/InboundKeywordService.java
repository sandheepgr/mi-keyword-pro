package com.microideation.app.keywordpro.service;

import com.microideation.app.keywordpro.dictionary.InboundSMSRequest;

/**
 * Created by sandheepgr on 17/12/15.
 */
public interface InboundKeywordService {

    public int processInboundSMS(InboundSMSRequest inboundSMSRequest);
    public int processInboundSMS(String mobile, String subject, String accessCode);

}

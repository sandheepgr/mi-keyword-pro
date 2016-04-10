package com.microideation.app.keywordpro.service;

/**
 * Created by sandheepgr on 17/12/15.
 */
public interface InboundKeywordService {

    public int processInboundSMS(String mobile, String subject, String accessCode);

}

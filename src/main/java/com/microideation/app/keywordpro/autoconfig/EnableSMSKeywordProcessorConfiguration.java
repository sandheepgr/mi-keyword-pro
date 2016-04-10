package com.microideation.app.keywordpro.autoconfig;

import com.microideation.app.keywordpro.SMSKeywordProcessorApplication;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by sandheepgr on 9/4/16.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({SMSKeywordProcessorApplication.class})
public  @interface EnableSMSKeywordProcessorConfiguration {
}

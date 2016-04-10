package com.microideation.app.keywordpro.dictionary;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * Created by sandheepgr on 20/4/14.
 */
public enum APIErrorCode {



    ERR_INVALID_INPUT(1),
    ERR_OPERATION_FAILED(2),
    ERR_EXCEPTION(3),
    ERR_NO_DATA(4),
    ERR_NOT_AUTHORIZED(5),
    ERR_NO_DATA_FOUND(6),
    ERR_DUPLICATE_ENTRY(7),
    ERR_STREAM_ERROR(8),
    ERR_NO_BALANCE(9),
    ERR_NO_LOYALTY_ID(10),
    ERR_PRODUCT_NOT_FOUND(11),
    ERR_INSUFFICIENT_POINT_BALANCE(12),
    ERR_TRANSACTION_EXCEPTION(13),
    ERR_OPERATION_NOT_ALLOWED(14),
    ERR_INVALID_CARD_NUMBER_RANGE(15),
    ERR_TOPUP_VALUE_LESS_THAN_MINIMUM(16),
    ERR_CARD_VALUE_HIGHER_THAN_MAXIMUM(17),
    ERR_CARD_EXPIRED(18),
    ERR_TOPUP_AMOUNT_INVALID(19),
    ERR_INSUFFICIENT_BALANCE(20),
    ERR_INVALID_PIN(21),
    ERR_NO_AUTHENTICATION(22);

    private int value;

    APIErrorCode(int value) {

        this.value = value;

    }


    /**
     * Function to get the localized description of the errorcode for the APIErrorCode
     * Here the MessageSource is passed from calling function context
     * Function will read the localized message for the current error code
     *
     * @param messageSource - The MessageSource from the calling function
     *
     * @return - Return the String representation of the messageSoure
     */
    public String getLocalizedDesc(MessageSource messageSource) {

        // Get the current locale
        Locale locale = LocaleContextHolder.getLocale();

        // Variable holding the message
        String message = "";

        try {

            // Get the string
            message = messageSource.getMessage(this.toString(),new Object[0],locale);

        } catch (Exception e) {

            message = this.toString();

        }


        // Return the message
        return message;

    }
}

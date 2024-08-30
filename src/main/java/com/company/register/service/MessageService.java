package com.company.register.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageSource messageSource;

    /**
     * Retrieve a message by key with optional arguments.
     *
     * @param key  the message key in the properties file
     * @param args optional arguments to replace placeholders in the message
     * @return the resolved message as a String
     */
    public String getMessage(String key, Object... args) {
        return messageSource.getMessage(key, args, LocaleContextHolder.getLocale());
    }

}

package org.greeting.service;

import lombok.extern.slf4j.Slf4j;
import org.greeting.model.Language;

@Slf4j
public class GreetingService {
    public void sayHello(Language language) {
        log.info("Requested language: {}", language);
        switch (language) {
            case EN:
                System.out.println("Hello, please to meet you.");
                break;
            case ES:
                System.out.println("Hola, encantado a conoceros.");
                break;
            default:
                throw new IllegalStateException("Unexpected language: " + language);
        }
    }
}
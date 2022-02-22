package org.greeting.service;

import org.greeting.model.Language;
import org.greeting.repository.GreetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;


@SuppressWarnings("java:S2699")
@ExtendWith(MockitoExtension.class)
class GreetingServiceTest {

    @Mock
    GreetingRepository greetingRepository;

    GreetingService unitUnderTest;

    @BeforeEach
    void setup() {
        reset(greetingRepository);
        unitUnderTest = new GreetingService(greetingRepository);
    }

    @Test
    void sayHello_whenLanguageExists_returns() {
        Language validLanguage = Language.EN;
        when(greetingRepository.readGreetingText(validLanguage)).thenReturn(Optional.of("some text"));

        unitUnderTest.sayHello(validLanguage);
    }

    @Test
    void sayHello_whenLanguageNotFound_throws() {
        Language unsupportedLanguage = Language.ES;
        when(greetingRepository.readGreetingText(unsupportedLanguage)).thenReturn(Optional.empty());

        IllegalStateException actualException = assertThrows(IllegalStateException.class,
                () -> unitUnderTest.sayHello(unsupportedLanguage));

        assertEquals("Unexpected language: Language.ES(displayName=Spanish)", actualException.getMessage());
    }
}
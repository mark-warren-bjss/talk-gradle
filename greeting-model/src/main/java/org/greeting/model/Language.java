package org.greeting.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public enum Language {
    EN("English"),
    ES("Spanish");

    private final String displayName;

    Language(String displayName) {
        this.displayName = displayName;
    }
}
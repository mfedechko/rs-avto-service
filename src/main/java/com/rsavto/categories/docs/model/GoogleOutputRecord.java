package com.rsavto.categories.docs.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author mfedechko
 */
@Getter
@Setter
public class GoogleOutputRecord extends OutputRecord {

    private String id;
    private String name;
    private String link;
    private String state;
    private String availability;
    private String gtin;
    private String manCode;
    private String category;
}

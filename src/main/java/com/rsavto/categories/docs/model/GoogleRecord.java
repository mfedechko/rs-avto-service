package com.rsavto.categories.docs.model;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Mykola Fedechko
 */
@Getter
@Setter
public class GoogleRecord {

    private String id;
    private String name;
    private String description;
    private String link;
    private String state;
    private String price;
    private boolean isAvailable;
    private String picture;
    private String brand;

}

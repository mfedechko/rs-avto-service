package com.rsavto.categories.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author mfedechko
 */
@Getter
@Setter
@Component
public class DataService {

    @Value("${data.phoneNumber}")
    private String phoneNumber;

    private double rate;

}

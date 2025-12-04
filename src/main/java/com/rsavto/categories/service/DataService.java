package com.rsavto.categories.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author mfedechko
 */
@Getter
@Service
public class DataService {

    @Value("data.phoneNumber")
    private String phoneNumber;

}

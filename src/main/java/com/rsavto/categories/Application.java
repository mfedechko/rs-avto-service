package com.rsavto.categories;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author Mykola Fedechko
 */
@SpringBootApplication
public class Application {

    public static void main(final String... args) {
        System.setProperty("https.protocols", "SSLv3,TLSv1,TLSv1.1,TLSv1.2");
        new SpringApplicationBuilder(Application.class)
                .run(args);
    }

}

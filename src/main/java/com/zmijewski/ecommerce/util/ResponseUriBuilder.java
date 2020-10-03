package com.zmijewski.ecommerce.util;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Component
public class ResponseUriBuilder {

    public URI buildUriWithAppendedId(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
    public URI buildUriWithAddedPathAndEmail(String path, String email) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path(path)
                .queryParam("email", email)
                .build()
                .toUri();
    }
}

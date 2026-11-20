package com.restauranteAPI.restauranteAPI.exception;
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String recurso, Long id) {
        super(recurso + " com ID " + id + " não encontrado");
    }
}

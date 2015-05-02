package hu.zalatnai.sdk.service;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UUIDGenerator {
    public UUID generateV4() {
        return UUID.randomUUID();
    }
}

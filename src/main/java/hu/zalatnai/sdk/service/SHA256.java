package hu.zalatnai.sdk.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class SHA256
{
    MessageDigest md;

    public SHA256() {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] hash(byte[] data) {
        return md.digest(data);
    }
}

package hu.zalatnai.sdk.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.security.SecureRandom;

@Service
public class RandomGenerator {

    private SecureRandom secureRandom;

    public RandomGenerator() {
        secureRandom = new SecureRandom();
        secureRandom.nextInt();
    }

    public byte[] getBytes(int count) {
        Assert.isTrue(count > 0);
        byte[] randomBytes = new byte[count];

        secureRandom.nextBytes(randomBytes);

        return randomBytes;
    }
}

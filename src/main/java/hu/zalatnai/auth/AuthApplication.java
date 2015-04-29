package hu.zalatnai.auth;

import hu.zalatnai.auth.domain.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import java.security.SecureRandom;
import java.time.Clock;
import java.util.Base64;

@SpringBootApplication
@ComponentScan({"hu.zalatnai.auth", "hu.zalatnai.sdk"})
@EntityScan(basePackageClasses = {AuthApplication.class, Jsr310JpaConverters.class})
public class AuthApplication implements CommandLineRunner {

    @Autowired
    ApplicationRepository applicationRepository;


    @Bean
    public SecureRandom getSecureRandom() {
        return new SecureRandom();
    }

    @Bean
    public Clock getClock() {
        return Clock.systemUTC();
    }

    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        byte[] token = new byte[32];
        new SecureRandom().nextBytes(token);
        System.out.println(Base64.getEncoder().encodeToString(token));
    }
}

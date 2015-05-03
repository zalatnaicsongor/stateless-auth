package hu.zalatnai.auth;

import hu.zalatnai.auth.dto.output.ClientDTO;
import hu.zalatnai.sdk.service.infrastructure.InstantToUnixTimestampConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.security.SecureRandom;
import java.time.Clock;

@SpringBootApplication
@ComponentScan({"hu.zalatnai.auth", "hu.zalatnai.sdk"})
@EntityScan(basePackageClasses = {AuthApplication.class, InstantToUnixTimestampConverter.class})
@EnableTransactionManagement
public class AuthApplication implements CommandLineRunner {

    @Autowired
    AuthFacade authFacade;

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
        System.out.println("booted");
    }
}

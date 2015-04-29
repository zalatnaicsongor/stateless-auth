package hu.zalatnai.auth;

import hu.zalatnai.auth.domain.*;
import hu.zalatnai.sdk.service.infrastructure.InstantToUnixTimestampConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;
import java.security.SecureRandom;
import java.time.Clock;

@SpringBootApplication
@ComponentScan({"hu.zalatnai.auth", "hu.zalatnai.sdk"})
@EntityScan(basePackageClasses = {AuthApplication.class, InstantToUnixTimestampConverter.class})
@EnableTransactionManagement
public class AuthApplication implements CommandLineRunner {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ApplicationInstantiator applicationInstantiator;

    @Autowired
    ClientRepository clientRepository;


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
    @Transactional
    public void run(String... strings) throws Exception {
        Application application = applicationRepository.getById("8vW+TABgk4Vy7sPIjeb5LVvntKb7+I2nwgsDXBDhtnc=");
        Client client = applicationInstantiator.create(application, "deviceUuid", "deviceName");
        client.persist();

        clientRepository.saveAndFlush(client);
    }
}

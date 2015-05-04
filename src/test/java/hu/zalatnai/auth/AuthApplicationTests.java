package hu.zalatnai.auth;

import hu.zalatnai.auth.domain.exception.ApplicationNotFoundException;
import hu.zalatnai.auth.domain.exception.ClientNotFoundException;
import hu.zalatnai.auth.domain.exception.InvalidRefreshTokenException;
import hu.zalatnai.auth.dto.output.ClientDTO;
import hu.zalatnai.auth.dto.output.RefreshedAccessTokenDTO;
import hu.zalatnai.sdk.service.SHA256;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.UUID;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AuthApplication.class)
@WebAppConfiguration
public class AuthApplicationTests {

    public static String applicationId = "8vW+TABgk4Vy7sPIjeb5LVvntKb7+I2nwgsDXBDhtnc=";

    @Autowired
    AuthFacade authFacade;

    @Autowired
    SHA256 sha256;

    @Test
    public void contextLoads() {
    }

    @Test
    public void canRegisterAClient() {
        ClientDTO clientDTO = authFacade.registerClient(applicationId, "testDeviceUuid", "testDeviceName");
    }

    @Test(expected = ApplicationNotFoundException.class)
    public void RegisteringAClientWithAnInvalidApplicationIdThrows() {
        authFacade.registerClient("blah", "testDeviceUuid", "testDeviceName");
    }

    @Test
    public void canRefreshARegisteredClientWithAValidRefreshToken() {
        ClientDTO clientDTO = authFacade.registerClient(applicationId, "testDeviceUuid", "testDeviceName");

        try {
            Thread.sleep(1100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        RefreshedAccessTokenDTO refreshedAccessTokenDTO = authFacade
                .refreshToken(clientDTO.getClientUuid(), clientDTO.getRefreshToken());

        assertNotEquals(refreshedAccessTokenDTO.getRefreshedAccessToken(), clientDTO.getAccessToken());
        assertTrue(refreshedAccessTokenDTO.getRefreshedTokenIssuedAt().isAfter(clientDTO.getTokenIssuedAt()));
        assertTrue(
                refreshedAccessTokenDTO.getRefreshedTokenExpirationTime().isAfter(clientDTO.getTokenExpirationTime()));
    }

    @Test(expected = InvalidRefreshTokenException.class)
    public void RefreshingARegisteredClientWithAnInvalidRefreshTokenThrows() {
        ClientDTO clientDTO = authFacade.registerClient(applicationId, "testDeviceUuid", "testDeviceName");

        RefreshedAccessTokenDTO refreshedAccessTokenDTO = authFacade
                .refreshToken(clientDTO.getClientUuid(), "asd");
    }

    @Test(expected = ClientNotFoundException.class)
    public void RefreshingANonExistentClientThrows() {
        RefreshedAccessTokenDTO refreshedAccessTokenDTO = authFacade
                .refreshToken(UUID.randomUUID(), "asd");
    }
}

package hu.zalatnai.auth;

import hu.zalatnai.auth.application.ClientService;
import hu.zalatnai.auth.domain.Client;
import hu.zalatnai.auth.dto.output.ClientDTO;
import hu.zalatnai.auth.dto.output.RefreshedAccessTokenDTO;
import hu.zalatnai.sdk.service.SHA256;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Base64;
import java.util.UUID;

@Service
public class AuthFacade {

    @Autowired
    ClientService clientService;

    @Autowired
    SHA256 sha256;

    @Transactional
    public ClientDTO registerClient(@NotNull String applicationId, @NotNull String deviceUuid, @NotNull String deviceName) {
        Assert.hasText(applicationId);
        Assert.hasText(deviceUuid);
        Assert.hasText(deviceName);

        Client newClient = clientService.registerClient(applicationId, deviceUuid, deviceName);

        String accessToken = Base64.getEncoder().encodeToString(newClient.getAccessToken());
        String refreshToken = Base64.getEncoder().encodeToString(newClient.getRefreshToken());

        clientService.saveClient(newClient);

        return new ClientDTO(newClient.getClientUuid(), deviceUuid, deviceName,
                accessToken, refreshToken, newClient.getTokenExpirationTime(), newClient.getTokenIssuedAt(),
                newClient.getApplicationId());
    }

    @Transactional
    public RefreshedAccessTokenDTO refreshToken(@NotNull UUID clientUuid, @NotNull String refreshTokenBase64) {
        Assert.notNull(clientUuid);
        Assert.hasText(refreshTokenBase64);

        Client client = clientService.getClientByClientUuid(clientUuid);

        client.refreshToken(sha256.hash(Base64.getDecoder().decode(refreshTokenBase64)));

        RefreshedAccessTokenDTO refreshedAccessTokenDTO = new RefreshedAccessTokenDTO(
                Base64.getEncoder().encodeToString(client.getAccessToken()), client.getTokenExpirationTime(),
                client.getTokenIssuedAt());

        clientService.saveClient(client);

        return refreshedAccessTokenDTO;
    }
}

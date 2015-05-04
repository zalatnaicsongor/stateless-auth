package hu.zalatnai.auth.application;

import hu.zalatnai.auth.domain.*;
import hu.zalatnai.auth.domain.exception.ClientNotFoundException;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ClientService {

    @Autowired
    ApplicationRepository applicationRepository;

    @Autowired
    ApplicationInstantiator applicationInstantiator;

    @Autowired
    StateRepository<ClientState> clientStateRepository;

    @Autowired
    StateRepository<TokenState> tokenStateRepository;

    @Autowired
    ClientRepository clientRepository;

    //Q
    public Client getClientByClientUuid(UUID clientUuid) {
        Client client = clientRepository.findOne(clientUuid);
        if (client == null) {
            throw new ClientNotFoundException();
        }

        client.resolveStateFromStatus(clientStateRepository, tokenStateRepository);

        return client;
    }

    //Q
    public Client registerClient(@NotNull String applicationId, @NotNull String deviceUuid, @NotNull String deviceName) {
        Application app = applicationRepository.getById(applicationId);

        return applicationInstantiator.create(app, deviceUuid, deviceName);
    }

    //C
    public void saveClient(Client client) {
        client.persist();
        clientRepository.save(client);
    }
}

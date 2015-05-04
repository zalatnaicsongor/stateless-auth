package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.StateAlreadyResolvedException;
import hu.zalatnai.sdk.service.domain.StateRepository;
import hu.zalatnai.sdk.service.domain.exception.UUIDAlreadyAssignedException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.UUID;

public class ClientTest {

    @Mock
    ClientState clientState;

    @Mock
    StateRepository<ClientState> clientStateRepository;

    @Mock
    StateRepository<TokenState> tokenStateRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = StateAlreadyResolvedException.class)
    public void testResolveStateFromStatusThrowsIfTheStateHasAlreadyBeenResolved() throws NoSuchFieldException, IllegalAccessException {
        Client client = new Client();

        Field clientStateField = client.getClass().getDeclaredField("clientState");
        clientStateField.setAccessible(true);
        clientStateField.set(client, clientState);

        client.resolveStateFromStatus(clientStateRepository, tokenStateRepository);
    }

    @Test(expected = UUIDAlreadyAssignedException.class)
    public void testAssignClientUUIDThrowsIfAUUIDHasAlreadyBeenAssignedToTheClient() throws NoSuchFieldException, IllegalAccessException {
        Client client = new Client();

        Field clientUUID = client.getClass().getDeclaredField("clientUuid");
        clientUUID.setAccessible(true);
        clientUUID.set(client, UUID.randomUUID());

        client.assignClientUUID(UUID.randomUUID());
    }
}
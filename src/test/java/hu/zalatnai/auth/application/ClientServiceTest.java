package hu.zalatnai.auth.application;

import hu.zalatnai.auth.domain.*;
import hu.zalatnai.auth.domain.exception.ClientNotFoundException;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class ClientServiceTest {

    UUID clientUuid = UUID.fromString("f42a326b-ca2c-4128-a33d-aa56bec9d78b");
    String applicationId = "applicationId";

    @Mock
    ApplicationRepository applicationRepository;

    @Mock
    ApplicationInstantiator applicationInstantiator;

    @Mock
    StateRepository<ClientState> clientStateRepository;

    @Mock
    StateRepository<TokenState> tokenStateRepository;

    @Mock
    ClientRepository clientRepository;

    @Mock
    Client existingClient;

    @Mock
    Client newClient;

    @Mock
    Application application;

    //SUT
    @InjectMocks
    ClientService clientService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(clientRepository.findOne(clientUuid)).thenReturn(existingClient);
        when(applicationRepository.getById(applicationId)).thenReturn(application);
        when(applicationInstantiator.create(application, "deviceName", "deviceUuid")).thenReturn(newClient);
    }

    @Test
    public void testGetClientByClientUuidGetsTheClientFromTheRepository() {
        clientService.getClientByClientUuid(clientUuid);

        verify(clientRepository).findOne(clientUuid);
    }

    @Test(expected = ClientNotFoundException.class)
    public void testGetClientByIdThrowsIfTheClientCouldNotBeRetrievedByTheSuppliedUUID() {
        when(clientRepository.findOne(clientUuid)).thenReturn(null);
        clientService.getClientByClientUuid(clientUuid);
    }

    @Test
    public void testGetClientResolvesTheRetrievedClientsStateFromItsStatus() {
        clientService.getClientByClientUuid(clientUuid);

        verify(existingClient).resolveStateFromStatus(clientStateRepository, tokenStateRepository);
    }

    @Test
    public void testGetClientReturnsTheRetrievedClient() {
        assertEquals(existingClient, clientService.getClientByClientUuid(clientUuid));
    }

    @Test
    public void testRegisterClientRetrievesTheApplicationByItsId() {
        clientService.registerClient(applicationId, "deviceName", "deviceUuid");

        verify(applicationRepository).getById(applicationId);
    }

    @Test
    public void testRegisterClientInstantiatesAClientOfTheRetrievedApplication() {
        clientService.registerClient(applicationId, "deviceName", "deviceUuid");

        verify(applicationInstantiator).create(application, "deviceName", "deviceUuid");
    }

    @Test
    public void testRegisterClientReturnsTheInstantiatedClientOfTheRetrievedApplication() {
        assertEquals(newClient, clientService.registerClient(applicationId, "deviceName", "deviceUuid"));
    }

    @Test
    public void testSaveClientInstructsTheClientToPrepareItselfForSaving() {
        clientService.saveClient(existingClient);

        verify(existingClient).persist();
    }

    @Test
    public void testSaveClientAddsTheClientToTheRepository() {
        clientService.saveClient(existingClient);

        verify(clientRepository).save(existingClient);
    }
}

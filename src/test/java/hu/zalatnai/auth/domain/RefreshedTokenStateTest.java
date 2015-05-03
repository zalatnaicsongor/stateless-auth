package hu.zalatnai.auth.domain;

import hu.zalatnai.sdk.service.SHA256;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RefreshedTokenStateTest {

    @Mock
    SHA256 sha256;

    @Mock
    StateRepository<TokenState> tokenStateRepository;

    @Mock
    Token token;

    @Mock
    HashedTokenState hashedTokenState;

    byte[] accessToken = {'a', 'b'};
    byte[] hashedAccessToken = {'x', 'y'};

    //SUT
    @InjectMocks
    RefreshedTokenState refreshedTokenState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(token.getAccessToken()).thenReturn(accessToken);
        when(sha256.hash(accessToken)).thenReturn(hashedAccessToken);

        when(tokenStateRepository.getById(TokenState.STATE_HASHED)).thenReturn(hashedTokenState);
    }

    @Test
    public void testHashHashesTheAccessToken() {
        refreshedTokenState.hash(token);

        verify(sha256).hash(accessToken);
    }

    @Test
    public void testHashSetsTheHashedAccessTokenAsTheAccessTokenOnTheToken() {
        refreshedTokenState.hash(token);

        verify(token).setAccessToken(hashedAccessToken);
    }

    @Test
    public void testHashReturnsHashedTokenStateAsTheNextState() {
        TokenState retval = refreshedTokenState.hash(token);

        verify(tokenStateRepository).getById(TokenState.STATE_HASHED);

        assertTrue(retval instanceof HashedTokenState);
    }

    @Test
    public void testGetIdReturnsThatTheStateIsTheRefreshedState() {
        assertEquals(TokenState.STATE_REFRESHED, refreshedTokenState.getId());
    }
}
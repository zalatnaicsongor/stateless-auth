package hu.zalatnai.auth.domain;

import hu.zalatnai.sdk.service.SHA256;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UnhashedTokenStateTest {

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

    byte[] refreshToken = {'c', 'd'};
    byte[] hashedRefreshToken = {'f', 'g'};

    //SUT
    @InjectMocks
    UnhashedTokenState unhashedTokenState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(token.getAccessToken()).thenReturn(accessToken);
        when(token.getRefreshToken()).thenReturn(new byte[]{'c', 'd'});
        when(sha256.hash(accessToken)).thenReturn(hashedAccessToken);
        when(sha256.hash(refreshToken)).thenReturn(hashedRefreshToken);

        when(tokenStateRepository.getById(TokenState.STATE_HASHED)).thenReturn(hashedTokenState);
    }

    @Test
    public void testPersistHashesTheAccessToken() {
        unhashedTokenState.hash(token);

        verify(sha256).hash(accessToken);
    }

    @Test
    public void testPersistHashesTheRefreshToken() {
        unhashedTokenState.hash(token);

        verify(sha256).hash(refreshToken);
    }

    @Test
    public void testPersistSetsTheHashedRefreshTokenAsTheRefreshTokenOnTheToken() {
        unhashedTokenState.hash(token);

        verify(token).setRefreshToken(hashedRefreshToken);
    }

    @Test
    public void testPersistSetsTheHashedAccessTokenAsTheAccessTokenOnTheToken() {
        unhashedTokenState.hash(token);

        verify(token).setAccessToken(hashedAccessToken);
    }

    @Test
    public void testPersistReturnsHashedTokenStateAsTheNextState() {
        TokenState retval = unhashedTokenState.hash(token);

        verify(tokenStateRepository).getById(TokenState.STATE_HASHED);

        assertTrue(retval instanceof HashedTokenState);
    }

    @Test
    public void testGetIdReturnsThatTheStateIsTheUnhashedState() {
        assertEquals(TokenState.STATE_UNHASHED, unhashedTokenState.getId());
    }
}
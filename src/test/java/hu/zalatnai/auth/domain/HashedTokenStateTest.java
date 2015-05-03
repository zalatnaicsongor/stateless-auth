package hu.zalatnai.auth.domain;

import hu.zalatnai.auth.domain.exception.InvalidRefreshTokenException;
import hu.zalatnai.auth.domain.exception.OperationInapplicableException;
import hu.zalatnai.sdk.service.RandomGenerator;
import hu.zalatnai.sdk.service.domain.StateRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class HashedTokenStateTest {

    @Mock
    StateRepository<TokenState> tokenStateRepository;

    @Mock
    RefreshedTokenState refreshedTokenState;

    @Mock
    RandomGenerator randomGenerator;

    @Mock
    Token token;

    byte[] originalRefreshToken = {'c', 'p', 'x'};

    byte[] refreshedAccessToken = {'j', 'a', 'g'};

    //SUT
    @InjectMocks
    HashedTokenState hashedTokenState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(tokenStateRepository.getById(TokenState.STATE_REFRESHED)).thenReturn(refreshedTokenState);

        when(token.getRefreshToken()).thenReturn(originalRefreshToken);

        when(randomGenerator.getBytes(32)).thenReturn(refreshedAccessToken);
    }

    @Test(expected = InvalidRefreshTokenException.class)
    public void RefreshThrowsIfTheSuppliedHashedRefreshTokenIsNotValid() {
        hashedTokenState.refresh(token, new byte[]{'d', 'e', 'c', 'k', ' ', 's', 'e', 'a', 'l', 'a', 'n', 't'});
    }

    @Test
    public void RefreshGenerates32BytesOfRandomAsTheNewAccessToken() {
        hashedTokenState.refresh(token, originalRefreshToken);

        verify(randomGenerator).getBytes(32);
    }

    @Test
    public void RefreshSetsTheNewlyGeneratedAccessTokenAsTheAccessTokenOfTheToken() {
        hashedTokenState.refresh(token, originalRefreshToken);

        verify(token).setAccessToken(refreshedAccessToken);
    }

    @Test
    public void RefreshReturnsRefreshedStateAsTheNextState() {
        TokenState retval = hashedTokenState.refresh(token, originalRefreshToken);

        verify(tokenStateRepository).getById(TokenState.STATE_REFRESHED);

        assertTrue(retval instanceof RefreshedTokenState);
    }

    @Test
    public void getIdReturnsHashedAsTheStatesId() {
        assertEquals(TokenState.STATE_HASHED, hashedTokenState.getId());
    }

    @Test(expected = OperationInapplicableException.class)
    public void testHashThrows() {
        hashedTokenState.hash(token);
    }
}
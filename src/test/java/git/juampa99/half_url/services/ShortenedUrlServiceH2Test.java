package git.juampa99.half_url.services;

import git.juampa99.half_url.domain.ShortenedUrl;
import git.juampa99.half_url.errors.InvalidKeyException;
import git.juampa99.half_url.errors.InvalidUrlException;
import git.juampa99.half_url.repositories.ShortenedUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ShortenedUrlServiceH2Test {

    String VALID_URL = "www.google.com";
    String INVALID_URL = "a";
    String VALID_KEY = "000000";
    String INVALID_KEY = "12345678910111231354887";

    @Mock
    ShortenedUrlRepository shortenedUrlRepository;

    ShortenedUrlServiceH2 shortenedUrlServiceH2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        shortenedUrlServiceH2 = new ShortenedUrlServiceH2(shortenedUrlRepository);
    }

    @Test
    void getUrlByKey() {

        try {
            shortenedUrlServiceH2.getUrlByKey(VALID_KEY); // Fails because repository is a mock object,
                                                          // so findByKey() returns null, and getUrlByKey uses a
                                                          // method of said object, causing a NullPointerException
        } catch (Exception ignored) { }

        verify(shortenedUrlRepository, times(1)).findByKey(VALID_KEY);
    }

    @Test
    void generateKey() {
        String key = shortenedUrlServiceH2.generateKey();

        assertEquals(key.length(), 6);
        assertTrue(shortenedUrlServiceH2.validateKey(key));
    }

    @Test
    void validateKey() {
        boolean passedNull = shortenedUrlServiceH2.validateKey(null);
        boolean passedValidKey = shortenedUrlServiceH2.validateKey(VALID_KEY);
        boolean testMaxKeyLength = shortenedUrlServiceH2.validateKey(INVALID_KEY);

        assertFalse(passedNull);
        assertTrue(passedValidKey);
        assertFalse(testMaxKeyLength);
    }

    @Test
    void validateUrl() {
        boolean googleValidation = shortenedUrlServiceH2.validateUrl("www.google.com");
        boolean invalid = shortenedUrlServiceH2.validateUrl("a");

        assertTrue(googleValidation);
        assertFalse(invalid);
    }

    @Test
    void saveValid() {
        shortenedUrlServiceH2.save(VALID_URL, VALID_KEY);

        verify(shortenedUrlRepository, times(1)).save(anyObject());
    }

    @Test
    void saveInvalid() {
        assertThrows(InvalidUrlException.class, () -> {
            shortenedUrlServiceH2.save(INVALID_URL);
        });

        assertThrows(InvalidKeyException.class, () -> {
            shortenedUrlServiceH2.save(VALID_URL, INVALID_KEY);
        });
    }
}
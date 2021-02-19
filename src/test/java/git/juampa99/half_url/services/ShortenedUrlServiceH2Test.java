package git.juampa99.half_url.services;

import git.juampa99.half_url.domain.ShortenedUrl;
import git.juampa99.half_url.repositories.ShortenedUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ShortenedUrlServiceH2Test {

    @Mock
    ShortenedUrlRepository shortenedUrlRepository;

    ShortenedUrlServiceH2 shortenedUrlServiceH2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        shortenedUrlServiceH2 = new ShortenedUrlServiceH2(shortenedUrlRepository);
    }

    // TODO: More testing
    @Test
    void getUrlByKey() {
        String url = null;

        try {
            url = shortenedUrlServiceH2.getUrlByKey("000000");
        }
        catch(Exception e){};

        assertNull(url);
    }

    @Test
    void generateKey() {
        String key = shortenedUrlServiceH2.generateKey();

        assertEquals(key.length(), 6);
    }

    // TODO: Test for repeated keys
    @Test
    void validateKey() {
        boolean passedNull = shortenedUrlServiceH2.validateKey(null);
        boolean passedValidKey = shortenedUrlServiceH2.validateKey("000000");

        assertFalse(passedNull);
        assertTrue(passedValidKey);
    }
}
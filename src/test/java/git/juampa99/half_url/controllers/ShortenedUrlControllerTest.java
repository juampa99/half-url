package git.juampa99.half_url.controllers;

import git.juampa99.half_url.services.ShortenedUrlService;
import git.juampa99.half_url.services.ShortenedUrlServiceH2;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyCollection;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

class ShortenedUrlControllerTest {

    ShortenedUrlController shortenedUrlController;

    @Mock
    ShortenedUrlService shortenedUrlService;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        shortenedUrlController = new ShortenedUrlController(shortenedUrlService);
    }

    @Test
    void saveUrl() {
        shortenedUrlController.saveUrl("www.example.com");
        shortenedUrlController.saveUrl("www.example.com", "valid_key");

        verify(shortenedUrlService, times(1)).save("www.example.com");
        verify(shortenedUrlService, times(1)).save("www.example.com", "valid_key");
    }


    @Test
    void redirect() {
        shortenedUrlController.redirect("valid_key");

        verify(shortenedUrlService, times(1)).getUrlByKey("valid_key");
    }
}
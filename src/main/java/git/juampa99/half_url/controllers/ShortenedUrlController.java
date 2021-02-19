package git.juampa99.half_url.controllers;

import git.juampa99.half_url.domain.ShortenedUrl;
import git.juampa99.half_url.services.ShortenedUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class ShortenedUrlController {

    ShortenedUrlService shortenedUrlService;

    ShortenedUrlController() {}

    @Autowired
    ShortenedUrlController(ShortenedUrlService shortenedUrlService) {
        this.shortenedUrlService = shortenedUrlService;
    }

    @RequestMapping(value = "/save/{url}", method = GET)
    @ResponseBody
    public ResponseEntity<?> saveUrl(@PathVariable String url) {
        ShortenedUrl savedUrl = shortenedUrlService.save(url);

        if(savedUrl != null)
            return new ResponseEntity<>( savedUrl.getKey(), HttpStatus.OK );
        else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @RequestMapping(value = "/save/{url}/{key}", method = GET)
    @ResponseBody
    public ResponseEntity<?> saveUrl(@PathVariable String url, @PathVariable String key) {
        ShortenedUrl savedUrl = shortenedUrlService.save(url, key);

        if(savedUrl != null)
            return new ResponseEntity<>( savedUrl.getKey(), HttpStatus.OK );
        else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @RequestMapping(value = "/{key}", method = GET)
    @ResponseBody
    public ResponseEntity<?> redirect(@PathVariable String key) {
        String url = shortenedUrlService.getUrlByKey(key);

        if(url != null)
            return new ResponseEntity<>(url, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

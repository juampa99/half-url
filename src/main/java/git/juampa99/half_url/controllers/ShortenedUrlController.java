package git.juampa99.half_url.controllers;

import git.juampa99.half_url.domain.ShortenedUrl;
import git.juampa99.half_url.errors.InvalidKeyException;
import git.juampa99.half_url.errors.InvalidUrlException;
import git.juampa99.half_url.services.ShortenedUrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
public class ShortenedUrlController {

    ShortenedUrlService shortenedUrlService;

    ShortenedUrlController() {}

    @Autowired
    ShortenedUrlController(ShortenedUrlService shortenedUrlService) {
        this.shortenedUrlService = shortenedUrlService;
    }

    /*
     * Takes an URL and saves it to the database, together with an automatically generated KEY.
     * If the URL is not valid, returns an error
     * */

    @RequestMapping(value = "/save/{url}", method = POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> saveUrl(@PathVariable String url) {

        try {
            ShortenedUrl savedUrl = shortenedUrlService.save(url);
            return new ResponseEntity<>(Collections.singletonMap("key", savedUrl.getKey()) , HttpStatus.CREATED );
        } catch (InvalidUrlException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", "invalid url") , HttpStatus.NOT_FOUND);
        }

    }

    /*
    * Takes an URL and a KEY and saves it to the database, if either the URL or the KEY are not valid, returns an error
    * */

    @RequestMapping(value = "/save/{url}/{key}", method = POST, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> saveUrl(@PathVariable String url, @PathVariable String key) {

        try {
            ShortenedUrl savedUrl = shortenedUrlService.save(url, key);
            return new ResponseEntity<>(Collections.singletonMap("key", savedUrl.getKey()) , HttpStatus.CREATED );
        } catch (InvalidKeyException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", "invalid key"), HttpStatus.NOT_FOUND);
        } catch(InvalidUrlException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", "invalid url"), HttpStatus.NOT_FOUND);
        }

    }

    /*
    * Takes a KEY and returns its respective URL, if KEY does not exist in the database, returns an error
    * */

    @RequestMapping(value = "/{key}", method = GET, produces = "application/json")
    @ResponseBody
    public ResponseEntity<?> redirect(@PathVariable String key) {

        try {
            String url = shortenedUrlService.getUrlByKey(key);
            return new ResponseEntity<>(Collections.singletonMap("url", url), HttpStatus.OK);
        } catch (InvalidKeyException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", "invalid key") , HttpStatus.NOT_FOUND);
        }

    }
}

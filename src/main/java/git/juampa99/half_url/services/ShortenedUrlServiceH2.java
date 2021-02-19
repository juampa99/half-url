package git.juampa99.half_url.services;

import git.juampa99.half_url.domain.ShortenedUrl;
import git.juampa99.half_url.repositories.ShortenedUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class ShortenedUrlServiceH2 implements ShortenedUrlService {

    final private int HEX_LENGTH = 6;
    final private int MAX_HEX = (int) Math.pow(16, HEX_LENGTH);

    private ShortenedUrlRepository shortenedUrlRepository;

    @Autowired
    public ShortenedUrlServiceH2(ShortenedUrlRepository shortenedUrlRepository) {
        this.shortenedUrlRepository = shortenedUrlRepository;
    }

    @Override
    public String getUrlByKey(String key) {
        Optional<ShortenedUrl> shortenedUrl = shortenedUrlRepository.findByKey(key);
        String url = null;

        if(shortenedUrl.isPresent())
            url = shortenedUrl.get().getOriginalUrl();
        else
            throw new RuntimeException("Couldnt find url with key " + key);

        return url;
    }

    @Override
    public ShortenedUrl save(ShortenedUrl shortenedUrl) {
        ShortenedUrl url;
        if(shortenedUrl != null)
            url = shortenedUrlRepository.save(shortenedUrl);
        else
            throw new RuntimeException("Url can't be null");

        return url;
    }

    @Override
    public ShortenedUrl save(String originalUrl) {
        ShortenedUrl newShortenedUrl = new ShortenedUrl(originalUrl, generateKey());

        return shortenedUrlRepository.save(newShortenedUrl);
    }

    @Override
    public ShortenedUrl save(String originalUrl, String key) throws RuntimeException {
        if(!validateKey(key))
            throw new RuntimeException(key + " is not a valid key");

        ShortenedUrl newShortenedUrl = new ShortenedUrl(originalUrl, generateKey());

        return shortenedUrlRepository.save(newShortenedUrl);
    }

    @Override
    public Iterable<ShortenedUrl> getAll() {
        return shortenedUrlRepository.findAll();
    }

    @Override
    public String generateKey() {

        String key;

        do {
            int randNum = ThreadLocalRandom.current().nextInt(1, MAX_HEX);
            key = Integer.toHexString(randNum);
            key = hexToSixDigits(key);
        } while(!validateKey(key));

        return key;
    }

    @Override
    public boolean validateKey(String key) {
        if(key == null)
            return false;

        boolean isSixChars = key.length() == HEX_LENGTH;
        boolean exists = shortenedUrlRepository.findByKey(key).isPresent();

        return isSixChars && !exists;
    }

    private String hexToSixDigits(String hex) {
        while(hex.length() < 6)
            hex = '0' + hex;

        return hex;
    }


}

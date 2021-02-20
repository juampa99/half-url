package git.juampa99.half_url.services;

import git.juampa99.half_url.domain.ShortenedUrl;
import git.juampa99.half_url.errors.InvalidKeyException;
import git.juampa99.half_url.errors.InvalidUrlException;
import git.juampa99.half_url.repositories.ShortenedUrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ShortenedUrlServiceH2 implements ShortenedUrlService {

    final private int MAX_KEY_LENGTH = 16;
    final private int HEX_LENGTH = 6;
    final private int MAX_HEX = (int) Math.pow(16, HEX_LENGTH);

    private ShortenedUrlRepository shortenedUrlRepository;

    @Autowired
    public ShortenedUrlServiceH2(ShortenedUrlRepository shortenedUrlRepository) {
        this.shortenedUrlRepository = shortenedUrlRepository;
    }

    @Override
    public String getUrlByKey(String key) throws InvalidKeyException {
        Optional<ShortenedUrl> shortenedUrl = shortenedUrlRepository.findByKey(key);
        String url;

        if(shortenedUrl.isPresent())
            url = shortenedUrl.get().getOriginalUrl();
        else
            throw new InvalidKeyException(key);

        return url;
    }

    @Override
    public ShortenedUrl save(ShortenedUrl shortenedUrl) throws RuntimeException {
        ShortenedUrl url;

        if(shortenedUrl == null)
            throw new RuntimeException("null ShortenedUrl object is not valid");
        if(!validateUrl(shortenedUrl.getOriginalUrl()))
            throw new InvalidUrlException(shortenedUrl.getOriginalUrl());
        if(!validateKey(shortenedUrl.getKey()))
            throw new InvalidKeyException(shortenedUrl.getKey());
        else
            url = shortenedUrlRepository.save(shortenedUrl);

        return url;
    }

    @Override
    public ShortenedUrl save(String originalUrl) throws InvalidUrlException {
        if(!validateUrl(originalUrl))
            throw new InvalidUrlException(originalUrl);

        ShortenedUrl newShortenedUrl = new ShortenedUrl(originalUrl, generateKey());

        return shortenedUrlRepository.save(newShortenedUrl);
    }

    @Override
    public ShortenedUrl save(String originalUrl, String key) throws InvalidKeyException, InvalidUrlException {
        if(!validateKey(key))
            throw new InvalidKeyException(key);
        if(!validateUrl(originalUrl))
            throw new InvalidUrlException(originalUrl);

        ShortenedUrl newShortenedUrl = new ShortenedUrl(originalUrl, generateKey());

        return shortenedUrlRepository.save(newShortenedUrl);
    }

    @Override
    public Iterable<ShortenedUrl> getAll() {
        return shortenedUrlRepository.findAll();
    }

    @Override
    public boolean validateUrl(String url) {
        String urlRegex = "[-a-zA-Z0-9@:%._\\+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_\\+.~#?&//=]*)";

        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);

        return matcher.find();
    }

    /*
    * Generates a 6 digit hex string
    * */
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

    /*
    * Makes sure the KEY is between the predefined length bounds and that its unique (it doesnt already exists in the DB)
    * */
    @Override
    public boolean validateKey(String key) {
        if(key == null)
            return false;

        boolean isBetweenBounds = key.length() <= MAX_KEY_LENGTH && key.length() > 0;
        boolean exists = shortenedUrlRepository.findByKey(key).isPresent();

        return isBetweenBounds && !exists;
    }

    private String hexToSixDigits(String hex) {
        while(hex.length() < HEX_LENGTH)
            hex = '0' + hex;

        return hex;
    }


}

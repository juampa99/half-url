package git.juampa99.half_url.services;

import git.juampa99.half_url.domain.ShortenedUrl;
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
    public String getUrlByKey(String key) throws RuntimeException {
        Optional<ShortenedUrl> shortenedUrl = shortenedUrlRepository.findByKey(key);
        String url;

        if(shortenedUrl.isPresent())
            url = shortenedUrl.get().getOriginalUrl();
        else
            throw new RuntimeException("Couldnt find url with key " + key);

        return url;
    }

    @Override
    public ShortenedUrl save(ShortenedUrl shortenedUrl) throws RuntimeException {
        ShortenedUrl url;

        if(shortenedUrl == null)
            throw new RuntimeException("null ShortenedUrl object is not valid");
        if(!validateUrl(shortenedUrl.getOriginalUrl()))
            throw new RuntimeException(shortenedUrl.getOriginalUrl() + " is not a valid URL");
        if(!validateKey(shortenedUrl.getKey()))
            throw new RuntimeException(shortenedUrl.getOriginalUrl() + " is not a valid KEY");
        else
            url = shortenedUrlRepository.save(shortenedUrl);

        return url;
    }

    @Override
    public ShortenedUrl save(String originalUrl) throws RuntimeException {
        if(!validateUrl(originalUrl))
            throw new RuntimeException(originalUrl + " is not a valid URL");

        ShortenedUrl newShortenedUrl = new ShortenedUrl(originalUrl, generateKey());

        return shortenedUrlRepository.save(newShortenedUrl);
    }

    @Override
    public ShortenedUrl save(String originalUrl, String key) throws RuntimeException {
        if(!validateKey(key))
            throw new RuntimeException(key + " is not a valid key");
        if(!validateUrl(originalUrl))
            throw new RuntimeException(originalUrl + " is not a valid url");

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

        boolean isBetweenBounds = key.length() <= MAX_KEY_LENGTH;
        boolean exists = shortenedUrlRepository.findByKey(key).isPresent();

        return isBetweenBounds && !exists;
    }

    private String hexToSixDigits(String hex) {
        while(hex.length() < HEX_LENGTH)
            hex = '0' + hex;

        return hex;
    }


}

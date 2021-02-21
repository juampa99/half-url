package git.juampa99.half_url.services;

import git.juampa99.half_url.domain.ShortenedUrl;
import git.juampa99.half_url.errors.InvalidKeyException;
import git.juampa99.half_url.errors.InvalidUrlException;
import git.juampa99.half_url.repositories.ShortenedUrlRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("shortenedUrlService")
@Profile({"H2", "default"})
public class ShortenedUrlServiceH2 implements ShortenedUrlService {


    private final int HEX_LENGTH;
    private final int MAX_KEY_LENGTH;
    private final int MAX_HEX_VALUE;

    private final ShortenedUrlRepository shortenedUrlRepository;

    public ShortenedUrlServiceH2(ShortenedUrlRepository shortenedUrlRepository,
                                 @Value("${keyconfig.HEX_LENGTH}") int HEX_LENGTH,
                                 @Value("${keyconfig.MAX_KEY_LENGTH}") int MAX_KEY_LENGTH
    ) {
        this.shortenedUrlRepository = shortenedUrlRepository;

        this.HEX_LENGTH = HEX_LENGTH;
        this.MAX_KEY_LENGTH = MAX_KEY_LENGTH;
        this.MAX_HEX_VALUE = (int) Math.pow(16, HEX_LENGTH);
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
            int randNum = ThreadLocalRandom.current().nextInt(1, MAX_HEX_VALUE);
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

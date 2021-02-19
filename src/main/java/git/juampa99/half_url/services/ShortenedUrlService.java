package git.juampa99.half_url.services;

import git.juampa99.half_url.domain.ShortenedUrl;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface ShortenedUrlService {

    public String getUrlByKey(String key);

    public String generateKey();

    public boolean validateKey(String key);

    public ShortenedUrl save(ShortenedUrl shortenedUrl);

    public ShortenedUrl save(String originalUrl);

    public ShortenedUrl save(String originalUrl, String key) throws RuntimeException;

    public Iterable<ShortenedUrl> getAll();

}
package git.juampa99.half_url.repositories;

import git.juampa99.half_url.domain.ShortenedUrl;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ShortenedUrlRepository extends CrudRepository<ShortenedUrl, Long> {

    public Optional<ShortenedUrl> findByKey(String key);

}

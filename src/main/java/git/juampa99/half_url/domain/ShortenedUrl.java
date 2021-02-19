package git.juampa99.half_url.domain;

import javax.persistence.*;

@Entity
public class ShortenedUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String key;
    private String originalUrl;

    public ShortenedUrl() {}

    public ShortenedUrl(String originalUrl, String key) {
        this.originalUrl = originalUrl;
        this.key = key;
    }

    @Override
    public String toString() {
        return "{ originalUrl: " + originalUrl + " }";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

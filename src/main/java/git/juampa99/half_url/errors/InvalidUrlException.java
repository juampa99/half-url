package git.juampa99.half_url.errors;

/**
 * Should be used when ShortenedUrlService.validateUrl(url) is false
 */

public class InvalidUrlException extends RuntimeException{

    public InvalidUrlException() {
        super();
    }

    public InvalidUrlException(String url) {
        super("Invalid URL: " + url);
    }

}

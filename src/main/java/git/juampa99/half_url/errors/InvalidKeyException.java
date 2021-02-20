package git.juampa99.half_url.errors;

/**
 * Should be used when ShortenedUrlService.validateKey(key) is false
 */

public class InvalidKeyException extends RuntimeException {

    public InvalidKeyException() {
        super();
    }

    public InvalidKeyException(String key) {
        super("Invalid KEY: " + key);
    }

}

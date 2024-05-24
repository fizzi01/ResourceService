package it.unisalento.pasproject.resourceservice.security;

/**
 * The SecurityConstants class provides constants related to security configuration.
 * It includes a JWT_SECRET constant that is used for signing JSON Web Tokens (JWTs).
 */
public class SecurityConstants {
    /**
     * The secret key used for signing JWTs.
     * This should be kept secret and secure.
     */
    public static final String JWT_SECRET = "QGgjfe56Lenuir3nsLnuSa8X9WazGRmh" +
            "dQ28u7l5FwKieig8mUG4AkpAetrYrjzB" +
            "K0Rslh8KuQmJnzPGOi1wq00YgS1KBx6F" +
            "fBipUl6ADlLgXgIsnOQgG16xHaaYTNlo" +
            "HFcjeyMWFvfvqegYmsx4O1Sa6oUL8o3u" +
            "XCCpbwzzFrTcKz2bf0ggdCg16t4Hg35I" +
            "g1X3xdGRJQWxjbIjkUKVgnlAj7biLEZt" +
            "rLHcy3Fdfzfd9UvNgXOy6kirjPT3ChGf";

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_UTENTE = "UTENTE";
    public static final String ROLE_MEMBRO = "MEMBRO";
}

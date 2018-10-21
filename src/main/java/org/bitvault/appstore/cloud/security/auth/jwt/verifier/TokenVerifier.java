package org.bitvault.appstore.cloud.security.auth.jwt.verifier;

public interface TokenVerifier {
    public boolean verify(String jti);
}

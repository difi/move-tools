package no.difi.move.serviceregistry.oauth2;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyType;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.proc.BadJWSException;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;

public class JwtDecoder {

    public static String getPayload(String serialized, URL jwkUrl) throws BadJWSException {
        JWSObject jwsObject;
        try {
            jwsObject = JWSObject.parse(serialized);
        } catch (ParseException e) {
            throw new BadJWSException("Could not parse signed string", e);
        }

        JWKSet jwkSet;
        try {
            jwkSet = JWKSet.load(jwkUrl);
        } catch (IOException | ParseException e) {
            throw new BadJWSException(String.format("Could not load JWK Set from url %s", jwkUrl));
        }
        RSAKey rsaKey = jwkSet.getKeys().stream()
                .filter(k -> k.getKeyType() == KeyType.RSA)
                .findFirst()
                .map(k -> (RSAKey) k)
                .orElseThrow(() -> new BadJWSException("RSA keytype not found in JWK"));

        try {
            JWSVerifier jwsVerifier = new RSASSAVerifier(rsaKey);
            if (!jwsObject.verify(jwsVerifier)) {
                throw new BadJWSException("Signature did not successfully verify");
            }
        } catch (JOSEException e) {
            throw new BadJWSException("Could not verify JWS", e);
        }

        return jwsObject.getPayload().toString();
    }
}

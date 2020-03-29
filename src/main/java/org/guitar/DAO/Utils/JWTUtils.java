package org.guitar.DAO.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/* Our simple static class that demonstrates how to create and decode JWTs. */
public class JWTUtils {

    // The secret key. This should be in a property file NOT under source
    // control and not hard coded in real life. We're putting it here for
    // simplicity.
    private static String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5_9hKolVX8xNrQDcNRfVEdTZNOuOyqEGhXEbdJI-ZQ19k_o9MI0y3eZN2lp9jow55FfXMiINEdt1XR85VipRLSOkT6kSpzs2x-jbLDiz9iFVzkd81YKxMgPA7VfZeQUm4n-mOmnWMaVX30zGFU4L3oPBctYKkl4dYfqYWqRNfrgPJVi5DGFjywgxx0ASEiJHtV72paI3fDR2XwlSkyhhmY-ICjCRmsJN4fX1pdoL8a18-aQrvyu4j0Os6dVPYIoPvvY0SAZtWYKHfM15g7A3HD4cVREf9cUsprCRK93w";

    public static String createJWT(
            String id,
            String issuer,
            String subject,
            String audience,
            long ttlMillis,
            String givenName,
            String surname,
            String email,
            String role) {

        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our ApiKey secret
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                //Additional Claims
                //.setClaims(claims)
                .claim("GivenName", givenName)
                .claim("Surname", surname)
                .claim("Email", email)
                .claim("Role", role)
                //Standard JWT Claims
                .setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .setIssuer(issuer)
                .setAudience(audience)
                //Signing
                .signWith(signatureAlgorithm, signingKey);

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Claims decodeJWT(String jwt) {

        //This line will throw an exception if it is not a signed JWS (as expected)
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }


    public void getClaimFromToken() {

    }

    public static Boolean isJWTValid(Claims claims) {
        // Add other checks in future like :

        //(claims.getId(), "Token id hasn't been found");
        //(claims.getSubject(), "Token subject hasn't been found");
        //(claims.getExpiration(), "Token expiration date hasn't been found");
        //(claims.getIssuedAt(), "Token creation date hasn't been found");

         Boolean validity = true;

         //Checking Expiration Claim
         if (claims.getExpiration() != null){
             Date expDate = claims.getExpiration();
             Date date = new Date();
             if (expDate.getTime() < date.getTime()) {
                 validity = false;
                 System.out.println("Token Expired");
             }
         } else {
             validity = false;
             System.out.println("Token expiration date hasn't been found (No Expiration Claim)");
         }

        return validity;
    }
}
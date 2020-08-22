package com.jfavilau.demologin.jwt;

import com.jfavilau.demologin.entity.Login;
import io.jsonwebtoken.*;
import java.util.Date;

/**
 *
 * @author jhon.avila
 * 
 * Generador de JSON web token para la autorización del aplicativo
 */
public class GeneratorJWT 
{
    
    /**
     * 
     * @param username
     * @param id
     * @param subject
     * @param keySecret
     * @param ttlMillis
     * @return 
     */
    public static String CreateJWT(Login username, String id, String subject, String keySecret, long ttlMillis )
    {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        
        Date now = new Date( nowMillis );
        
        JwtBuilder builder = Jwts.builder().setId( id )
                .setIssuedAt( now )
                .setSubject( subject )
                .signWith( signatureAlgorithm, keySecret );

        if ( ttlMillis >= 0 ) 
        {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date( expMillis ) ;
            builder.setExpiration( exp ) ;

        }

        return builder.compact () ;
    }

}

package com.jfavilau.demologin.repository;

import com.jfavilau.demologin.entity.Login;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author jhon.avila
 */

public interface ILoginRepository extends JpaRepository <Login, Long >
{

    Login findByUser( String user );

    Login findByEmail( String email );
}

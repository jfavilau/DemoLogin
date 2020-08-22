package com.jfavilau.demologin.util;

/**
 *
 * @author jhon.avila
 * 
 * Respuestas de los servicios establecidos
 */
public enum ResponseServices 
{

    SAVEUSUARIOOK( "{\"Response\":\" User saved Correctly \"}" ),
    NOTFOUNDUSUARIO( "{\"Response\":\" User not found \"}" ),
    PARSEJSONERROR( "{\"Response\":\" Bad request JSON \"}" ),
    PASSINCORRECT( "{\"Response\":\" Password Incorrect \"}" ),
    RESETPASSOK( "{\"Response\":\" Reset password correct \"}" ),
    CHANGEPASSOK( "{\"Response\":\" Change of password correct \"}" ),
    QUESTIONSSAVEOK( "{\"Response\":\" Questions saved correctly \"}" ),
    ANSWERSINCORRECT( "{\"Response\":\" Answers Incorrect \"}" ),
    NEWPASSINCORRECT( "{\"Response\":\" Can't use the same password or any of the last two passwords\"}"),
    USERINACTIVE( "{\"Response\":\" User isn't Active\"}" ),
    SEVERALUSERS( "{\"Response\":\"Several users have this email\"}" ),
    USERANDORPASSINCORRECT( "{\"Response\":\" User and/or Password incorrect \"}" );

    String text;

    ResponseServices(String text)
    {
        this.text = text;
    }

    @Override
    public String toString() 
    {
        return text;
    }
}

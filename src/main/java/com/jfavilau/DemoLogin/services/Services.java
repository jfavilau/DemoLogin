package com.jfavilau.demologin.services;

import com.jfavilau.demologin.config.Settings;
import com.jfavilau.demologin.entity.Login;
import com.jfavilau.demologin.jwt.GeneratorJWT;
import com.jfavilau.demologin.mail.MailSender;
import com.jfavilau.demologin.password.PasswordGenerator;
import com.jfavilau.demologin.repository.ILoginRepository;
import com.jfavilau.demologin.util.ResponseServices;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.Session;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Properties;

/**
 * @author jhon.avila
 * <p>
 * Servicios REST para el proceso de autenticación
 */

@CrossOrigin(origins = {"*"}, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS}, allowedHeaders = {"*"})
@RestController
@RequestMapping(value = "/auth")
public class Services {
    private static final Logger logger = LoggerFactory.getLogger(Services.class);
    private static final String NEW_PASS = "newPass";
    private static final String PASSWORD = "password";
    private static final String ACTIVE = "active";
    private static final String USER = "user";
    private static final String EMAIL = "email";
    private static final String MAIL_HOST = "mail.smtp.host";
    private static final String MAIL_PORT = "mail.smtp.port";
    private static final String NOT_FOUND_USER = "User not found";
    private static final String WRONG_QUESTIONS = "Wrong questions";
    private static final String WRONG_USER_OR_PASS = "User and/or password wrong";
    private static final String SHA_256 = "SHA-256";
    
    private final Settings properties;
    

    @Autowired
    public Services(Settings properties) {
        this.properties = properties;
    }

    @Autowired
    private ILoginRepository repository;

    /**
     * Servicio por el cual se hace la autenticación para ingresar al aplicativo,
     *
     * @param login JSON que contiene el usuario y la contraseña
     * @return JSON contiene un JSON web token para asegurar los request a los
     * recursos protegidos y una bandera que me indica el estado del usuario
     */
    @PostMapping(value = "/singin", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    String auth(@RequestBody Login login) {

        Login userLogin = repository.findByUser(login.getUser());

        if (userLogin != null) {
            if (userLogin.getPassword().equals(login.getPassword())) {
                if (userLogin.getActive().equalsIgnoreCase(ACTIVE)) {
                    String jwt = GeneratorJWT.CreateJWT(login, login.getUser(), properties.getTokenIssuer(), properties.getTokenSigningKey(), properties.getTokenExpirationTime());
                    Date now = new Date();
                    userLogin.setDateLastSignIn(new Timestamp(now.getTime()));
                    repository.flush();
                    logger.info("Token: {}", jwt);

                    return "{\"Token\":\"" + jwt + "\","
                            + "\"flag\":\"" + userLogin.getFlag() + "\"}";
                } else {
                    return ResponseServices.USERINACTIVE.toString();
                }

            } else {
                logger.error(WRONG_USER_OR_PASS);
                return ResponseServices.USERANDORPASSINCORRECT.toString();
            }

        } else {
            logger.error(WRONG_USER_OR_PASS);
            return ResponseServices.USERANDORPASSINCORRECT.toString();
        }

    }

    /**
     * Servicio para almacenar las preguntas y respuestas de un usuario, cuando
     * el usuario ingresa por primera vez al aplicativo se obliga a almacenarlas
     * y cuando se hace un reset de las preguntas
     *
     * @param users JSON contiene el usuario, las preguntas y respuestas
     * @return JSON contiene mensajes satisfactorio o de error del proceso
     */
    @PostMapping(value = "/questions/save", consumes = "application/json", produces = "application/json")
    public @ResponseBody String saveQuestions(@RequestBody Login users) {
        Login questions;
        questions = repository.findByUser(users.getUser());

        if (questions != null) {
            questions.setQuestion1(users.getQuestion1());
            questions.setQuestion2(users.getQuestion2());
            questions.setQuestion3(users.getQuestion3());
            questions.setQuestion4(users.getQuestion4());
            questions.setQuestion5(users.getQuestion5());
            questions.setReply1(users.getReply1());
            questions.setReply2(users.getReply2());
            questions.setReply3(users.getReply3());
            questions.setReply4(users.getReply4());
            questions.setReply5(users.getReply5());
            repository.flush();

            return ResponseServices.QUESTIONSSAVEOK.toString();
        } else {
            return ResponseServices.NOTFOUNDUSUARIO.toString();
        }

    }



    /**
     * Servicio para consultar las preguntas almacenadas cuando se inicia el
     * proceso para recordar la contraseña, ésta búsqueda se realiza por el dni
     * del usuario
     *
     * @param usuario dni del usuario
     * @return JSON contiene las preguntas almacenadas para el usuario
     */
    @GetMapping(value = "/questions/{user}", produces = "application/json")
    public @ResponseBody
    String askQuestionsById(@PathVariable(value = USER) String usuario) {
        Login preguntas;
        Login resPreguntas = new Login();

        preguntas = repository.findByUser(usuario);

        if (preguntas != null) {
            resPreguntas.setUser(preguntas.getUser());
            resPreguntas.setQuestion1(preguntas.getQuestion1());
            resPreguntas.setQuestion2(preguntas.getQuestion2());
            resPreguntas.setQuestion3(preguntas.getQuestion3());
            resPreguntas.setQuestion4(preguntas.getQuestion4());
            resPreguntas.setQuestion5(preguntas.getQuestion5());
        } else {
            logger.info(NOT_FOUND_USER);
            return ResponseServices.NOTFOUNDUSUARIO.toString();
        }

        return "{\"usuario\":\"" + resPreguntas.getUser() + "\","
                + "\"pregunta1\":\"" + resPreguntas.getQuestion1() + "\","
                + "\"pregunta2\":\"" + resPreguntas.getQuestion2() + "\","
                + "\"pregunta3\":\"" + resPreguntas.getQuestion3() + "\","
                + "\"pregunta4\":\"" + resPreguntas.getQuestion4() + "\","
                + "\"pregunta5\":\"" + resPreguntas.getQuestion5() + "\"}";
    }

    /**
     * Servicio para consultar las preguntas almacenadas cuando se inicia el
     * proceso para recordar la contraseña, ésta búsqueda se realiza por el
     * correo electrónico del usuario
     *
     * @param email correo electrónico del usuario
     * @return JSON contiene las preguntas almacenadas para el usuario
     */
    @GetMapping(value = "/questions/{email}", produces = "application/json")
    public @ResponseBody
    String getQuestionsByEmail(@PathVariable(value = EMAIL) String email) {
        Login preguntas;
        Login resPreguntas = new Login();

        try {
            preguntas = repository.findByEmail(email);
        } catch (Exception ex) {
            return ResponseServices.SEVERALUSERS.toString();
        }
        preguntas = repository.findByEmail(email);

        if (preguntas != null) {
            resPreguntas.setUser(preguntas.getUser());
            resPreguntas.setQuestion1(preguntas.getQuestion1());
            resPreguntas.setQuestion2(preguntas.getQuestion2());
            resPreguntas.setQuestion3(preguntas.getQuestion3());
            resPreguntas.setQuestion4(preguntas.getQuestion4());
            resPreguntas.setQuestion5(preguntas.getQuestion5());
        } else {
            logger.info(NOT_FOUND_USER);
            return ResponseServices.NOTFOUNDUSUARIO.toString();
        }
        return "{\"usuario\":\"" + resPreguntas.getUser() + "\","
                + "\"pregunta1\":\"" + resPreguntas.getQuestion1() + "\","
                + "\"pregunta2\":\"" + resPreguntas.getQuestion2() + "\","
                + "\"pregunta3\":\"" + resPreguntas.getQuestion3() + "\","
                + "\"pregunta4\":\"" + resPreguntas.getQuestion4() + "\","
                + "\"pregunta5\":\"" + resPreguntas.getQuestion5() + "\"}";
    }

    /**
     * Servicio para realizar la validación de las respuestas del usuario, para
     * poder realizar el reestablecimiento de la contraseña con éxito
     *
     * @param JsonUserPreguntas JSON contiene el usuario, las preguntas, las
     *                          respuestas y la url del login(interno o externo) por el cual se inicio el
     *                          proceso
     * @return JSON contiene mensaje satisfactorio o de error del proceso
     */
    @PostMapping(value = "/questions/check", consumes = "application/json", produces = "application/json")
    public @ResponseBody
    String checkQuestions(@RequestBody String JsonUserPreguntas) throws NoSuchAlgorithmException {
        Login pregyresp;
        try {
            JSONObject obj = new JSONObject(JsonUserPreguntas);
            pregyresp = repository.findByUser((String) obj.get(USER));
            if (pregyresp != null) {
                if ((obj.get("respuesta1").equals(pregyresp.getReply1()))
                        && (obj.get("respuesta2").equals(pregyresp.getReply2()))
                        && (obj.get("respuesta3").equals(pregyresp.getReply3()))
                        && (obj.get("respuesta4").equals(pregyresp.getReply4()))
                        && (obj.get("respuesta5").equals(pregyresp.getReply5()))) {
                    String password = PasswordGenerator.getPassword(8);

                    String text = obj.get(USER) + password;

                    MessageDigest md = MessageDigest.getInstance(SHA_256);
                    md.update(text.getBytes(StandardCharsets.UTF_8));

                    byte byteData[] = md.digest();

                    StringBuilder sb = new StringBuilder();
                    for (byte byteDatum : byteData) {
                        sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
                    }

                    logger.info("Hex format : {}", sb.toString());

                    String passCifrada = sb.toString();

                    MailSender mail = new MailSender();

                    Properties props = new Properties();
                    props.put(MAIL_HOST, properties.getHostMail());
                    props.put(MAIL_PORT, properties.getPortMail());

                    Session session = Session.getInstance(props);

                    mail.SendPasswordMailRecordar(pregyresp.getEmail(), pregyresp.getUser(), password,
                            properties.getUrl(), properties.getEncabezado(), properties.getBoton(), session, properties.getName(), properties.getFrom());

                    pregyresp.setPassword3(pregyresp.getPassword2());
                    pregyresp.setPassword2(pregyresp.getPassword());
                    pregyresp.setPassword(passCifrada);
                    Date now = new Date();
                    pregyresp.setDateCreatePass(new Timestamp(now.getTime()));
                    pregyresp.setFlag("2");
                    repository.flush();

                    return ResponseServices.RESETPASSOK.toString();
                } else {
                    logger.error(WRONG_QUESTIONS);
                    return ResponseServices.ANSWERSINCORRECT.toString();
                }

            } else {
                logger.info(NOT_FOUND_USER);
                return ResponseServices.NOTFOUNDUSUARIO.toString();
            }

        } catch (JSONException e) {
            logger.error(e.getMessage());
            return ResponseServices.PARSEJSONERROR.toString();
        }
    }

    /**
     * Servicio para realizar el cambio de contraseña, esta funcionalidad aplica
     * cuando el usuario ingresa por primera vez y cuando inicia el proceso de
     * reestablecimiento de contraseña
     *
     * @param jsonCambiarClave JSON contiene el usuario, la contraseña actual y
     *                         la nueva contraseña
     * @return JSON contiene mensaje satisfactorio o de error del proceso
     */

    @PostMapping(value = "/changePass", produces = "application/json")
    public @ResponseBody
    String changePassword(@RequestBody String jsonCambiarClave) {
        Login login;
        try {
            JSONObject obj = new JSONObject(jsonCambiarClave);
            login = repository.findByUser((String) obj.get(USER));
            if (login != null) {
                if (obj.get(PASSWORD).equals(login.getPassword())) {
                    if (obj.get(NEW_PASS).equals(login.getPassword()) || obj.get(NEW_PASS).equals(login.getPassword2()) || obj.get(NEW_PASS).equals(login.getPassword3())) {

                        return ResponseServices.NEWPASSINCORRECT.toString();
                    } else {
                        Date now = new Date();
                        login.setPassword3(login.getPassword2());
                        login.setPassword2(login.getPassword());
                        login.setPassword((String) obj.get(NEW_PASS));
                        login.setDateCreatePass(new Timestamp(now.getTime()));
                        login.setFlag("0");
                        repository.flush();
                        return ResponseServices.CHANGEPASSOK.toString();
                    }
                } else {
                    return ResponseServices.PASSINCORRECT.toString();
                }

            } else {
                logger.info(NOT_FOUND_USER);
                return ResponseServices.NOTFOUNDUSUARIO.toString();
            }

        } catch (JSONException e) {
            logger.info(e.getMessage());
            return ResponseServices.PARSEJSONERROR.toString();
        }
    }

}

package com.jfavilau.demologin.mail;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author jhon.avila
 * <p>
 * Metodo para enviar correo al usuario, para notificarle las nuevas credenciales
 * de acceso para ingreso al aplicativo luego del proceso de reeestablecimiento
 * de la contraseña
 */
public class MailSender {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);

    public void SendPasswordMailRecordar(String to, String id, String password, String url, String imagenEncabezado, String imagenBoton, Session session, String name, String username) {

        String urlEncabezado = "cid:encabezado";
        String urlImages = "cid:boton";

        Map<String, String> velocityContext = new HashMap<>();
        velocityContext.put("id", id);
        velocityContext.put("password", password);
        velocityContext.put("urlImages", urlImages);
        velocityContext.put("urlEncabezado", urlEncabezado);
        velocityContext.put("urlApp", url);

        VelocityEngine ve = new VelocityEngine();
        ve.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        ve.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        ve.init();

        Template t = ve.getTemplate("templates/plantilla_restablecimiento.vm");

        VelocityContext context = new VelocityContext(velocityContext);

        StringWriter writer = new StringWriter();
        t.merge(context, writer);

        String htmlMail = writer.toString();

        logger.info(htmlMail);

        Message message = new MimeMessage(session);
        MimeMultipart multipart = new MimeMultipart();

        try {


            BodyPart encabezado = new MimeBodyPart();
            DataSource fds = new FileDataSource(imagenEncabezado);
            encabezado.setDataHandler(new DataHandler(fds));
            encabezado.setHeader("Content-ID", "<encabezado>");
            multipart.addBodyPart(encabezado);

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlMail, "text/html; charset=utf-8");

            multipart.addBodyPart(htmlPart);
//            
            BodyPart boton = new MimeBodyPart();
            DataSource fdsbot = new FileDataSource(imagenBoton);
            boton.setDataHandler(new DataHandler(fdsbot));
            boton.setHeader("Content-ID", "<boton>");

            multipart.addBodyPart(boton);

            message.setFrom(new InternetAddress(username, name));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Restablecimiento de contraseña");
            message.setContent(multipart);

            Transport.send(message);

        } catch (UnsupportedEncodingException | MessagingException ex) {
            logger.error(ex.getMessage());
        }
    }

}

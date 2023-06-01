package com.example.proyectogticsgrupo2.service;
import jakarta.servlet.http.HttpServletRequest;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
public class CorreoService {
    public void props(String correo, String pass) {
        Properties props = new Properties();
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("clinicainterpucp@gmail.com","iciqgxwezyenisom");
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo, true));
            message.setSubject("Correo de Confirmación");

            // Crear una parte para el contenido HTML
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(getHTMLContent(correo, pass), "text/html");

            // Crear el multipart para combinar el contenido HTML y el texto plano
            MimeMultipart multipart = new MimeMultipart("alternative");
            multipart.addBodyPart(htmlPart);

            // Agregar el multipart al mensaje
            message.setContent(multipart);

            System.out.println("snding...");
            Transport.send(message);
            System.out.println("Mensjaje enviado...");

        }catch (MessagingException me){
            System.out.println("Exp: "+me);
        }
    }
    // Método para cargar el contenido HTML desde el archivo invitacion.html
    private String getHTMLContent(String user, String pwd) {
        String htmlContent = ""; // Contenido HTML del archivo

        // Código para cargar el contenido HTML desde el archivo invitacion.html
        try {
            Path path = Paths.get("src/main/resources/templates/administrador/invitar.html");
            htmlContent = new String(Files.readAllBytes(path));

            htmlContent = htmlContent.replace("%user%",user);
            htmlContent = htmlContent.replace("%pwd%", pwd);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return htmlContent;
    }
}

package com.example.proyectogticsgrupo2.service;

import com.example.proyectogticsgrupo2.entity.CitaTemporal;
import com.example.proyectogticsgrupo2.entity.Paciente;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class CorreoCitaRegistrada {
    public void props(String correo, CitaTemporal cita){
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
            message.setSubject("Registro de cita");

            // Crear una parte para el contenido HTML
            MimeBodyPart htmlPart = new MimeBodyPart();
            //htmlPart.setContent(getHTMLContent(correo, paciente, link), "text/html");

            // Crear el multipart para combinar el contenido HTML y el texto plano
            MimeMultipart multipart = new MimeMultipart("alternative");
            multipart.addBodyPart(htmlPart);

            // Agregar el multipart al mensaje
            message.setContent(multipart);

            System.out.println("snding...");
            Transport.send(message);
            System.out.println("Mensjaje enviado...");

        }
        catch (MessagingException me){
            System.out.println("Exp: "+me);
        }
    }

    // Método para cargar el contenido HTML desde el archivo invitacion.html
    private String getHTMLContent(String user, Paciente paciente, String link) {

        String htmlContent = "";
        return htmlContent;

    }
}

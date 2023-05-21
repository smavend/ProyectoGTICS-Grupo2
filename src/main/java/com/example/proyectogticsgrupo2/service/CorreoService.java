package com.example.proyectogticsgrupo2.service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
public class CorreoService {
    public void props(String correo) {
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
            message.setText("Estimado(a) usuario,\n" +
                    "¡Bienvenido(a) a nuestra plataforma clínica!");
            System.out.println("snding...");
            Transport.send(message);
            System.out.println("Mensjaje enviado...");

        }catch (MessagingException me){
            System.out.println("Exp: "+me);
        }
    }
}

package com.example.proyectogticsgrupo2.service;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
public class CorreoServiceSuperAdmin {
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

            message.setText("Estimado(a) usuario,\n\n" +
                    "¡Le damos una cálida bienvenida a nuestra destacada plataforma clínica! Hemos creado especialmente para usted una cuenta que le brindará un acceso privilegiado como  \n" +
                    "Administrador o Administrativo, para que pueda llevar a cabo sus tareas de forma ágil y eficiente en línea. Estamos encantados de contar con su presencia y esperamos \n" +
                    "que disfrute de una experiencia excepcional mientras navega por nuestra plataforma.\n" +
                    "\n" +
                    "Su nombre de usuario es: "+correo+"\n" +
                    "\n" +
                    "Su contraseña temporal es: "+pass+"\n" +
                    "\n" +
                    "Por motivos de seguridad, le pedimos que cambie su contraseña la primera vez que inicie sesión.\n" +
                    "\n" +
                    "Gracias por confiar en nosotros.");

            System.out.println("snding...");
            Transport.send(message);
            System.out.println("Mensjaje enviado...");

        }catch (MessagingException me){
            System.out.println("Exp: "+me);
        }
    }
    // Método para cargar el contenido HTML desde el archivo invitacion.html
    private String getHTMLContent() {
        String htmlContent = ""; // Contenido HTML del archivo

        // Código para cargar el contenido HTML desde el archivo invitacion.html
        try {
            Path path = Paths.get("src/main/resources/templates/administrador/invitar.html");
            htmlContent = new String(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return htmlContent;
    }
}

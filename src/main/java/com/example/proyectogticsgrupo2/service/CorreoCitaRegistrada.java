package com.example.proyectogticsgrupo2.service;

import com.example.proyectogticsgrupo2.entity.AdministrativoPorEspecialidadPorSede;
import com.example.proyectogticsgrupo2.entity.Cita;
import com.example.proyectogticsgrupo2.repository.AdministrativoPorEspecialidadPorSedeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class CorreoCitaRegistrada {

    final AdministrativoPorEspecialidadPorSedeRepository xRepository;

    public CorreoCitaRegistrada(AdministrativoPorEspecialidadPorSedeRepository xRepository){
        this.xRepository = xRepository;
    }

    public void props(String host, String correo, Cita cita) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("clinicainterpucp@gmail.com", "iciqgxwezyenisom");
            }
        });
        try {
            MimeMessage message = new MimeMessage(session);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(correo, true));
            message.setSubject("Registro de cita");

            // Crear una parte para el contenido HTML
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(getHTMLContent(host, cita), "text/html");

            // Crear el multipart para combinar el contenido HTML y el texto plano
            MimeMultipart multipart = new MimeMultipart("alternative");
            multipart.addBodyPart(htmlPart);

            // Agregar el multipart al mensaje
            message.setContent(multipart);

            System.out.println("enviando...");
            Transport.send(message);
            System.out.println("Mensaje enviado...");

        } catch (MessagingException me) {
            System.out.println("Error: " + me);
        }
    }

    // Método para cargar el contenido HTML desde el archivo invitacion.html
    private String getHTMLContent(String host, Cita cita) {

        String htmlContent = """
                <html>
                <body align='center'>
                <table border='0' cellpadding='0' cellspacing='0' class='m_6554632393618514601deviceWidth' style='width:100%;max-width:600px' width='600'>
                        <tbody>
                            <tr>
                                <td align='center' bgcolor='#f4f4f5'></td>
                                <td height='32' style='height:32px;min-height:32px;line-height:32px;font-size:1px'>&nbsp;</td>
                            </tr>
                            <tr>
                                <td align='center' style='background-color: #f4f4f5;'>
                                    <a href='http://%host%' target='_blank' data-saferedirecturl='https://www.google.com/url?q=http://%host%'>
                                    <img height='auto' src='https://img.freepik.com/darmowe-wektory/projekt-logo-szpitala-wektor-medyczny-krzyz_53876-136743.jpg' style='display:block;border:0px;text-decoration:none;border-style:none;color:#f4f4f5;border-width:0' width='70' class='CToWUd' data-bit='iit'>
                                    </a>
                                    <h1 style='margin:0;font-family:Arial,Helvetica,sans-serif;font-size:20px;line-height:normal;font-weight:700;letter-spacing:0;color:#888888'>Clínica InterPUCP</h1>
                                </td>
                            </tr>
                            <tr>
                                <td height='32' style='height:32px;min-height:32px;line-height:32px;font-size:1px;background-color: #f4f4f5;'>&nbsp;</td>
                            </tr>
                        </tbody>
                    </table>
                    <table border='0' cellpadding='0' cellspacing='0' class='m_6554632393618514601deviceWidth' style='max-width:600px' width='100%'>
                        <tbody>
                            <tr>
                                <td align='center' bgcolor='#f4f4f5'>
                                <table border='0' cellpadding='0' cellspacing='0' width='100%'>
                                    <tbody>
                                        <tr>
                                            <td align='center'>
                                            <h1 style='margin:0;font-family:Arial,Helvetica,sans-serif;font-size:22px;line-height:normal;font-weight:700;letter-spacing:0;color:#4c4c4c'>Estimado(a), %nombre% </h1>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td valign='top'>
                                                <table border='0' cellpadding='0' cellspacing='0' width='100%'>
                                                    <tbody>
                                                        <tr>
                                                            <td style='width:24px;min-width:24px;max-width:24px' width='24'>&nbsp;</td>
                                                            <td align='center'>
                                                                <table align='left' border='0' cellpadding='0' cellspacing='0' width='100%'>
                                                                    <tbody>
                                                                        <tr>
                                                                            <td class='m_6554632393618514601mobilespace24' height='24' style='height:24px;line-height:24px'>&nbsp;</td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td align='center'>
                                                                                <p style='margin:0;font-family:Arial,Helvetica,sans-serif;font-size:16px;line-height:26px;font-weight:400;letter-spacing:0;color:#777777;max-width:520px'>
                                                                                El registro de su cita ha sido procesado de manera satisfactoria.<br>
                                                                                Usted ha registrado la siguiente cita:
                                                                                <br>
                                                                            </td>
                                                                        </tr>
                                                                        <tr>
                                                                            <td class='m_6554632393618514601mobilespace24' height='24' style='height:5px;'>&nbsp;</td>
                                                                        </tr>""";

        if (cita.getModalidad() == 0) {
            htmlContent = htmlContent + "<tr>\n" +
                    "                     <td style='font-size: 16px; text-align: center; font-family:Arial; color:#777777'>\n" +
                    "                       <p>Modalidad: <span style='color: #4c4c4c; font-weight: 600;'>PRESENCIAL</span></p>\n" +
                    "                       <p>Especialidad: <span style='color: #4c4c4c; font-weight: 600;'>%especialidad%</span></p>\n" +
                    "                       <p>Doctor: <span style='color: #4c4c4c; font-weight: 600;'>%doctor%</span></p>\n" +
                    "                       <p>Sede: <span style='color: #4c4c4c; font-weight: 600;'>%sede%</span></p>\n" +
                    "                       <p>Dirección: <span style='color: #4c4c4c; font-weight: 600;'>%direccion%</span></p>\n" +
                    "                       <p>Torre: <span style='color: #4c4c4c; font-weight: 600;'>%torre%</span></p>\n" +
                    "                       <p>Piso: <span style='color: #4c4c4c; font-weight: 600;'>%piso%</span></p>\n" +
                    "                       <p>Fecha: <span style='color: #4c4c4c; font-weight: 600;'>%fecha%</span></p>\n" +
                    "                       <p>Horario: <span style='color: #4c4c4c; font-weight: 600;'>%horario%</span></p>\n" +
                    "                     </td>\n" +
                    "                    </tr>";
        } else {
            htmlContent = htmlContent + "<tr>\n" +
                    "                     <td style='font-size: 16px; text-align: center; font-family:Arial; color:#777777'>\n" +
                    "                      <p>Modalidad: <span style='color: #4c4c4c; font-weight: 600;'>VIRTUAL</span></p>\n" +
                    "                      <p>Especialidad: <span style='color: #4c4c4c; font-weight: 600;'>%especialidad%</span></p>\n" +
                    "                      <p>Doctor: <span style='color: #4c4c4c; font-weight: 600;'>%doctor%</span></p>\n" +
                    "                      <p>Fecha: <span style='color: #4c4c4c; font-weight: 600;'>%fecha%</span></p>\n" +
                    "                      <p>Horario: <span style='color: #4c4c4c; font-weight: 600;'>%horario%</span></p>\n" +
                    "                     </td>\n" +
                    "                    </tr>";
        }

        htmlContent = htmlContent + "<tr>\n" +
                "                     <td align='center'>\n" +
                "                      <table border='0' cellpadding='0' cellspacing='0' style='width:220px;border-spacing:0;border-collapse:collapse' width='220'>\n" +
                "                       <tbody>\n" +
                "                        <tr>\n" +
                "                         <p style='text-align: center; margin-bottom:15px; font-family:Arial,Helvetica,sans-serif; font-size:16px; line-height:26px; font-weight:400; letter-spacing:0; color:#777777; max-width:520px'>\n" +
                "                          Puede revisar su cita en el siguiente enlace:\n" +
                "                         </p>\n" +
                "                        </tr>\n" +
                "                        <tr>\n" +
                "                         <td align='center' height='43' style='border-collapse:collapse;background-color:#1cb0f6;border-radius:9px;white-space:nowrap'>\n" +
                "                          <a href='http://%host%/Paciente/citas' target='_blank' data-saferedirecturl='https://www.google.com/url?q=http://%host%/Paciente/citas' style='display:inline-block;width:100%;font-family:Arial,Helvetica,sans-serif;font-size:14px;font-weight:bold;line-height:19px;text-transform:uppercase;color:#ffffff;text-align:center;text-decoration:none;background-color:#1cb0f6;border-radius:14px;border-top:12px solid #1cb0f6;border-bottom:12px solid #1cb0f6' data-saferedirecturl='https://www.google.com/url?q=https://www.duolingo.com/?email_type%3Dresurrection%26target%3Dget_back_on_track%26utm_source%3Dcomeback%26utm_medium%3Demail%26utm_campaign%3Dresurrection&amp;source=gmail&amp;ust=1685717009586000&amp;usg=AOvVaw1w3dFvXzWW6tAsd8O6tcNi'>\n" +
                "                           Revisar citas\n" +
                "                          </a>\n" +
                "                         </td>\n" +
                "                        </tr>\n" +
                "                       </tbody>\n" +
                "                      </table>\n" +
                "                     </td>\n" +
                "                    </tr>";

        if (cita.getModalidad() == 0) {
            htmlContent = htmlContent + "<tr>\n" +
                    "                     <td>\n" +
                    "                      <p style=\"font-family: Arial, Helvetica, sans-serif; text-align: center; color: #777777; line-height: 26px; font-size: 16px;\">\n" +
                    "                       Recuerde que el pago se realiza en caja el día de la cita\n" +
                    "                      </p>\n" +
                    "                     </td>\n" +
                    "                    </tr>";
        } else {
            htmlContent = htmlContent + "<tr>\n" +
                    "                     <td>\n" +
                    "                      <p style=\"font-family: Arial, Helvetica, sans-serif; text-align: center; color: #777777; line-height: 26px; font-size: 16px;\">\n" +
                    "                       Recuerde que puede realizar el pago de manera virtual y como máximo 1 HORA antes de la cita.\n" +
                    "                      </p>\n" +
                    "                     </td>\n" +
                    "                    </tr>";
        }

        htmlContent = htmlContent + "<tr>\n" +
        "                                                            <td align='center' valign='top' width='520'>\n" +
        "                                                                <p style='margin:0;font-family:Arial,Helvetica,sans-serif;font-size:16px;line-height:26px;font-weight:400;letter-spacing:0;color:#777777;max-width:520px'>\n" +
        "                                                                    Gracias por confiar en Clínica InterPUCP.\n" +
        "                                                                </p>\n" +
        "                                                            </td>\n" +
        "                                                        </tr>\n" +
        "                                                        <tr>\n" +
        "                                                            <td class='m_6554632393618514601mobilespace24' height='20' style='height:24px;line-height:24px'>&nbsp;</td>\n" +
        "                                                        </tr>\n" +
        "                                                    </tbody>\n" +
        "                                                </table>\n" +
        "                                            </td>\n" +
        "                                            <td style='width:24px;min-width:24px;max-width:24px' width='24'>&nbsp;</td>\n" +
        "                                        </tr>\n" +
        "                                    </tbody>\n" +
        "                                </table>\n" +
        "                            </td>\n" +
        "                        </tr>\n" +
        "                        <tr>\n" +
        "                            <td height='60' style='height:60px;min-height:60px;line-height:60px;font-size:1px'>&nbsp;</td>\n" +
        "                        </tr>\n" +
        "                    </tbody>\n" +
        "                </table>\n" +
        "                </td>\n" +
        "            </tr>\n" +
        "        </tbody>\n" +
        "    </table>\n" +
        "    <table border='0' cellpadding='0' cellspacing='0' class='m_6554632393618514601deviceWidth' style='max-width:600px' width='100%'>\n" +
        "        <tbody>\n" +
        "            <tr>\n" +
        "                <td align='center' bgcolor='#ffffff'>\n" +
        "                    <table border='0' cellpadding='0' cellspacing='0' width='100%'>\n" +
        "                        <tbody>\n" +
        "                            <tr>\n" +
        "                                <td height='40' style='height:40px;line-height:40px'>&nbsp;</td>\n" +
        "                            </tr>\n" +
        "                            <tr>\n" +
        "                                <td height='50' style='height:50px;min-height:50px;line-height:50px;font-size:1px;border-bottom:2px solid #f2f2f2'>&nbsp;</td>\n" +
        "                            </tr>\n" +
        "                        </tbody>\n" +
        "                    </table>\n" +
        "                </td>\n" +
        "            </tr>\n" +
        "        </tbody>\n" +
        "    </table>\n" +
        "    <table border='0' cellpadding='0' cellspacing='0' class='m_6554632393618514601deviceWidth' style='max-width:600px' width='100%'>\n" +
        "        <tbody>\n" +
        "            <tr>\n" +
        "                <td align='center' style='padding-top:23px'>\n" +
        "                    <table border='0' cellpadding='0' cellspacing='0' width='100%'>\n" +
        "                        <tbody>\n" +
        "                            <tr>\n" +
        "                                <td style='width:32px;min-width:32px;max-width:32px' width='32'>&nbsp;</td>\n" +
        "                                <td valign='top'>\n" +
        "                                    <table align='right' border='0' cellpadding='0' cellspacing='0' class='m_6554632393618514601responsive-table' style='min-width:100%;width:20%;max-width:100%;min-width:-webkit-calc(20%);min-width:calc(20%);width:-webkit-calc(287296px - 53600%);width:calc(287296px - 53600%)' width='105'>\n" +
        "                                        <tbody>\n" +
        "                                            <tr>\n" +
        "                                                <td align='left' style='padding-top:4px;padding-bottom:16px'>\n" +
        "                                                    <table border='0' cellpadding='0' cellspacing='0' width='105'>\n" +
        "                                                        <tbody>\n" +
        "                                                            <tr>\n" +
        "                                                                <td>\n" +
        "                                                                    <a style='color:#a7a7a7' target='_blank' data-saferedirecturl='https://www.google.com/url?q=https://www.instagram.com/duolingo/&amp;source=gmail&amp;ust=1685717009586000&amp;usg=AOvVaw0p6CAjAQEiQ59KLpZhUikY'><img alt='Instagram' height='auto' src='https://ci4.googleusercontent.com/proxy/T-8B-MBCuHBMxX5OC9uTJRmiKzxqOrtl-SObLmkeRyfV9ClHgzl9O7tmFi_VbsWRgOv6cDAx-nv2reyB36awNGzPPPSb=s0-d-e1-ft#https://dzvpwvcpo1876.cloudfront.net/Instagram.png' style='display:block;border:0' width='25' class='CToWUd' data-bit='iit'> </a>\n" +
        "                                                                </td>\n" +
        "                                                                <td style='width:15px;min-width:15px;font-size:1px' width='15'>&nbsp;&nbsp;</td>\n" +
        "                                                                <td>\n" +
        "                                                                    <a style='color:#a7a7a7' target='_blank' data-saferedirecturl='https://www.google.com/url?q=https://twitter.com/duolingo&amp;source=gmail&amp;ust=1685717009587000&amp;usg=AOvVaw1ukTijdjcyMb5xfdsH5pQT'><img alt='Twitter' height='auto' src='https://ci3.googleusercontent.com/proxy/zLfLVLwgiSl1Nkdi0pw2CkJcqelL7TPanZZRxjVF9JUGzf1rIZWVXY4rKRvb8S1KbK1TePkz3wgTIhQ8HKjI0fLVsg=s0-d-e1-ft#https://dzvpwvcpo1876.cloudfront.net/Twitter.png' style='display:block;border:0' width='25' class='CToWUd' data-bit='iit'> </a>\n" +
        "                                                                </td>\n" +
        "                                                                <td style='width:15px;min-width:15px;font-size:1px' width='15'>&nbsp;&nbsp;</td>\n" +
        "                                                                <td>\n" +
        "                                                                    <a style='color:#a7a7a7' target='_blank' data-saferedirecturl='https://www.google.com/url?q=https://www.facebook.com/duolingo&amp;source=gmail&amp;ust=1685717009587000&amp;usg=AOvVaw2HZ78OCSs0_wAgu4s_DJ9i'><img alt='Facebook' height='auto' src='https://ci6.googleusercontent.com/proxy/5a-rV_CqBJvE_fwm46mBxFjm79p6XXC4GwWgQAPKfuv89j4aAEUvIXTUSQRP1Z57uP38GIatDXI8EbKxARcndfsl7lw=s0-d-e1-ft#https://dzvpwvcpo1876.cloudfront.net/Facebook.png' style='display:block;border:0' width='25' class='CToWUd' data-bit='iit'> </a>\n" +
        "                                                                </td>\n" +
        "                                                            </tr>\n" +
        "                                                        </tbody>\n" +
        "                                                    </table>\n" +
        "                                                </td>\n" +
        "                                            </tr>\n" +
        "                                        </tbody>\n" +
        "                                    </table>\n" +
        "                                    <table align='left' border='0' cellpadding='0' cellspacing='0' class='m_6554632393618514601responsive-table' style='min-width:100%;width:54%;max-width:100%;min-width:-webkit-calc(54%);min-width:calc(54%);width:-webkit-calc(287296px - 53600%);width:calc(287296px - 53600%)' width='290'>\n" +
        "                                        <tbody>\n" +
        "                                            <tr>\n" +
        "                                                <td align='center'>\n" +
        "                                                    <table align='left' border='0' cellpadding='0' cellspacing='0' width='100%'>\n" +
        "                                                        <tbody>\n" +
        "                                                            <tr>\n" +
        "                                                                <td align='left'>\n" +
        "                                                                    <p style='margin:0;font-family:Arial,Helvetica,sans-serif;font-size:13px;line-height:15px;font-weight:400;color:#a7a7a7'>Clínica InterPUCP</p>\n" +
        "                                                                    </p>\n" +
        "                                                                </td>\n" +
        "                                                            </tr>\n" +
        "                                                        </tbody>\n" +
        "                                                    </table>\n" +
        "                                                </td>\n" +
        "                                            </tr>\n" +
        "                                        </tbody>\n" +
        "                                    </table>\n" +
        "                                </td>\n" +
        "                                <td style='width:32px;min-width:32px;max-width:32px' width='32'>&nbsp;</td>\n" +
        "                            </tr>\n" +
        "                        </tbody>\n" +
        "                    </table>\n" +
        "                </td>\n" +
        "            </tr>\n" +
        "            <tr>\n" +
        "                <td height='60' style='height:60px;min-height:60px;line-height:60px;font-size:1px'>&nbsp;</td>\n" +
        "            </tr>\n" +
        "        </tbody>\n" +
        "    </table>" +
        "  </body>" +
        "</html>";

        htmlContent = htmlContent.replace("%host%", host);
        htmlContent = htmlContent.replace("%nombre%", cita.getPaciente().getNombreYApellido());
        htmlContent = htmlContent.replace("%especialidad%", cita.getEspecialidad().getNombre());
        htmlContent = htmlContent.replace("%doctor%", cita.getDoctor().getNombreYApellido());

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fecha = df.format(cita.getInicio().toLocalDate());
        df = DateTimeFormatter.ofPattern("HH:mm");

        htmlContent = htmlContent.replace("%fecha%", fecha);
        htmlContent = htmlContent.replace("%horario%", df.format(cita.getInicio().toLocalTime())+" - "+df.format(cita.getFin().toLocalTime()));
        if (cita.getModalidad() == 0){

            htmlContent = htmlContent.replace("%sede%", cita.getSede().getNombre());
            htmlContent = htmlContent.replace("%direccion%", cita.getSede().getDireccion());
            htmlContent = htmlContent.replace("%torre%", xRepository.findBySedeIdIdSedeAndEspecialidadIdIdEspecialidad(cita.getSede().getIdSede(), cita.getEspecialidad().getIdEspecialidad()).getTorre());
            htmlContent = htmlContent.replace("%piso%", xRepository.findBySedeIdIdSedeAndEspecialidadIdIdEspecialidad(cita.getSede().getIdSede(), cita.getEspecialidad().getIdEspecialidad()).getPiso());
        }

        return htmlContent;
    }
}

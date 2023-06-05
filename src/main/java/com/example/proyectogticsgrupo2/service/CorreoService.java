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
    public void props(String correo, String pass, String link) {
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
            htmlPart.setContent(getHTMLContent(correo, pass, link), "text/html");

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
    private String getHTMLContent(String user, String pwd, String link) {
        String htmlContent = ""; // Contenido HTML del archivo

        // Código para cargar el contenido HTML desde el archivo invitacion.html
        //Path path = Paths.get("src/main/resources/templates/administrador/invitar.html");
        //htmlContent = new String(Files.readAllBytes(path));
        htmlContent = "<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">\n" +
                "  <body align=\"center\">\n" +
                "    <td align=\"center\" bgcolor=\"#f4f4f5\">\n" +
                "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"m_6554632393618514601deviceWidth\" style=\"width:100%;max-width:600px\" width=\"600\">\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <td height=\"32\" style=\"height:32px;min-height:32px;line-height:32px;font-size:1px\">&nbsp;</td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td align=\"center\" style=\"background-color: #f4f4f5;\">\n" +
                "              <a href=\"http://%link%\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=http://%link%\">\n" +
                "                <img height=\"auto\" src=\"https://img.freepik.com/darmowe-wektory/projekt-logo-szpitala-wektor-medyczny-krzyz_53876-136743.jpg\" style=\"display:block;border:0px;text-decoration:none;border-style:none;color:#f4f4f5;border-width:0\" width=\"70\" class=\"CToWUd\" data-bit=\"iit\">\n" +
                "              </a>\n" +
                "              <h1 style=\"Margin:0;margin:0;font-family:Arial,Helvetica,sans-serif;font-size:20px;line-height:normal;font-weight:700;letter-spacing:0;color:#888888\">Clínica InterPUCP</h1>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td height=\"32\" style=\"height:32px;min-height:32px;line-height:32px;font-size:1px;background-color: #f4f4f5;\">&nbsp;</td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "\n" +
                "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"m_6554632393618514601deviceWidth\" style=\"max-width:600px\" width=\"100%\">\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <td align=\"center\" bgcolor=\"#f4f4f5\">\n" +
                "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "                <tbody>\n" +
                "                  <tr>\n" +
                "                    <td align=\"center\">\n" +
                "                      <h1 class=\"m_6554632393618514601f40\" style=\"Margin:0;margin:0;font-family:Arial,Helvetica,sans-serif;font-size:34px;line-height:normal;font-weight:700;letter-spacing:0;color:#4c4c4c\">¡Bienvenido a nuestra plataforma clínica!</h1>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td class=\"m_6554632393618514601mobilespace24\" height=\"24\" style=\"height:24px;line-height:24px\">&nbsp;</td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td align=\"center\" class=\"m_6554632393618514601pl-pr-32\">\n" +
                "                      <a target=\"_blank\">\n" +
                "                        <img height=\"auto\" src=\"https://prd-auna-digitalassets-images.s3.amazonaws.com/img_clinica_delgado_2x_ee44ad567b.jpg\" style=\"display:block;border:0px\" width=\"auto\" class=\"CToWUd\" data-bit=\"iit\">\n" +
                "                      </a>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td valign=\"top\">\n" +
                "                      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td style=\"width:24px;min-width:24px;max-width:24px\" width=\"24\">&nbsp;</td>\n" +
                "                            <td align=\"center\">\n" +
                "                              <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "                                <tbody>\n" +
                "                                  <tr>\n" +
                "                                    <td class=\"m_6554632393618514601mobilespace24\" height=\"24\" style=\"height:24px;line-height:24px\">&nbsp;</td>\n" +
                "                                  </tr>\n" +
                "                                  <tr>\n" +
                "                                    <td align=\"center\">\n" +
                "                                      <p style=\"Margin:0;margin:0;font-family:Arial,Helvetica,sans-serif;font-size:16px;line-height:26px;font-weight:400;letter-spacing:0;color:#777777;max-width:520px\">\n" +
                "                                        Le hemos creado una cuenta para que pueda acceder a sus registros médicos y realizar consultas en línea.<br>\n" +
                "                                        <span style=\"font-weight:700\">Rol de usuario: MÉDICO.</span>\n" +
                "                                        <br><br>\n" +
                "                                        Puede ingresar a su cuenta con las siguientes credenciales:\n" +
                "                                      </p>\n" +
                "                                    </td>\n" +
                "                                  </tr>\n" +
                "                                  <tr>\n" +
                "                                    <td class=\"m_6554632393618514601mobilespace24\" height=\"20\" style=\"height:24px;line-height:24px\">&nbsp;</td>\n" +
                "                                  </tr>\n" +
                "                                  <tr>\n" +
                "                                    <td align=\"center\">\n" +
                "                                      <div>\n" +
                "\n" +
                "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:220px;border-spacing:0;border-collapse:collapse\" width=\"220\">\n" +
                "                                          <tbody>\n" +
                "                                            <tr>\n" +
                "                                              <td align=\"center\" height=\"43\" style=\"border-collapse:collapse;background-color:#771cf6;border-radius:9px;white-space:nowrap\">\n" +
                "                                                <a style=\"display:inline-block;width:100%;font-family:Arial,Helvetica,sans-serif;font-size:15px;font-weight:bold;line-height:19px;color:#ffffff;text-align:center;text-decoration:none;background-color:#771cf6;border-radius:14px;border-top:12px solid #771cf6;border-bottom:12px solid #771cf6\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://www.duolingo.com/?email_type%3Dresurrection%26target%3Dget_back_on_track%26utm_source%3Dcomeback%26utm_medium%3Demail%26utm_campaign%3Dresurrection&amp;source=gmail&amp;ust=1685717009586000&amp;usg=AOvVaw1w3dFvXzWW6tAsd8O6tcNi\">\n" +
                "                                                  &nbsp;&nbsp;USUARIO: %user% &nbsp;&nbsp;<br>\n" +
                "                                                  &nbsp;&nbsp;CONTRASEÑA: %pwd% &nbsp;&nbsp;\n" +
                "                                                </a>\n" +
                "                                              </td>\n" +
                "                                            </tr>\n" +
                "                                          </tbody>\n" +
                "                                        </table>\n" +
                "                                      </div>\n" +
                "                                    </td>\n" +
                "                                  </tr>\n" +
                "                                  <tr>\n" +
                "                                    <td class=\"m_6554632393618514601mobilespace24\" height=\"20\" style=\"height:24px;line-height:24px\">&nbsp;</td>\n" +
                "                                  </tr>\n" +
                "                                  <tr>\n" +
                "                                    <td align=\"center\" valign=\"top\" width=\"520\">\n" +
                "                                      <p style=\"Margin:0;margin:0;font-family:Arial,Helvetica,sans-serif;font-size:16px;line-height:26px;font-weight:400;letter-spacing:0;color:#777777;max-width:520px\">\n" +
                "                                        Por motivos de seguridad, le pedimos que cambie su contraseña la primera vez que inicie sesión.\n" +
                "                                        Gracias por confiar en Clínica InterPUCP.\n" +
                "                                      </p>\n" +
                "\n" +
                "                                    </td>\n" +
                "                                  </tr>\n" +
                "                                  <tr>\n" +
                "                                    <td class=\"m_6554632393618514601mobilespace24\" height=\"20\" style=\"height:24px;line-height:24px\">&nbsp;</td>\n" +
                "                                  </tr>\n" +
                "                                  <tr>\n" +
                "                                    <td align=\"center\">\n" +
                "                                      <div>\n" +
                "\n" +
                "                                        <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"width:220px;border-spacing:0;border-collapse:collapse\" width=\"220\">\n" +
                "                                          <tbody>\n" +
                "                                            <tr>\n" +
                "                                              <td align=\"center\" height=\"43\" style=\"border-collapse:collapse;background-color:#1cb0f6;border-radius:9px;white-space:nowrap\">\n" +
                "\n" +
                "                                                <a href=\"http://%link%/login\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=http://%link%/login\" style=\"display:inline-block;width:100%;font-family:Arial,Helvetica,sans-serif;font-size:15px;font-weight:bold;line-height:19px;text-transform:uppercase;color:#ffffff;text-align:center;text-decoration:none;background-color:#1cb0f6;border-radius:14px;border-top:12px solid #1cb0f6;border-bottom:12px solid #1cb0f6\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://www.duolingo.com/?email_type%3Dresurrection%26target%3Dget_back_on_track%26utm_source%3Dcomeback%26utm_medium%3Demail%26utm_campaign%3Dresurrection&amp;source=gmail&amp;ust=1685717009586000&amp;usg=AOvVaw1w3dFvXzWW6tAsd8O6tcNi\">\n" +
                "                                                  &nbsp;&nbsp;Iniciar sesión&nbsp;&nbsp;\n" +
                "                                                </a>\n" +
                "\n" +
                "                                              </td>\n" +
                "                                            </tr>\n" +
                "                                          </tbody>\n" +
                "                                        </table>\n" +
                "                                      </div>\n" +
                "                                    </td>\n" +
                "                                  </tr>\n" +
                "                                </tbody>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                            <td style=\"width:24px;min-width:24px;max-width:24px\" width=\"24\">&nbsp;</td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td height=\"60\" style=\"height:60px;min-height:60px;line-height:60px;font-size:1px\">&nbsp;</td>\n" +
                "                  </tr>\n" +
                "                </tbody>\n" +
                "              </table>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "\n" +
                "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"m_6554632393618514601deviceWidth\" style=\"max-width:600px\" width=\"100%\">\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <td align=\"center\" bgcolor=\"#ffffff\">\n" +
                "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "                <tbody>\n" +
                "                  <tr>\n" +
                "                    <td height=\"40\" style=\"height:40px;line-height:40px\">&nbsp;</td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td align=\"center\" style=\"padding-left:24px;padding-right:24px\">\n" +
                "                      <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td align=\"center\" class=\"m_6554632393618514601mobile-center\">\n" +
                "                              <p style=\"Margin:0;margin:0;font-family:Arial,Helvetica,sans-serif;font-size:14px;line-height:22px;font-weight:510;letter-spacing:0;color:#a7a7a7;max-width:510px\">\n" +
                "                                Al iniciar sesión en su cuenta, aceptará automáticamente los <a style=\"color:#1cb0f6;text-decoration:underline\" target=\"_blank\">\n" +
                "                                Términos y Condiciones</a> de la clínica a la que ha sido agregado. Para más información, contactese con el administrador de su sede.</p>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                  <tr>\n" +
                "                    <td height=\"50\" style=\"height:50px;min-height:50px;line-height:50px;font-size:1px;border-bottom:2px solid #f2f2f2\">&nbsp;</td>\n" +
                "                  </tr>\n" +
                "                </tbody>\n" +
                "              </table>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "      <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"m_6554632393618514601deviceWidth\" style=\"max-width:600px\" width=\"100%\">\n" +
                "        <tbody>\n" +
                "          <tr>\n" +
                "            <td align=\"center\" style=\"padding-top:23px\">\n" +
                "              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "                <tbody>\n" +
                "                  <tr>\n" +
                "                    <td style=\"width:32px;min-width:32px;max-width:32px\" width=\"32\">&nbsp;</td>\n" +
                "                    <td valign=\"top\">\n" +
                "                      <table align=\"right\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"m_6554632393618514601responsive-table\" style=\"min-width:100%;width:20%;max-width:100%;min-width:-webkit-calc(20%);min-width:calc(20%);width:-webkit-calc(287296px - 53600%);width:calc(287296px - 53600%)\" width=\"105\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td align=\"left\" style=\"padding-top:4px;padding-bottom:16px\">\n" +
                "                              <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"105\">\n" +
                "                                <tbody>\n" +
                "                                  <tr>\n" +
                "                                    <td>\n" +
                "                                      <a style=\"color:#a7a7a7\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://www.instagram.com/duolingo/&amp;source=gmail&amp;ust=1685717009586000&amp;usg=AOvVaw0p6CAjAQEiQ59KLpZhUikY\"><img alt=\"Instagram\" height=\"auto\" src=\"https://ci4.googleusercontent.com/proxy/T-8B-MBCuHBMxX5OC9uTJRmiKzxqOrtl-SObLmkeRyfV9ClHgzl9O7tmFi_VbsWRgOv6cDAx-nv2reyB36awNGzPPPSb=s0-d-e1-ft#https://dzvpwvcpo1876.cloudfront.net/Instagram.png\" style=\"display:block;border:0\" width=\"25\" class=\"CToWUd\" data-bit=\"iit\"> </a>\n" +
                "                                    </td>\n" +
                "                                    <td style=\"width:15px;min-width:15px;font-size:1px\" width=\"15\">&nbsp;&nbsp;</td>\n" +
                "                                    <td>\n" +
                "                                      <a style=\"color:#a7a7a7\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://twitter.com/duolingo&amp;source=gmail&amp;ust=1685717009587000&amp;usg=AOvVaw1ukTijdjcyMb5xfdsH5pQT\"><img alt=\"Twitter\" height=\"auto\" src=\"https://ci3.googleusercontent.com/proxy/zLfLVLwgiSl1Nkdi0pw2CkJcqelL7TPanZZRxjVF9JUGzf1rIZWVXY4rKRvb8S1KbK1TePkz3wgTIhQ8HKjI0fLVsg=s0-d-e1-ft#https://dzvpwvcpo1876.cloudfront.net/Twitter.png\" style=\"display:block;border:0\" width=\"25\" class=\"CToWUd\" data-bit=\"iit\"> </a>\n" +
                "                                    </td>\n" +
                "                                    <td style=\"width:15px;min-width:15px;font-size:1px\" width=\"15\">&nbsp;&nbsp;</td>\n" +
                "                                    <td>\n" +
                "                                      <a style=\"color:#a7a7a7\" target=\"_blank\" data-saferedirecturl=\"https://www.google.com/url?q=https://www.facebook.com/duolingo&amp;source=gmail&amp;ust=1685717009587000&amp;usg=AOvVaw2HZ78OCSs0_wAgu4s_DJ9i\"><img alt=\"Facebook\" height=\"auto\" src=\"https://ci6.googleusercontent.com/proxy/5a-rV_CqBJvE_fwm46mBxFjm79p6XXC4GwWgQAPKfuv89j4aAEUvIXTUSQRP1Z57uP38GIatDXI8EbKxARcndfsl7lw=s0-d-e1-ft#https://dzvpwvcpo1876.cloudfront.net/Facebook.png\" style=\"display:block;border:0\" width=\"25\" class=\"CToWUd\" data-bit=\"iit\"> </a>\n" +
                "                                    </td>\n" +
                "                                  </tr>\n" +
                "                                </tbody>\n" +
                "                              </table>\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "                      <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"m_6554632393618514601responsive-table\" style=\"min-width:100%;width:54%;max-width:100%;min-width:-webkit-calc(54%);min-width:calc(54%);width:-webkit-calc(287296px - 53600%);width:calc(287296px - 53600%)\" width=\"290\">\n" +
                "                        <tbody>\n" +
                "                          <tr>\n" +
                "                            <td align=\"center\">\n" +
                "\n" +
                "                              <table align=\"left\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "                                <tbody><tr>\n" +
                "                                  <td align=\"left\">\n" +
                "                                    <p style=\"Margin:0;margin:0;font-family:Arial,Helvetica,sans-serif;font-size:13px;line-height:15px;font-weight:400;color:#a7a7a7\">Clínica InterPUCP</p>\n" +
                "                                    </p>\n" +
                "                                  </td>\n" +
                "                                </tr>\n" +
                "                                </tbody></table>\n" +
                "\n" +
                "                            </td>\n" +
                "                          </tr>\n" +
                "                        </tbody>\n" +
                "                      </table>\n" +
                "                    </td>\n" +
                "                    <td style=\"width:32px;min-width:32px;max-width:32px\" width=\"32\">&nbsp;</td>\n" +
                "                  </tr>\n" +
                "                </tbody>\n" +
                "              </table>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "          <tr>\n" +
                "            <td height=\"60\" style=\"height:60px;min-height:60px;line-height:60px;font-size:1px\">&nbsp;</td>\n" +
                "          </tr>\n" +
                "        </tbody>\n" +
                "      </table>\n" +
                "    </td>\n" +
                "  </body>\n" +
                "</html>";

        htmlContent = htmlContent.replace("%user%",user);
        htmlContent = htmlContent.replace("%pwd%", pwd);
        htmlContent = htmlContent.replace("%link%", link);

        return htmlContent;
    }
}

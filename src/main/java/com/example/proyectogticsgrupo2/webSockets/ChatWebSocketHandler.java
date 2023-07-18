package com.example.proyectogticsgrupo2.webSockets;

import com.example.proyectogticsgrupo2.entity.Mensaje;
import com.example.proyectogticsgrupo2.repository.MensajeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ChatWebSocketHandler implements WebSocketHandler {



    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private HashMap<WebSocketSession, String> Session_IdSesion = new HashMap<>();
    private HashMap<String,String> user_IDsesion = new HashMap<>();

    /*Se ejecuta cuando se establece una nueva coneccion webSocket
      se agrega la sesion del cliente a la lista de sesiones activas*/
    int i=1;
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        System.out.println("sesion "+i+": "+session);
        i++;

    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        // Obtener el mensaje enviado por el cliente
        String messageText = message.getPayload().toString();

        if (messageText.startsWith("CLICK:")) {
            System.out.println("Contenido del mensaje enviado: " + messageText);

            /*se divide el mensaje en partes utilizando el carácter ":" como separador.
                El número 3 indica que se dividirá el mensaje en máximo 4 partes. */
            String[] parts = messageText.split(":", 4);

            /* parts[0] contendrá "CLICK", parts[1] contendrá el destinatario y parts[2] contendrá el remitente, parts[3] el contenido del mensaje.*/
            String destinatario = parts[1];
            String remitente = parts[2];
            String contenido = parts[3];
            System.out.println("el remitente es: "+remitente);
            System.out.println("el destinaraios es: "+destinatario);
            System.out.println("el contenido es: "+contenido);


        }



    }



    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }


}

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
    @Autowired
    MensajeRepository mensajeRepository;

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

        if (messageText.startsWith("OPEN:")) {
            String user = messageText.substring(5);
            System.out.println("LocalStorage recibido: "+user);
            Session_IdSesion.put(session,user);
        }

        for (Map.Entry<WebSocketSession,String> entry: Session_IdSesion.entrySet()){
            WebSocketSession session1 = entry.getKey();
            String user = entry.getValue();
            System.out.println("El user: "+user+", la sesion: "+session1);
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

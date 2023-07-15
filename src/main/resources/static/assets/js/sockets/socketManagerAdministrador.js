
let socket; // Variable para almacenar la instancia de WebSocket
function connectWebSocket() {
    socket = new WebSocket("ws://localhost:8080/chat");
    return socket;
}
function getWebSocketInstance() {
    return socket;
}

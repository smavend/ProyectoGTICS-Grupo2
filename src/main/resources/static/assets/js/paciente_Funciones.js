document.getElementById("botonLogout").addEventListener("click", function (){
    CometChatWidget.logout().then(response => {
        console.log("CometChat: Deslogueado");
    });
})

function getUrl(){
    return window.location.protocol+"//"+window.location.hostname+":8080";
}
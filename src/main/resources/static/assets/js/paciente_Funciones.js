const loading = document.querySelector("#loading");

function ocultarLoading() {
    loading.style.display = "none";
}
function mostrarLoading() {
    loading.style.display = "block";
}

ocultarLoading();
document.getElementById("botonLogout").addEventListener("click", function (){
    mostrarLoading();
    CometChatWidget.init({
        "appID": `${config.CometChatAppId}`,
        "appRegion": `${config.CometChatRegion}`,
        "authKey": `${config.CometChatAuthKey}`
    }).then(response => {
        CometChatWidget.CometChat.getLoggedinUser().then(
            user => {
                if (user){
                    CometChatWidget.logout().then(response => {
                        console.log("Sesión de usuario encontrada");
                        document.getElementById("logoutForm").submit();
                        ocultarLoading();
                    });
                }else{
                    console.log("Sesión no encontrada");
                    document.getElementById("logoutForm").submit();
                    ocultarLoading();
                }
            }, error => {
            }
        );
    });
})

function getUrl(){
    return window.location.protocol+"//"+window.location.hostname+":8080";
}
document.getElementById("botonLogout").addEventListener("click", function (){

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
                    });
                }else{
                    console.log("Sesión no encontrada");
                }
            }, error => {

            }
        );
    });
})

function getUrl(){
    return window.location.protocol+"//"+window.location.hostname+":8080";
}
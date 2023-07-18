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
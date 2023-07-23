const loading = document.querySelector("#loading");

function ocultarLoading() {
    loading.style.display = "none";
}
function mostrarLoading() {
    loading.style.display = "block";
}

ocultarLoading();

function getUrl(){
    return window.location.protocol+"//"+window.location.hostname+":8080";
}
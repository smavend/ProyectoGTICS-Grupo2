// Obtener los elementos del DOM
var selectUsuario = document.getElementById("Select Usuario");
var inputDNI = document.getElementById("inputText");
var inputNombres = document.getElementById("inputNombres");
var inputApellidos = document.getElementById("inputApellidos");
var selectClinica = document.getElementById("clinica-lista-crearform");
var otraClinica = document.getElementById("otraClinica");
var otraClinicaInput = document.getElementById("otraClinicaInput");
var otraSedeInput = document.getElementById("OtraSedeInput");
var clinicaContainer = document.getElementById("clinicaContainer");
var sedeContainer = document.getElementById("SedeContainer");
var sede_lista_container = document.getElementById("sede-lista-crearform");
var especialidadContainer = document.getElementById("EspecialidadContainer");
var selectEspecialidad = document.getElementById("especialidad-lista-crearform");
var correo_nueva_clinica = document.getElementById("correo_nueva_clinica");
var correoUser = document.getElementById("correoUser")
var telefono_nueva_clinica = document.getElementById( "telefono_nueva_clinica");

// Función para resetear los valores y ocultar la sección de Sede si es necesario
function resetValues() {
    inputDNI.value = "";
    inputNombres.value = "";
    inputApellidos.value = "";
    selectClinica.selectedIndex = 0;
    selectEspecialidad.selectedIndex = 0;
    sede_lista_container.selectedIndex = 0;
    otraClinicaInput.value = "";
    otraSedeInput.value = "";
    correoUser.value = "";
    correo_nueva_clinica.value = "";
    telefono_nueva_clinica.value = "";


    if (selectUsuario.value === "administrador"){
        clinicaContainer.style.display = "flex";
        sedeContainer.style.display = "none";
        especialidadContainer.style.display = "none";
        selectClinica.querySelector('option[value="otro"]').style.display = "block";
    } else if (selectUsuario.value === "administrativo") {
        clinicaContainer.style.display = "flex";
        sedeContainer.style.display = "none";
        especialidadContainer.style.display = "none";
        selectClinica.querySelector('option[value="otro"]').style.display = "none";
    } else {
        clinicaContainer.style.display = "none";
        sedeContainer.style.display = "none";
        selectClinica.querySelector('option[value="otro"]').style.display = "none";
    }
    // Ocultar campos de nombre de la nueva clínica y sede
    otraClinica.style.display = "none";

}

// Evento para detectar el cambio en el select de usuario
selectUsuario.addEventListener("change", function() {
    // Resetear los valores cada vez que se cambie la selección de usuario
    resetValues();
});
// Evento para detectar el cambio en el select de clinica
selectClinica.addEventListener("change", function() {
    // Si se selecciona "otro", mostrar campos de entrada para ingresar el nombre de la nueva clínica y sede
    if (selectClinica.value === "otro") {
        otraClinica.style.display = "flex";
        sedeContainer.style.display = "none";
        especialidadContainer.style.display = "none"
    } else if (selectClinica.value === "Seleccionar Clínica") {
        otraClinica.style.display = "none";
        sedeContainer.style.display = "none";
        especialidadContainer.style.display = "none";
    } else{
        otraClinica.style.display = "none";
        sedeContainer.style.display = "flex";
        especialidadContainer.style.display = "flex";
        if(selectUsuario.value === "administrador"){
            especialidadContainer.style.display = "none";
        }
        $.ajax({
            url: '/SuperAdminHomePage/getSedesByClinica',
            method: 'POST',
            data: {
                clinica: selectClinica.value
            },
            success: function(result) {
                // Actualizar la lista de sedes con los valores devueltos por el servidor
                $('#sede-lista-crearform').html(result);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log('Error al hacer la solicitud AJAX: ' + textStatus);
            }
        });
    }

});




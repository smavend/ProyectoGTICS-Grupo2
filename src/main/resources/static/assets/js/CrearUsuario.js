document.addEventListener("DOMContentLoaded", function () {
    var selectUsuario = document.getElementById("SelectUsuario");
    var inputDNI = document.getElementById("inputText");
    var inputNombres = document.getElementById("inputNombres");
    var inputApellidos = document.getElementById("inputApellidos");
    var selectClinica = document.getElementById("clinica-lista-crearform");
    var otraClinica = document.getElementById("otraClinica");
    var otraClinicaInput = document.getElementById("otraClinicaInput");
    var otraSedeInput = document.getElementById("OtraSedeInput");
    var clinicaContainer = document.getElementById("clinicaContainer");
    var sedeContainer = document.getElementById("SedeContainer");
    var especialidadContainer = document.getElementById("EspecialidadContainer");
    var selectEspecialidad = document.getElementById("especialidad-lista-crearform");
    var correoUser = document.getElementById("correoUser");
    var selectSede = document.getElementById("sede-lista-crearform");


    function resetValues2() {
        if (selectUsuario.value === "administrador") {
            selectEspecialidad.selectedIndex = "1";
            sedeContainer.style.display = "flex";
            especialidadContainer.style.display = "none";
        } else if (selectUsuario.value === "administrativo") {
            selectEspecialidad.selectedIndex = "0";
            sedeContainer.style.display = "flex";
            especialidadContainer.style.display = "flex";
        } else {
            sedeContainer.style.display = "none";
            especialidadContainer.style.display = "none";

        }

    }
    function resetValues() {
        console.log(selectUsuario.value);
        if (selectUsuario.value === "administrador") {
            especialidadContainer.style.display = "none";
        } else if (selectUsuario.value === "administrativo") {
            especialidadContainer.style.display = "flex";
        } else {
            sedeContainer.style.display = "none"
            console.log("esta en flex el sedecontainer")
            console.log(selectSede.selectedIndex)

        }
        // Ocultar campos de nombre de la nueva clínica y sede
    }

    // Evento para detectar el cambio en el select de usuario
    selectUsuario.addEventListener("change", function () {
        if (selectUsuario.value === "administrador") {
            selectEspecialidad.selectedIndex = "1";
            // Muestra el campo de clínica y establece el valor predeterminado si ha cambiado el tipo de usuario
            sedeContainer.style.display = "flex";
            especialidadContainer.style.display = "none";
        } else if (selectUsuario.value === "administrativo") {
            selectEspecialidad.selectedIndex = "0";
            // Oculta el campo de clínica y establece el valor predeterminado si ha cambiado el tipo de usuario
            sedeContainer.style.display = "flex";
            especialidadContainer.style.display = "flex";
        }else{
            especialidadContainer.style.display = "none";
            sedeContainer.style.display = "none";
        }
        // lastUsuario = selectUsuario.value;
        // // Resetear los valores cada vez que se cambie la selección de usuario
        resetValues();
    });
    resetValues2();
});
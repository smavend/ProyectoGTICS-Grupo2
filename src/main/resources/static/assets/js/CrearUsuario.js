let sedeSeleccionada = localStorage.getItem("sedeSeleccionada") || "";
document.addEventListener("DOMContentLoaded", function () {
    let updatingFromResetValues2 = false;
    let lastUsuario = "";
    let lastClinica = "";
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
    var correo_nueva_clinica = document.getElementById("correo_nueva_clinica");
    var correoUser = document.getElementById("correoUser");
    var telefono_nueva_clinica = document.getElementById("telefono_nueva_clinica");
    var selectSede = document.getElementById("sede-lista-crearform");
    var otraSede = document.getElementById("otraSede");
    var otraSede4input = document.getElementById("otraSede4input");
    var sede_nueva4_direccion = document.getElementById("sede_nueva4_direccion");
    var sede_nueva_direccion = document.getElementById("sede_nueva_direccion");

// Función para resetear los valores y ocultar la sección de Sede si es necesario

    function resetValues2() {
        if (selectUsuario.value === "administrador") {
            console.log("el valo de selectClinica.value es >>>°°°!!!!!!!!!!!!!: ", selectClinica.value);
            console.log("el valo de selectSede.value es >>>°°°!!!!!!!!!!!!!: ", selectSede.value);
            sedeSeleccionada = "";
            selectSede.value = sedeSeleccionada;
            selectClinica.dispatchEvent(new Event("change"));
            clinicaContainer.style.display = "flex";
            especialidadContainer.style.display = "none";
            if (selectSede.value === "otro" || selectSede.value === "") {
                sedeContainer.style.display = "none";
            } else {
                sedeContainer.style.display = "none";
            }
            selectClinica.querySelector('option[value="otro"]').style.display = "block";

        } else if (selectUsuario.value === "administrativo") {
            selectClinica.dispatchEvent(new Event("change"));
            clinicaContainer.style.display = "flex";
            if(selectClinica.value !== ""){
                sedeContainer.style.display = "flex";
                especialidadContainer.style.display = "flex";
            }else{
                sedeContainer.style.display = "none";
                especialidadContainer.style.display = "none";
            }

            selectClinica.querySelector('option[value="otro"]').style.display = "none";
        } else {
            clinicaContainer.style.display = "none";
            sedeContainer.style.display = "none";
            selectClinica.querySelector('option[value="otro"]').style.display = "none";
        }
        updatingFromResetValues2 = true;
        updatingFromResetValues2 = false;
    }

    function resetValues() {
        inputDNI.value = "";
        inputNombres.value = "";
        inputApellidos.value = "";
        selectEspecialidad.selectedIndex = 0;
        sede_nueva_direccion.value = "";
        otraClinicaInput.value = "";
        otraSedeInput.value = "";
        correoUser.value = "";
        correo_nueva_clinica.value = "";
        telefono_nueva_clinica.value = "";
        otraSede4input.value = "";
        sede_nueva4_direccion.value = "";


        if (selectUsuario.value === "administrador") {
            sedeSeleccionada = "";
            selectSede.value = sedeSeleccionada;
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
        otraSede.style.display = "none";
        especialidadContainer.style.display = "none";


    }


// Evento para detectar el cambio en el select de usuario
    selectUsuario.addEventListener("change", function () {
        if (selectUsuario.value === "administrador") {
            // Muestra el campo de clínica y establece el valor predeterminado si ha cambiado el tipo de usuario
            clinicaContainer.style.display = "flex";
            selectClinica.querySelector('option[value="otro"]').style.display = "block";
            if (selectUsuario.value !== lastUsuario) {
                clinicaContainer.style.display = "flex";
                sedeContainer.style.display = "none";
                especialidadContainer.style.display = "none";
                selectClinica.selectedIndex = 0; // Establece el valor predeterminado en "Seleccionar Clínica"
                lastClinica = "";
            }
        } else if (selectUsuario.value === "administrativo") {
            // Oculta el campo de clínica y establece el valor predeterminado si ha cambiado el tipo de usuario
            clinicaContainer.style.display = "flex";
            selectClinica.querySelector('option[value="otro"]').style.display = "none";
            if (selectUsuario.value !== lastUsuario) {
                selectClinica.selectedIndex = 0; // Establece el valor predeterminado en "Seleccionar Clínica"
                lastClinica = "";
            }
        }
        lastUsuario = selectUsuario.value;
        // Resetear los valores cada vez que se cambie la selección de usuario
        resetValues();
    });
// Evento para detectar el cambio en el select de clinica
    selectClinica.addEventListener("change", function () {
        // Si se selecciona "otro", mostrar campos de entrada para ingresar el nombre de la nueva clínica y sede

        if(selectUsuario.value==="administrador"){
            selectSede.value = sedeSeleccionada;
        }else if(selectUsuario.value ==="administrativo"){
            selectEspecialidad.selectedIndex = "0";
            if(sedeSeleccionada = "otro"){
                sedeSeleccionada = "";
                selectSede.value = sedeSeleccionada;
            }
        }
        console.log(" VALOR 1 EN SELECTSEDE.VALUE ES: ",selectSede.value);

        otraSede.style.display = "none";
        if (selectClinica.value === "otro") {
            if (selectUsuario.value === "administrador") {
                selectEspecialidad.selectedIndex = "1";
                otraSede4input.value = "->Ingrese una nombre válido<-";
                sede_nueva4_direccion.value = "->Ingrese una direccion válida<-";
            }
            otraClinica.style.display = "flex";
            sedeContainer.style.display = "none";
            especialidadContainer.style.display = "none"
        } else if (selectClinica.value === "") {
            otraClinica.style.display = "none";
            sedeContainer.style.display = "none";
            especialidadContainer.style.display = "none";
        } else {
            if (selectUsuario.value === "administrador") {

                console.log("el valor -<>>>-<_<_<_<_<_>>_ de selectSede.value es : UWU :", selectSede.value);
                otraSede4input.value = "->Ingrese una nombre válido<-";
                sede_nueva4_direccion.value = "->Ingrese una direccion válida<-";
                console.log("esta sede es ... : ", otraSede4input.value);
                console.log("esta dirección es ... : ", sede_nueva4_direccion.value);
                selectEspecialidad.selectedIndex = "1";
                otraSedeInput.value = "->Ingrese una nombre de Sede válido<-";
                sede_nueva_direccion.value = "->Ingrese una dirección de Sede válido<-";
                telefono_nueva_clinica.value = "0000000";
                correo_nueva_clinica.value = "Correo@ClinicaCambiar.com";
                otraClinicaInput.value = "->Ingrese una nombre de Clínica válido<-";

            }
            if (selectUsuario.value === "administrativo") {

                otraSede4input.value = "->Ingrese una nombre válido<-";
                sede_nueva4_direccion.value = "->Ingrese una direccion válida<-";
                otraSedeInput.value = "->Ingrese una nombre de Sede válido<-";
                sede_nueva_direccion.value = "->Ingrese una dirección de Sede válido<-";
                telefono_nueva_clinica.value = "0000000";
                correo_nueva_clinica.value = "Correo@ClinicaCambiar.com";
                otraClinicaInput.value = "->Ingrese una nombre de Clínica válido<-";

            }


            otraClinica.style.display = "none";

            if (selectUsuario.value === "") {
                sedeContainer.style.display = "none";
                especialidadContainer.style.display = "none";
            } else {
                sedeContainer.style.display = "flex";
                especialidadContainer.style.display = "flex";
            }
            if (selectUsuario.value === "administrador") {
                especialidadContainer.style.display = "none";
            }
            $.ajax({
                url: '/SuperAdminHomePage/getSedesByClinica',
                method: 'POST',
                data: {
                    clinica: selectClinica.value
                },
                success: function (result) {
                    // Actualizar la lista de sedes con los valores devueltos por el servidor
                    $('#sede-lista-crearform').html(result);
                    let sedeFound = false;
                    $('#sede-lista-crearform option').each(function () {
                        if ($(this).val() === sedeSeleccionada) {
                            sedeFound = true;
                        }
                    });
                    if(selectUsuario.value ==="administrador") {
                        if (sedeFound && sedeSeleccionada !== "") {
                            selectSede.value = sedeSeleccionada;
                        }
                    }else if(selectUsuario.value === "administrativo"){
                        sedeSeleccionada = "";
                        selectSede.value = sedeSeleccionada;
                    }
                    if (selectUsuario.value === "administrador") {
                        $('#sede-lista-crearform option').each(function () {
                            var option = $(this);
                            if (option.data('admin')) {
                                option.prop('disabled', true);
                                option.css({
                                    'background-color': '#dcdcdc',
                                    'color': '#9a9a9a'
                                });
                            }

                        });
                    } else if (selectUsuario.value === "administrativo") {
                        selectSede.querySelector('option[value="otro"]').style.display = "none";
                    }
                    if (selectClinica.value !== "" && sedeContainer.style.display == "none") {
                        selectClinica.dispatchEvent(new Event("change"));
                    }
                    handleSelectSedeChange(true);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error al hacer la solicitud AJAX: ' + textStatus);
                }
            });
        }
    });

    function updateSedeSeleccionada() {
        sedeSeleccionada = selectSede.value;
        localStorage.setItem("sedeSeleccionada", sedeSeleccionada);
        console.log("el valor de la nueva sede en UPDATE es:", selectSede.value);
    }

    selectSede.addEventListener("change", function () {
        handleSelectSedeChange(true);
    });

    function handleSelectSedeChange(updateSedeSeleccionadaFlag) {
        if (selectSede.value === "otro") {
            otraSede.style.display = "flex";
        } else {
            otraSede.style.display = "none";
        }
        if (updateSedeSeleccionadaFlag && selectClinica.value !== lastClinica) {
            if (selectSede.value === "") {
                sedeSeleccionada = "";
            } else if (selectSede.value !== "otro") {
                sedeSeleccionada = selectSede.value;
            }
        } else if (updateSedeSeleccionadaFlag) {
            updateSedeSeleccionada();
        }
        lastClinica = selectClinica.value;
    }
    resetValues2();
});
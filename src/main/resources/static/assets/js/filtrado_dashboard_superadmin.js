// $('#filter-form').submit(function (event) {
//     // Prevenir el comportamiento predeterminado del formulario (recargar la página)
//     event.preventDefault();
//
//     // Obtener el botón que activó el envío del formulario
//     var action = $(document.activeElement).data('action');
//
//     // Establecer los valores de los campos de filtro a los valores predeterminados si la acción es "clear"
//     if (action === 'clear') {
//         $('#clinica-filter').val('todos').trigger('change');
//         $('#sede-filter').val('todos').trigger('change');
//         $('#especialidad-filter').val('todos').trigger('change');
//         $('#nombre-filter').val('').trigger('change');
//     }
//
//     // Obtener los valores seleccionados en el formulario
//     var clinica = $('#clinica-filter').val();
//     var sede = $('#sede-filter').val();
//     var especialidad = $('#especialidad-filter').val();
//     var nombre = $('#nombre-filter').val();
//
//     // Hacer la solicitud AJAX con los valores seleccionados
//     $.ajax({
//         url: '/SuperAdminHomePage/filtrar',
//         method: 'POST',
//         data: {clinica: clinica, sede: sede, especialidad: especialidad, nombre: nombre},
//         success: function (result) {
//             // Actualizar la tabla con los administrativos filtrados
//             $('#superadmin_administrativos_table').html(result);
//
//             // Vuelve a configurar la paginación después de actualizar la tabla
//             setupTablePagination($('#superadmin_administrativos_table'), 5);
//         },
//         error: function (jqXHR, textStatus, errorThrown) {
//             console.log('Error al hacer la solicitud AJAX: ' + textStatus);
//         }
//     });
// });

//-------------->>>>>>>>>>>>>>>>>>>>>>>>>>>

//
// $(document).ready(function () {
//     $('#filter-form').submit(function (event) {
//         function showTableSegment(table, startIndex, endIndex) {
//             table.find('tbody tr').hide()
//                 .slice(startIndex, endIndex).show();
//
//             $('#prev-btn2').prop('disabled', startIndex === 0);
//             $('#next-btn2').prop('disabled', endIndex >= table.find('tbody tr').length);
//
//             var currentPage = startIndex / 5 + 1;
//             $('#page-num2').text(currentPage);
//         }
//
//         function setupTablePagination(table, itemsPerPage) {
//             var startIndex = 0;
//             var endIndex = itemsPerPage;
//
//             showTableSegment(table, startIndex, endIndex);
//
//             function updateIndex(isNext) {
//                 if (isNext) {
//                     if (startIndex + itemsPerPage < table.find('tbody tr').length) {
//                         startIndex += itemsPerPage;
//                     }
//                 } else {
//                     if (startIndex > 0) {
//                         startIndex -= itemsPerPage;
//                     }
//                 }
//                 endIndex = startIndex + itemsPerPage;
//                 showTableSegment(table, startIndex, endIndex);
//             }
//
//             $('#prev-btn2').click(function () {
//                 updateIndex(false);
//             });
//
//             $('#next-btn2').click(function () {
//                 updateIndex(true);
//             });
//             $('#AdministrativoTable').css('display', '');
//         }
//
//         setupTablePagination($('#superadmin_administrativos_table'), 5);
//
//         // Prevenir el comportamiento predeterminado del formulario (recargar la página)
//         event.preventDefault();
//         // Obtener el botón que activó el envío del formulario
//         var action = $(document.activeElement).data('action');
//         var clinica
//         var sede
//         var especialidad
//         var nombre
//
//         // Establecer los valores de los campos de filtro a los valores predeterminados si la acción es "clear"
//         if (action === 'clear') {
//             $('#clinica-filter').val('todos').trigger('change');
//             $('#sede-filter').val('todos').trigger('change');
//             $('#especialidad-filter').val('todos').trigger('change');
//             $('#nombre-filter').val('').trigger('change');
//
//             // Asignar 'todos' a las variables después de establecer los valores de los campos de filtro
//             clinica = 'todos';
//             sede = 'todos';
//             especialidad = 'todos';
//             nombre = '';
//
//             // Hacer la solicitud AJAX con los valores seleccionados
//             $.ajax({
//                 url: '/SuperAdminHomePage/filtrar',
//                 method: 'POST',
//                 data: {clinica: clinica, sede: sede, especialidad: especialidad, nombre: nombre},
//                 success: function (result) {
//                     // Actualizar la tabla con los administrativos sin filtros
//                     $('#superadmin_administrativos_table').html(result);
//
//                     // Vuelve a configurar la paginación después de actualizar la tabla
//                     setupTablePagination($('#superadmin_administrativos_table'), 5);
//                 },
//                 error: function (jqXHR, textStatus, errorThrown) {
//                     console.log('Error al hacer la solicitud AJAX: ' + textStatus);
//                 }
//             });
//         } else if (action === 'filter') {
//
//             // Obtener los valores seleccionados en el formulario
//             clinica = $('#clinica-filter').val();
//             sede = $('#sede-filter').val();
//             especialidad = $('#especialidad-filter').val();
//             nombre = $('#nombre-filter').val();
//             // Hacer la solicitud AJAX con los valores seleccionados
//             $.ajax({
//                 url: '/SuperAdminHomePage/filtrar',
//                 method: 'POST',
//                 data: {clinica: clinica, sede: sede, especialidad: especialidad, nombre: nombre},
//                 success: function (result) {
//                     // Actualizar la tabla con los administrativos filtrados
//                     $('#superadmin_administrativos_table').html(result);
//
//                     // Vuelve a configurar la paginación después de actualizar la tabla
//                     setupTablePagination($('#superadmin_administrativos_table'), 5);
//                 },
//                 error: function (jqXHR, textStatus, errorThrown) {
//                     console.log('Error al hacer la solicitud AJAX: ' + textStatus);
//                 }
//             });
//         }
//     });
// });


//-------->>>>>>>>>>>>>>>>>>>>>>>>>>
$(document).ready(function () {
    function showTableSegment(table, startIndex, endIndex, prevBtnSelector, nextBtnSelector, pageNumber) {
        table.find('tbody tr').hide()
            .slice(startIndex, endIndex).show();

        $(prevBtnSelector).prop('disabled', startIndex === 0);
        $(nextBtnSelector).prop('disabled', endIndex >= table.find('tbody tr').length);

        var currentPage = startIndex / 5 + 1;
        $(pageNumber).text(currentPage);
    }

    function setupTablePagination(table, itemsPerPage, prevBtnSelector, nextBtnSelector, pageNumber) {
        var startIndex = 0;
        var endIndex = itemsPerPage;

        showTableSegment(table, startIndex, endIndex, prevBtnSelector, nextBtnSelector, pageNumber);

        function updateIndex(isNext) {
            if (isNext) {
                if (startIndex + itemsPerPage < table.find('tbody tr').length) {
                    startIndex += itemsPerPage;
                }
            } else {
                if (startIndex > 0) {
                    startIndex -= itemsPerPage;
                }
            }
            endIndex = startIndex + itemsPerPage;
            showTableSegment(table, startIndex, endIndex, prevBtnSelector, nextBtnSelector, pageNumber);
        }

        $(prevBtnSelector).click(function () {
            updateIndex(false);
        });

        $(nextBtnSelector).click(function () {
            updateIndex(true);
        });
    }

    $('.filter-form').submit(function (event) {
        // Prevenir el comportamiento predeterminado del formulario (recargar la página)
        event.preventDefault();
        // Obtener el botón que activó el envío del formulario
        var action = $(document.activeElement).data('action');
        if (action === 'clear') {
            $('#clinica-filter').val('todos').trigger('change');
            $('#sede-filter').val('todos').trigger('change');
            $('#especialidad-filter').val('todos').trigger('change');
            $('#nombre-filter').val('').trigger('change');
            clinica = 'todos';
            sede = 'todos';
            especialidad = 'todos';
            nombre = '';

            $.ajax({
                url: '/SuperAdminHomePage/filtrar',
                method: 'POST',
                data: {clinica: clinica, sede: sede, especialidad: especialidad, nombre: nombre},
                success: function (result) {
                    // Actualizar la tabla con los administrativos filtrados
                    $('#superadmin_administrativos_table').html(result);

                    // Vuelve a configurar la paginación después de actualizar la tabla
                    setupTablePagination($('#superadmin_administrativos_table'), 5, '#prev-btn2', '#next-btn2', '#page-num2');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error al hacer la solicitud AJAX: ' + textStatus);
                }
            });
        } else if (action === 'filter') {
            // Obtener los valores seleccionados en el formulario
            var clinica = $('#clinica-filter').val();
            var sede = $('#sede-filter').val();
            var especialidad = $('#especialidad-filter').val();
            var nombre = $('#nombre-filter').val();
            // Hacer la solicitud AJAX con los valores seleccionados
            $.ajax({
                url: '/SuperAdminHomePage/filtrar',
                method: 'POST',
                data: {clinica: clinica, sede: sede, especialidad: especialidad, nombre: nombre},
                success: function (result) {
                    // Actualizar la tabla con los administrativos filtrados
                    $('#superadmin_administrativos_table').html(result);

                    // Vuelve a configurar la paginación después de actualizar la tabla
                    setupTablePagination($('#superadmin_administrativos_table'), 5, '#prev-btn2', '#next-btn2', '#page-num2');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error al hacer la solicitud AJAX: ' + textStatus);
                }
            });
        } else if (action === 'clear-paciente') {
            $('#clinica-filter-paciente').val('todos').trigger('change');
            $('#sede-filter-paciente').val('todos').trigger('change');
            // $('#especialidad-filter-paciente').val('todos').trigger('change');
            $('#nombre-filter-paciente').val('').trigger('change');
            clinica = 'todos';
            sede = 'todos';
            // especialidad = 'todos';
            nombre = '';
            $.ajax({
                url: '/SuperAdminHomePage/filtrarPaciente', // Asegúrate de cambiar esta URL a la correcta
                method: 'POST',
                // data: {clinica: clinica, sede: sede, especialidad: especialidad, nombre: nombre},

                 data: {clinica: clinica, sede: sede, nombre: nombre},

                success: function (result) {
                    // Actualizar la tabla con los pacientes filtrados
                    $('#superadmin_Pacientes_table').html(result);

                    // Vuelve a configurar la paginación después de actualizar la tabla
                    setupTablePagination($('#superadmin_Pacientes_table'), 5, '#prev-btn4', '#next-btn4', '#page-num4');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error al hacer la solicitud AJAX: ' + textStatus);
                }
            });
        } else if (action === 'filter-paciente') {
            var clinica = $('#clinica-filter-paciente').val();
            var sede = $('#sede-filter-paciente').val();
            // var especialidad = $('#especialidad-filter-paciente').val();
            var nombre = $('#nombre-filter-paciente').val();
            // Hacer la solicitud AJAX con los valores seleccionados
            $.ajax({
                url: '/SuperAdminHomePage/filtrarPaciente', // Asegúrate de cambiar esta URL a la correcta
                method: 'POST',
                // data: {clinica: clinica, sede: sede, especialidad: especialidad, nombre: nombre},

                 data: {clinica: clinica, sede: sede, nombre: nombre},

                success: function (result) {
                    // Actualizar la tabla con los pacientes filtrados
                    $('#superadmin_Pacientes_table').html(result);

                    // Vuelve a configurar la paginación después de actualizar la tabla
                    setupTablePagination($('#superadmin_Pacientes_table'), 5, '#prev-btn4', '#next-btn4', '#page-num4');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error al hacer la solicitud AJAX: ' + textStatus);
                }
            });
        } else if (action === 'clear-administrador') {
            $('#clinica-filter-administradores').val('todos').trigger('change');
            $('#sede-filter-administradores').val('todos').trigger('change');
            // $('#especialidad-filter-paciente').val('todos').trigger('change');
            $('#nombre-filter-administradores').val('').trigger('change');
            clinica = 'todos';
            sede = 'todos';
            // especialidad = 'todos';
            nombre = '';
            $.ajax({
                url: '/SuperAdminHomePage/filtrarAdministradores', // Asegúrate de cambiar esta URL a la correcta
                method: 'POST',
                // data: {clinica: clinica, sede: sede, especialidad: especialidad, nombre: nombre},
                data: {clinica: clinica, sede: sede, nombre: nombre},

                success: function (result) {
                    // Actualizar la tabla con los pacientes filtrados
                    $('#superadmin_administrador_table').html(result);

                    // Vuelve a configurar la paginación después de actualizar la tabla
                    setupTablePagination($('#superadmin_administrador_table'), 5, '#prev-btn1', '#next-btn1', '#page-num1');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error al hacer la solicitud AJAX: ' + textStatus);
                }
            });

        } else if (action === 'filter-administrador') {
            var clinica = $('#clinica-filter-administradores').val();
            var sede = $('#sede-filter-administradores').val();
            // var especialidad = $('#especialidad-filter-paciente').val();
            var nombre = $('#nombre-filter-administradores').val();
            // Hacer la solicitud AJAX con los valores seleccionados
            $.ajax({
                url: '/SuperAdminHomePage/filtrarAdministradores', // Asegúrate de cambiar esta URL a la correcta
                method: 'POST',
                // data: {clinica: clinica, sede: sede, especialidad: especialidad, nombre: nombre},
                data: {clinica: clinica, sede: sede, nombre: nombre},
                success: function (result) {
                    // Actualizar la tabla con los pacientes filtrados
                    $('#superadmin_administrador_table').html(result);

                    // Vuelve a configurar la paginación después de actualizar la tabla
                    setupTablePagination($('#superadmin_administrador_table'), 5, '#prev-btn1', '#next-btn1', '#page-num1');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error al hacer la solicitud AJAX: ' + textStatus);
                }
            });
        } else if (action === 'clear-doctores') {
            $('#clinica-filter-doctores').val('todos').trigger('change');
            $('#sede-filter-doctores').val('todos').trigger('change');
            $('#especialidad-filter-doctores').val('todos').trigger('change');
            $('#nombre-filter-doctores').val('').trigger('change');
            clinica = 'todos';
            sede = 'todos';
            especialidad = 'todos';
            nombre = '';
            $.ajax({
                url: '/SuperAdminHomePage/filtrarDoctores', // Asegúrate de cambiar esta URL a la correcta
                method: 'POST',
                // data: {clinica: clinica, sede: sede, especialidad: especialidad, nombre: nombre},
                data: {clinica: clinica, sede: sede, nombre: nombre,especialidad: especialidad},

                success: function (result) {
                    // Actualizar la tabla con los pacientes filtrados
                    $('#superadmin_Doctores_table').html(result);

                    // Vuelve a configurar la paginación después de actualizar la tabla
                    setupTablePagination($('#superadmin_Doctores_table'), 5, '#prev-btn3', '#next-btn3', '#page-num3');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error al hacer la solicitud AJAX: ' + textStatus);
                }
            });

        } else if (action === 'filter-doctores') {
            var clinica = $('#clinica-filter-doctores').val();
            var sede = $('#sede-filter-doctores').val();
            var especialidad = $('#especialidad-filter-doctores').val();
            var nombre = $('#nombre-filter-doctores').val();
            // Hacer la solicitud AJAX con los valores seleccionados
            $.ajax({
                url: '/SuperAdminHomePage/filtrarDoctores', // Asegúrate de cambiar esta URL a la correcta
                method: 'POST',
                // data: {clinica: clinica, sede: sede, especialidad: especialidad, nombre: nombre},
                data: {clinica: clinica, sede: sede, nombre: nombre, especialidad: especialidad},
                success: function (result) {
                    // Actualizar la tabla con los pacientes filtrados
                    $('#superadmin_Doctores_table').html(result);

                    // Vuelve a configurar la paginación después de actualizar la tabla
                    setupTablePagination($('#superadmin_Doctores_table'), 5, '#prev-btn3', '#next-btn3', '#page-num3');
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log('Error al hacer la solicitud AJAX: ' + textStatus);
                }
            });
        }
    });
});
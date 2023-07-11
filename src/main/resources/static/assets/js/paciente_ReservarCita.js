// OBTENER ESPECIALIDADES
$("#select_modalidad").click(function(){
    let selectEsp = $("#select_especialidad");
    let campoSede = $("#campoSede");
    let modalidad = $(this).val();

    if(modalidad !== ""){
        $.ajax({
            method:"GET",
            url: getUrl()+"/Paciente/api/especialidades/"+modalidad
        }).done(function(data){
            selectEsp.empty();
            selectEsp.append("<option value=''>-- Seleccione una especialidad --</option>");
            for (let i = 0; i < data.especialidades.length; i++){
                selectEsp.append("<option value='"+data.especialidades[i].idEspecialidad+"'>"+data.especialidades[i].nombre+"</option>");
            }
        }).fail(function(e){
            console.log("error");
        });

        if(modalidad === "1"){
            campoSede.slideUp();
            $("#campoSede option[value=1]").attr("selected", "selected");
        }
        else if(modalidad === "0"){
            campoSede.slideDown();
            $("#campoSede option[value=]").attr("selected", "selected");
        }
    }
});

// VALIDAR SELECCIONES Y OBTENER HORARIOS EN RESERVAR CITA
$("#select_doctor").change(function(){
    buscarHorarios($(this).val(), $("#input_fecha").val());
});

$("#input_fecha").change(function(){
    buscarHorarios($("#select_doctor").val(), $(this).val());
})

function buscarHorarios(doctor, fecha){
    let selectHorario = $("#select_horario");

    if(doctor !== "" && fecha !== ""){
        $.ajax({
            method: "GET",
            url: getUrl()+"/Paciente/api/horarios/consulta/"+doctor+"/"+fecha
        }).done(function(data){
            selectHorario.empty();
            selectHorario.append("<option value=''>-- Seleccione un horario --</option>");
            for(let i = 0; i < data.horarios.length; i++){
                selectHorario.append("<option value='"+data.horarios[i].inicio+"'>"+data.horarios[i].inicio.substring(0,5)+" - "+data.horarios[i].fin.substring(0,5)+"</option>");
            }

        }).fail(function(e){
            console.log("error");
        });
    }
}

// FUNCIONES
function getUrl(){
    return window.location.protocol+"//"+window.location.hostname+":8080";
}
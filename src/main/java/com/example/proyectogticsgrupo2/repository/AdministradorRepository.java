package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.dto.*;
import com.example.proyectogticsgrupo2.entity.Administrador;

import com.example.proyectogticsgrupo2.entity.AdministrativoPorEspecialidadPorSede;
import com.example.proyectogticsgrupo2.entity.Paciente;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, String> {
    @Transactional
    @Modifying
    @Query(value = "INSERT INTO administrador (id_administrador, nombre, apellidos, estado, sede_id_sede, correo) VALUES (:dni, :nombres, :apellidos, 1, :sedenuevaId, :correoUser)", nativeQuery = true)
    void insertarAdministrador(@Param("dni") String dni, @Param("nombres") String nombres, @Param("apellidos") String apellidos, @Param("sedenuevaId") int sedenuevaId, @Param("correoUser") String correoUser);
    @Query(nativeQuery = true, value = "select * from sede_x_especialidad_x_administrativo where administrativo_id_administrativo = ?1")
    AdministrativoPorEspecialidadPorSede buscarPorAdministrativoId(String id);
    @Query(nativeQuery = true, value = "select * from sede_x_especialidad_x_administrativo where sede_id_sede = ?1")
    AdministrativoPorEspecialidadPorSede buscarPorSedeId(String id);
    @Query(nativeQuery = true, value = "select sede_id_sede from administrador where id_administrador = ?1")
    int obteneSedePorAdministradorId(String id);

    @Query("SELECT a.sede.idSede FROM Administrador a")
    List<Integer> findSedesConAdministrador();

    Administrador findByCorreo(String correo);
    @Query(nativeQuery = true, value = "select p.fecha_cancelada as fechacancelada, concat(paci.nombre,' ',paci.apellidos) as nombreuser,\n" +
            "\t\te.nombre as especialidadcita,\n" +
            "\t\tcase when e.es_examen=1 then 'Consulta' else 'Examen' end  as concepto,\n" +
            "        s.nombre as nombreseguro, sxexa.precio_cita as preciocita, p.tipo_pago as tipopago\n" +
            "from pago p\n" +
            "inner join cita c on (p.cita_id_cita=c.id_cita)\n" +
            "inner join paciente paci on (c.paciente_id_paciente=paci.id_paciente)\n" +
            "inner join doctor d on (c.doctor_id_doctor=d.id_doctor)\n" +
            "inner join especialidad e on (d.especialidad_id_especialidad=e.id_especialidad)\n" +
            "left join sede_x_especialidad_x_administrativo sxexa on (sxexa.especialidad_id_especialidad=e.id_especialidad)\n" +
            "inner join seguro s on (paci.seguro_id_seguro=s.id_seguro)\n" +
            "order by p.fecha_cancelada desc")
    List<AdministradorIngresos> obtenerIgresos();
    @Query(nativeQuery = true, value = "select p.fecha_cancelada as fechacancelada, concat(paci.nombre,' ',paci.apellidos) as nombreuser,\n" +
            "\t\te.nombre as especialidadcita,\n" +
            "\t\tcase when e.es_examen=1 then 'Consulta' else 'Examen' end  as concepto,\n" +
            "        s.nombre as nombreseguro, sxexa.precio_cita as preciocita, p.tipo_pago as tipopago\n" +
            "from pago p\n" +
            "inner join cita c on (p.cita_id_cita=c.id_cita)\n" +
            "inner join paciente paci on (c.paciente_id_paciente=paci.id_paciente)\n" +
            "inner join doctor d on (c.doctor_id_doctor=d.id_doctor)\n" +
            "inner join especialidad e on (d.especialidad_id_especialidad=e.id_especialidad)\n" +
            "left join sede_x_especialidad_x_administrativo sxexa on (sxexa.especialidad_id_especialidad=e.id_especialidad)\n" +
            "inner join seguro s on (paci.seguro_id_seguro=s.id_seguro)\n" +
            "where s.id_seguro= ?1 " +
            "order by p.fecha_cancelada desc")
    List<AdministradorIngresos> obtenerIgresosPorSeguro(int id);
    @Query(nativeQuery = true, value = "select p.fecha_cancelada as fechacancelada, concat(paci.nombre,' ',paci.apellidos) as nombreuser,\n" +
            "\t\te.nombre as especialidadcita,\n" +
            "\t\tcase when e.es_examen=1 then 'Consulta' else 'Examen' end  as concepto,\n" +
            "        s.nombre as nombreseguro, sxexa.precio_cita as preciocita, p.tipo_pago as tipopago\n" +
            "from pago p\n" +
            "inner join cita c on (p.cita_id_cita=c.id_cita)\n" +
            "inner join paciente paci on (c.paciente_id_paciente=paci.id_paciente)\n" +
            "inner join doctor d on (c.doctor_id_doctor=d.id_doctor)\n" +
            "inner join especialidad e on (d.especialidad_id_especialidad=e.id_especialidad)\n" +
            "left join sede_x_especialidad_x_administrativo sxexa on (sxexa.especialidad_id_especialidad=e.id_especialidad)\n" +
            "inner join seguro s on (paci.seguro_id_seguro=s.id_seguro)\n" +
            "where e.id_especialidad=?1 " +
            "order by p.fecha_cancelada desc")
    List<AdministradorIngresos> obtenerIgresosPorEspecialidad(int id);
    @Query(nativeQuery = true, value = "select p.fecha_cancelada as fechacancelada, concat(paci.nombre,' ',paci.apellidos) as nombreuser,\n" +
            "\t\te.nombre as especialidadcita,\n" +
            "\t\tcase when e.es_examen=1 then 'Consulta' else 'Examen' end  as concepto,\n" +
            "        s.nombre as nombreseguro, sxexa.precio_cita as preciocita, p.tipo_pago as tipopago\n" +
            "from pago p\n" +
            "inner join cita c on (p.cita_id_cita=c.id_cita)\n" +
            "inner join paciente paci on (c.paciente_id_paciente=paci.id_paciente)\n" +
            "inner join doctor d on (c.doctor_id_doctor=d.id_doctor)\n" +
            "inner join especialidad e on (d.especialidad_id_especialidad=e.id_especialidad)\n" +
            "left join sede_x_especialidad_x_administrativo sxexa on (sxexa.especialidad_id_especialidad=e.id_especialidad)\n" +
            "inner join seguro s on (paci.seguro_id_seguro=s.id_seguro)\n" +
            "where p.tipo_pago= ?1 " +
            "order by p.fecha_cancelada desc")
    List<AdministradorIngresos> obtenerIgresosPorTipoPago(String tipo);

    @Query(nativeQuery = true, value = "select case when e.es_examen=1 then 'Consulta' else 'Examen' end  as concepto,\n" +
            "\ttruncate(sxexa.precio_cita*s.doctor,2) as pagodoctor, concat(d.nombre,' ',d.apellidos) as nombreuser,\n" +
            "\te.nombre as especialidadcita, s.nombre as nombreseguro,  \n" +
            "\tp.fecha_cancelada as fecha, 'Gastos de personal médico' as categoriagasto\n" +
            "from pago p\n" +
            "inner join cita c on (p.cita_id_cita=c.id_cita)\n" +
            "inner join paciente paci on (c.paciente_id_paciente=paci.id_paciente)\n" +
            "inner join doctor d on (c.doctor_id_doctor=d.id_doctor)\n" +
            "inner join especialidad e on (d.especialidad_id_especialidad=e.id_especialidad)\n" +
            "left join sede_x_especialidad_x_administrativo sxexa on (sxexa.especialidad_id_especialidad=e.id_especialidad)\n" +
            "inner join seguro s on (paci.seguro_id_seguro=s.id_seguro)\n" +
            "order by p.fecha_cancelada desc")
    List<AdministradorEgresos> obtenerEgresos();
    @Query(nativeQuery = true, value = "select case when e.es_examen=1 then 'Consulta' else 'Examen' end  as concepto,\n" +
            "\ttruncate(sxexa.precio_cita*s.doctor,2) as pagodoctor, concat(d.nombre,' ',d.apellidos) as nombreuser,\n" +
            "\te.nombre as especialidadcita, s.nombre as nombreseguro,  \n" +
            "\tp.fecha_cancelada as fecha, 'Gastos de personal médico' as categoriagasto\n" +
            "from pago p\n" +
            "inner join cita c on (p.cita_id_cita=c.id_cita)\n" +
            "inner join paciente paci on (c.paciente_id_paciente=paci.id_paciente)\n" +
            "inner join doctor d on (c.doctor_id_doctor=d.id_doctor)\n" +
            "inner join especialidad e on (d.especialidad_id_especialidad=e.id_especialidad)\n" +
            "left join sede_x_especialidad_x_administrativo sxexa on (sxexa.especialidad_id_especialidad=e.id_especialidad)\n" +
            "inner join seguro s on (paci.seguro_id_seguro=s.id_seguro)\n" +
            "where s.id_seguro= ?1 " +
            "order by p.fecha_cancelada desc")
    List<AdministradorEgresos> obtenerEgresosPorSeguro(int id);
    @Query(nativeQuery = true, value = "select case when e.es_examen=1 then 'Consulta' else 'Examen' end  as concepto,\n" +
            "\ttruncate(sxexa.precio_cita*s.doctor,2) as pagodoctor, concat(d.nombre,' ',d.apellidos) as nombreuser,\n" +
            "\te.nombre as especialidadcita, s.nombre as nombreseguro,  \n" +
            "\tp.fecha_cancelada as fecha, 'Gastos de personal médico' as categoriagasto\n" +
            "from pago p\n" +
            "inner join cita c on (p.cita_id_cita=c.id_cita)\n" +
            "inner join paciente paci on (c.paciente_id_paciente=paci.id_paciente)\n" +
            "inner join doctor d on (c.doctor_id_doctor=d.id_doctor)\n" +
            "inner join especialidad e on (d.especialidad_id_especialidad=e.id_especialidad)\n" +
            "left join sede_x_especialidad_x_administrativo sxexa on (sxexa.especialidad_id_especialidad=e.id_especialidad)\n" +
            "inner join seguro s on (paci.seguro_id_seguro=s.id_seguro)\n" +
            "where e.id_especialidad=?1 " +
            "order by p.fecha_cancelada desc")
    List<AdministradorEgresos> obtenerEgresosPorEspecialidad(int id);

    @Query(nativeQuery = true,value = "select a.nombre as nombrealergia\n" +
            "from alergias a\n" +
            "where a.paciente_id_paciente = ?1")
    List<AlergiasDto> alergias (String id_paciente);

    @Query(nativeQuery = true,value = "select con.consentimiento as concentimientospaciente\n" +
            "from consentimientos con \n" +
            "inner join paciente_has_consentimientos pc on (pc.consentimientos_id_consentimiento = con.id_consentimiento)\n" +
            "where pc.paciente_id_paciente=?1")
    List<ConsentimPaciDto> consentimientos (String id_paciente );

    @Query(nativeQuery = true, value = "select c.diagnostico as diagnosticopaci,c.receta as recetapaci, c.tratamiento as tratamientopaci\n" +
            "from cita c\n" +
            "where c.paciente_id_paciente = ?1")
    List<TratamientPaciDto> tratamiento (String id_paciente);

    @Query(nativeQuery = true,value = "select c.inicio as iniciocita,c.fin as fincita, concat(d.nombre,' ',d.apellidos) as nombredoctor\n" +
            "from cita c\n" +
            "inner join doctor d on (c.doctor_id_doctor=d.id_doctor)\n" +
            "where c.paciente_id_paciente = ?1")
    List<ProximasCitasDto> proximascitas(String id_paciente );


}

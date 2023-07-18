package com.example.proyectogticsgrupo2.repository;

import com.example.proyectogticsgrupo2.entity.PacientePorConsentimiento;
import com.example.proyectogticsgrupo2.entity.PacientePorConsentimientoID;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PacientePorConsentimientoRepository extends JpaRepository<PacientePorConsentimiento, PacientePorConsentimientoID> {
    List<PacientePorConsentimiento> findByIdIdPaciente(String idPaciente);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update paciente_has_consentimientos \n" +
                                        "set valor = 1 " +
                                        "where paciente_id_paciente = ?1 and consentimientos_id_consentimiento = ?2")
    void actualizarConsentimientoAPositivo(String idPaciente, String idConsentimiento);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "update paciente_has_consentimientos \n" +
            "set valor = 0 " +
            "where paciente_id_paciente = ?1")
    void actualizarConsentimientosANegativo(String idPaciente);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = "insert into paciente_has_consentimientos (paciente_id_paciente, consentimientos_id_consentimiento, valor) values (?1,?2,?3)")
    void cargarConsentimentos(String idPaciente, int idConsent, int valor);

}

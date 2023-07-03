package com.example.proyectogticsgrupo2.repository;
import com.example.proyectogticsgrupo2.entity.FormularioJson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FormularioJsonRepository extends JpaRepository<FormularioJson, Integer> {
    // Aquí puedes agregar métodos personalizados para consultas si los necesitas
    @Query(nativeQuery = true, value = "SELECT * FROM formularios_json WHERE rutaController = ?1")
    FormularioJson findByRutaController(String rutaController);
}

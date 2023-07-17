package com.example.proyectogticsgrupo2.config;

import com.example.proyectogticsgrupo2.entity.Paciente;
import com.example.proyectogticsgrupo2.entity.SuperAdmin;
import com.example.proyectogticsgrupo2.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.security.SecureRandom;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    final DataSource dataSource;
    final PacienteRepository pacienteRepository;
    final DoctorRepository doctorRepository;
    final AdministradorRepository administradorRepository;
    final AdministrativoRepository administrativoRepository;
    final SuperAdminRepository superAdminRepository;


    public SecurityConfig(DataSource dataSource, PacienteRepository pacienteRepository, DoctorRepository doctorRepository, AdministradorRepository administradorRepository, AdministrativoRepository administrativoRepository, SuperAdminRepository superAdminRepository) {
        this.dataSource = dataSource;
        this.pacienteRepository = pacienteRepository;
        this.doctorRepository = doctorRepository;
        this.administradorRepository = administradorRepository;
        this.administrativoRepository = administrativoRepository;
        this.superAdminRepository = superAdminRepository;
    }
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public String generateRandomPassword() {
        int length = 10; // Longitud de la contraseña deseada
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"; // Caracteres disponibles para la contraseña (solo letras mayúsculas, minúsculas y números)
        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/credenciales")
                .successHandler((request, response, authentication) -> {
                    DefaultSavedRequest defaultSavedRequest =
                            (DefaultSavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

                    if (defaultSavedRequest != null) {
                        String targetURL = defaultSavedRequest.getRedirectUrl();
                        redirectStrategy.sendRedirect(request, response, targetURL);
                    } else {
                        String rol = "";
                        HttpSession session = request.getSession();
                        for (GrantedAuthority role : authentication.getAuthorities()) {
                            rol = role.getAuthority();
                            break;
                        }
                        switch (rol) {
                            case "paciente" -> {
                                response.sendRedirect("/Paciente");
                            }
                            case "doctor" -> {
                                session.setAttribute("doctor", doctorRepository.findByCorreo(authentication.getName()));
                                response.sendRedirect("/doctor");
                            }
                            case "administrador" -> {
                                session.setAttribute("administrador", administradorRepository.findByCorreo(authentication.getName()));
                                response.sendRedirect("/administrador/dashboard");
                            }
                            case "administrativo" -> {
                                session.setAttribute("administrativo", administrativoRepository.findByCorreo(authentication.getName()));

                                response.sendRedirect("/administrativo");
                            }
                            case "superadmin" -> {
                                session.setAttribute("superadmin", superAdminRepository.findByCorreo(authentication.getName()));
                                response.sendRedirect("/SuperAdminHomePage");
                            }
                        }
                    }
                });

        http.authorizeHttpRequests()
                .requestMatchers("/Paciente","/Paciente/***").hasAnyAuthority("paciente", "superadmin")
                .requestMatchers("/doctor","/doctor/***").hasAnyAuthority("doctor", "superadmin")
                .requestMatchers("/administrativo","/administrativo/***").hasAnyAuthority("administrativo", "superadmin")
                .requestMatchers("/SuperAdminHomePage","/SuperAdminHomePage/***").hasAuthority("superadmin")

                .requestMatchers("/administrador","/administrador/***").hasAnyAuthority("administrador", "superadmin")

                .requestMatchers("/","/login","/login/**","/signin","/signin/**").anonymous()

                .anyRequest().permitAll();

        http.logout().logoutSuccessUrl("/").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

        http.exceptionHandling()
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                    String rol = auth.getAuthorities().iterator().next().getAuthority();
                    switch (rol) {
                        case "paciente":
                            response.sendRedirect("/Paciente");
                            break;
                        case "doctor":
                            response.sendRedirect("/doctor");
                            break;
                        case "administrativo":
                            response.sendRedirect("/administrativo");
                            break;
                        case "administrador":
                            response.sendRedirect("/administrador/dashboard");
                            break;
                        case "superadmin":
                            response.sendRedirect("/SuperAdminHomePage");
                            break;
                        default:
                            response.sendRedirect("/login");
                            break;
                    }
                })
                /*
                .defaultAuthenticationEntryPointFor(
                new HttpStatusEntryPoint(HttpStatus.NOT_FOUND),
                new AntPathRequestMatcher("/**"))
                */
        ;

        return http.build();
    }
    @Bean
    public UserDetailsManager users(DataSource dataSource){
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        String sql1 = "SELECT\n" +
                "    c.correo,\n" +
                "    c.contrasena_hasheada,\n" +
                "    CASE \n" +
                "\t\tWHEN exists(select estado from paciente where id_paciente=id_credenciales and (estado!=0 or estado!=3)) THEN 1\n" +
                "\t\tWHEN exists(select estado from doctor where id_doctor=id_credenciales and estado!=0) THEN 1\n" +
                "\t\tWHEN exists(select estado from administrativo where id_administrativo=id_credenciales and estado!=0) THEN 1\n" +
                "\t\tWHEN exists(select estado from administrador where id_administrador=id_credenciales and estado!=0) THEN 1\n" +
                "\t\tWHEN exists(select * from superadmin where id_superadmin=id_credenciales) THEN 1\n" +
                "        ELSE 0\n" +
                "\tEND AS estado\n" +
                "\tFROM credenciales c\n" +
                "    WHERE c.correo = ?";
        String sql2 = "SELECT\n" +
                "    c.correo,\n" +
                "    CASE \n" +
                "\t\tWHEN exists(select * from paciente where id_paciente=id_credenciales) THEN 'paciente'\n" +
                "\t\tWHEN exists(select * from doctor where id_doctor=id_credenciales) THEN 'doctor'\n" +
                "\t\tWHEN exists(select * from administrativo where id_administrativo=id_credenciales) THEN 'administrativo'\n" +
                "\t\tWHEN exists(select * from administrador where id_administrador=id_credenciales) THEN 'administrador'\n" +
                "\t\tWHEN exists(select * from superadmin where id_superadmin=id_credenciales) THEN 'superadmin'\n" +
                "\tEND AS rol\n" +
                "\tFROM credenciales c\n" +
                "    WHERE c.correo = ?";

        users.setUsersByUsernameQuery(sql1);
        users.setAuthoritiesByUsernameQuery(sql2);
        return users;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

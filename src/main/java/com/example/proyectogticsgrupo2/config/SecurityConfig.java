package com.example.proyectogticsgrupo2.config;

import com.example.proyectogticsgrupo2.repository.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;

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
                        for (GrantedAuthority role : authentication.getAuthorities()) {
                            rol = role.getAuthority();
                            break;
                        }
                        if (rol.equals("paciente")) {
                            response.sendRedirect("/Paciente");
                        } else if (rol.equals("doctor")) {
                            response.sendRedirect("/doctor");
                        } else if (rol.equals("administrador")) {
                            response.sendRedirect("/administrador/dashboard");
                        } else if (rol.equals("administrativo")) {
                            response.sendRedirect("/administrativo");
                        } else{
                            response.sendRedirect("/SuperAdminHomePage");
                        }
                    }
                });

        http.authorizeHttpRequests()
                .requestMatchers("/Paciente","/Paciente/***").hasAuthority("paciente")
                .requestMatchers("/doctor","/doctor/***").hasAuthority("doctor")
                .requestMatchers("/administrativo","/administrativo/***").hasAuthority("administrativo")
                .requestMatchers("/SuperAdminHomePage","/SuperAdminHomePage/***").hasAuthority("superadmin")
                .requestMatchers("/administrador","/administrador/***").hasAuthority("administrador")
                .anyRequest().permitAll();

        http.logout().logoutSuccessUrl("/").deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
        return http.build();
    }
    @Bean
    public UserDetailsManager users(DataSource dataSource){
        JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
        String sql1 = "call credencial_estado(?)";
        String sql2 = "call correo_rol(?)";

        users.setUsersByUsernameQuery(sql1);
        users.setAuthoritiesByUsernameQuery(sql2);
        return users;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}

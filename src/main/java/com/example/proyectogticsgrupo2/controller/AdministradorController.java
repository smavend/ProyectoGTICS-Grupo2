package com.example.proyectogticsgrupo2.controller;

import com.example.proyectogticsgrupo2.config.SecurityConfig;
import com.example.proyectogticsgrupo2.dto.AdministradorIngresos;
import com.example.proyectogticsgrupo2.entity.*;
import com.example.proyectogticsgrupo2.metodos.ReporteExcel;
import com.example.proyectogticsgrupo2.repository.*;
import com.example.proyectogticsgrupo2.service.CorreoNuevoPaciente;
import com.example.proyectogticsgrupo2.service.CorreoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import jakarta.validation.Valid;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

    final PacienteRepository pacienteRepository;
    final DoctorRepository doctorRepository;
    final SeguroRepository seguroRepository;
    final AdministrativoRepository administrativoRepository;
    final DistritoRepository distritoRepository;
    final EspecialidadRepository especialidadRepository;
    final SedeRepository sedeRepository;
    final AdministradorRepository administradorRepository;
    final CredencialesRepository credencialesRepository;
    final TemporalRepository temporalRepository;
    final SecurityConfig securityConfig;
    final PacientePorConsentimientoRepository ppcRepository;

    final StylevistasRepository stylevistasRepository;


    public AdministradorController(PacienteRepository pacienteRepository, DoctorRepository doctorRepository, SeguroRepository seguroRepository, AdministrativoRepository administrativoRepository, DistritoRepository distritoRepository, EspecialidadRepository especialidadRepository, SedeRepository sedeRepository, AdministradorRepository administradorRepository, CredencialesRepository credencialesRepository, TemporalRepository temporalRepository, SecurityConfig securityConfig, PacientePorConsentimientoRepository ppcRepository, StylevistasRepository stylevistasRepository) {

        this.pacienteRepository = pacienteRepository;
        this.doctorRepository = doctorRepository;
        this.seguroRepository = seguroRepository;
        this.administrativoRepository = administrativoRepository;
        this.distritoRepository = distritoRepository;
        this.especialidadRepository = especialidadRepository;
        this.sedeRepository = sedeRepository;
        this.administradorRepository = administradorRepository;
        this.credencialesRepository = credencialesRepository;
        this.temporalRepository = temporalRepository;
        this.securityConfig = securityConfig;
        this.ppcRepository = ppcRepository;
        this.stylevistasRepository = stylevistasRepository;
    }
    //#####################################33
   //Comentado por Gustavo
    /* @GetMapping("/dashboard")
    public String dashboard (Model model){
        List<Paciente> listaPaciente =pacienteRepository.findAll();
        List<Doctor> listaDoctores = doctorRepository.findAll();
        model.addAttribute("listaDoctores",listaDoctores);
        model.addAttribute("listaPaciente", listaPaciente);

        Optional<Stylevistas> style = stylevistasRepository.findById(2);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
<<<<<<< HEAD
            model.addAttribute("headerColorAdministrador", styleActual.getHeader());
            *//*model.addAttribute("sidebarColor", styleActual.getSidebar());*//*
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        return "administrador/dashboard";
    }*/
    @GetMapping("/dashboard")
    public String dashboard (Model model, HttpSession session, Authentication authentication){
        // Check if superadmin is logged in as administrador
        Boolean superAdminLogueadoComoAdministrador = (Boolean) session.getAttribute("superAdminLogueadoComoAdministrador");
        if (superAdminLogueadoComoAdministrador == null) {
            superAdminLogueadoComoAdministrador = false;
        }
        model.addAttribute("superAdminLogueadoComoAdministrador", superAdminLogueadoComoAdministrador);

        Administrador administrador;

        // Obtener el correo electrónico del administrador a "impersonar" desde la sesión
        String impersonatedUser = (String) session.getAttribute("impersonatedUser");
        if (impersonatedUser != null) {
            // Si hay un usuario "impersonado", buscar al administrador por ese correo electrónico
            administrador = administradorRepository.findByCorreo(impersonatedUser);
        } else {
            administrador = administradorRepository.findByCorreo(authentication.getName());
            if (administrador == null) {
                return "redirect:/error";
            }
        }

        session.setAttribute("administrador", administrador);

        List<Paciente> listaPaciente =pacienteRepository.findAll();
        List<Doctor> listaDoctores = doctorRepository.findAll();
        model.addAttribute("listaDoctores",listaDoctores);
        model.addAttribute("listaPaciente", listaPaciente);

        Optional<Stylevistas> style = stylevistasRepository.findById(2);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorAdministrador", styleActual.getHeader());
            /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        return "administrador/dashboard";
    }
    @GetMapping("/finanzas")
    public String finanzas(Model model){
        List<AdministradorIngresos> listaIngresos = administradorRepository.obtenerIgresos();
        model.addAttribute("listaIngresos",listaIngresos);
        //###########################################################
        model.addAttribute("listaSeguros", seguroRepository.findAll());
        model.addAttribute("listaEspecialidades", especialidadRepository.findAll());
        Optional<Stylevistas> style = stylevistasRepository.findById(2);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            model.addAttribute("headerColorAdministrador", styleActual.getHeader());
            /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        return "administrador/finanzas";}

    @RequestMapping("/reportes")
    public ResponseEntity<Resource> generarReportes(@RequestParam("tiporeporte") String tiporeporte, @RequestParam("tipopago") String tipopago,
                                                    @RequestParam("seguro") String seguro, @RequestParam("especialidad") String especialidad,
                                                    @RequestParam("todo") String todo, @RequestParam("formato") String formato){


        ReporteExcel reporte = new ReporteExcel();
        switch (tiporeporte){
            case "5":
                if (todo.isEmpty())  {
                    // Al menos uno de los campos está vacío, realiza alguna acción de manejo de errores o retorna una respuesta adecuada.
                    return ResponseEntity.badRequest().build();
                }else {
                    List<AdministradorIngresos> lista = administradorRepository.obtenerIgresos();
                    switch (formato){
                        case "1":
                            ResponseEntity<Resource> responseExcel = reporte.generarInformeIngresosExcel(lista,"ReporteGeneral");
                            return responseExcel;
                        case "2":
                            ResponseEntity<Resource> responsePdf = reporte.generateIncomeReportPDF(lista,"ReporteGeneral");
                            return responsePdf;
                    }
                }
            case "1":
                if (seguro.isEmpty())  {
                    // Al menos uno de los campos está vacío, realiza alguna acción de manejo de errores o retorna una respuesta adecuada.
                    return ResponseEntity.badRequest().build();
                }else {
                    List<AdministradorIngresos> listaIngresosPorSeguro = administradorRepository.obtenerIgresosPorSeguro(Integer.parseInt(seguro));
                    switch (formato){
                        case "1":
                            ResponseEntity<Resource> responseExcel = reporte.generarInformeIngresosExcel(listaIngresosPorSeguro,"ReporteIngresosPorSeguro");
                            return responseExcel;
                        case "2":
                            ResponseEntity<Resource> responsePdf = reporte.generateIncomeReportPDF(listaIngresosPorSeguro,"ReporteIngresosPorSeguro");
                            return responsePdf;
                    }
                }


            case "2":
                if (especialidad.isEmpty())  {
                    // Al menos uno de los campos está vacío, realiza alguna acción de manejo de errores o retorna una respuesta adecuada.
                    return ResponseEntity.badRequest().build();
                }else {
                    List<AdministradorIngresos> listaIngresosPorEspecialidad = administradorRepository.obtenerIgresosPorEspecialidad(Integer.parseInt(especialidad));
                    switch (formato){
                        case "1":
                            ResponseEntity<Resource> responseExcel = reporte.generarInformeIngresosExcel(listaIngresosPorEspecialidad,"ReporteIngresosEspecialidad");
                            return responseExcel;
                        case "2":
                            ResponseEntity<Resource> responsePdf = reporte.generateIncomeReportPDF(listaIngresosPorEspecialidad,"ReporteIngresosEspecialidad");
                            return responsePdf;
                    }
                }
            case "3":
                if (tipopago.isEmpty())  {
                    // Al menos uno de los campos está vacío, realiza alguna acción de manejo de errores o retorna una respuesta adecuada.
                    return ResponseEntity.badRequest().build();
                }else {
                    List<AdministradorIngresos> listaIngresosTipoPago = administradorRepository.obtenerIgresosPorTipoPago(tipopago);
                    switch (formato){
                        case "1":
                            ResponseEntity<Resource> responseExcel = reporte.generarInformeIngresosExcel(listaIngresosTipoPago,"ReporteIngresosTipoPago");
                            return responseExcel;
                        case "2":
                            ResponseEntity<Resource> responsePdf = reporte.generateIncomeReportPDF(listaIngresosTipoPago,"ReporteIngresosTipoPago");
                            return responsePdf;
                    }
                }


            default:
                return null;
        }
    }
    @GetMapping("/config")
    public String config(Model model){
        Optional<Stylevistas> style = stylevistasRepository.findById(2);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            model.addAttribute("headerColorAdministrador", styleActual.getHeader());
            /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        return "administrador/config";
    }
    @GetMapping("/registro")
    public String registro(Model model){
        List<Temporal> listaTemporal = temporalRepository.listatemporalesLlenados();
        model.addAttribute("listaTemporal",listaTemporal);
        Optional<Stylevistas> style = stylevistasRepository.findById(2);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            model.addAttribute("headerColorAdministrador", styleActual.getHeader());
            /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        return "administrador/rptaForm";}
    @PostMapping("/guardarTemporales")
    public String guardarTemporales(HttpServletRequest request, @RequestParam("usuarios") List<Integer> ids, Paciente paciente, RedirectAttributes attr) throws UnknownHostException {
        List<Temporal> pacientesTemp = temporalRepository.findAllById(ids);
            //Cuanto funcione perfectamente los temporales, entonces los filtro por llenado 1
            // y usare el datablindig
            for (Temporal pacitemp : pacientesTemp){
                paciente.setIdPaciente(pacitemp.getDni());
                paciente.setNombre(pacitemp.getNombre());
                paciente.setApellidos(pacitemp.getApellidos());
                paciente.setTelefono(pacitemp.getTelefono());
                paciente.setDireccion(pacitemp.getDireccion());
                paciente.setFechanacimiento(pacitemp.getFecha_nacimiento());
                paciente.setSeguro(pacitemp.getSeguro());
                paciente.setDistrito(pacitemp.getDistrito());
                paciente.setGenero(pacitemp.getGenero());
                paciente.setCorreo(pacitemp.getCorreo());
                paciente.setEstado(1);
                paciente.setFecharegistro(LocalDateTime.now());
                paciente.setFechainvitado(pacitemp.getFechainvitado());
                paciente.setFoto(null);
                paciente.setFotoname(null);
                paciente.setFotocontenttype(null);
                pacienteRepository.save(paciente);
                temporalRepository.deleteById(pacitemp.getId_temporal());

                ppcRepository.cargarConsentimentos(paciente.getIdPaciente(), 1,1);
                ppcRepository.cargarConsentimentos(paciente.getIdPaciente(), 2,1);
                ppcRepository.cargarConsentimentos(paciente.getIdPaciente(), 3,1);
                ppcRepository.cargarConsentimentos(paciente.getIdPaciente(), 4,1);
                ppcRepository.cargarConsentimentos(paciente.getIdPaciente(), 5,1);

                String passRandom= securityConfig.generateRandomPassword();
                PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
                // Ahora puedes usar el passwordEncoder para codificar una contraseña
                String encodedPassword = passwordEncoder.encode(passRandom);
                credencialesRepository.crearCredenciales(paciente.getIdPaciente(),paciente.getCorreo(),encodedPassword);
                CorreoNuevoPaciente correoNuevoPaciente= new CorreoNuevoPaciente();

                InetAddress address = InetAddress.getLocalHost();
                byte[] bIPAddress = address.getAddress();
                String sIPAddress = "";
                for (int i = 0; i < bIPAddress.length; i++){
                    if (i>0) {
                        sIPAddress += ".";
                    }
                    int unsignedByte = bIPAddress[i] & 0xFF;
                    sIPAddress += unsignedByte;
                }
                String link = request.getServerName()+":"+request.getLocalPort();

                correoNuevoPaciente.props(paciente.getCorreo(),passRandom, link);
                attr.addFlashAttribute("msgPaci","Pacientes creados exitosamente");

            }

            return "redirect:/administrador/dashboard";

    }
    @GetMapping("/perfil")
    public String perfil(Model model, HttpServletRequest request){
        HttpSession session = request.getSession();
        Administrador administrador = (Administrador) session.getAttribute("administrador");
        model.addAttribute("administrador", administrador);
        return "administrador/perfil";}
    @GetMapping("/finanzas-recibos")
    public String finanzas_recibos(){return "administrador/finanzas-recibos";}
    //###########################################################################
    @GetMapping("/crearPaciente")
    public String crearPaciente( @ModelAttribute("paciente") Paciente paciente,  Model model){
        List<Seguro> listaSeguro  = seguroRepository.findAll();
        List<Distrito> listaDistrito = distritoRepository.findAll();
        List<Administrativo> listaAdministrativo = administrativoRepository.findAll();
        model.addAttribute("listaSeguro",listaSeguro);
        model.addAttribute("listaDistrito",listaDistrito);
        model.addAttribute("listaAdministrativo",listaAdministrativo);
        Optional<Stylevistas> style = stylevistasRepository.findById(2);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            model.addAttribute("headerColorAdministrador", styleActual.getHeader());
            /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        return "administrador/crearPaciente";}
    @PostMapping("/guardarPaciente")
    public String guardarEmpleado(HttpServletRequest request,
                                  @RequestParam("archivo") MultipartFile file,
                                  @ModelAttribute("paciente") @Valid Paciente paciente, BindingResult bindingResult,
                                  Model model, RedirectAttributes attr) throws UnknownHostException {
        Optional<Stylevistas> style = stylevistasRepository.findById(2);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();

            model.addAttribute("headerColorAdministrador", styleActual.getHeader());
            /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        Optional<Paciente> opt = pacienteRepository.findById(paciente.getIdPaciente());
        Paciente pacienteCorreoExist = pacienteRepository.findByCorreo(paciente.getCorreo());
        if(bindingResult.hasErrors() || opt.isPresent() || pacienteCorreoExist!=null ||
                paciente.getSeguro()==null || paciente.getDistrito()==null || paciente.getFechanacimiento()==null){
            if(opt.isPresent()){
                bindingResult.rejectValue("idPaciente","errorDoctor","Este DNI ya se encuentra registrado");

            }
            if (pacienteCorreoExist!=null) {
                bindingResult.rejectValue("correo","errorCorreoDoc","Este correo ya se encuentra registrado");
            }
            if (paciente.getSeguro()==null) {
                bindingResult.rejectValue("seguro","erroresseguro","Seleccione un seguro");
            }
            if (paciente.getDistrito()==null) {
                bindingResult.rejectValue("distrito","erroresdistrito","Seleccione un distrito");
            }
            if(paciente.getFechanacimiento()==null){
                bindingResult.rejectValue("fechanacimiento","erroresdistrito","");

            }
            List<Seguro> listaSeguro  = seguroRepository.findAll();
            List<Distrito> listaDistrito = distritoRepository.findAll();
            List<Administrativo> listaAdministrativo = administrativoRepository.findAll();
            model.addAttribute("listaSeguro",listaSeguro);
            model.addAttribute("listaDistrito",listaDistrito);
            model.addAttribute("listaAdministrativo",listaAdministrativo);
            return "administrador/crearPaciente";
        } else{
            if (file.isEmpty()) {
                    paciente.setFoto(null);
                    paciente.setFotoname(null);
                    paciente.setFotocontenttype(null);

            }else{
                String fileName = file.getOriginalFilename();
                try {
                    paciente.setFoto(file.getBytes());
                    paciente.setFotoname(fileName);
                    paciente.setFotocontenttype(file.getContentType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            paciente.setEstado(1);
            paciente.setFecharegistro(LocalDateTime.now());
            pacienteRepository.save(paciente);
            String passRandom= securityConfig.generateRandomPassword();
            PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
            // Ahora puedes usar el passwordEncoder para codificar una contraseña
            String encodedPassword = passwordEncoder.encode(passRandom);
            credencialesRepository.crearCredenciales(paciente.getIdPaciente(),paciente.getCorreo(),encodedPassword);
            CorreoNuevoPaciente correoNuevoPaciente = new CorreoNuevoPaciente();

            InetAddress address = InetAddress.getLocalHost();
            byte[] bIPAddress = address.getAddress();
            String sIPAddress = "";
            for (int i = 0; i < bIPAddress.length; i++){
                if (i>0) {
                    sIPAddress += ".";
                }
                int unsignedByte = bIPAddress[i] & 0xFF;
                sIPAddress += unsignedByte;
            }
            String link = request.getServerName()+":"+request.getLocalPort();

            correoNuevoPaciente.props(paciente.getCorreo(),passRandom, link);
            attr.addFlashAttribute("msgPaci","El paciente "+ paciente.getNombre()+' '+paciente.getApellidos()+" creado exitosamente");
            return "redirect:/administrador/dashboard";
        }
    }
    //###########################################################################
    @GetMapping("/crearDoctor")
    public String crearDoctor(@ModelAttribute("doctor") Doctor doctor,Model model){
        List<Especialidad> listaEspecialidad = especialidadRepository.findAll();
        List<Sede> listaSede = sedeRepository.findAll();
        model.addAttribute("listaSede",listaSede);
        model.addAttribute("listaEspecialidad",listaEspecialidad);
        Optional<Stylevistas> style = stylevistasRepository.findById(2);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            model.addAttribute("headerColorAdministrador", styleActual.getHeader());
            /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        return "administrador/crearDoctor";}
    @PostMapping("/guardarDoctor")
    public String guardarDoctor(HttpServletRequest request,
                                @RequestParam("archivo") MultipartFile file,
                                @ModelAttribute("doctor") @Valid Doctor doctor, BindingResult bindingResult,
                                Model model, RedirectAttributes attr) throws UnknownHostException {
        Optional<Doctor> opt = doctorRepository.findById(doctor.getId_doctor());
        Doctor doctorCorreoExist = doctorRepository.findByCorreo(doctor.getCorreo());

        if(bindingResult.hasErrors() || opt.isPresent() || doctorCorreoExist!=null ||
                doctor.getEspecialidad()==null || doctor.getSede()==null){
            if(opt.isPresent()){
                bindingResult.rejectValue("id_doctor","errorDoctor","Este DNI ya se encuentra registrado");
                bindingResult.rejectValue("correo","errorCorreoDoc","Este correo ya se encuentra registrado");
            }
            if (doctorCorreoExist!=null) {
                bindingResult.rejectValue("correo","errorCorreoDoc","Este correo ya se encuentra registrado");
            }
            if (doctor.getSede()==null) {
                bindingResult.rejectValue("sede","errorespecialidad","Seleccione una sede");
            }
            if (doctor.getEspecialidad()==null) {
                bindingResult.rejectValue("especialidad","errorespecialidad","Seleccione una especialidad");
            }
            List<Especialidad> listaEspecialidad = especialidadRepository.findAll();
            List<Sede> listaSede = sedeRepository.findAll();
            model.addAttribute("listaSede",listaSede);
            model.addAttribute("listaEspecialidad",listaEspecialidad);
            return "administrador/crearDoctor";
        }else {
            if(file.isEmpty()){
                doctor.setFoto(null);
                doctor.setFotoname(null);
                doctor.setFotocontenttype(null);
            }else {
                String fileName = file.getOriginalFilename();
                try {
                    doctor.setFoto(file.getBytes());
                    doctor.setFotoname(fileName);
                    doctor.setFotocontenttype(file.getContentType());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            doctor.setEstado(1);
            doctorRepository.save(doctor);

            String passRandom= securityConfig.generateRandomPassword();
            PasswordEncoder passwordEncoder = securityConfig.passwordEncoder();
            // Ahora puedes usar el passwordEncoder para codificar una contraseña
            String encodedPassword = passwordEncoder.encode(passRandom);

            credencialesRepository.crearCredenciales(doctor.getId_doctor(),doctor.getCorreo(),encodedPassword);
            CorreoService correoService = new CorreoService();

            InetAddress address = InetAddress.getLocalHost();
            String domain = request.getServerName();
            byte[] bIPAddress = address.getAddress();
            String sIPAddress = "";
            for (int i = 0; i < bIPAddress.length; i++){
                if (i>0) {
                    sIPAddress += ".";
                }
                int unsignedByte = bIPAddress[i] & 0xFF;
                sIPAddress += unsignedByte;
            }
            String link = request.getServerName()+":"+request.getLocalPort();
            System.out.println(link);
            System.out.println("servername:"+domain);
            correoService.props(doctor.getCorreo(),passRandom, link);
            attr.addFlashAttribute("msgDoc","El doctor "+ doctor.getNombre()+' '+doctor.getApellidos()+" creado exitosamente");
            Optional<Stylevistas> style = stylevistasRepository.findById(2);
            if (style.isPresent()) {
                Stylevistas styleActual = style.get();

                model.addAttribute("headerColorAdministrador", styleActual.getHeader());
                /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
            } else {
                // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
            }
            return "redirect:/administrador/dashboard";
        }
    }
    @GetMapping("/calendario")
    public String calendario(Model model){
        Optional<Stylevistas> style = stylevistasRepository.findById(2);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            model.addAttribute("headerColorAdministrador", styleActual.getHeader());
            /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        return "administrador/calendario";}
    @GetMapping("/mensajeria")
    public String mensajeria(Model model){
        //En onstruccion
        Optional<Stylevistas> style = stylevistasRepository.findById(2);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            model.addAttribute("headerColorAdministrador", styleActual.getHeader());
            /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }
        return "administrador/mensajeria";}
    @GetMapping("/historialPaciente")
    public String historialPaciente(@RequestParam("id") String id, Model model){
        Optional<Stylevistas> style = stylevistasRepository.findById(2);
        if (style.isPresent()) {
            Stylevistas styleActual = style.get();
            model.addAttribute("headerColorAdministrador", styleActual.getHeader());
            /*model.addAttribute("sidebarColor", styleActual.getSidebar());*/
        } else {
            // Puedes manejar aquí el caso en que no se encuentra el 'stylevistas'
        }

        Optional<Paciente> optPaciente = pacienteRepository.findById(id);
        if(optPaciente.isPresent()){
            Paciente paciente = optPaciente.get();
            model.addAttribute("paciente", paciente);
            model.addAttribute("alergias",administradorRepository.alergias(paciente.getIdPaciente()));
            model.addAttribute("consentimientos",administradorRepository.consentimientos(paciente.getIdPaciente()));
            model.addAttribute("tratamiento",administradorRepository.tratamiento(paciente.getIdPaciente()));
            model.addAttribute("prximascitas",administradorRepository.proximascitas(paciente.getIdPaciente()));
            return "administrador/historialPaciente";
        }else {
            return "redirect:/administrador/dashboard";
        }

    }

    @GetMapping("/imagePaci/{id}")
    public ResponseEntity<byte[]> mostrarImagen(@PathVariable("id") String dni) {
        Optional<Paciente> opt = pacienteRepository.findById(dni);

        if (opt.isPresent()) {
            Paciente p = opt.get();

            byte[] imagenComoBytes = p.getFoto();
            /*//agregue desde aca
            if(imagenComoBytes==null){
                try {
                    File foto = new File("source/userPorDefecto.jpg");
                    FileInputStream input = new FileInputStream(foto);
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) !=-1){
                        output.write(buffer,0,length);
                    }
                    input.close();;
                    output.close();
                    byte[] bytes = output.toByteArray();
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(
                            MediaType.parseMediaType("image/jpg"));

                    return new ResponseEntity<>(
                            bytes,
                            httpHeaders,
                            HttpStatus.OK);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }else {

            } //agregue hasta aca*/

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(p.getFotocontenttype()));

            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }
    @GetMapping("/invitacion")
    public String invitacion (){
        return "administrador/invitar";
    }
    @GetMapping("/imageDoc/{id}")
    public ResponseEntity<byte[]> mostrarImagenDoc(@PathVariable("id") String dni) {
        Optional<Doctor> opt = doctorRepository.findById(dni);

        if (opt.isPresent()) {
            Doctor doc = opt.get();

            byte[] imagenComoBytes = doc.getFoto();
            //agregue desde aca
            /*if(imagenComoBytes==null){
                try {
                    File foto = new File("src/main/resources/static/assets/img/userPorDefecto.jpg");
                    FileInputStream input = new FileInputStream(foto);
                    ByteArrayOutputStream output = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = input.read(buffer)) !=-1){
                        output.write(buffer,0,length);
                    }
                    input.close();;
                    output.close();
                    byte[] bytes = output.toByteArray();
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.setContentType(
                            MediaType.parseMediaType("image/jpg"));

                    return new ResponseEntity<>(
                            bytes,
                            httpHeaders,
                            HttpStatus.OK);

                }catch (IOException e){
                    e.printStackTrace();
                }
            }else {

            } //agregue hasta aca*/

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(
                    MediaType.parseMediaType(doc.getFotocontenttype()));

            return new ResponseEntity<>(
                    imagenComoBytes,
                    httpHeaders,
                    HttpStatus.OK);
        } else {
            return null;
        }
    }

    //###############################3



}

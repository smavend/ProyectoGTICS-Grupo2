const darkModeSwitch = document.querySelector('#darkModeSwitch');
const body = document.body;
const main = document.querySelector('main');
const aside = document.querySelector('aside');
const  cardbody = document.querySelector('.card-body');
// Verificar si el modo oscuro está habilitado en el almacenamiento local
const isDarkMode = localStorage.getItem('darkMode') === 'true';

// Si el modo oscuro está habilitado, activa el interruptor y establece las clases de fondo y texto oscuro en el cuerpo, el contenido principal y el contenido lateral
if (isDarkMode) {
    darkModeSwitch.checked = true;
    body.classList.add('bg-dark');
    body.classList.add('text-light');
    main.classList.add('bg-darks');
    main.classList.add('text-light');
    aside.style.backgroundColor = 'var(--bs-secondary-bg)';
    aside.classList.add('text-light');
    cardbody.classList.add('bg-dark');
    cardbody.classList.add('text-light');
}

// Agregar un evento de escucha al interruptor
darkModeSwitch.addEventListener('change', () => {
    if (darkModeSwitch.checked) {
        // Si el interruptor está activado, habilita el modo oscuro y establece las clases de fondo y texto oscuro en el cuerpo, el contenido principal y el contenido lateral
        body.classList.add('bg-dark');
        body.classList.add('text-light');
        main.classList.add('bg-dark');
        main.classList.add('text-light');
        aside.style.backgroundColor = 'var(--bs-secondary-bg)';
        aside.classList.add('text-light');
        cardbody.classList.add('bg-dark');
        cardbody.classList.add('text-light');
        localStorage.setItem('darkMode', 'true');
    } else {
        // Si el interruptor está desactivado, desactiva el modo oscuro y elimina las clases de fondo y texto oscuro del cuerpo, el contenido principal y el contenido lateral
        body.classList.remove('bg-dark');
        body.classList.remove('text-light');
        main.classList.remove('bg-dark');
        main.classList.remove('text-light');
        aside.style.backgroundColor = ''; // remove the custom background color
        aside.classList.remove('text-light');
        cardbody.classList.remove('bg-dark');
        cardbody.classList.remove('text-light');
        localStorage.setItem('darkMode', 'false');
    }
});
		
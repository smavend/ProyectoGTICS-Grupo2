const darkModeSwitch = document.querySelector('#darkModeSwitch');
const body = document.body;
const main = document.querySelector('main');
const aside = document.querySelector('aside');
const cards = document.querySelectorAll('.card');
const navLinks = document.querySelectorAll('.sidebar-nav .nav-link.collapsed');
const tables = document.querySelectorAll('.table');
const tableHeaders = document.querySelectorAll('.dashboard .top-selling .table thead');

// Verificar si el modo oscuro está habilitado en el almacenamiento local
const isDarkMode = localStorage.getItem('darkMode') === 'true';

// Función para aplicar el estilo oscuro a los elementos
function applyDarkMode() {
    body.style.backgroundColor = 'var(--bs-gray-dark)';
    body.classList.add('text-light');
    main.style.backgroundColor = 'var(--bs-secondary-bg)';
    main.classList.add('text-light');
    aside.classList.add('bg-dark');
    aside.classList.add('text-light');

    cards.forEach((card) => {
        card.classList.add('bg-dark');
        card.classList.add('text-light');
    });

    navLinks.forEach((link) => {
        link.style.backgroundColor = 'var(--bs-gray-700)';
        link.style.color = 'var(--bs-white)';
    });
    tables.forEach((table) => {
        table.classList.add('text-light');
    });
    tableHeaders.forEach((header) => {
        header.style.backgroundColor = 'var(--bs-gray-dark)';
    });

    localStorage.setItem('darkMode', 'true');
}

// Función para desactivar el modo oscuro y restaurar los estilos originales
function disableDarkMode() {
    body.style.backgroundColor = '';
    body.classList.remove('text-light');
    main.style.backgroundColor = '';
    main.classList.remove('text-light');
    aside.classList.remove('bg-dark');
    aside.classList.remove('text-light');

    cards.forEach((card) => {
        card.classList.remove('bg-dark');
        card.classList.remove('text-light');
    });

    navLinks.forEach((link) => {
        link.style.backgroundColor = '';
        link.style.color = '';

    });
    tables.forEach((table) => {
        table.classList.remove('text-light');
    });
    tableHeaders.forEach((header) => {
        header.style.backgroundColor = '';
    });

    localStorage.setItem('darkMode', 'false');
}

// Si el modo oscuro está habilitado, activa el interruptor y aplica los estilos oscuros
if (isDarkMode) {
    darkModeSwitch.checked = true;
    applyDarkMode();
}

// Agregar un evento de escucha al interruptor
darkModeSwitch.addEventListener('change', () => {
    if (darkModeSwitch.checked) {
        // Si el interruptor está activado, habilita el modo oscuro
        applyDarkMode();
    } else {
        // Si el interruptor está desactivado, desactiva el modo oscuro
        disableDarkMode();
    }
});

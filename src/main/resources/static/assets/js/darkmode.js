window.addEventListener('load', (event) => {
    if(localStorage.getItem('dark-mode') === 'true') {
        document.body.classList.add('dark-mode');
        document.getElementById('dark-mode-button').checked = true;
    } else {
        document.body.classList.add('light-mode');
    }
});

document.getElementById('dark-mode-button').addEventListener('change', function() {
    if(this.checked) {
        localStorage.setItem('dark-mode', 'true');
        document.body.classList.remove('light-mode');
        document.body.classList.add('dark-mode');
    } else {
        localStorage.setItem('dark-mode', 'false');
        document.body.classList.remove('dark-mode');
        document.body.classList.add('light-mode');
    }
});

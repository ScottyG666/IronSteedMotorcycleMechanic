var eyeIcon = document.querySelector('.fa-eye');

eyeIcon.addEventListener('click', () => {
    if (eyeIcon.classList.contains('fa-eye')) {
        eyeIcon.classList.replace('fa-eye', 'fa-eye-slash');
        document.querySelector("#password").type = 'text';
    } else {
        eyeIcon.classList.replace('fa-eye-slash', 'fa-eye');
        document.querySelector("#password").type = 'password';
    }
});





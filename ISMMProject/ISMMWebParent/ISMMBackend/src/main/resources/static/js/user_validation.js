
var emailTextBox = document.querySelector("#email-validation");

emailTextBox.addEventListener('blur' , () => {
    var userEmail = {
        'username' : emailTextBox.value
    }
})

async function checkIfUserExists (user) 
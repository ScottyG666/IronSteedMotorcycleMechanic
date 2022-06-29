
var emailTextBox = document.querySelector("#email-validation");

emailTextBox.addEventListener('blur' , () => {
    var userEmail = {
        'username' : emailTextBox.value
    }
    checkIfUserExists(userEmail)
})

async function checkIfUserExists (user) {
    
}
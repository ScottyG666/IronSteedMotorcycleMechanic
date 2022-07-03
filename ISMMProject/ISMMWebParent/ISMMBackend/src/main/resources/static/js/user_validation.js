var emailTextBox = document.querySelector("#email-validation");


emailTextBox.addEventListener('blur' , () => {
    var userEmail = {
        'email' : emailTextBox.value
    }
    checkIfUserExists(userEmail)
})

let csrfValue = document.querySelectorAll('input[name=csrf_token]');
function checkIfUserExists (user) {
    fetch('/ISMMAdmin/users/validate-User', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			"X-CSRF-Token": csrfValue
		},
		body: JSON.stringify(user)
	}).then( (response) => response.json())
	  .then((data) => {
		if(data === 'OK') {
			console.log('its true bitch')
		} else {
			console.log(data)
			console.log(csrfValue)
		}
	  })
}
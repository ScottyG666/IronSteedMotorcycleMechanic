var emailTextBox = document.querySelector("#email-validation");


emailTextBox.addEventListener('blur' , () => {
    var userEmail = {
        'email' : emailTextBox.value
    }
    checkIfUserExists(userEmail)
})


function checkIfUserExists (user) {
    fetch('/ISMMAdmin/users/validate-User', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(user)
	}).then( (response) => response.json())
	  .then((data) => {
		if(data === 'OK') {
			console.log('its true bitch')
		} else {
			console.log(data)
		}
	  })
/*
	let asyncResponse = await asyncResponse.json();

	if (asyncResponse === true) {
		emailTextBox.style.transition = '0.5s';
		emailTextBox.style.boxShadow = '0 0 8px 3px red;';
	} else {
		emailTextBox.style.transition = '0.5s';
		emailTextBox.style.boxShadow = 'none;';
	}
	*/
}
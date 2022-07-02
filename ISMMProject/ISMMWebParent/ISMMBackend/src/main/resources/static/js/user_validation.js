

emailTextBox.addEventListener('blur' , () => {
	var emailTextBox = document.querySelector("#email-validation");
    var userEmail = {
        'email' : emailTextBox.value
    }
    checkIfUserExists(userEmail)
})


async function checkIfUserExists (user) {
    fetch('/users/check_email', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json'
		},
		body: JSON.stringify(user)
	}).then( response => response.json())
	  .then(data => {
		console.log(data.body)
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
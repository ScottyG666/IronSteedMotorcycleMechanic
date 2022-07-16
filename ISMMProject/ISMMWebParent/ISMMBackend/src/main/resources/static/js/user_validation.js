var emailInputBox =  document.querySelector('#emailInput');

emailInputBox.addEventListener('blur' , () => {
	var CSRFToken = document.querySelector('input[name$="_csrf"]').value;
	var userEmail = emailInputBox.value;

	var user = {
		"email" : userEmail

	}

	fetch('/ISMMAdmin/users/check_email' , {
		method : 'POST' ,
		headers : {
			'Content-Type' : 'application/json' ,
			'X-CSRF-Token' : CSRFToken
		},
		body : JSON.stringify(user)
	})
	.then( (responseEntity) => responseEntity.text())
	.then( (data) => {
		if (data == 'OK') {
			console.log('its kosher')
		} else if (data == 'Duplicated') {
			console.log("Duplicated email!")
		}
	})
})




/*
function checkIfEmailUnique () {
	var CSRFToken = document.querySelector('input[name$="_csrf"]').value;
	var userEmail = emailInputBox.value;

	console.log(userEmail)

	var user = {
		"email" : userEmail
	}

	fetch('/ISMMAdmin/users/check_email' , {
		method : 'POST' ,
		headers : {
			'Content-Type' : 'application/json' ,
			'X-CSRF-Token' : CSRFToken
		},
		body : JSON.stringify(user)
	})
	.then((responseEntity) => responseEntity.json())
	.then( (responseData) => {
		responseData
	})
}


*/















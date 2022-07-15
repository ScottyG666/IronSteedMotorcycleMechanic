var emailInputBox =  document.querySelector('#emailInput');

emailInputBox.addEventListener('blur' , checkIfEmailUnique)

function checkIfEmailUnique () {
	var CSRFToken = document.querySelector('input[name$="_csrf"]').value;
	var userEmail = document.querySelector('#emailInput').value;

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
		console.log(responseData)
	})
}


















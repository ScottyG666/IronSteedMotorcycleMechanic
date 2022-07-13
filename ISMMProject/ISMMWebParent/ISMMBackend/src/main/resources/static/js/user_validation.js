var emailInputBox =  document.querySelector('#emailInput');

emailInputBox.addEventListener('blur' , checkIfEmailUnique)

function checkIfEmailUnique () {
	var CSRFToken = document.querySelectorAll('input[name$="value"]').value;
	var user = emailInputBox.value;

	fetch('/ISMMAdmin/users/check_email' , {
		method : 'POST' ,
		headers : {
			'Content-Type' : 'application/json' ,
			'X-XSRF-TOKEN' : CSRFToken
		},
		body : JSON.stringify(user)
	})
	.then((responseEntity) => responseEntity.json)
	.then( (responseData) => {
		alert(responseData)
	})
}


















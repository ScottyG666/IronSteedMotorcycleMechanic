var emailInputBox =  document.querySelector('#emailInput');

emailInputBox.addEventListener('blur' , () => {
	var CSRFToken = document.querySelector('input[name$="_csrf"]').value;
	var userEmail = emailInputBox.value;
	var userId = document.querySelector('input[name$="id"]').value;

	var user = {
		"id" : userId,
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
			emailInputBox.className = 'form-control'
		} else if (data == 'Duplicated') {
			emailInputBox.className = 'form-control shadow-danger-custom';
			alert('The email ' + userEmail + ' is already in use by another user.')
		}
	})
})



var form = document.querySelector('form');
form.onsubmit = (e) => {
	e.preventDefault;
	//preventSubmission();

}

function preventSubmission() {
    var CSRFToken = document.querySelector('input[name$="_csrf"]').value;
	var userEmail = emailInputBox.value;
	var userId = document.querySelector('input[name$="id"]').value;

	var user = {
		"id" : userId,
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
			console.log('Form submission');
			form.submit();
		} else if (data == 'Duplicated') {
			alert('Please change the email ' + userEmail + ' to proceed.')
		}
	})
}









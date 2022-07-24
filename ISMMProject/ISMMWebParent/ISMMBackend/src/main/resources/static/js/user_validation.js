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


$(document).ready(function() {

	$("form").submit( () => {
		var url = "[[@{/users/check_email}]]";
		var userEmail = $("#email").val();
		var userId = $("#id").val();
		var csrfValue = $("input[name='_csrf']").val();
		params = {id: userId, email: userEmail, _csrf: csrfValue};

		$.post(url, params , (response) => {
			if (response == "OK" ) {
				//form.submit();
				console.log('Form Submission');
				return false;
			} else if (response == "Duplicated") {
				$("#modalTitle").text("Warning");
				$("#modalBody").text("There is another user using the Email: " + userEmail)
				$("#modalDialog").modal();
			}
		});
		
		return false;
			}  
			 )
	
});


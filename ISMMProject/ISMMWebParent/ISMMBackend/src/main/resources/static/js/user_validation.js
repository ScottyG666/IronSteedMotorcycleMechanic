let emailInputBox =  document.getElementById('emailInput');

emailInputBox.addEventListener('blur' , checkEmailUnique() )

function checkEmailUnique(event) {
	
	url = "[[@{/users/check_email}]]";
	userEmail = $("#email").val();
	csrfValue = $("input[name='_csrf']").val();
	params = {email : userEmail, _csrf: csrfValue };

	$.post(url, params, function(response) {
		
		if (response == "OK") {
			emailInputBox.className -= ' duplicatedUserShadow'

		} else if (response = "Duplicated") {
			
			emailInputBox.className += ' duplicatedUserShadow'
		}

		alert('Response from server: ' + response);
	});
} 
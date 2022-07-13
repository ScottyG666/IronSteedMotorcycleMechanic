
/*

$(document).ready(function(){
    $("select,input").change(function ()
    {
		let emailInputBox =  document.querySelector('#emailInput')

		emailInputBox.addEventListener('blur' , checkEmailUnique() )

		function checkEmailUnique() {
			
			url = "check_email";
			userEmail = $("#email").val();
			csrfValue = $("input[name='_csrf']").val();
			params = {email : userEmail, _csrf: csrfValue };

			$.post(url, params, function(response) {
				
				if (response == "OK") {
					// toruskit.com for danger shadow examples
					emailInputBox.className -= ' duplicatedUserShadow'

					alert('Response from server: ' + response);

				} else if (response = "Duplicated") {
					
					emailInputBox.className += ' duplicatedUserShadow'
					alert('Response from server: ' + response);

				} else {
				alert('Response from server: ' + response);
					
				}

			});
		} 
	})
    // Other event handlers.
});


*/
document.getElementById('#create-user').addEventListener('submit', checkEmailUnique)


function checkEmailUnique(event) {
	
	url = "[[@{/users/check_email}]]";
	userEmail = $("#email").val();
	csrfValue = $("input[name='_csrf']").val();
	params = {email : userEmail, _csrf: csrfValue };

	$.post(url, params, function(response) {
		
		if (repsonse == "OK") {
			form.submit();
		} else {
			
		}

		alert('Response from server: ' + response);
	});

	event.preventDefault();
} 
 /*
onsubmit="return checkEmailUnique(this);
*/
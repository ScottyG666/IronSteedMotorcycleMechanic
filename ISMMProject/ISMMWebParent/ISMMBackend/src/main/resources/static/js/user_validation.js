var emailTextBox = document.querySelector("#email-validation");
 

emailTextBox.addEventListener('blur' , () => {
    var userEmail = {
        'email' : emailTextBox.value
    }
    checkIfUserExists(userEmail)
})

let csrfValue = document.querySelectorAll('input[name=_csrf]');
function checkIfUserExists (user) {

	const token = document.querySelector('meta[name="_csrf"]').content;
	const header = document.querySelector('meta[name="_csrf_header"]').content;
	
    fetch('/ISMMAdmin/users/check_email', {
		method: 'POST',
		headers: {
			'Content-Type': 'application/json',
			header : token
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
	  
	  checking validation when form is submited and responding with an alert.
	  this is the solution provided by the instructor using JQuery
	      
            
*/
}
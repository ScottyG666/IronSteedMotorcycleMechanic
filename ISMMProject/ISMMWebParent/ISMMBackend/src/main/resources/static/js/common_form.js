var eyeIcons = document.querySelectorAll('.fa-eye');

eyeIcons.forEach( (eyeIcon) => {
	eyeIcon.addEventListener('click', () => {
		if (eyeIcon.classList.contains('fa-eye')) {
			eyeIcon.classList.replace('fa-eye', 'fa-eye-slash')
			if (eyeIcon.getAttribute('id') === 'passwordEyeIcon') {
				document.querySelector("#password").type = 'text'
			} else {
				document.querySelector("#confirmPassword").type = 'text'

			}
		} else {
			eyeIcon.classList.replace('fa-eye-slash', 'fa-eye')
			if (eyeIcon.getAttribute('id') === 'passwordEyeIcon') {
				document.querySelector("#password").type = 'password'
			} else {
				document.querySelector("#confirmPassword").type = 'password'
			}
		}
	})
})

$(function() {
	$("#buttonCancel").on("click", function() {
		window.location = moduleURL;
	});
	
	$("#fileImage").change(function() {

		fileSize = this.files[0].size;
		
		if (fileSize > 102400) {
			this.setCustomValidity("You must choose an image less than 100KB!");
			this.reportValidity();
		} else {
			this.setCustomValidity("");	
			showImageThumbnail(this);
		}

	});	
	
});

function showImageThumbnail(fileInput) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#thumbnail").attr("src", e.target.result);
	};
	
	reader.readAsDataURL(file);
}

function checkPasswordMatch(confirmPasswordField) {
	if (confirmPasswordField.value != $("#password").val()) {
		confirmPasswordField.setCustomValidity("Passwords do not match!")
	} else {
		confirmPasswordField.setCustomValidity("")
	}
}
                    
function showModalDialog (title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message)
    $("#modalDialog").modal();
}

function showErrorModal(message) {
	showModalDialog("Error" ,message)
}

function showWarningModal(message) {
	showModalDialog("Warning" ,message)
}



	/**
	 * 			USED WITHIN THE BRAND FORM 
	 * 		Allows for multiple categories to be selected, with common
	 * 	styling by appending a div containing the 
	 */
	 $(function() {
		dropdownCategories = $("#categories");
		divChosenCategories = $("#chosenCategories");

		dropdownCategories.change(function() {
			divChosenCategories.empty();
			showChosenCategories();
		});
		});


		function showChosenCategories() {
		dropdownCategories.children("option:selected").each(function() {
			selectedCategory = $(this);
			catId = selectedCategory.val();
			catName = selectedCategory.text().replace(/-/g, "");

			divChosenCategories.append("<span class='badge badge-secondary m-1'>" + catName + "</span>");
		});
	}
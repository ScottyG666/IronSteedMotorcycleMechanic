var eyeIcon = document.querySelector('.fa-eye');

eyeIcon.addEventListener('click', () => {
    if (eyeIcon.classList.contains('fa-eye')) {
        eyeIcon.classList.replace('fa-eye', 'fa-eye-slash');
        document.querySelector("#password").type = 'text';
    } else {
        eyeIcon.classList.replace('fa-eye-slash', 'fa-eye');
        document.querySelector("#password").type = 'password';
    }
});


$(function() {
	$("#buttonCancel").on("click", function() {
		window.location = moduleURL;
	});
	
	$("#fileImage").change(function() {

		fileSize = this.files[0].size;
		
		if (fileSize > 1048576) {
			this.setCustomValidity("You must choose an image less than 1MB!");
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


                    
function showModalDIalog (title, message) {
    $("#modalTitle").text(title);
    $("#modalBody").text(message)
    $("#modalDialog").modal();
}
            

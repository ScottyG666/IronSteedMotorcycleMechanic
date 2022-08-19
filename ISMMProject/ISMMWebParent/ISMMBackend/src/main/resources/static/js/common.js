$( function() {
    $("#logoutLink").on("click", function(e) {
        e.preventDefault();
        document.logoutForm.submit();
    })
customizeDropdownMenu();

});


function customizeDropdownMenu () {
    /*
        overiding the hover event.
            First function is for the dropdown menu sliding down
            Second function is for sliding the drop down back into the hidden position
    */
    $(".navbar .dropdown").hover(
        function() {
            $(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown();
        },
        function() {
            $(this).find('.dropdown-menu').first().stop(true,true).delay(100).slideUp();
        })

    $(".dropdown > a").click(function() {
        location.href = this.href;
    });
}
$(document).ready(function () {
    $('#options').hide();
    $(".radio").change(function () { //use change event
        if (this.value == "2") {
        	 $('#options').stop(true,true).show(500);
            $('#difficulty').stop(true,true).hide(500); //than show
        } else {
            $('#difficulty').stop(true,true).show(500); //else hide
            $('#options').stop(true,true).hide(500);
        }
    });
    
    jQuery('.numbersOnly').keyup(function () { 
        this.value = this.value.replace(/[^0-9\.]/g,'');
    });
});
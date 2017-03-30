function startTimer(duration, display) {
    var timer = duration,  seconds;
    setInterval(function () {
        seconds = parseInt(timer % 60, 10);

        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.text(seconds);

        if (--timer < 0) {
        	$('form#practiceForm').submit();
        }
    }, 1000);
}

function setTimer(timer){
        display = $('#time');
    startTimer(timer, display);
};
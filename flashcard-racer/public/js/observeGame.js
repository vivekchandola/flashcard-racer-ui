var currentCard;

$(function() {
	var gameAddress = window.location.href.split("/").pop();
	console.debug("Game address is" + gameAddress);
	var socket = new WebSocket('ws://localhost:9000/observegamews/' + gameAddress);
	
	$("#answer").click(function() {
		console.debug(currentCard);
		currentCard.result = $('#result').val();
		$('#result').val('')
		socket.send(JSON.stringify(currentCard));
	});
	
	socket.onmessage = function(event) {
		var object = $.parseJSON(event.data);
		if ("userId" in object && "percentDone" in object) {
			var entry = $("#players div#" + object.userId);
			
			if (entry.length == 0) {
				$("#players").append("<div id=" + object.userId + ">" + object.userId + ": " + object.percentDone + "% done!</div>");
			} else {
				$("#players div#" + object.userId).replaceWith("<div id=" + object.userId + ">"  + object.userId + ": " + object.percentDone + "% done!</div>");
			}
		} else {
			console.debug("wtf? " + object);
		}
	}

	socket.onopen = function(event) {
	};
	
	socket.onclose = function(event) {
	}
	
});
var currentCard;

$(function() {
	var socket = new WebSocket('ws://localhost:9000/gamews');
	
	$("#answer").click(function() {
		console.debug(currentCard);
		currentCard.result = $('#result').val();
		$('#result').val('')
		socket.send(JSON.stringify(currentCard));
	});
	
	socket.onmessage = function(event) {
		var object = $.parseJSON(event.data);
		
		if ("a" in object && "b" in object && "op" in object) {
			console.debug("got next card");
			currentCard = object;
			$("#currentCard").replaceWith('<div id="currentCard">' + currentCard.a + ' ' + currentCard.op + ' ' + currentCard.b + ' = </div>');
		} else if ("userId" in object && "percentDone" in object) {
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
		alert("A winner is you!");
	}
	
});
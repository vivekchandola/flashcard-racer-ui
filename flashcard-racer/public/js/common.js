// Holds all undo items
var undo = [];
// Holds all Redo items
var redo = [];

$(document).ready(function() {
	$('#options').hide();
	$(".radio").change(function() { // use change event
		if (this.value == "2") {
			$('#options').stop(true, true).show(500);
			$('#difficulty').stop(true, true).hide(500); // than show
		} else {
			$('#difficulty').stop(true, true).show(500); // else hide
			$('#options').stop(true, true).hide(500);
		}
	});

	jQuery('.numbersOnly').keyup(function() {
		this.value = this.value.replace(/[^0-9\.]/g, '');
	});
});

function optionsDisable(type) {
	$("#" + type).prop('disabled', true);
	$("#" + type + "label").addClass('disabled');
	var action = {
		action : 'enable',
		id : type
	};
	undo.push(action);
	$('#undo').css('opacity', 1);
	$('#undo').css('cursor', 'pointer');
}

function optionFunctionEnable(type) {
	$("#" + type).prop('disabled', false);
	$("#" + type + "label").addClass('enabled');
	$("#" + type + "label").removeClass('disabled');
	$("#" + type).prop('checked', true); 
}

function optionFunctionDisable(type) {
	$("#" + type).prop('disabled', true);
	$("#" + type + "label").addClass('disabled');
	$("#" + type + "label").removeClass('enabled');
	$("#" + type).prop('checked', false); 
}

function checkOption(action) {
	if (action.action == 'enable') {
		optionFunctionEnable(action.id);
	} else {
		optionFunctionDisable(action.id);
	}
}

/**
 * Removes latest action made to the operator.
 */
$("#undo").click(function() {
	if (undo.length > 0) {
		var action = undo.splice(-1, 1);
		var actionLoad;
		if (action[0].action == 'enable') {
			actionLoad = {
				action : 'disable',
				id : action[0].id
			};
		} else {
			actionLoad = {
				action : 'enable',
				id : action[0].id
			};
		}
		redo.push(actionLoad);
		checkOption(action[0]);
		//undo.pop();
		$('#redo').css('opacity', 1);
		$('#redo').css('cursor', 'pointer');
	}
	if (undo.length == 0) {
		$(this).css('opacity', 0.5);
		$(this).css('cursor', 'default');
	}
});

/**
 * Redos the latest action which was undone by the undo.
 */
$("#redo").click(function() {
	if (redo.length > 0) {
		var action = redo.splice(-1, 1);
		var actionLoad;
		if (action[0].action == 'enable') {
			actionLoad = {
				action : 'disable',
				id : action[0].id
			};
		} else {
			actionLoad = {
				action : 'enable',
				id : action[0].id
			};
		}
		
		undo.push(actionLoad);
		checkOption(action[0]);
		//redo.pop();
		$('#undo').css('opacity', 1);
		$('#undo').css('cursor', 'pointer');
	}
	if (redo.length == 0) {
		$(this).css('opacity', 0.5);
		$(this).css('cursor', 'default')
	}
});
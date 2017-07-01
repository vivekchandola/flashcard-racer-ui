var egg = new Egg();
/**
 * Easter egg while typing 'car' in the game choice page
 */
egg 
	.addCode("c,a,r",function(){
		jQuery('#egggif').fadeIn(500,function(){
			window.setTimeout(function() {jQuery('#egggif').hide();},8000);
			});
		})
		.addHook(function(){
			console.log("Hook called for:" + this.activeEgg.keys);
			console.log(this.activeEgg.metadata);
		}).listen();

/**
 * Easter egg while typing 'credit' in the final result page
 */
egg 
	.addCode("c,r,e,d,i,t",function(){
		jQuery('#credits').fadeIn(500,function(){
			window.setTimeout(function() {jQuery('#credits').hide();},15000);
			});
		})
		.addHook(function(){
			console.log("Hook called for:" + this.activeEgg.keys);
			console.log(this.activeEgg.metadata);
		}).listen();
var egg = new Egg();
egg 
	.addCode("c,a,r",function(){
		jQuery('#egggif').fadeIn(500,function(){
			window.setTimeout(function() {jQuery('#egggif').hide();},5000);
			});
		})
		.addHook(function(){
			console.log("Hook called for:" + this.activeEgg.keys);
			console.log(this.activeEgg.metadata);
		}).listen();

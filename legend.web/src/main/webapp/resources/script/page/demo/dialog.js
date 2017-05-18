function abc() {
	seajs.use([ "dialog" ], function(dg) {
		dg.dialog({
			"dom" : "#testDialog"
		});
	});
}
function abc2() {
	seajs.use([ "dialog" ], function(dg) {
		dg.dialog({
			"dom" : "#testDialog2"
		});
	});
}
$(function ($) {
	var tableName = "bill_search_table";
	seajs.use(["table","ajax"],function(table,ajax){
		table.fill(tableName);
	});

	$(document).on("click", '.clear_btn', function() {
		$('#bill_search').find("input[type=text]").val("")
			.end().find("select").val("");
	})
});
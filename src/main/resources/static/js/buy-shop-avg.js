$(function() {
	var now = new Date();
	$("#year").val(now.getFullYear());
	$("#month").val(now.getMonth() + 1);
	loadDatas();

});

function loadDatas() {
	$.ajax({
		type : "POST",
		url : "/buyShop/avg",
		dataType : "json",
		data : {
			year : $("#year").val(),
			month : $("#month").val(),
			type : $("#type").val()
		},
		success : function(data) {
			var html = "";
			$.each(data, function() {
				html += "<tr>"
				html += "<td>" + this.name + "</td>";
				html += "<td>" + this.sumPrice + "</td>";
				html += "<td>" + this.sumCount + "</td>";
				html += "<td>" + this.avgPrice + "</td>";
				html += "</tr>"
			})
			$("tbody").html(html);
		}
	});

}

function exportExcel(){
	$("form").submit();
}
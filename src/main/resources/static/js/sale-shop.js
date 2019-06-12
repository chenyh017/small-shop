$(function() {
	var now = new Date();
	$("#year").val(now.getFullYear());
	$("#month").val(now.getMonth() + 1);
	loadDatas();

});

function loadDatas() {
	$.ajax({
		type : "POST",
		url : "/saleShop/queryByDate",
		dataType : "json",
		data : {
			year : $("#year").val(),
			month : $("#month").val(),
			day : $("#day").val() == "" ? 0 : $("#day").val(),
			type : $("#type").val()
		},
		success : function(data) {
			var html = "";
			$.each(data, function() {
				html += "<tr id='" + this.id + "'>";
				html += "<td>" + this.name + "</td>";
				html += "<td>" + this.price + "</td>";
				html += "<td>" + this.count + "</td>";
				html += "<td>" + this.time + "</td>";
				html += "<td><a onclick=dlg('" + this.id
						+ "') href='#'>修改</a> <a onclick=del('" + this.id
						+ "') href='#'>删除</a></td>";
				html += "</tr>"
			})
			$("tbody").html(html);
		}
	});

}

function del(id) {
	layer.msg('是否确认删除？', {
		time : 0 // 不自动关闭
		,
		btn : [ '确认', '取消' ],
		yes : function(index) {
			layer.close(index);
			$.ajax({
				type : "POST",
				url : "/saleShop/delete",
				dataType : "json",
				data : {
					"id" : id
				},
				complete : loadDatas
			});
		}
	});
}

function dlg(id) {
	var html = "<form>";
	html += '<div class="form-group">';
	html += '<label for="name">商品名称</label>';
	html += '<input type="text" class="form-control" id="dlg-name" placeholder="商品名称" onblur="getPrice()">';
	html += '</div>';
	html += '<div class="form-group">';
	html += '<label for="type">数量</label>';
	html += '<input type="text" class="form-control" id="dlg-count" placeholder="数量" >';
	html += '</div>';
	html += '<div class="form-group">';
	html += '<label for="type">单价</label>';
	html += '<input type="text" class="form-control" id="dlg-price" placeholder="单价">';
	html += '<input type="hidden" class="form-control" id="dlg-id">';
	html += '</div>';
	html += '<div class="form-group">';
	html += '<label for="type">年</label>';
	html += '<input type="text" class="form-control" id="dlg-year" placeholder="年" onblur="getPrice()">';
	html += '</div>';
	html += '<div class="form-group">';
	html += '<label for="type">月</label>';
	html += '<input type="text" class="form-control" id="dlg-month" placeholder="月"  onblur="getPrice()">';
	html += '</div>';
	html += '<div class="form-group">';
	html += '<label for="type">日</label>';
	html += '<input type="text" class="form-control" id="dlg-day" placeholder="日" >';
	html += '</div>';
	html += "</form>";
	layer.open({
		type : 1,
		skin : 'layui-layer-rim', // 加上边框
		area : [ '550px', 'auto' ], // 宽高
		content : html,
		btn : [ '保存' ],
		yes : function(index) {
			layer.close(index);
			save();
		}

	});
	if (id != "") {
		var tds = $("#" + id).find("td");
		$("#dlg-id").val(id);
		$("#dlg-name").val($(tds[0]).html());
		$("#dlg-price").val($(tds[1]).html());
		$("#dlg-count").val($(tds[2]).html());
	}
	$("#dlg-day").val(
			$("#day").val() == "" ? new Date().getDate() : $("#day").val());
	$("#dlg-year").val($("#year").val());
	$("#dlg-month").val($("#month").val());
}

function save() {
	$.ajax({
		type : "POST",
		url : "/saleShop/save",
		dataType : "json",
		data : {
			id : $("#dlg-id").val(),
			name : $("#dlg-name").val(),
			count : $("#dlg-count").val(),
			price : $("#dlg-price").val(),
			year : $("#dlg-year").val(),
			month : $("#dlg-month").val(),
			day : $("#dlg-day").val()
		},
		complete : loadDatas
	});
}

function exportExcel() {
	if ($("#day").val() == "") {
		$("#day").val(0);
	}
	$("form").submit();
}

function getPrice() {
	$.ajax({
		type : "POST",
		url : "/shopSalePrice/getPrice",
		dataType : "json",
		data : {
			name : $("#dlg-name").val(),
			year : $("#dlg-year").val(),
			month : $("#dlg-month").val()
		},
		success : function(data) {
			if (data == -1) {
				return;
			}
			$("#dlg-price").val(data);
		}
	});
}
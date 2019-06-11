$(function() {
	var now = new Date();
	$("#year").val(now.getFullYear());
	$("#month").val(now.getMonth() + 1);
	loadDatas();

});

function loadDatas() {
	$.ajax({
		type : "POST",
		url : "/shopSalePrice/queryByDate",
		dataType : "json",
		data : {
			year : $("#year").val(),
			month : $("#month").val(),
			name : $("#name").val()
		},
		success : function(data) {
			var html = "";
			$.each(data, function() {
				html += "<tr id='" + this.id + "'>";
				html += "<td>" + this.name + "</td>";
				html += "<td>" + this.type + "</td>";
				html += "<td>" + this.unit + "</td>";
				html += "<td>" + this.price + "</td>";
				html += "<td>" + this.date + "</td>";
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
				url : "/shopSalePrice/deleteById",
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
	html += '<input type="text" class="form-control" id="dlg-name" placeholder="商品名称" >';
	html += '</div>';
	html += '<div class="form-group">';
	html += '<label for="type">商品类型</label>';
	html += '<input type="text" class="form-control" id="dlg-type" placeholder="商品类型" >';
	html += '</div>';
	html += '<div class="form-group">';
	html += '<label for="type">商品单位</label>';
	html += '<input type="text" class="form-control" id="dlg-unit" placeholder="商品单位" >';
	html += '</div>';
	html += '<div class="form-group">';
	html += '<label for="type">商品售价</label>';
	html += '<input type="text" class="form-control" id="dlg-price" placeholder="商品售价" >';
	html += '<input type="hidden" class="form-control" id="dlg-id">';
	html += '</div>';
	html += "</html>";
	layer.open({
		type : 1,
		skin : 'layui-layer-rim', // 加上边框
		area : [ '550px', 'autoa' ], // 宽高
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
		$("#dlg-type").val($(tds[1]).html());
		$("#dlg-unit").val($(tds[2]).html());
		$("#dlg-price").val($(tds[3]).html());
	}
}

function copy() {
	layer.msg('确认复制会删除当前月份的数据，是否确认？', {
		time : 0 // 不自动关闭
		,
		btn : [ '确认', '取消' ],
		yes : function(index) {
			layer.close(index);
			$.ajax({
				type : "POST",
				url : "/shopSalePrice/copy",
				dataType : "json",
				complete : loadDatas
			});
		}
	});
}

function save() {
	$.ajax({
		type : "POST",
		url : "/shopSalePrice/save",
		dataType : "json",
		data : {
			id : $("#dlg-id").val(),
			name : $("#dlg-name").val(),
			type : $("#dlg-type").val(),
			unit : $("#dlg-unit").val(),
			price : $("#dlg-price").val(),
			year:$("#year").val(),
			month:$("#month").val()
		},
		complete : loadDatas
	});
}

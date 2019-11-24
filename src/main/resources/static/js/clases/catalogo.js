$(function() {
	$('#tablaCatalogo').DataTable();
	$("#catalogo").html('');
	obtenerListaMateriaPrima()
});

function obtenerListaMateriaPrima() {
	$.ajax({
		type : "POST",
		datatype : "JSON",
		url : "/obtenerProductos",
		data : {},
		success : function(data) {
			var tabla = data;
			console.log(tabla);
			$("#catalogo").html(tabla);
		},
		error : function(e) {
			alert("ERROR...");
		}
	});
}
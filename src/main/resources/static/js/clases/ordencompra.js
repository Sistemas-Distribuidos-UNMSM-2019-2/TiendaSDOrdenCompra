$(document).ready(function() {
	mostrarProductos();
	$("#tipoOperacion").val('add');
	$("#Eliminar").hide();
	$("#codigoOrdenCompra").hide();
	// Poner día al día actual
	$("#fechaOrden").val(fechaTexto);
});

function mostrarProductos(){
	$.ajax({
		type : "POST",
		datatype : "JSON",
		url : "/obtenerProductos",
		data : {},
		success : function(data) {
			var tabla = data;
			$("#tablaProductos").html(tabla);
		},
		error : function(err) {
			alert(err);
		}
	});
}

function openModalProducto(codProducto) {
	$("#modalCantidadProducto").modal('show');
	$("#cantProducto").val('');
	$("#codProducto").val(codProducto);
}

function closeModalProducto() {
	$("#modalCantidadProducto").modal('hide');
	$("#cantProducto").val('');
	$("#codProducto").val('');
}

function agregarCantidadAProducto() {
	var codProducto = $("#codProducto").val();
	var cantProducto = $("#cantProducto").val();
	$("#c_tablaProductosSeleccionados").html('');
	$("#totalPagar").html('');

	$.ajax({
		type : "POST",
		datatype : "JSON",
		url : "/agregarCantOrdenCompra",
		data : {
			codProducto : codProducto,
			cantProducto : cantProducto
		},
		success : function(data) {
			var datos = data.split("@|");
			var tablaSeleccionados = datos[0];
			var totales = datos[1];
			$("#c_tablaProductosSeleccionados").html(tablaSeleccionados);
			$("#totalPagar").html("S/." + totales);
			closeModalProducto();
		},
		error : function(e) {
			closeModalProducto();
			alert("ERROR...");
		}
	});
}
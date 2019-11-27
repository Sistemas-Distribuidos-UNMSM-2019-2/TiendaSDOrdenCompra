$(document).ready(function() {
	mostrarProductos();
	$("#tipoOperacion").val('add');
	$("#Eliminar").hide();
	$("#codigoOrdenCompra").hide();
	//capturaOrdenCompra();
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

function openModalProducto2(codProducto) {
	$("#modalCantidadMenosProducto").modal('show');
	$("#cantProducto2").val('');
	$("#codProducto2").val(codProducto);
}

function closeModalProducto2() {
	$("#modalCantidadMenosProducto").modal('hide');
	$("#cantProducto2").val('');
	$("#codProducto2").val('');
}

function openModalValidacion() {
	$("#modalValidacionOrdenCompra").modal('show');
}

function closeModalValidacion() {
	$("#modalValidacionOrdenCompra").modal('hide');
}

function openModalHayStock() {
	$("#modalHayStock").modal('show');
}

function closeModalHayStock() {
	$("#modalHayStock").modal('hide');
}

function validarCompra(){
	$.ajax({
		type : "POST",
		datatype : "JSON",
		url : "/validarOrdenCompra",
		data : {},
		success : function(data) {
			var datos = data;
			if(data == "Procede"){
				openModalHayStock();
			}else{
				$("#tablaValidacion").html(datos);
				openModalValidacion();
			}
		},
		error : function(e) {
			closeModalProducto();
			alert("ERROR...");
		}
	});
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

function eliminarCantidadProducto(codMatPrim) {
	var codProducto2 = $("#codProducto2").val();
	var cantProducto2 = $("#cantProducto2").val();
	$("#c_tablaProductosSeleccionados").html('');
	$("#totalPagar").html('');

	$.ajax({
		type : "POST",
		datatype : "JSON",
		url : "/eliminarCantOrdenCompra",
		data : {
			codProducto2 : codProducto2,
			cantProducto2 : cantProducto2
		},
		success : function(data) {
			var datos = data.split("@|");
			var tablaSeleccionados = datos[0];
			console.log(tablaSeleccionados);
			var totales = datos[1];
			$("#c_tablaProductosSeleccionados").html(tablaSeleccionados);
			$("#totalPagar").html("S/." + totales);
			closeModalProducto2();
		},
		error : function(e) {
			closeModalProducto2();
			alert("ERROR...");
		}
	});
}

function realizarCompra(){
	$.ajax({
		type : "POST",
		datatype : "JSON",
		url : "/procederCompra",
		success : function(data) {
			closeModalHayStock();
		},
		error : function(xhr, status, error) {
			var err= JSON.parse(xhr.responseText);
			console.log("ERROR: "+err.Message);
		}
	});
}

function enviarOrdenCompra(){
	validarCompra();
	console.log("enviarCompra");
	$.ajax({
		type : "POST",
		datatype : "JSON",
		url : "/enviarOrdenCompra",
		success : function(data) {
			
			/*$.notify({
				icon : "fa fa-check"",
				title : " <strong>" + "Aviso" + ": </strong>",
				message : "<p>" + data + "</p>"
			}, {
				delay : 6000,
				type : "success"
			})*/
		},
		error : function(xhr, status, error) {
			var err= JSON.parse(xhr.responseText);
			console.log("ERROR: "+err.Message);
		}
	});
}
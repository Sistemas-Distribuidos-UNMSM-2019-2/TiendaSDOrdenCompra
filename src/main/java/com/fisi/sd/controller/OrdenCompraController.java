package com.fisi.sd.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fisi.sd.model.OrdenCompraDetalleModel;
import com.fisi.sd.model.OrdenCompraModel;
import com.fisi.sd.model.ProductoModel;
import com.fisi.sd.model.UsuarioModel;
import com.fisi.sd.serviceimpl.ClienteServicioImpl;
import com.fisi.sd.serviceimpl.KafkaServicioImpl;
import com.fisi.sd.serviceimpl.ProductoServicioImpl;
import com.fisi.sd.serviceimpl.UsuarioClienteServicioImpl;

@Controller
public class OrdenCompraController {
	@Autowired
	@Qualifier("productoService")
	private ProductoServicioImpl productoServicio;
	@Autowired
	@Qualifier("usuarioService")
	private UsuarioClienteServicioImpl usuarioServicio;
	@Autowired
	@Qualifier("clienteService")
	private ClienteServicioImpl clienteServicio;
	@Autowired
	@Qualifier("kafkaService")
	private KafkaServicioImpl kafkaServicio;
	
	private static final String LSITAMODELPRODUCTO = "PROD" + (UUID.randomUUID());
	private static final String LSITAMODELPRODUCTO2 = "PROD" + (UUID.randomUUID());
	
	@RequestMapping( {"/afterlogin","/", "/ordenCompra", "/home"})
	public String ordenCompra(Model model, HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		UsuarioModel oUsuarioModel = usuarioServicio.buscarUsuario(authentication.getName());
		
		OrdenCompraModel ordenCompraModel = new OrdenCompraModel();
		ordenCompraModel.setsRucCliente(oUsuarioModel.getsRUC());
		
		model.addAttribute("nombreUsuario", authentication.getName());
		model.addAttribute("ordenCompra", ordenCompraModel);
		model.addAttribute("cliente", clienteServicio.buscarCliente(oUsuarioModel.getsRUC()));
		HttpSession session = request.getSession();
		session.setAttribute(LSITAMODELPRODUCTO, productoServicio.listarProductos());
		session.setAttribute(LSITAMODELPRODUCTO2, new ArrayList<OrdenCompraDetalleModel>());
		return "view/ordencompra";
	}
	
	@PostMapping("/enviarOrdenCompra") 
	public String agregarOrdenCompra(
			@ModelAttribute("ordenCompraModel") OrdenCompraModel ordenCompraModel,
			Model model, HttpServletRequest request, HttpServletResponse response,Authentication authentication) {
		model.addAttribute("nombreUsuario", authentication.getName());
		
		HttpSession session = request.getSession();
		List<OrdenCompraDetalleModel> listaSeleccionados = (List<OrdenCompraDetalleModel>) session.getAttribute(LSITAMODELPRODUCTO2);
		ordenCompraModel.setlDetalleCompra(listaSeleccionados);
		
		double nTotalPagar = calcularSumaTotales(listaSeleccionados);
		ordenCompraModel.setnPrecioTotal(nTotalPagar);
		model.addAttribute("msj", "EnvioExito");
		
		kafkaServicio.enviarMensaje(ordenCompraModel);
		
		return "view/ordencompra";
	}
	
	@KafkaListener(topics = "inventario")
	public String listenerKafka(String ordenCompra) {
		OrdenCompraModel ordenCompraModel = kafkaServicio.recibirMensaje(ordenCompra);
		
		return "view/ordencompra";
	}
	
	@PostMapping("/agregarCantOrdenCompra")
	public void agregarCantAProducto(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		List<ProductoModel> listaTodos = (List<ProductoModel>) session.getAttribute(LSITAMODELPRODUCTO);
		List<OrdenCompraDetalleModel> listaSeleccionados = (List<OrdenCompraDetalleModel>) session.getAttribute(LSITAMODELPRODUCTO2);
		
		String codProducto = request.getParameter("codProducto");
		int nCodigoProducto = Integer.parseInt(codProducto);
		int cantProducto = Integer.parseInt(request.getParameter("cantProducto"));
		
		ProductoModel oProductoModel = productoServicio.buscarProducto(nCodigoProducto);
		
		if(listaSeleccionados.isEmpty()) {
			OrdenCompraDetalleModel oCompraDetalleModel = new OrdenCompraDetalleModel();
			oCompraDetalleModel.setnCantidadProducto(cantProducto);
			oCompraDetalleModel.setsNombreProducto(oProductoModel.getsNombre());
			oCompraDetalleModel.setnCodigoProducto(nCodigoProducto);
			oCompraDetalleModel.setnTotalParcial(cantProducto * oProductoModel.getnPrecioUnitario());
			oCompraDetalleModel.setbExistencia(true);
			
			listaSeleccionados.add(oCompraDetalleModel);
		}
		else {
			boolean bandera = false;
			for(OrdenCompraDetalleModel auxiliar : listaSeleccionados) {
				if(auxiliar.getnCodigoProducto() == nCodigoProducto) {
					auxiliar.setnCantidadProducto(auxiliar.getnCantidadProducto() + cantProducto);
					auxiliar.setnTotalParcial(auxiliar.getnCantidadProducto() * oProductoModel.getnPrecioUnitario());
					bandera = true;
				}
			}
			
			if(!bandera) {
				OrdenCompraDetalleModel oCompraDetalleModel = new OrdenCompraDetalleModel();
				oCompraDetalleModel.setnCantidadProducto(cantProducto);
				oCompraDetalleModel.setsNombreProducto(oProductoModel.getsNombre());
				oCompraDetalleModel.setnCodigoProducto(nCodigoProducto);
				oCompraDetalleModel.setnTotalParcial(cantProducto * oProductoModel.getnPrecioUnitario());
				oCompraDetalleModel.setbExistencia(true);
				
				listaSeleccionados.add(oCompraDetalleModel);
			}
		}
		
		/**/
		session.removeAttribute(LSITAMODELPRODUCTO);
		session.removeAttribute(LSITAMODELPRODUCTO2);
		/* Re-enviamos las listas a la sesion */
		session.setAttribute(LSITAMODELPRODUCTO, listaTodos);
		session.setAttribute(LSITAMODELPRODUCTO2, listaSeleccionados);

		StringBuilder tablaSelecionado = mostrarDetalle(listaSeleccionados);
				
		PrintWriter out;
		try {
			out = response.getWriter();
			String mensajeSalida = tablaSelecionado.toString() + "@|" + calcularSumaTotales(listaSeleccionados);
			out.write(mensajeSalida);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public StringBuilder mostrarDetalle(List<OrdenCompraDetalleModel> lOrdenCompraDetalle) {
		StringBuilder sb = new StringBuilder();// tablaMateriaPrima
		sb.append("<table id=\"" + "c_tablaProductosSeleccionados" + "\" class=\"table table-hover table-striped bdt\" >");
		sb.append("<th>Nombre</th>");
		sb.append("<th style=\"text-align: center;\">Cantidad</th>");
		sb.append("<th style=\"text-align: center;\">Total parcial</th>");
		sb.append("<th style=\"text-align: center;\">Eliminar</th></tr>");

		for(OrdenCompraDetalleModel auxiliar : lOrdenCompraDetalle) {
			sb.append("<tr>");
			sb.append("<td>" + auxiliar.getsNombreProducto() + "</td>");
			sb.append("<td style=\"text-align: center;\">" + auxiliar.getnCantidadProducto() + "</td>");
			sb.append("<td style=\"text-align: center;\">" + auxiliar.getnTotalParcial() + "</td>");
			sb.append("<td style=\"text-align: center;\"><button type=\"button\" onclick=\"eliminarMateriaPrima('" + auxiliar.getnCodigoProducto()
					+ "')\" style=\"border-style:solid; border-width:1px; border-color:#2C1CD9; text-align: center;\"  "
					+ "class=\"btn btn-default btn-sm\"><i  class=\"fas fa-minus-circle\"></i></i></button></td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb;
	}
	
	public double calcularSumaTotales(List<OrdenCompraDetalleModel> listaSeleccionados) {
		double suma = 0;
		for (OrdenCompraDetalleModel mpm : listaSeleccionados) {
			suma += mpm.getnTotalParcial();
		}
		return suma;
	}
}

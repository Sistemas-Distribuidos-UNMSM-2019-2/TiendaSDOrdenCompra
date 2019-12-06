package com.fisi.sd.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	private String sMensaje;
	
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
		session.removeAttribute(LSITAMODELPRODUCTO);
		session.removeAttribute(LSITAMODELPRODUCTO2);
		session.setAttribute(LSITAMODELPRODUCTO, productoServicio.listarProductos());
		session.setAttribute(LSITAMODELPRODUCTO2, new ArrayList<OrdenCompraDetalleModel>());
		return "view/ordencompra";
	}
	
	@PostMapping("/enviarOrdenCompra") 
	public ResponseEntity<?> agregarOrdenCompra(
			@ModelAttribute("ordenCompraModel") OrdenCompraModel ordenCompraModel,
			Model model, HttpServletRequest request, HttpServletResponse response,Authentication authentication) {
		model.addAttribute("nombreUsuario", authentication.getName());
		
		HttpSession session = request.getSession();
		
		UsuarioModel oUsuarioModel = usuarioServicio.buscarUsuario(authentication.getName());
		ordenCompraModel.setsRucCliente(oUsuarioModel.getsRUC());
		List<OrdenCompraDetalleModel> listaSeleccionados = (List<OrdenCompraDetalleModel>) session.getAttribute(LSITAMODELPRODUCTO2);
		ordenCompraModel.setlDetalleCompra(listaSeleccionados);
		
		double nTotalPagar = calcularSumaTotales(listaSeleccionados);
		ordenCompraModel.setnPrecioTotal(nTotalPagar);
		
		kafkaServicio.enviarMensaje(ordenCompraModel, "validar");
		
		return ResponseEntity.ok("Envio exitoso");
	}
	
	@KafkaListener(groupId = "inve", topics = "inventario")
	public void listenerKafka(String ordenCompra) {
		sMensaje = ordenCompra;
	}
	
	@PostMapping("/validarOrdenCompra")
	public void mostrarValidacionOrdenCompra(HttpServletRequest request, HttpServletResponse response) {
		sMensaje = "";
		
		while(sMensaje.compareTo("")==0) {
			
		}
		
		System.out.println(sMensaje);
		
		OrdenCompraModel ordenCompraModel = kafkaServicio.recibirMensaje(sMensaje);
	
		List<OrdenCompraDetalleModel> listaSeleccionados = ordenCompraModel.getlDetalleCompra();
		boolean bandera = false;
		
		for(OrdenCompraDetalleModel auxiliar : listaSeleccionados) {
			if(!auxiliar.isbExistencia()) {
				bandera = true;
			}
		}
				
		PrintWriter out;
		try {
			out = response.getWriter();
			if(bandera) {
				StringBuilder tablaSelecionado = mostrarTablaValidacion(listaSeleccionados);
				
				String mensajeSalida = tablaSelecionado.toString();
				out.write(mensajeSalida);
			}
			else {
				out.write("Procede");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@PostMapping("/procederCompra")
	public void procederOrdenCompra(HttpServletRequest request, HttpServletResponse response) {
		OrdenCompraModel ordenCompraModel = kafkaServicio.recibirMensaje(sMensaje);
		ordenCompraModel.setdFechaCompra(new Date());
		kafkaServicio.enviarMensaje(ordenCompraModel, "comprar");
	}
	
	private StringBuilder mostrarTablaValidacion(List<OrdenCompraDetalleModel> lOrdenCompraDetalle) {
		StringBuilder sb = new StringBuilder();// tablaMateriaPrima
		sb.append("<table id=\"" + "tablaValidacion" + "\" class=\"table\" >");
		sb.append("<thead class=\"thead-dark\" align=\"center\">");
		sb.append("<tr>");
		sb.append("<th class=\"align-middle\">Nombre</th>");
		sb.append("<th class=\"align-middle\">Cantidad</th>");
		sb.append("<th class=\"align-middle\">Estado</th></tr>");
		sb.append("</tr>");
		sb.append("</thead>");
		
		for(OrdenCompraDetalleModel auxiliar : lOrdenCompraDetalle) {
			sb.append("<tr>");
			sb.append("<td class=\"align-middle\" align=\"center\">" + auxiliar.getsNombreProducto() + "</td>");
			sb.append("<td class=\"align-middle\" align=\"center\">" + auxiliar.getnCantidadProducto() + "</td>");
			sb.append("<td class=\"align-middle\" align=\"center\">" + (auxiliar.isbExistencia()?"Tenemos":"No tenemos") + "</td>");
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb;
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
			oCompraDetalleModel.setnTotalParcial((double) ((Math.round(cantProducto * oProductoModel.getnPrecioUnitario()) * 100d)/ 100d));
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
				oCompraDetalleModel.setnTotalParcial((double) ((Math.round(cantProducto * oProductoModel.getnPrecioUnitario()) * 100d)/ 100d));
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
	
	@PostMapping("/eliminarCantOrdenCompra")
	public void eliminarCantOrdenCompra(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		List<ProductoModel> listaTodos = (List<ProductoModel>) session.getAttribute(LSITAMODELPRODUCTO);
		List<OrdenCompraDetalleModel> listaSeleccionados = (List<OrdenCompraDetalleModel>) session.getAttribute(LSITAMODELPRODUCTO2);
		
		String codProducto = request.getParameter("codProducto2");
		int nCodigoProducto = Integer.parseInt(codProducto);
		int cantProducto = Integer.parseInt(request.getParameter("cantProducto2"));
		
		ProductoModel oProductoModel = productoServicio.buscarProducto(nCodigoProducto);
		
		if(!listaSeleccionados.isEmpty()) {
			int pos = -1;
			int cont = 0;
			for(OrdenCompraDetalleModel auxiliar : listaSeleccionados) {
				if(auxiliar.getnCodigoProducto() == nCodigoProducto) {
					if(cantProducto >= auxiliar.getnCantidadProducto()) {
						pos = cont;
					}
					else {
						auxiliar.setnCantidadProducto(auxiliar.getnCantidadProducto() - cantProducto);
						auxiliar.setnTotalParcial((double) ((Math.round(auxiliar.getnCantidadProducto() * oProductoModel.getnPrecioUnitario()) * 100d)/ 100d));
					}
					
				}
				cont++;
			}
			
			if(pos != -1) {
				listaSeleccionados.remove(pos);
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
		sb.append("<table id=\"" + "c_tablaProductosSeleccionados" + "\" class=\"table\" >");
		sb.append("<thead class=\"table-info\" align=\"center\">");
		sb.append("<tr>");
		sb.append("<th class=\"align-middle\">Nombre</th>");
		sb.append("<th class=\"align-middle\">Cantidad</th>");
		sb.append("<th class=\"align-middle\">Total parcial</th>");
		sb.append("<th class=\"align-middle\">Eliminar</th></tr>");
		sb.append("</tr>");
		sb.append("</thead>");

		for(OrdenCompraDetalleModel auxiliar : lOrdenCompraDetalle) {
			sb.append("<tr>");
			sb.append("<td class=\"align-middle\">"+ auxiliar.getsNombreProducto() + "</td>");
			sb.append("<td class=\"align-middle\">"+ auxiliar.getnCantidadProducto() + "</td>");
			sb.append("<td class=\"align-middle\">" + auxiliar.getnTotalParcial() + "</td>");
			sb.append("<td class=\"align-middle\"><button type=\"button\" onclick=\"openModalProducto2('" + auxiliar.getnCodigoProducto()
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
		suma = (double) ((Math.round(suma) * 100d)/ 100d);
		return suma;
	}
}

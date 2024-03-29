package com.fisi.sd.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fisi.sd.model.ProductoModel;
import com.fisi.sd.serviceimpl.ProductoServicioImpl;

@Controller
public class CatalogoController {
	@Autowired
	@Qualifier("productoService")
	private ProductoServicioImpl productoServicio;
	
	@GetMapping("/productos")
	public String showMantProducto(Model model, Authentication authentication) {
		model.addAttribute("nombreUsuario", authentication.getName());
		model.addAttribute("Producto", new ProductoModel());
		return "view/catalogo";
	}
	
	@RequestMapping("/obtenerProductos")
	public void listaMateriaPrimaTabla(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<ProductoModel> lProductoModal = productoServicio.listarProductos();
			PrintWriter out = response.getWriter();
			StringBuilder sb = new StringBuilder();// tablaMateriaPrima
			sb.append("<table id=\"tablaCatalogo\" class=\"table\" >");
			sb.append("<thead class=\"table-primary\" align=\"center\">");
			sb.append("<tr>");
			sb.append("<th class=\"align-middle\">Nombre</th>");
			sb.append("<th class=\"align-middle\">Descripci&oacute;n</th>");
			sb.append("<th class=\"align-middle\">Precio unitario</th>");
			sb.append("<th class=\"align-middle\">Agregar</th>");
			sb.append("</tr>");
			sb.append("</thead>");
			for (ProductoModel auxiliar: lProductoModal) {
				sb.append("<tr>");
				sb.append("<td class=\"align-middle\">" + auxiliar.getsNombre()+ "</td>");
				sb.append("<td class=\"align-middle\">" + auxiliar.getsDescripcion() + "</td>");
				sb.append("<td class=\"align-middle\">" + auxiliar.getnPrecioUnitario() + "</td>");
				sb.append("<td class=\"align-middle\"><button type=\"button\" onclick=\"openModalProducto('"
						+ auxiliar.getnCodigo()
						+ "')\" style=\"border-style:solid; border-width:1px; border-color:#2C1CD9; text-align: center;\"  class=\"btn btn-default btn-sm\"><i  class=\"fas fa-plus\"></i></i></button></td>");
				sb.append("</tr>");
			}
			sb.append("</table>");
			out.print(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

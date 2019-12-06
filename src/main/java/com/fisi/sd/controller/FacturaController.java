package com.fisi.sd.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fisi.sd.model.ClienteModel;
import com.fisi.sd.model.FacturaModel;
import com.fisi.sd.model.OrdenCompraDetalleModel;
import com.fisi.sd.model.OrdenCompraModel;
import com.fisi.sd.serviceimpl.ClienteServicioImpl;
import com.google.gson.Gson;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.view.JasperViewer;

@Controller
public class FacturaController {
	private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMMM yyyy");
	private SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
	@Autowired
	@Qualifier("clienteService")
	private ClienteServicioImpl clienteServicio;
	@Autowired
    private Gson jsonConverter;
	
	private String sMensaje;
	
	
	@RequestMapping(value = "imprimirFactura", method = RequestMethod.GET)
	@ResponseBody
    public void factura(HttpServletResponse response) {
		FacturaModel facturaModel = jsonConverter.fromJson(sMensaje, FacturaModel.class);
		ClienteModel clienteModel = clienteServicio.buscarCliente(facturaModel.getsRucCliente());
		facturaModel.setsNombreCliente(clienteModel.getsApellidoPaterno() + " " + clienteModel.getsApellidoMaterno() + ", " + clienteModel.getsNombre());
		facturaModel.setsDireccionCliente(clienteModel.getsDireccion());
		
		Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("dataDetalleOrdenCompra", new JRBeanCollectionDataSource(facturaModel.getlDetalleCompra()));
        try {
			parameters.put("sFechaFactura", formatoFecha.format(formatter1.parse(facturaModel.getFechaFactura())).replace(" ", " de "));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        //parameters.put("sFechaFactura", facturaModel.getFechaFactura());
        parameters.put("sRucEmpresa", "20602775683");

        List<FacturaModel> lFactura = new ArrayList<>();
        lFactura.add(facturaModel);
        
		try {
			InputStream employeeReportStream = getClass().getResourceAsStream("/jasper/rpt_factura.jrxml");
			JasperReport jasperReport = JasperCompileManager.compileReport(employeeReportStream);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JRBeanCollectionDataSource(lFactura));
			
			JasperViewer.viewReport(jasperPrint, false);
		} catch (JRException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

    }
	
	@KafkaListener(groupId = "cuent", topics = "cuentas")
	public void listenerKafka1(String ordenCompra) {
		sMensaje = ordenCompra;
	}
	
	@PostMapping("/recibirFactura")
	public void mostrarValidacionOrdenCompra(HttpServletRequest request, HttpServletResponse response) {
		sMensaje = "";
		
		while(sMensaje.compareTo("")==0) {
			
		}
		
		FacturaModel facturaModel = jsonConverter.fromJson(sMensaje, FacturaModel.class);
		ClienteModel clienteModel = clienteServicio.buscarCliente(facturaModel.getsRucCliente());
		facturaModel.setsNombreCliente(clienteModel.getsApellidoPaterno() + " " + clienteModel.getsApellidoMaterno() + ", " + clienteModel.getsNombre());
		facturaModel.setsDireccionCliente(clienteModel.getsDireccion());
				
		PrintWriter out;
		try {
			out = response.getWriter();
			StringBuilder tablaSelecionado = mostrarFactura(facturaModel);
			
			String mensajeSalida = tablaSelecionado.toString();
			out.write(mensajeSalida);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private StringBuilder mostrarFactura(FacturaModel facturaModel) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-md-8 text-center\">");
		sb.append("</div>");
		sb.append("<div class=\"col-md-4 text-center\">");
		sb.append("<h4>20602775683");
		sb.append("</h4>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-md-8 text-center\">");
		sb.append("<h4>Tienda Sistemas Distribuidos SAC");
		sb.append("</h4>");
		sb.append("</div>");
		sb.append("<div class=\"col-md-4 text-center\">");
		sb.append("<h4>Factura");
		sb.append("</h4>");
		sb.append("</div>");		
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-md-8 text-center\">");
		sb.append("</div>");
		sb.append("<div class=\"col-md-4 text-center\">");
		sb.append("<h4>" + facturaModel.getnCodigoOrden());
		sb.append("</h4>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-md-12 text-center\">");
		try {
			sb.append("<h5 align=\"left\">Lima,"+ formatoFecha.format(formatter1.parse(facturaModel.getFechaFactura())).replace(" ", " de ") +"</h5>");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		sb.append("</div>");
		sb.append("</div>");
		
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-md-8 text-center\">");
		sb.append("<h5 align=\"left\">Se&ntilde;or (es) " + facturaModel.getsNombreCliente());
		sb.append("</h5>");
		sb.append("</div>");
		sb.append("<div class=\"col-md-4 text-center\">");
		sb.append("<h5>RUC: " + facturaModel.getsRucCliente());
		sb.append("</h5>");
		sb.append("</div>");
		sb.append("</div>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-md-12 text-center\">");
		sb.append("<h5 align=\"left\">Direcci&oacute;n: "+ facturaModel.getsDireccionCliente());
		sb.append("</h5>");
		sb.append("</div>");
		sb.append("</div>");
		
		//sb.append("<p align=\"left\">Lima,"+ facturaModel.getFechaFactura() +"</p>");
		
		sb.append("<div class=\"row\">");
		sb.append("<div class=\"col-md-12 text-center\">");
		
		sb.append("<table id=\"" + "tablaFactura" + "\" class=\"table\" >");
		sb.append("<thead class=\"table-primary\" align=\"center\">");
		sb.append("<tr>");
		sb.append("<th class=\"align-middle\" align=\"center\">Nombre</th>");
		sb.append("<th class=\"align-middle\" align=\"center\">Cantidad</th>");
		sb.append("<th class=\"align-middle\" align=\"center\">Valor de venta (en soles)</th>");
		sb.append("</tr>");
		sb.append("</thead>");
		

		for(OrdenCompraDetalleModel auxiliar : facturaModel.getlDetalleCompra()) {
			sb.append("<tr>");
			sb.append("<td class=\"align-middle\" align=\"center\">" + auxiliar.getsNombreProducto() + "</td>");
			sb.append("<td class=\"align-middle\" align=\"center\">" + auxiliar.getnCantidadProducto() + "</td>");
			sb.append("<td class=\"align-middle\" align=\"center\">" + auxiliar.getnTotalParcial()+ "</td>");
			sb.append("</tr>");
		}
		sb.append("<tr>");
		sb.append("<td> </td>");
		sb.append("<td class=\"align-middle\" align=\"center\">Total</td>");
		sb.append("<td class=\"align-middle\" align=\"center\">" + calcularSumaTotales(facturaModel.getlDetalleCompra()) + "</td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("</div>");
		sb.append("</div>");
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

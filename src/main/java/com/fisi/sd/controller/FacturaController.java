package com.fisi.sd.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
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
	private Date date;
	private SimpleDateFormat formatoFecha = new SimpleDateFormat("dd MMMM yyyy");
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
        parameters.put("sFechaFactura", formatoFecha.format(date).replace(" ", " de "));
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
		
		date = Calendar.getInstance().getTime();
		
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
		sb.append("<table border=\"2px\">");
		sb.append("<tr>");
		sb.append("<td> </td>");
		sb.append("<td> </td>");
		sb.append("<td> </td>");
		sb.append("<td> </td>");
		sb.append("<td style=\"text-align: center; border=\"2px\";\">20602775683</td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td style=\"text-align: left;\">Tienda Sistemas Distribuidos SAC</td>");
		sb.append("<td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </td>");
		sb.append("<td> </td>");
		sb.append("<td> </td>");
		sb.append("<td style=\"text-align: center;\">Factura</td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td> </td>");
		sb.append("<td> </td>");
		sb.append("<td> </td>");
		sb.append("<td> </td>");
		sb.append("<td style=\"text-align: center;\">n°"+facturaModel.getnCodigoOrden()+"</td>");
		sb.append("</tr>");
		sb.append("</table>");
		
		sb.append("<p align=\"left\">Lima,"+ formatoFecha.format(date).replace(" ", " de ") +"</p>");
		
		sb.append("<table>");
		sb.append("<tr>");
		sb.append("<td style=\"text-align: left;\">Se&ntilde;or (es) "+ facturaModel.getsNombreCliente() +"</td>");
		sb.append("<td> </td>");
		sb.append("<td> </td>");
		sb.append("<td style=\"text-align: rigth;\">RUC: "+ facturaModel.getsRucCliente() +"</td>");
		sb.append("<tr>");
		sb.append("<td style=\"text-align: left;\">Direcci&oacute;n: "+ facturaModel.getsDireccionCliente() +"</td>");
		sb.append("<td> </td>");
		sb.append("<td> </td>");
		sb.append("<td> </td>");
		sb.append("</tr>");
		sb.append("</table>");
		
		
		sb.append("<table id=\"" + "tablaFactura" + "\" class=\"table table-hover table-striped bdt\" >");
		sb.append("<th>Nombre</th>");
		sb.append("<th style=\"text-align: center;\">Cantidad</th>");
		sb.append("<th style=\"text-align: center;\">Valor de venta (en soles)</th></tr>");

		for(OrdenCompraDetalleModel auxiliar : facturaModel.getlDetalleCompra()) {
			sb.append("<tr>");
			sb.append("<td>" + auxiliar.getsNombreProducto() + "</td>");
			sb.append("<td style=\"text-align: center;\">" + auxiliar.getnCantidadProducto() + "</td>");
			sb.append("<td style=\"text-align: center;\">" + auxiliar.getnTotalParcial()+ "</td>");
			sb.append("</tr>");
		}
		sb.append("<tr>");
		sb.append("<td> </td>");
		sb.append("<td style=\"text-align: center;\">Total</td>");
		sb.append("<td style=\"text-align: center;\">" + calcularSumaTotales(facturaModel.getlDetalleCompra()));
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

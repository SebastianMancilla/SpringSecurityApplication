package com.bolsadeideas.springboot.app.view.xlsx;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.bolsadeideas.springboot.app.models.entity.Factura;
import com.bolsadeideas.springboot.app.models.entity.ItemFactura;

@Component("factura/ver.xlsx")
public class FacturaXlsxView extends AbstractXlsxView{

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        response.setHeader("Content-Disposition", "attachment; filename=\"factura_view.xlsx\"");
                
        Factura factura = (Factura) model.get("factura");
        Sheet sheet = workbook.createSheet("Factura Spring");

        //Primera fila datos clientes
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);

        cell.setCellValue("DATOS DEL CLIENTE");
        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getNombre() + " " + factura.getCliente().getApellido());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue(factura.getCliente().getEmail());

        //Creando una fila (encadenando los metodos)
        sheet.createRow(4).createCell(0).setCellValue("DATOS DE LA FACTURA");
        sheet.createRow(5).createCell(0).setCellValue("Folio: "+ factura.getId());
        sheet.createRow(6).createCell(0).setCellValue("Descripcion: "+ factura.getDescripcion());
        sheet.createRow(7).createCell(0).setCellValue("Fecha: " + factura.getCreateAt());

        CellStyle theaderStyle = workbook.createCellStyle();
        theaderStyle.setBorderBottom(BorderStyle.MEDIUM);       
        theaderStyle.setBorderLeft(BorderStyle.MEDIUM);     
        theaderStyle.setBorderRight(BorderStyle.MEDIUM);     
        theaderStyle.setBorderTop(BorderStyle.MEDIUM);
        theaderStyle.setFillForegroundColor(IndexedColors.GOLD.index);
        theaderStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle tbodyrStyle = workbook.createCellStyle();
        tbodyrStyle.setBorderBottom(BorderStyle.THIN);       
        tbodyrStyle.setBorderLeft(BorderStyle.THIN);     
        tbodyrStyle.setBorderRight(BorderStyle.THIN);     
        tbodyrStyle.setBorderTop(BorderStyle.THIN);

        Row header = sheet.createRow(9);
        header.createCell(0).setCellValue("Producto");
        header.createCell(1).setCellValue("Precio");
        header.createCell(2).setCellValue("Cantidad");
        header.createCell(3).setCellValue("Total");

        //Estilo
        header.getCell(0).setCellStyle(theaderStyle);
        header.getCell(1).setCellStyle(theaderStyle);
        header.getCell(2).setCellStyle(theaderStyle);
        header.getCell(3).setCellStyle(theaderStyle);
        

        //cada fial
        int rownum = 10;
        for(ItemFactura item : factura.getItems()){
            Row fila = sheet.createRow(rownum++);
            cell = fila.createCell(0);
            cell.setCellValue(item.getProducto().getNombre());
            cell.setCellStyle(tbodyrStyle);

            cell = fila.createCell(1);
            cell.setCellValue(item.getProducto().getPrecio());
            cell.setCellStyle(tbodyrStyle);

            cell = fila.createCell(2);
            cell.setCellValue(item.getCantidad());
            cell.setCellStyle(tbodyrStyle);

            cell = fila.createCell(3);
            cell.setCellValue(item.calcularImporte());
            cell.setCellStyle(tbodyrStyle);
        }

        Row filaTotal = sheet.createRow(rownum);
        filaTotal.createCell(2).setCellValue("Gran Total: ");
        filaTotal.createCell(3).setCellValue(factura.getTotal());
        
    }

    
}

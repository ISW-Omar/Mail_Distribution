/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author QuatSolutions SC
 */
public class Excel {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        
      try {
            String rutaArchivoExcel = "C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto\\Token\\Tokenizador editar\\Wiki\\detalle20190128.xlsx";
            FileInputStream inputStream = new FileInputStream(new File(rutaArchivoExcel));
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator iterator = firstSheet.iterator();
           // String datos [][] = new String();
            //ArrayList<String> guardar_palabras = new ArrayList<String>();
            DataFormatter formatter = new DataFormatter();
            while (iterator.hasNext()) {
                Row nextRow = (Row) iterator.next();
                Iterator cellIterator = nextRow.cellIterator();
                while(cellIterator.hasNext()) {
                    Cell cell = (Cell) cellIterator.next();
                    String contenidoCelda = formatter.formatCellValue(cell);
                    System.out.println("celda: " + contenidoCelda);
                   // guardar_palabras.add(contenidoCelda);
                }
                
            }
           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 *
 * @author QuatSolutions SC
 */
public class Leerxls {
    
    
    
    public static void main(String[] args) {
        File archivo = new File("C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto Zurich - Evidencia\\exportar\\18-02-2019\\Excel\\resumenPolizas20190101.xls");
        int polizasGeneradas=obtenerPolizas(archivo);
        System.out.println("Polizas Generadas:\t"+polizasGeneradas);
    }
    public static int obtenerPolizas(File archivo){
        int polizasGeneradas=0;
        ArrayList<String> datos = new ArrayList();
        
         try{
             Runtime.getRuntime().exec("cmd /c start C:\\Users\\QuatSolutions SC\\Desktop\\Respaldo_diario.bat");
            InputStream fileInputStream = new FileInputStream(archivo);
            HSSFWorkbook libro = new HSSFWorkbook(fileInputStream);
            HSSFSheet hoja=libro.getSheetAt(0);
            Iterator filas = hoja.rowIterator();
            while (filas.hasNext()){
                HSSFRow hfila; 
                hfila=(HSSFRow) filas.next();
		Iterator iterador = hfila.cellIterator();
                List celdatemp = new ArrayList();
                while(iterador.hasNext()){
                    HSSFCell celda=(HSSFCell) iterador.next();
                    celdatemp.add(celda);
                }
                datos.add(celdatemp.toString());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
         String polizas="";
         for (int i = 0; i <datos.size(); i++) {
             //System.out.println(i+"\t"+datos.get(i));
             if (datos.get(i).contains("Pólizas Generadas")) {
                 String [] dato=datos.get(i).split(",");
                  polizas=dato[1].replace("]", "").replace(" ", "");
             }
        }
        polizasGeneradas=Integer.parseInt(polizas);
        return polizasGeneradas;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;

import com.opencsv.CSVReader;
import static excel.LeerExcel.cargarDatos;
import static excel.LeerExcel.conexion;
import static excel.LeerExcel.crearTabla;
import static excel.LeerExcel.limpiar_palabra;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author QuatSolutions SC
 */
public class pruebas {
    public static final String SEPARADOR = ",";
    public static void renombrarArchivo(String archivofuente){
        File archivo1 = new File(archivofuente);
        File archivo2 = new File("archivo_2.xlsx");
        boolean renombrado=archivo1.renameTo(archivo2);
        if(renombrado){
            System.out.println("cambio de nombre exitoso");
        }else{
            System.out.println("no se pudo reonmbrear");
        } 
        String nuevo_archivo=archivo2.getName();
        //return nuevo_archivo;
}
    public static void main(String[] args) throws Exception {
        System.out.println("--------------");
        //File archivo = new File("C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto\\Token\\Tokenizador editar\\Wiki\\detalle20190128.xls");
        File archivo = new File("D:\\exportar\\detalle20190128.xls");
        File archivo2 = new File("archivo_2.xlsx");
        boolean renombrado=archivo.renameTo(archivo2);
        if(renombrado){
            System.out.println("cambio de nombre exitoso");
        }else{
            System.out.println("no se pudo reonmbrear");
        } 
        String nombre=archivo.getName();
        char [] nombre_cadena= nombre.toCharArray();
        if(nombre_cadena[nombre_cadena.length-1]=='s'){
            System.out.println("es un archivo xls");
            renombrarArchivo(nombre);
            //System.out.println(nuevo);
        }else{
            System.out.println("es un archivo xlsx");
        }
        System.out.println("----------");
        System.out.println(nombre);
        System.out.println("----------");
        
        
        if(archivo.exists()){
            pruebas leer= new pruebas(archivo);
            //LeerExcel leer = new LeerExcel(archivo);
            
        }
        System.out.println("--------------");
        String hora=obtenerHora();
        String miarray[] = {"A","Vlasdfa","I","L","A"};
        ArrayList<String> list = new ArrayList<>();
        StringBuffer cadena = new StringBuffer();
        for (int i = 0; i < miarray.length; i++) {
            System.out.print(miarray[i]);
            cadena=cadena.append(miarray[i]);
        }
        System.out.println("\n  "+cadena);
        list.add("abjha");
        list.add("bwhchs");
        System.out.println(list);
        StringBuffer listString=new StringBuffer();
        for(String string:list)
             listString=listString.append(String.valueOf(string)); 
        System.out.println(listString);
        
        String pa="Omar\"Torres\'";
        System.out.println(pa.length());
        String muestra=limpiar_palabra(pa);
        System.out.println(muestra);
        System.out.println("\n\n\n-------------");
        System.out.println(hora);
        System.out.println("--------------");
        
        String correo =" 5478654469 ";
        String prueba="1981.0";
        String p="126._hol";
        System.out.println("---+++-+++-+---+-");
        System.out.println(correo+ "\t"+correo.length());
        System.out.println(quitar_espacios(correo)+ "\t"+quitar_espacios(correo).length());
        System.out.println("****************");
        String name="Torres Flores Omar Juan";
        String mail="bcapymes02@santander.com";
        System.out.println("\t\t"+reacomodoNombre(name)+"\t\toriginal "+name);
        String email="xx@xx.xx";
        System.out.println(validaCorreo(name, mail));
        float f=Float.parseFloat(prueba);
       int numero=(int) f;
       String regresa=""+numero;
       // System.out.println(regresa);
        if (prueba.contains("."))  {
           // System.out.println("Es un numero");
        }else{
         //   System.out.println("No lo es");
        }

            
        Pattern pat = Pattern.compile("[a-zA-Z]+");
         Matcher mat = pat.matcher(p);
         if (mat.find()) {
         //    System.out.println("******si tiene");
        }
     if (mat.matches()) {
        // System.out.println("SI tiene");
     } else {
        // System.out.println("NO");
     }
     
        System.out.println("\n\n");
        System.out.println(email);
        System.out.println(isValidEmail(email));
        System.out.println(isValidEmailAddress(email));
        
        BufferedReader red = new BufferedReader(new FileReader("Reporte Red.csv"));
        BufferedReader canales = new BufferedReader(new FileReader("Reporte Canales Alternos.csv"));
        CargarReportes(red, canales);
        
        
  
        
    }
    public static void CargarReportes(BufferedReader red, BufferedReader canales){
             ArrayList<String> camp = new ArrayList();
             ArrayList<String> camred = new ArrayList();
             ArrayList<String> camcanales = new ArrayList();
             ArrayList<String> union = new ArrayList();
             ArrayList<Integer> cantidad = new ArrayList();
             ArrayList<String> data = new ArrayList();
             ArrayList<String> consultas=new ArrayList();
         
            try {
             int tam=0;
             String linea = red.readLine();
             int aux=0;
             while (linea != null) {
              String[] campos = linea.split(SEPARADOR); 
              String pal=Arrays.toString(campos);
             camred.add(pal);
              linea = red.readLine();
                 aux++;
             }
                for (int i = 0; i < camred.size(); i++) {
                    union.add(camred.get(i));
                }
             String lincam = canales.readLine();
             while (lincam != null) {
              String[] campos = lincam.split(SEPARADOR); 
              String pal=Arrays.toString(campos);
             camcanales.add(pal);
              lincam = canales.readLine();
                 
             }
                for (int i = 1; i < camcanales.size(); i++) {
                    //System.out.println(camcanales.get(i));
                    union.add(camcanales.get(i));
                }
//                
                for (int i = 0; i < union.size(); i++) {
                    
                    String[] campos = union.get(i).split(SEPARADOR);
                    String cons="select *from detalle where canal=\""+campos[0].replace("[", "")+"\" AND ramo=\""+campos[1].replace(" ","")+"\" AND poliza=\""+campos[3].replace(" ","")+"\";";
                    System.out.println(cons);
                    consultas.add(cons);
                    if (campos.length==48) {
                        for (int j = 0; j < campos.length; j++) {
                            data.add(campos[j]);
                        }
                    }else{
                        for (int j = 0; j < campos.length; j++) {
                            if (j==5) {
                                data.add(campos[j]+campos[j+1]);
                            }
                            else{
                                if(j!=6){
                                    data.add(campos[j]);
                                }
                            }
                        }
                    }
                   
                    
                }
                System.out.println(data.size());  
                System.out.println("red:"+camred.size()+"\tcanales:"+camcanales.size()+"\t union:"+union.size()+"\tmalos"+cantidad.size()+"\t"+data.size()); 
                for (int i = 0; i < consultas.size(); i++) {
                    System.out.println(consultas.get(i));
                    
                }
            } 
            catch (IOException e) {
             e.printStackTrace();
            }
            finally {
             // Cierro el buffer de lectura
             if (red != null) {
              try {
               red.close();
              } 
              catch (IOException e) {
               e.printStackTrace();
              }
             }
            }   
  }
    
    public static ArrayList consulta (String consul) throws Exception{
       String consulta=consul;
       int nombre_correo=0;
       ArrayList lista=new ArrayList();
       ArrayList lista_salida=new ArrayList();
       
       
       
       
       
       
       //ArrayList salida=new ArrayList();
       try{
           Connection con =conexion();
           PreparedStatement crear =con.prepareStatement(consulta);
           ResultSet resultado= crear.executeQuery();
           System.out.println(resultado);
           for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
              // System.out.println("\t\t\t\t\tt\t\t\t\t\t\t\tCampos---------------------------------------------");
                   System.out.print(resultado.getMetaData().getColumnName(i)+ ",");
                   lista.add(resultado.getMetaData().getColumnName(i)+ ",");
                   
               }
           
           if(lista.size()==43){
               for (int i = 0; i < lista.size()-2; i++) {
                       lista_salida.add(lista.get(i));
           }
               lista_salida.add("ID,");
               lista_salida.add("SEGMENTO,");
           }else{
               for (int i = 0; i < lista.size()-1; i++) {
                       lista_salida.add(lista.get(i));
           }
               lista_salida.add("ID,");
           }
           lista.add("\r\n");
           lista_salida.add("\r\n");
           System.out.println("\t\t\t\t\tt\t\t\t\t\t\t\tCampos---------------------------------------------"+lista_salida);
           System.out.println("");
           while(resultado.next()){
               for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                   System.out.print(resultado.getString(i)+",");
                   lista_salida.add(resultado.getString(i)+",");
               }
               
               lista_salida.add("\r\n");
               System.out.println("");
               
           }
           /////borrar----------
           
           for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
              // System.out.println("\t\t\t\t\tt\t\t\t\t\t\t\tCampos---------------------------------------------");
               if (resultado.getMetaData().getColumnName(i)=="CORREO_ELECTRONICO") {
                   
               }
                   System.out.print(resultado.getMetaData().getColumnName(i)+ ",");
                   lista.add(resultado.getMetaData().getColumnName(i)+ ",");

               }
           while(resultado.next()){
               ArrayList salida_correo=new ArrayList();
               //resultado.getMetaData().getColumnName(0)
               for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                   
                   salida_correo.add(resultado.getString(i));
               }
             //  if()
               //salida_correo.add(lista_salida);
               lista_salida.add("\r\n");
               System.out.println("");
               
           }//////borrar----------
           
           
       }catch(Exception e){
           System.out.println("Error: "+e);
       }
       return lista_salida;
   }
    
    public static boolean isValidEmail(String email) {
             String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
             java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
             java.util.regex.Matcher m = p.matcher(email);
             return m.matches();
       }
    
    public static boolean isValidEmailAddress(String email) {
             boolean result = true;
             try {
                    InternetAddress emailAddr = new InternetAddress(email);
                    emailAddr.validate();
             } catch (AddressException ex) {
                    result = false;
             }
             return result;

       }
    public static String reacomodoNombre(String nombre){
         String cambio = "";
         if (nombre.contains("/")) {
             System.out.println(nombre);
             String[] campos = nombre.split("/"); 
              cambio=campos[2]+" "+campos[0]+" "+campos[1];
         }else{
             cambio=nombre;
         }
             return cambio;
    }
    
    
    public static boolean validaCorreo(String nombre, String email){

             boolean flag = false;
             System.out.println(email);
             System.out.println(nombre);

             if(email.contains("santander.")) {
                    nombre = nombre.toLowerCase();
                    System.out.println(nombre);
                    String[] nombres = nombre.split(" ");
                    String emailName = email.substring(0,email.indexOf("@"));
                    System.out.println(emailName);
                    for(String param: nombres){
                          System.out.println(param + " in " + emailName);
                          flag = emailName.contains(param);
                          if(flag) {
                                 System.out.println(email + " is valid");
                                 break;
                          }
                    }
             }
             return flag;
       }
    
    public static String limpiar_palabra(String cadena){
        char [] palabrachar= new char[cadena.length()];
        palabrachar=cadena.toCharArray();
        List conversion=new ArrayList();
        
        for (int i = 0; i < palabrachar.length; i++) {
            if((palabrachar[i]=='\"')|| (palabrachar[i]=='\'')){
                conversion.add(" ");
            }else{
                conversion.add(palabrachar[i]);
            }
        }
        System.out.println(conversion);
        StringBuffer conv=new StringBuffer();
        for(Object string:conversion)
             conv=conv.append(String.valueOf(string)); 
        String palabra_convertida=conv.toString();
        
        return palabra_convertida;
    }
    public ArrayList obtenerDatosXLS (List listaDatos){
        ArrayList<String> valores = new ArrayList<String>();
        int aux=0;
        for (int i = 0; i < listaDatos.size(); i++) {
            aux++;
            //if(aux=)
        }
        for (int i = 2; i < listaDatos.size(); i++) {
            //System.out.println("");
            List filas =  (List) listaDatos.get(i);
            for (int j = 0; j < filas.size(); j++) {
                //XSSFCell  columnas = (XSSFCell) filas.get(j);
                HSSFCell columnas=(HSSFCell) filas.get(i);
                String datos = columnas.toString();
                String cadena=limpiar_palabra(datos);
                valores.add(cadena);
                //System.out.print(datos+" ");
            }
        }
        return valores;
    }
//    public ArrayList obtenerCampos(List campo_bd){ 
//        ArrayList<String> campos = new ArrayList<String>();
//        for (int i = 1; i < campo_bd.size(); i++) {
//            
//            List filas =  (List) campo_bd.get(i);
//            for (int j = 0; j < filas.size(); j++) {
//                XSSFCell  columnas = (XSSFCell) filas.get(j);
//                String datos = columnas.toString();
//                if(i==1){
//                    campos.add(datos);
//                }
//            }
//        }
//        for (int i = 0; i < campos.size(); i++) {
//            String campo= campos.get(i);
//            String nueva_palabra=unirPalabras(campo);
//            campos_db.add(nueva_palabra);
//        }
//        System.out.println(campos_db);
//        return campos_db;
//    }
    
    public static String  quitar_espacios(String palabra){
        char [] palabrachar= new char[palabra.length()];
        palabrachar=palabra.toCharArray();
        List conversion=new ArrayList();
        for (int i = 0; i < palabrachar.length; i++) {
            if((palabrachar[i]==' ')|| (palabrachar[i]==' ') ){
                
            }else{
                conversion.add(palabrachar[i]);
            }
        }
        StringBuffer conv=new StringBuffer();
        for(Object string:conversion)
             conv=conv.append(String.valueOf(string)); 
        String palabra_convertida=conv.toString();
        return palabra_convertida;   
    }
    
    public static String obtenerHora(){
        String hora;
         int dia=0, mes=0, año=0, minutos=0, segundos=0, horas=0;  
        Calendar calendario = Calendar.getInstance();
        año=calendario.get(Calendar.YEAR);
        
        mes=calendario.get(Calendar.MONTH)+1;
        dia=calendario.get(Calendar.DAY_OF_MONTH);
        horas =calendario.get(Calendar.HOUR_OF_DAY);
        minutos = calendario.get(Calendar.MINUTE);
        segundos = calendario.get(Calendar.SECOND);
        
        hora=dia+"-"+mes+"-"+año+"--"+horas+"-"+minutos+"-"+segundos;
        return hora;
    }
    public pruebas(File nombreArchivo) throws Exception{
        List datos = new ArrayList();
        
        try{
            InputStream fileInputStream = new FileInputStream(nombreArchivo);
            HSSFWorkbook libro = new HSSFWorkbook(fileInputStream);
            HSSFSheet hoja=libro.getSheetAt(0);
            Iterator filas = hoja.rowIterator();
            
            while (filas.hasNext()){
                HSSFRow hfila=(HSSFRow) filas.next();
		Iterator iterador = hfila.cellIterator();
                List celdatemp = new ArrayList();
                while(iterador.hasNext()){
                    HSSFCell celda=(HSSFCell) iterador.next();
                    String cel=celda.toString();
                    celdatemp.add(celda);
                   // System.out.print(cel+", ");
                }
                
                datos.add(celdatemp);
                //System.out.println("");
            }
            for (int i = 2; i < datos.size(); i++) {
                List list = (List) datos.get(i);
                for (int j = 0; j < list.size(); j++) {
                    
//                    System.out.print("_________(");
                    System.out.print(".."+j+"..");
//                    System.out.print(")_________");
                    Cell cell = (Cell) list.get(j);
                     if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                         System.out.print(cell.getNumericCellValue());

                     } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                         System.out.print(cell.getRichStringCellValue());
                     }else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                         System.out.print(cell.getBooleanCellValue());
                     }else if(list.get(19)=="AUTOCOMPARA"){
                         list.set(19, " yupi");
                         System.out.print(" ");
                     }
                         System.out.println("xxxxxxxxx"+list.get(19));
                     if (j < list.size() - 1) {
                         System.out.print(", ");
                     }             
                }
                System.out.println("");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("el tamaño es "+datos.size()+"\n");
        int contador=0;
        for (int i = 0; i < datos.size(); i++) {
            contador++;
            if (contador<=41) {
               // System.out.print(datos.get(i)+", ");
            }else{
              //  System.out.println("");
                contador=0;
            }
        }
        System.out.println(datos.get(datos.size()-1));
        //datos_bd=obtenerDatos(datos);
        //dat=obtenerCampos(datos);
        
       // String campos_b=campos_cad(datos);
       // cadena_campo(dat);
       // crearQuery(campos_b, datos_bd, dat);
        //salida(dat, datos_bd);
        //crearTabla(dat);
        //cargarDatos(dat, datos_bd);

    }
    public static void readDataLineByLine(String file) 
{ 
  
    try { 
  
        // Create an object of filereader 
        // class with CSV file as a parameter. 
        FileReader filereader = new FileReader(file); 
  
        // create csvReader object passing 
        // file reader as a parameter 
        CSVReader csvReader = new CSVReader(filereader); 
        String[] nextRecord; 
  
        // we are going to read data line by line 
        while ((nextRecord = csvReader.readNext()) != null) { 
            for (String cell : nextRecord) { 
                System.out.print(cell + "\t"); 
            } 
            System.out.println(); 
        } 
    } 
    catch (Exception e) { 
        e.printStackTrace(); 
    } 
}
}

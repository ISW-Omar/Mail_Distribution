package excel;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.DateUtil;

/**
 *
 * @author QuatSolutions SC
 */
public class G {
    ArrayList<String> campos = new ArrayList<String>();
    ArrayList<String> campos_db = new ArrayList<String>();
    ArrayList<String> dat = new ArrayList<String>();
    ArrayList<String> datos_bd = new ArrayList<String>();
    public static ArrayList<String> llave = new ArrayList<String>();
    public static int cregistros=0;
    public static int cmail=0;
    public static int csms=0;
    public static int comisiones_cregistros=0;
    public static int comisiones_cmail=0;
    public static int comisiones_csms=0;
    public static int comisiones_cdescartados=0;
    public static  String SEPARADOR = ",";
    public static int cdescartados=0;
    public static String NEWLINE = "<br/>";
    public static String outFolder = "C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto Zurich - Evidencia\\exportar\\18-02-2019\\Excel\\" + DateUtil.dateToString() + "\\out\\";
    
    public static void CargarReportes(BufferedReader red, BufferedReader canales) throws Exception{
             ArrayList<String> camp = new ArrayList();
             ArrayList<String> camred = new ArrayList();
             ArrayList<String> camcanales = new ArrayList();
             ArrayList<String> union = new ArrayList();
             ArrayList<Integer> cantidad = new ArrayList();
             ArrayList<String> data = new ArrayList();
             ArrayList<String> sin_registros = new ArrayList();
             ArrayList<String> consultas=new ArrayList();
             ArrayList<String> campB=new ArrayList();
             ArrayList<String> repetidos=new ArrayList();
             int archivo_red=0, archivo_canales=0;
         
            try {
             int tam=0;
             String linea = red.readLine();
             int aux=0, auxc=0;
             while (linea != null) {
              String[] campos = linea.split(SEPARADOR); 
              String pal=Arrays.toString(campos);
             camred.add(pal);
              linea = red.readLine();
                 aux++;
                 
             }
             archivo_red=aux-1;
             System.out.println("..."+archivo_red);
                for (int i = 0; i < camred.size(); i++) {
                    union.add(camred.get(i));
                }
             String lincam = canales.readLine();
             while (lincam != null) {
              String[] campos = lincam.split(SEPARADOR); 
              String pal=Arrays.toString(campos);
             camcanales.add(pal);
              lincam = canales.readLine();
                 auxc++;
             }
             archivo_canales=auxc-1;
                System.out.println("..."+archivo_canales);
                
                comisiones_cregistros=(aux+auxc);
                for (int i = 1; i < camcanales.size(); i++) {
                    //System.out.println(camcanales.get(i));
                    union.add(camcanales.get(i));
                }
//                
                for (int i = 0; i < union.size(); i++) {
                   // System.out.println("++++"+union.get(i));
                    String[] campos = union.get(i).split(SEPARADOR);
                    
                    String cons=campos[0].replace("[", "")+"-"+campos[1].replace(" ","")+"-"+campos[3].replace(" ","");
                    //System.out.println(cons);
                    consultas.add(cons);
                    if (campos.length==48) {
                        for (int j = 0; j < campos.length; j++) {
                            data.add(campos[j].replace("[", "").replace("]", ""));
                        }
                    }else{
                        for (int j = 0; j < campos.length; j++) {
                            if (j==5) {
                                data.add(campos[j]+campos[j+1]);
                            }
                            else{
                                if(j!=6){
                                    data.add(campos[j].replace("[", "").replace("]", ""));
                                }
                            }
                        }
                    }   
                }
                
                //System.out.println(data.size());  
               // System.out.println("red:"+camred.size()+"\tcanales:"+camcanales.size()+"\t union:"+union.size()+"\tmalos"+cantidad.size()+"\t"+data.size()); 
                Connection con =conexion();
                int cont=0;
                String from="select canal, ramo, poliza from detalle;";
                PreparedStatement verificar =con.prepareStatement(from);
                ResultSet resultado=verificar.executeQuery();
                resultado.next();
                //System.out.println("esta consulta"+resultado.next());
                ArrayList<String> obtenerConsulta= new ArrayList();
                obtenerConsulta=consulta_llave(from);
                
                for (int i = 0; i < llave.size(); i++) {
                  //  System.out.println("consultas: "+llave.get(i));
                    for (int j = 0; j < consultas.size(); j++) {
                        if (llave.get(i).equals(consultas.get(j))) {
                            cantidad.add(j);
                    //        System.out.println("********------"+j);
                        }
                        
                    }
                }
                
                
                
                
                
                String camposBase=unirPalabras(union.get(0));
                String[] campos = camposBase.split(SEPARADOR);
                String var="varchar(255)";
                String var2="PRIMARY KEY (REPORTES_ID));";
                String cadena="CREATE TABLE IF NOT EXISTS REPORTES(REPORTES_ID INT NOT NULL AUTO_INCREMENT, ";
                ArrayList<String> cade=new ArrayList();
                StringBuffer lista=new StringBuffer();

                for (int i = 0; i < campos.length; i++) {
                    campB.add(campos[i].replace("[", "").replace("]", ""));
                    String dato=campos[i].replace("[", "").replace("]", "")+" "+var+",";
                    cade.add(dato);
                    
                }
                for (int i = 0; i < data.size(); i++) {
                    if (i<campB.size()) {
                        
                    }else{
                        repetidos.add(data.get(i));
                    }
                }
                for(String string:cade)
                    lista=lista.append(String.valueOf(string)); 
                String que=cadena+lista+var2;
                System.out.println(que+cadena+lista+var2);
                PreparedStatement cargarTabla =con.prepareStatement(que);
                    cargarTabla.executeUpdate();
                if (archivo_canales!=0 || archivo_red!=0) {
                    insertarReportes(campB, repetidos, cantidad);
                }else{
                        System.out.println("EL archivo de red o el archivo de canales alternos esta vacio");
                }

                
                
            } 
            catch (IOException e) {
             e.printStackTrace();
            }
            finally {
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
    
    
    
    public static void salida_csv(ArrayList datos, String nombre){
        String hora=obtenerHora();
        try {
                File archivo = new File(outFolder + nombre+hora+".csv");
		//FileOutputStream out = new FileOutputStream("consultas\\"+hora+"consulta.csv");
               Writer escribir = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(archivo), "UTF-32"));
		for (int i = 0; i < datos.size(); i++) {
                       escribir.write(datos.get(i).toString());
                    }
                escribir.close();
	    } 
	    catch (IOException e) 
	    {
			System.out.println(e.getMessage());
	    }
	
    }
    public static void  ssalida_csv (ArrayList datos){
        String hora=obtenerHora();
        FileWriter fichero = null;
		try {
                    fichero = new FileWriter(outFolder + hora+"consulta.csv");
                    for (int i = 0; i < datos.size(); i++) {
                        fichero.write((String) datos.get(i));
                    }
			fichero.close();
		} catch (Exception ex) {
			System.out.println("Mensaje de la excepciÃ³n: " + ex.getMessage());
		}   
    }
    public void salida ( ArrayList campos, ArrayList datos){
        FileWriter fichero = null;
		try {
                    fichero = new FileWriter("fichero_escritura.csv");
                    int aux=0;
                    for (int i = 0; i < datos.size(); i++) {
                        aux++;
                        if (aux==campos.size()) {
                            fichero.write("\r\n");
                            aux=0;
                        }else{
                            String palabra= (String) datos.get(i);
                            fichero.write(palabra+", ");
                        }
                }
			fichero.close();
		} catch (Exception ex) {
			System.out.println("Mensaje de la excepciÃ³n: " + ex.getMessage());
		}   
    }
    public G(File nombreArchivo, int uno) throws Exception{
        List datos = new ArrayList();
        
         try{
            InputStream fileInputStream = new FileInputStream(nombreArchivo);
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
                datos.add(celdatemp);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
	datos_bd=obtenerDatosXLS(datos);
        dat=obtenerCamposXLS(datos);
     

        crearTabla(dat);
        cargarDatos(dat, datos_bd);

    }
    public G(File nombreArchivo) throws Exception{
        List datos = new ArrayList();
        try{
            FileInputStream fileInputStream = new FileInputStream (nombreArchivo);
            XSSFWorkbook libro =new XSSFWorkbook(fileInputStream);
            XSSFSheet hoja = libro.getSheetAt(0);
            Iterator filas = hoja.rowIterator();
            int contador=0;
            while (filas.hasNext()){
                XSSFRow hfila = (XSSFRow) filas.next();
                Iterator iterador  = hfila.cellIterator();
                List celdatemp = new ArrayList();
                while(iterador.hasNext()){
                    XSSFCell celda  = (XSSFCell) iterador.next();
                    celdatemp.add(celda);
                    
                }
                datos.add(celdatemp);
                contador++;
            }
         cregistros=contador-2;
            
        }catch(Exception e){
            e.printStackTrace();
        }
        datos_bd=obtenerDatos(datos);
        int con=0, tem=0;
        
        for (int i = 0; i < datos_bd.size(); i++) {
            if (con<40) {
               // System.out.print(" "+datos_bd.get(i));
                tem++;
                if (con==3) {
                    llave.add(datos_bd.get(i)+"-"+datos_bd.get(i+1)+"-"+datos_bd.get(i+2));
                   // System.out.println(datos_bd.get(i)+"-"+datos_bd.get(i+1)+"-"+datos_bd.get(i+2));
                }
                
            }else{
               // System.out.println("");
            }
            con++;
            if(con==41){
                con=0;
            }
            
            
        }
        dat=obtenerCampos(datos);
        
        String campos_b=campos_cad(datos);
        crearTabla(dat);
        cargarDatos(dat, datos_bd);
    }
    public static void Comisiones( File red, File canales_alternos) throws Exception{
        List datosr = new ArrayList();
        List datosc = new ArrayList();
        ArrayList<String> obtenerd = new ArrayList<String>();
        try{
            FileInputStream rred = new FileInputStream (red);
            XSSFWorkbook libror =new XSSFWorkbook(rred);
            XSSFSheet hoja = libror.getSheetAt(0);
            Iterator filas = hoja.rowIterator();
            while (filas.hasNext()){
                XSSFRow hfila = (XSSFRow) filas.next();
                Iterator iterador  = hfila.cellIterator();
                List celdatemp = new ArrayList();
                while(iterador.hasNext()){
                    XSSFCell celda  = (XSSFCell) iterador.next();
                    celdatemp.add(celda);
                }
                datosr.add(celdatemp);
            }
            FileInputStream rcanales = new FileInputStream (canales_alternos);
            XSSFWorkbook libroc =new XSSFWorkbook(rcanales);
            XSSFSheet hojac = libroc.getSheetAt(0);
            Iterator filasc = hojac.rowIterator();
            while (filasc.hasNext()){
                XSSFRow hfila = (XSSFRow) filasc.next();
                Iterator iterador  = hfila.cellIterator();
                List celdatemp = new ArrayList();
                while(iterador.hasNext()){
                    XSSFCell celda  = (XSSFCell) iterador.next();
                    celdatemp.add(celda);
                }
                datosc.add(celdatemp);
            }
            ArrayList<String> campos = new ArrayList<String>();
        for (int i = 1; i < datosr.size(); i++) {
            List filascampos =  (List) datosr.get(i);
            for (int j = 0; j < filascampos.size(); j++) {
                XSSFCell  columnas = (XSSFCell) filascampos.get(j);
                String datos = columnas.toString();
                if(i==1){
                    campos.add(datos);
                }
            }
        }
            
            for (int i = 0; i < datosr.size(); i++) {
                List filasa =  (List) datosr.get(i);
                for (int j = 0; j < filasa.size(); j++) {
                    XSSFCell  columnas = (XSSFCell) filasa.get(j);
                    String datos = columnas.toString();
                    String cadena=limpiar_palabra(datos);
                    obtenerd.add(cadena);
                    //System.out.print(datos+" ");
                }
            }
            for (int i = 0; i < datosc.size(); i++) {
                List filasb =  (List) datosr.get(i);
                for (int j = 0; j < filasb.size(); j++) {
                    XSSFCell  columnas = (XSSFCell) filasb.get(j);
                    String datos = columnas.toString();
                    String cadena=limpiar_palabra(datos);
                    obtenerd.add(cadena);
                    //System.out.print(datos+" ");
                }
            }
            for (int i = 0; i < obtenerd.size(); i++) {
                for (int j = 0; j < campos.size(); j++) {
                  //  System.out.print(obtenerd.get(i)+", ");
                }
              //  System.out.println("");
            }
            
            
            
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //datos_bd=obtenerDatos(datos);
       // dat=obtenerCampos(datos);
        
       // String campos_b=campos_cad(datos);
       // crearTabla(dat);
       // cargarDatos(dat, datos_bd);
    }
    
    
    
    public static void cargarTablaGen(File nombreArchivo, String nombreBase, int numhoja) throws Exception{
    //////obtener datos
    List datos = new ArrayList();
        ArrayList <String> datosA= new ArrayList<String>();
        ArrayList <String> camposA= new ArrayList<String>();
        ArrayList <String> data= new ArrayList<String>();
        try{
            FileInputStream fileInputStream = new FileInputStream (nombreArchivo);
            XSSFWorkbook libro =new XSSFWorkbook(fileInputStream);
            XSSFSheet hoja = libro.getSheetAt(numhoja);
            Iterator filas = hoja.rowIterator();
            while (filas.hasNext()){
                XSSFRow hfila = (XSSFRow) filas.next();
                Iterator iterador  = hfila.cellIterator();
                List celdatemp = new ArrayList();
                while(iterador.hasNext()){
                    XSSFCell celda  = (XSSFCell) iterador.next();
                    celdatemp.add(celda);
                }
                datos.add(celdatemp);             
            }
        }catch(Exception e){
            e.printStackTrace();
        }
      //  System.out.println("ESta es el array");
        // System.out.println(datos);
         int tam=0;
         for (int i = 0; i < datos.size(); i++) {
             List filas =  (List) datos.get(i);
             tam=0;
             for (int j = 0; j < filas.size(); j++) {
                 XSSFCell  columnas = (XSSFCell) filas.get(j);
                String dat = columnas.toString();
                String cadena=limpiar_palabra(dat);
                String cad;
                Pattern pat = Pattern.compile("[a-zA-Z]+");
                Matcher mat = pat.matcher(cadena);
                 if ((cadena.contains(".")) && (!mat.find())) {
                     cad=convertirNumero(cadena);
                 }else{
                     cad=cadena;
                 }
                
                
               //  System.out.print(" "+cad);
                datosA.add(cad);
                tam++;
             }
             
           //  System.out.println("");
         }
         for (int i = 0; i < datosA.size(); i++) {
             if (i<tam) {
                 String pal=datosA.get(i);
                 camposA.add(unirPalabras(pal));
             }else{
                 data.add(datosA.get(i));
             }
        }
         
         ////extraer datos
         
         /////crear tabla
         String var="varchar(1000)";
        String var2="PRIMARY KEY ("+nombreBase+"_ID));";
        String cadena="CREATE TABLE IF NOT EXISTS "+nombreBase+"("+nombreBase+"_ID INT NOT NULL AUTO_INCREMENT, ";
        StringBuffer lista=new StringBuffer();
        ArrayList<String> cade=new ArrayList();
        for (int i = 0; i < camposA.size(); i++) {
            String dato=camposA.get(i)+" "+var+", ";
           // System.out.println(camposA.get(i));
            cade.add(dato);
        }
        for(String string:cade)
             lista=lista.append(String.valueOf(string)); 
        String que=cadena+lista+var2;
       // System.out.println("variable que"+que);
        try{
            
           Connection con =conexion();
           PreparedStatement crear =con.prepareStatement(que);
           crear.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
        /////Fin crear tabla
        /////carga de datos a la base
        
        String var2viejo="select *from "+nombreBase+" where "+nombreBase+"_ID=\"1\";";
        String cadenaviejo="INSERT INTO "+nombreBase;
        StringBuffer datoviejo=new StringBuffer();
        StringBuffer datocampo=new StringBuffer();
        ArrayList<String> datviejo=new ArrayList();
        ArrayList<String> campviejo=new ArrayList();
        int tamcamp=0;
        for (int i = 0; i < camposA.size(); i++) {
            if(tamcamp==0){
                String dato="("+camposA.get(i)+", ";
                campviejo.add(dato);
            }else{
                if(tamcamp==(tam-1)){
                    String dato=camposA.get(i)+") ";
                campviejo.add(dato);
                }
                else{
                    String dato=camposA.get(i)+", ";
                    campviejo.add(dato);
                }
            }
            tamcamp++;
            if(tamcamp==tam){
                    tamcamp=0;
            } 
        }
        for(Object string:campviejo)
            datocampo=datocampo.append(String.valueOf(string));
     
        StringBuffer campo_query=datocampo;
       // System.out.println("\t+\t+\t+\n+\n+\n\t\t\t+++++"+campo_query);
        
        int aux=0;
        for (int i = 0; i < data.size()-1; i++) {
            
            if(aux==0){
                String dato="(\""+data.get(i)+"\", ";
                datviejo.add(dato);
            }else{
                if(aux==(tam-1)){
                    String dato="\""+data.get(i)+"\"), ";
                datviejo.add(dato);
                }
                else{
                    String dato="\""+data.get(i)+"\", ";
                    datviejo.add(dato);
                }
            }
            aux++;
            if(aux==tam){
                    aux=0;
            } 
       }
        String tempfinal=(String) data.get(data.size()-1)+"\");";
       datviejo.add("\""+tempfinal);
       
        
        for(Object string:datviejo)
             datoviejo=datoviejo.append(String.valueOf(string));
        
        StringBuffer sin_coma=datoviejo;
        data.add(""+sin_coma);

        String query=cadenaviejo+campo_query+" VALUES "+sin_coma;
       // System.out.println("+++++AQUI es \\n\n\n\n"+query);
        try{
            
           Connection con =conexion();
           PreparedStatement verificar =con.prepareStatement(var2viejo);
           ResultSet resultado=verificar.executeQuery();
           // System.out.println("el comando es "+verificar);
            if (resultado.next()==true) {
           //     System.out.println("ya existen registros");
                
            }else{
                
               // System.out.println("Se estan cargando los datos");
                PreparedStatement crear =con.prepareStatement(query);
                crear.executeUpdate();
            }
           
        }catch(Exception e){
            System.out.println(e + query);
        }
        
        /////fin de carga
        
   
    }
    
    
     public static void LeerLista(File nombreArchivo) throws Exception{
        List datos = new ArrayList();
        ArrayList <String> datosA= new ArrayList<String>();
        try{
            FileInputStream fileInputStream = new FileInputStream (nombreArchivo);
            XSSFWorkbook libro =new XSSFWorkbook(fileInputStream);
            XSSFSheet hoja = libro.getSheetAt(0);
            Iterator filas = hoja.rowIterator();
            while (filas.hasNext()){
                XSSFRow hfila = (XSSFRow) filas.next();
                Iterator iterador  = hfila.cellIterator();
                List celdatemp = new ArrayList();
                while(iterador.hasNext()){
                    XSSFCell celda  = (XSSFCell) iterador.next();
                    celdatemp.add(celda);
                }
                datos.add(celdatemp);             
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        //System.out.println("ESta es el array");
        // System.out.println(datos);
         for (int i = 0; i < datos.size(); i++) {
             List filas =  (List) datos.get(i);
             for (int j = 0; j < filas.size(); j++) {
                 XSSFCell  columnas = (XSSFCell) filas.get(j);
                String dat = columnas.toString();
                String cadena=limpiar_palabra(dat);
                datosA.add(cadena);
             }
         }
       //  System.out.println(datosA);
        cargarListaNegra(datosA);
    }
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
    public static String limpiar_palabra(String cadena){
        char [] palabrachar= new char[cadena.length()];
        palabrachar=cadena.toCharArray();
        List conversion=new ArrayList();
        
        for (int i = 0; i < palabrachar.length; i++) {
            if((palabrachar[i]=='\"')|| (palabrachar[i]=='\'') || (palabrachar[i]==',')){
                conversion.add(" ");
            }else{
                conversion.add(palabrachar[i]);
            }
        }
        //System.out.println(conversion);
        StringBuffer conv=new StringBuffer();
        for(Object string:conversion)
             conv=conv.append(String.valueOf(string)); 
        String palabra_convertida=conv.toString();
        
        return palabra_convertida;
    }
    
    public static String convertirNumero(String palabra){
        // si tiene un punto parsea el numero 
        float numerof=Float.parseFloat(palabra);
        int numeroe=(int) numerof;
        String regresa=""+numeroe;
        return regresa;
        
    }
    public ArrayList obtenerDatos (List listaDatos){
        ArrayList<String> valores = new ArrayList<String>();
        for (int i = 2; i < listaDatos.size(); i++) {
            //System.out.println("");
            List filas =  (List) listaDatos.get(i);
            for (int j = 0; j < filas.size(); j++) {
                XSSFCell  columnas = (XSSFCell) filas.get(j);
                String datos = columnas.toString();
                String cadena=limpiar_palabra(datos);
                valores.add(cadena);
                //System.out.print(datos+" ");
            }
        }
        return valores;
    }
    public ArrayList obtenerDatosXLS (List listaDatos){
        ArrayList<String> valores = new ArrayList<String>();
        for (int i = 2; i < listaDatos.size(); i++) {
            List filas =  (List) listaDatos.get(i);
            for (int j = 0; j < filas.size(); j++) {
                HSSFCell columnas=(HSSFCell) filas.get(j);
                String datos = columnas.toString();
                String cadena=limpiar_palabra(datos);
                valores.add(cadena);
            }
            
        }
        return valores;
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
    
    public ArrayList obtenerCampos(List campo_bd){ 
        ArrayList<String> campos = new ArrayList<String>();
        for (int i = 1; i < campo_bd.size(); i++) {
            
            List filas =  (List) campo_bd.get(i);
            for (int j = 0; j < filas.size(); j++) {
                XSSFCell  columnas = (XSSFCell) filas.get(j);
                String datos = columnas.toString();
                if(i==1){
                    campos.add(datos);
                }
            }
        }
        for (int i = 0; i < campos.size(); i++) {
            String campo= campos.get(i);
            String nueva_palabra=unirPalabras(campo);
            campos_db.add(nueva_palabra);
        }
      //  System.out.println(campos_db);
        return campos_db;
    }
    public ArrayList obtenerCamposXLS(List campo_bd){ 
        ArrayList<String> campos = new ArrayList<String>();
        for (int i = 1; i < campo_bd.size(); i++) {
            
            List filas =  (List) campo_bd.get(i);
            for (int j = 0; j < filas.size(); j++) {
                HSSFCell  columnas = (HSSFCell) filas.get(j);
                String datos = columnas.toString();
                if(i==1){
                    campos.add(datos);
                }
            }
        }
        for (int i = 0; i < campos.size(); i++) {
            String campo= campos.get(i);
            String nueva_palabra=unirPalabras(campo);
            campos_db.add(nueva_palabra);
        }
       // System.out.println(campos_db);
        return campos_db;
    }
     public static String  unirPalabras(String palabra){
         String cadena ="";
         char [] cad = palabra.toCharArray();
         char [] nueva= new char [cad.length];
         
         for (int i = 0; i < cad.length; i++) {
             
             if(cad[i]==' ' || cad[i]=='-' || cad[i]=='.'){
                 nueva[i]='_';
             }else{
                 nueva[i]=cad[i];
             }
         }
         cadena=String.valueOf(nueva);
         return cadena;
     }
     public String campos_cad(List campo_bd){ 
        String cadena="";
        
        ArrayList<String> arr = new ArrayList<String>();
        for (int i = 1; i < campo_bd.size(); i++) {
            
            List filas =  (List) campo_bd.get(i);
            for (int j = 0; j < filas.size(); j++) {
                XSSFCell  columnas = (XSSFCell) filas.get(j);
                String datos = columnas.toString();
                if(i==1){
                    arr.add(datos);
                }
            }
        }
        String [] cadtemp= new String[arr.size()];

         for (int i = 0; i < cadtemp.length; i++) {
             String palabra=unirPalabras(arr.get(i));
             cadtemp[i]=palabra;
         }
         cadena=String.valueOf(arr);
        // System.out.println(cadena);
        return cadena;
    }
     public String campos_cadXLS(List campo_bd){ 
        String cadena="";
        
        ArrayList<String> arr = new ArrayList<String>();
        for (int i = 1; i < campo_bd.size(); i++) {
            
            List filas =  (List) campo_bd.get(i);
            for (int j = 0; j < filas.size(); j++) {
                HSSFCell  columnas = (HSSFCell) filas.get(j);
                String datos = columnas.toString();
                if(i==1){
                    arr.add(datos);
                }
            }
        }
        String [] cadtemp= new String[arr.size()];

         for (int i = 0; i < cadtemp.length; i++) {
             String palabra=unirPalabras(arr.get(i));
             cadtemp[i]=palabra;
         }
         cadena=String.valueOf(arr);
        // System.out.println(cadena);
        return cadena;
    }
    public static Connection conexion() throws Exception{
       try{
            String driver= "com.mysql.jdbc.Driver";
            String usuario= "root";
            String pass= "torres340";
            String direccion = "jdbc:mysql://localhost:3306/proyecto_zurich";
            Class.forName(driver);
            Connection cone= DriverManager.getConnection(direccion, usuario, pass);
            //System.out.println("Conexion exitosa");
            return cone;
       }catch(Exception e){
           System.out.println(e);
       }
       return null;
   }
    public static void crearTabla(ArrayList campos) throws Exception{
        String var="varchar(255)";
        String var2="PRIMARY KEY (DETALLE_ID));";
        String cadena="CREATE TABLE IF NOT EXISTS DETALLE(DETALLE_ID INT NOT NULL AUTO_INCREMENT, ";
        StringBuffer lista=new StringBuffer();
        ArrayList<String> cade=new ArrayList();
        for (int i = 0; i < campos.size(); i++) {
            String dato=campos.get(i)+" "+var+", ";
           // System.out.println(campos.get(i));
            cade.add(dato);
        }
        for(String string:cade)
             lista=lista.append(String.valueOf(string)); 
        String que=cadena+lista+var2;
       // System.out.println(que);
        try{
            
           Connection con =conexion();
           PreparedStatement crear =con.prepareStatement(que);
           crear.executeUpdate();
        }catch(Exception e){
            //System.out.println(e);
        }
    }
    public static void cargarListaNegra( ArrayList datos) throws Exception{
        int campos=2;
        String crearListaNegra="CREATE TABLE IF NOT EXISTS LISTANEGRA(lISTA_ID INT NOT NULL AUTO_INCREMENT, LISTA VARCHAR(255), VALIDACION VARCHAR(5), PRIMARY KEY (LISTA_ID));";
        String var2="select *from LISTANEGRA where LISTA_ID=\"1\";";
        String cadena="INSERT INTO LISTANEGRA (LISTA, VALIDACION) VALUES";
        StringBuffer lista=new StringBuffer();
        StringBuffer datosarr=new StringBuffer();
        StringBuffer datoviejo=new StringBuffer();
        ArrayList<String> cade=new ArrayList();
        ArrayList<String> dat=new ArrayList();
        
        
        int aux=0;
        for (int i = 0; i < datos.size(); i++) {
            
            if(aux==0){
                String dato="(\""+datos.get(i)+"\",";
                dat.add(dato);
            }else{
                if(aux==(campos-1)){
                    String dato="\""+datos.get(i)+"\"),";
                dat.add(dato);
                }
            }
            aux++;
            if(aux==campos){
                    aux=0;
            } 
       }
        int tam=dat.size();
        String quit=dat.get(tam-1);
        dat.remove(tam-1);
        
        //System.out.println("EL ultimo elemento es "+quit);
        char [] quitacoma= quit.toCharArray();
        List viejo=new ArrayList();
        for (int i = 0; i < quitacoma.length; i++) {
            if (quitacoma[i]==',') {
            }else{
                viejo.add(quitacoma[i]);
            }
        }
        for(Object string:viejo)
             datoviejo=datoviejo.append(String.valueOf(string));
        
        StringBuffer sin_coma=datoviejo;
       // System.out.println("el viejo es\t"+ sin_coma);
        dat.add(""+sin_coma);
        for (int i = 0; i < dat.size(); i++) {
          //  System.out.println(dat.get(i));
        }
        char[] chars = dat.toString().toCharArray();
        
       // System.out.println(chars);
        List nuevos=new ArrayList();
        for (int i = 1; i < chars.length-2; i++) {
            if((chars[i]==',') && (chars[i+1]==',')){
            }else{
                nuevos.add(chars[i]);
            }
       }
       nuevos.add(");"); 
        for(String string:cade)
             lista=lista.append(String.valueOf(string)); 
        for(Object string:nuevos)
             datosarr=datosarr.append(String.valueOf(string));
        String que=cadena+lista+datosarr;
       // System.out.println(que);
        try{
            
           Connection con =conexion();
           PreparedStatement cargar =con.prepareStatement(crearListaNegra);
           cargar.executeUpdate();
           PreparedStatement verificar =con.prepareStatement(var2);
           ResultSet resultado=verificar.executeQuery();
         //   System.out.println("el comando es "+verificar);
            if (resultado.next()==true) {
                System.out.println("ya existen registros");
                
            }else{
                
                System.out.println("Se estan cargando los datos");
                PreparedStatement crear =con.prepareStatement(que);
                crear.executeUpdate();
            }
           
        }catch(Exception e){
            System.out.println(e + que);
        }
    }
    public static ArrayList consulta_Limpia (String consul) throws Exception{
       String consulta=consul;
       ArrayList lista=new ArrayList();
       ArrayList lista_salida=new ArrayList();
       try{
           Connection con =conexion();
           PreparedStatement crear =con.prepareStatement(consulta);
           ResultSet resultado= crear.executeQuery();
          // System.out.println(resultado);
           for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
              // System.out.println("\t\t\t\t\tt\t\t\t\t\t\t\tCampos---------------------------------------------");
                   //System.out.print(resultado.getMetaData().getColumnName(i)+ ",");
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
          // System.out.println("\t\t\t\t\tt\t\t\t\t\t\t\tCampos---------------------------------------------"+lista_salida);
         //  System.out.println("");
           while(resultado.next()){
               for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                  // System.out.print(resultado.getString(i)+",");
                   lista_salida.add(resultado.getString(i)+",");
               }
               lista_salida.add("\r\n");
             //  System.out.println("");
               
           }
       }catch(Exception e){
           System.out.println("Error: "+e);
       }
       return lista_salida;
   }
    public static void cargarDatosCopia(ArrayList campos, ArrayList datos) throws Exception{
        String var="VALUES(";
        String var2="select *from detalle where detalle_id=\"1\";";
        String cadena="INSERT INTO detalle (";
        StringBuffer lista=new StringBuffer();
        StringBuffer datosarr=new StringBuffer();
        ArrayList<String> cade=new ArrayList();
        ArrayList<String> dat=new ArrayList();
        for (int i = 0; i < campos.size(); i++) {
            if (i<campos.size()-1) {
                String dato=campos.get(i)+", ";
                //System.out.println(campos.get(i));
                cade.add(dato);
                }else{
                String dato=campos.get(i)+") VALUES ";
               // System.out.println(campos.get(i));
                cade.add(dato);
            }
        }
        
        int aux=0;
        for (int i = 0; i < datos.size(); i++) {

            if(aux==0){
                String dato="(\""+datos.get(i)+"\",";
                dat.add(dato);
            }else{
                if(aux==campos.size()-1){
                    String dato="\""+datos.get(i)+"\"),";
                dat.add(dato);
                
                }else{
                    
                    String dato="\""+datos.get(i)+"\",";
                    dat.add(dato);
                }
            }
            aux++;
            if(aux==campos.size()){
                    aux=0;
            }
           
       }
        char[] chars = dat.toString().toCharArray();
        List nuevos=new ArrayList();
        for (int i = 1; i < chars.length-2; i++) {
            if((chars[i]==',') && (chars[i+1]==',')){
            }else{
                nuevos.add(chars[i]);
            }
       }
       nuevos.add(";"); 
        for(String string:cade)
             lista=lista.append(String.valueOf(string)); 
        for(Object string:nuevos)
             datosarr=datosarr.append(String.valueOf(string));
        String que=cadena+lista+datosarr;
       // System.out.println(que);
        try{
            
           Connection con =conexion();
           PreparedStatement verificar =con.prepareStatement(var2);
           ResultSet resultado=verificar.executeQuery();
           // System.out.println("el comando es "+verificar);
            if (resultado.next()==true) {
                System.out.println("ya existen registros");
                
            }else{
                
                System.out.println("Se estan cargando los datos");
                PreparedStatement crear =con.prepareStatement(que);
                crear.executeUpdate();
            }
           
        }catch(Exception e){
            System.out.println(e + que);
        }
    }
    
    public static void insertarReportes(ArrayList campos, ArrayList datos, ArrayList cantidad) throws Exception{
        String var="VALUES(";
        String var2="select *from reportes where reportes_id=\"1\";";
        String cadena="INSERT INTO reportes (";
        StringBuffer lista=new StringBuffer();
        StringBuffer datosarr=new StringBuffer();
        ArrayList<String> cade=new ArrayList();
        ArrayList<String> dat=new ArrayList();
        ArrayList<String> copia=new ArrayList();
        int contador=0, auxiliar=0;
        
        if (!cantidad.isEmpty()) {
         for (int i = 0; i < datos.size(); i++) {
             
             //System.out.println("++++++++"+cantidad.size());
             for (int j = 0; j < cantidad.size(); j++) {
                 int ca=0;
                 int numero=(Integer)cantidad.get(j)-1;
                if (auxiliar!=numero) {
                    if ((contador==44) || (contador==46) || (contador==0) || (contador==1) || (contador==3) || (contador==4) || (contador==6) || (contador==10) || (contador==11) || (contador==34) || (contador==36) || (contador==40) || (contador==41)    ) {
                      String pala=(String) datos.get(i);
                       // System.out.println(pala);
                      String cad=quitar_espacios(pala);
                      copia.add(cad);
                    }else{
                      copia.add(limpiar_palabra(datos.get(i).toString()));
                    }
             }
             }
             contador++;
            if(contador==campos.size()){
                    contador=0;
                    auxiliar++;
            } 
         }   
        }else{
            for (int i = 0; i < datos.size(); i++) {
                if ((contador==44) || (contador==46) || (contador==0) || (contador==1) || (contador==3) || (contador==4) || (contador==6) || (contador==10) || (contador==11) || (contador==34) || (contador==36) || (contador==40) || (contador==41)    ) {
                      String pala=(String) datos.get(i);
                        //System.out.println(pala);
                      String cad=quitar_espacios(pala);
                      copia.add(limpiar_palabra(cad));
                    }else{
                      copia.add(limpiar_palabra(datos.get(i).toString()));
                    }
            }
            contador++;
            if(contador==campos.size()){
                    contador=0;
                    auxiliar++;
            } 
        }
         


        
        
        
        for (int i = 0; i < campos.size(); i++) {
            if (i<campos.size()-1) {
                String dato=campos.get(i)+", ";
                //System.out.println(campos.get(i));
                cade.add(dato);
                }else{
                String dato=campos.get(i)+") VALUES ";
               // System.out.println(campos.get(i));
                cade.add(dato);
            }
        }
        int aux=0;
        for (int i = 0; i < copia.size(); i++) {

            if(aux==0){
                String dato="(\""+copia.get(i)+"\",";
                dat.add(dato);
            }else{
                if(aux==campos.size()-1){
                    String dato="\""+copia.get(i)+"\"),";
                dat.add(dato);
                
                }else{
                    
                    String dato="\""+copia.get(i)+"\",";
                    dat.add(dato);
                }
            }
            aux++;
            if(aux==campos.size()){
                    aux=0;
            }
           
       }
        char[] chars = dat.toString().toCharArray();
        List nuevos=new ArrayList();
        for (int i = 1; i < chars.length-2; i++) {
            if((chars[i]==',') && (chars[i+1]==',')){
            }else{
                nuevos.add(chars[i]);
            }
       }
       nuevos.add(";"); 
        for(String string:cade)
             lista=lista.append(String.valueOf(string)); 
        for(Object string:nuevos)
             datosarr=datosarr.append(String.valueOf(string));
        String que=cadena+lista+datosarr;
        System.out.println(que);
        try{
            
           Connection con =conexion();
           PreparedStatement verificar =con.prepareStatement(var2);
           ResultSet resultado=verificar.executeQuery();
           // System.out.println("el comando es "+verificar);
            if (resultado.next()==true) {
                System.out.println("ya existen registros");
                
            }else{
                
                System.out.println("Se estan cargando los datos");
                PreparedStatement crear =con.prepareStatement(que);
                crear.executeUpdate();
            }
           
        }catch(Exception e){
            System.out.println(e + que);
        }
    }
    
     public static void cargarDatos(ArrayList campos, ArrayList datos) throws Exception{
        String var="VALUES(";
        String var2="select *from detalle where detalle_id=\"1\";";
        String cadena="INSERT INTO detalle (";
        StringBuffer lista=new StringBuffer();
        StringBuffer datosarr=new StringBuffer();
        ArrayList<String> cade=new ArrayList();
        ArrayList<String> dat=new ArrayList();
        ArrayList<String> copia=new ArrayList();
        int contador=0;
         for (int i = 0; i < datos.size(); i++) {
             
             if ((contador==33) || (contador==34)) {
                 String pala=(String) datos.get(i);
                 
                 String cad=quitar_espacios(pala);
                 //System.out.println(cad+"-");
                 copia.add(cad);
             }else{
                 copia.add(datos.get(i)+"");
             }
             
             
             contador++;
            if(contador==campos.size()){
                    contador=0;
            } 
         }

        
        
        
        for (int i = 0; i < campos.size(); i++) {
            if (i<campos.size()-1) {
                String dato=campos.get(i)+", ";
                //System.out.println(campos.get(i));
                cade.add(dato);
                }else{
                String dato=campos.get(i)+") VALUES ";
               // System.out.println(campos.get(i));
                cade.add(dato);
            }
        }
        
        int aux=0;
        for (int i = 0; i < copia.size(); i++) {

            if(aux==0){
                String dato="(\""+copia.get(i)+"\",";
                dat.add(dato);
            }else{
                if(aux==campos.size()-1){
                    String dato="\""+copia.get(i)+"\"),";
                dat.add(dato);
                
                }else{
                    
                    String dato="\""+copia.get(i)+"\",";
                    dat.add(dato);
                }
            }
            aux++;
            if(aux==campos.size()){
                    aux=0;
            }
           
       }
        char[] chars = dat.toString().toCharArray();
        List nuevos=new ArrayList();
        for (int i = 1; i < chars.length-2; i++) {
            if((chars[i]==',') && (chars[i+1]==',')){
            }else{
                nuevos.add(chars[i]);
            }
       }
       nuevos.add(";"); 
        for(String string:cade)
             lista=lista.append(String.valueOf(string)); 
        for(Object string:nuevos)
             datosarr=datosarr.append(String.valueOf(string));
        String que=cadena+lista+datosarr;
        //System.out.println(que);
        try{
            
           Connection con =conexion();
           PreparedStatement verificar =con.prepareStatement(var2);
           ResultSet resultado=verificar.executeQuery();
           // System.out.println("el comando es "+verificar);
            if (resultado.next()==true) {
                System.out.println("ya existen registros");
                
            }else{
                
                System.out.println("Se estan cargando los datos");
                PreparedStatement crear =con.prepareStatement(que);
                crear.executeUpdate();
            }
           
        }catch(Exception e){
            System.out.println(e + que);
        }
    }
    public static ArrayList consulta (String consul) throws Exception{
       String consulta=consul;
       int posnombre=0, poscorreo=0;
       ArrayList lista=new ArrayList();
       ArrayList lista_salida=new ArrayList();
       try{
           Connection con =conexion();
           PreparedStatement crear =con.prepareStatement(consulta);
           ResultSet resultado= crear.executeQuery();
           //System.out.println(resultado);
           for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
              // System.out.println("\t\t\t\t\tt\t\t\t\t\t\t\tCampos---------------------------------------------");
                  // System.out.print(resultado.getMetaData().getColumnName(i)+ ",");
                   lista.add(resultado.getMetaData().getColumnName(i)+ ",");

               }
           for (int i = 0; i < lista.size(); i++) {
               String camp=lista.get(i).toString();
               if (camp.contains("DESTINATARIO")) {
                   
                   posnombre=i;
                   //System.out.println("\t\t\t\t\tt\t\t\t\t\t\t\tCampos************"+posnombre);
               }
               if (camp.contains("CORREO")) {
                   poscorreo=i;
                  // System.out.println("\t\t\t\t\tt\t\t\t\t\t\t\tCampos**************"+poscorreo);
               }
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
           while(resultado.next()){
               ArrayList<String> temporal= new ArrayList();
               for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                   //System.out.print(resultado.getString(i)+",");
                   lista_salida.add(resultado.getString(i)+",");
                   temporal.add(resultado.getString(i)+",");
               }
             //  System.out.println(temporal.get(posnombre)+"\t\tCONSULTATEMPORAL\t\t"+temporal.get(poscorreo));
               lista_salida.add("\r\n");
              // System.out.println("");
              // System.out.println(validaCorreo(temporal.get(posnombre), temporal.get(poscorreo)));
               
           }
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
    public static ArrayList consulta_sms (String consul) throws Exception{
       String consulta=consul;
       
       ArrayList lista=new ArrayList();
       ArrayList lista_salida=new ArrayList();
       try{
           Connection con =conexion();
           PreparedStatement crear =con.prepareStatement(consulta);
           ResultSet resultado= crear.executeQuery();
         //  System.out.println(resultado);
           for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                  // System.out.print(resultado.getMetaData().getColumnName(i)+ ",");
                   lista.add(resultado.getMetaData().getColumnName(i)+ ",");

               }
           for (int i = 0; i < lista.size(); i++) {
               lista_salida.add(lista.get(i));
//               if (i==3) {
//                   lista_salida.add("POLIZA_CONFIRMAR,");
//               }else{
//                   if(i==4){
//                       lista_salida.add("TELEFONO,");
//                    }else{
//                        lista_salida.add(lista.get(i));       
//                   }
//               }
           }
           //lista_salida.add("NOMBRE_RAMO,");
           //lista.add("\r\n");
           lista_salida.add("\r\n");
           while(resultado.next()){
               for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                 //  System.out.print(resultado.getString(i)+",");
                   lista_salida.add(resultado.getString(i)+",");
               }
               lista_salida.add("\r\n");
              // System.out.println("");
               
           }
       }catch(Exception e){
           System.out.println("Error: "+e);
       }
       return lista_salida;
   }
    public static ArrayList consulta_llave (String consul) throws Exception{
       String consulta=consul;
       ArrayList lista=new ArrayList();
       ArrayList<String> temporal= new ArrayList();
       try{
           Connection con =conexion();
           PreparedStatement crear =con.prepareStatement(consulta);
           ResultSet resultado= crear.executeQuery();
          // System.out.println(resultado);
           while(resultado.next()){
               ArrayList<String> cad=new ArrayList();
               for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                   if (i<3) {
                       cad.add(resultado.getString(i)+"-");
                   }else{
                       cad.add(resultado.getString(i));
                   }
               }
               StringBuffer palabra=new StringBuffer();
               String cadena=cad.toString();
               for(Object string:cad)
             palabra=palabra.append(String.valueOf(string));
               temporal.add(palabra.toString());
               
           }
       }catch(Exception e){
           System.out.println("Error: "+e);
       }
       return temporal;
   }
    public static ArrayList consulta_mail (String consul) throws Exception{
       String consulta=consul;
       int posnombre=0,poscorreo=0;
       ArrayList lista=new ArrayList();
       try{
           Connection con =conexion();
           PreparedStatement crear =con.prepareStatement(consulta);
           ResultSet resultado= crear.executeQuery();
          // System.out.println(resultado);
           for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                  // System.out.print(resultado.getMetaData().getColumnName(i)+ ",");
                   lista.add(resultado.getMetaData().getColumnName(i)+ ",");

               }
          
                for (int i = 0; i < lista.size(); i++) {
                               String camp=lista.get(i).toString();
                               if (camp.contains("NOMBRE,")) {
                                   posnombre=i;
                               }
                               if (camp.contains("EMAIL")) {
                                   poscorreo=i;
                               }
                }
           lista.add("\r\n");
           while(resultado.next()){
               ArrayList<String> temporal= new ArrayList();
               ArrayList<String> temporal1= new ArrayList();
               for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                   temporal.add(resultado.getString(i)+",");
               }
               //quitar
               
               
               
               if (validaCorreo(temporal.get(posnombre), temporal.get(poscorreo))==true) {
                   for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                      lista.add(resultado.getString(i)+",");
                    }
                   lista.add("\r\n");
               }
               if (temporal.get(poscorreo).contains("santander")==false) {
                   if (isValidEmail(temporal.get(poscorreo))==true || isValidEmailAddress(temporal.get(poscorreo))==true && correobueno(temporal.get(poscorreo))==true) {
                       for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                      lista.add(resultado.getString(i)+",");
                    }
                   lista.add("\r\n");                                   
                   
                   }
                   
               }
               
           }
       }catch(Exception e){
           System.out.println("Error: "+e);
       }
       return lista;
   }
    public static boolean correobueno(String email){
        boolean bandera=true;
        int x= 4, cont=0;
        char [] correo =email.toCharArray();
        for (int i = 0; i < correo.length; i++) {
            if (correo[i]=='x' || correo[i]=='X') {
                cont++;
            }
        }
        if (email.contains("tien") || email.contains("@x") || email.contains("TIEN") || email.contains("@X")) {
            bandera=false;
            return bandera;
        }else{
            bandera=true;
        }
        
        if (cont>=x) {
            bandera=false;
            return bandera;
        }else{
            bandera=true;
        }
            
        
            
        
        return bandera;
    }
     public static boolean validaCorreo(String nombre, String email){

             boolean flag = false;
            // System.out.println(email);
            // System.out.println(nombre);

             if(email.contains("santander.")) {
                    nombre = nombre.toLowerCase();
              //      System.out.println(nombre);
                    String[] nombres = nombre.split(" ");
                    String emailName = email.substring(0,email.indexOf("@"));
                //    System.out.println(emailName);
                    for(String param: nombres){
                  //        System.out.println(param + " in " + emailName);
                          flag = emailName.contains(param);
                          if(flag) {
                    //             System.out.println(email + " is valid");
                                 break;
                          }
                    }
             }
             return flag;
       }
     public static String reacomodoNombreMail(String nombre){
         String cambio = "";
         if (nombre.contains("/")) {
             //System.out.println(nombre);
             String[] campos = nombre.split("/"); 
             if (campos.length==3) {
               cambio=campos[2]+" "+campos[0]+" "+campos[1];  
                // System.out.println(cambio);
             }else{
                 if (campos.length==2) {
                     cambio=campos[1]+" "+campos[0];
                 }else{
                     cambio=nombre;
                 }
                 
             }
              
         }else{
             cambio=nombre;
         }
             return cambio;
    }
     public static ArrayList<String> cambioNombreMail (ArrayList<String> datos){
         ArrayList<String> salida =new ArrayList();
         int contador=0;
         for (int i = 0; i < datos.size(); i++) {
             
             if (contador<25 && contador!=7) {
                 salida.add(datos.get(i));
             }else{
                 if (contador==7) {
                     String palabra=datos.get(i).replace(",", "");
                     if (datos.get(i).contains("/")) {
                         
                         salida.add(reacomodoNombreMail(palabra)+","); 
                     }else{
                         salida.add(palabra+",");
                     }
                 }
             }
             contador++;
             if (contador==25) {
                 contador=0;
             }
         }
         for (int i = 0; i < salida.size(); i++) {
            // System.out.println(salida.get(i));
         }
         return salida;
     }
     public static String reacomodoNombre(String nombre){
         String cambio = "";
         if (nombre.contains("/")) {
             //System.out.println(nombre);
             String[] campos = nombre.split("/"); 
              cambio=campos[2]+" "+campos[0]+" "+campos[1];
         }else{
             cambio=nombre;
         }
             return cambio;
    }
     public static ArrayList<String> cambioNombre (ArrayList<String> datos){
         ArrayList<String> salida =new ArrayList();
         int contador=0;
         for (int i = 0; i < datos.size(); i++) {
             
             if (contador<8 && contador!=5) {
                 salida.add(datos.get(i));
             }else{
                 if (contador==5) {
                     String palabra=datos.get(i).replace(",", "");
                    salida.add(reacomodoNombre(palabra)+","); 
                 }
             }
             contador++;
             if (contador==8) {
                 contador=0;
             }
         }
         for (int i = 0; i < salida.size(); i++) {
            // System.out.println(salida.get(i));
         }
         return salida;
     }
     
     
     public static ArrayList<String> layoutFinalSMS (ArrayList<String> layaoutd, ArrayList<String> layaoutr){
         
         ArrayList<String> salida =new ArrayList();
         for (int i = 0; i < layaoutd.size(); i++) {
             salida.add(layaoutd.get(i));
         }
         for (int i = 8; i < layaoutr.size(); i++) {
             salida.add(layaoutr.get(i));
         }
         for (int i = 0; i < salida.size(); i++) {
            // System.out.println(salida.get(i));
         }
         //System.out.println("\n\n\n"+layaoutd.get(layaoutd.size())+"\t"+layaoutd.get(layaoutd.size()));
       //  System.out.println("\n\n\n"+layaoutr.get(8));
         return salida;
     }
     public static ArrayList<String> layoutFinalMAIL (ArrayList<String> layaoutd, ArrayList<String> layaoutr){
         
         ArrayList<String> salida =new ArrayList();
         for (int i = 0; i < layaoutd.size(); i++) {
             salida.add(layaoutd.get(i));
         }
         for (int i = 25; i < layaoutr.size(); i++) {
             salida.add(layaoutr.get(i));
         }
         for (int i = 0; i < salida.size(); i++) {
            // System.out.println(salida.get(i));
         }
         //System.out.println("\n\n\n"+layaoutd.get(layaoutd.size())+"\t"+layaoutd.get(layaoutd.size()));
       //  System.out.println("\n\n\n"+layaoutr.get(8));
         return salida;
     }
     public static void formaEnvio(ArrayList<String> layoutMail, ArrayList<String> layoutSms ){
         ArrayList<String> salida= new ArrayList();
         ArrayList<String> temporal= new ArrayList();
         int aux1=0, aux2=0;
         for (int i = 25; i < layoutMail.size(); i++) {
             if (aux1<25) {
                 
                 if (aux1==12) {
                  
                  String palabra=layoutMail.get(i).replace("-", ",");
                  //   System.out.println(palabra);
                     temporal.add(palabra);
                 }
             }else{
                // System.out.println("");
             }
             aux1++;
             if (aux1==25) {
                 aux1=0;   
             }
         }
         for (int i = 8; i < layoutSms.size(); i++) {
             if (aux2<8) {
                 
                 if (aux2==3) {
                     String palabra=layoutSms.get(i).replace("-", ",");
                    // System.out.println(palabra);
                     temporal.add(palabra);
                 }
             }else{
                 //System.out.println("");
             }
             aux2++;
             if (aux2==8) {
                 aux2=0;
             }
         }
         for (int i = 0; i < temporal.size(); i++) {
             String [] cadena=temporal.get(i).split(",");
             String canal="",ramo="", poliza="";
                 if (cadena[0].length()==1) {
                     canal="0"+cadena[0]+",";
                 }else{
                     canal=cadena[0]+",";
                 }
                 if (cadena[1].length()==1) {
                     ramo="0"+cadena[1]+",";
                }else{
                     ramo=cadena[1]+",";
                 }
                 poliza=canal+ramo+cadena[2];
             salida.add(poliza);
             
         }
         String hora=obtenerHora();
        try {
                File archivo = new File(outFolder +"EntradaReactor"+hora+".txt");
                FileOutputStream escribir = new FileOutputStream(archivo);
                BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(escribir));

		for (int i = 0; i < salida.size(); i++) {
                       wr.write(salida.get(i));
                       wr.newLine();
                       
                    }
                wr.close();
	    } 
	    catch (IOException e) 
	    {
			System.out.println(e.getMessage());
	    }  
         
     }
     
    
    
    public static String doProcess(){
    	StringBuffer sb = new StringBuffer();
    	try {
    		File checkFolder = new File(outFolder);
    		if(!checkFolder.exists()) {
    			checkFolder.mkdir();
    		}
        	String basePath = "C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto Zurich - Evidencia\\exportar\\18-02-2019\\Excel\\";
            ConectarDB conex=new ConectarDB();
            ArrayList<String> layoutSMS = new ArrayList();
            ArrayList<String> layoutMAIL = new ArrayList();
            File archivo = new File("C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto Zurich - Evidencia\\exportar\\18-02-2019\\Excel\\detalle20190128.xlsx");
            File listaNegra = new File(basePath + "LISTA_NEGRA.xlsx");
            File nombreRamo = new File(basePath + "LayOut_SMS.xlsx");
            File layoutmail = new File(basePath + "LayOutMail.xlsx");
            File layoutfisica = new File (basePath + "EnvioFisico_DDMMAAAA.xlsx");
            cargarTablaGen(layoutfisica,"PRODUCTOS", 1);
            cargarTablaGen(nombreRamo,"NOMBRERAMO", 1);
            cargarTablaGen(layoutmail,"BENEFICIOS", 0);
            cargarTablaGen(layoutmail,"LIGA", 1);
            cargarTablaGen(layoutmail,"RENRAMO", 2);
            cargarTablaGen(layoutmail,"CG", 3);
            cargarTablaGen(layoutmail,"WK", 4);
            cargarTablaGen(layoutmail,"PLANTILLACM", 5);
            cargarTablaGen(layoutmail,"PLANTILLACC", 6);
            cargarTablaGen(layoutmail,"PLANTILLAMS", 7);
            cargarTablaGen(layoutmail,"PLANTILLACS", 8);
            cargarTablaGen(layoutmail,"PLANTILLA", 9);
            BufferedReader red = new BufferedReader (new InputStreamReader(new FileInputStream("C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto Zurich - Evidencia\\exportar\\18-02-2019\\Excel\\Reporte Red.csv"), "ISO-8859-1"));
            BufferedReader canales = new BufferedReader (new InputStreamReader(new FileInputStream("C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto Zurich - Evidencia\\exportar\\18-02-2019\\Excel\\Reporte Canales Alternos.csv"), "ISO-8859-1"));
            LeerLista(listaNegra);
            String tlmk="TLMK";
            String mail="MAIL";
            String sms="SMS";
            String tlmkR="LayoutTLMK";
            String mailR="LayoutMail";
            String smsR="LayoutSms";
            String nombre=archivo.getName();
            char [] nombre_cadena= nombre.toCharArray();
            int num=0;
            if((nombre_cadena[nombre_cadena.length-1]=='s')&& (archivo.exists())){
                G xls = new G(archivo, num);
            }else{
                G xlsx = new G(archivo);
            }
            CargarReportes(red, canales);
            /////TLMK
            String query_tlmk="select TIPO_DE_POLIZA, CAMPAÑA, CODIGO_IDENTIFICADOR, CANAL, RAMO, POLIZA, CERTIFICADO, NOMBRE_DEL_DESTINATARIO, DIRECCION, COLONIA, POBLACION, ESTADO, CODIGO_POSTAL, CENTRO_DE_REPARTO, FORMA_DE_IMPRESION, FORMA_DE_ENVIO, BUC, PRODUCTO, SUCURSAL, CLAVE_CONVENIO, NOMBRE_EMPRESA, FECHA_DE_DISPERSION, FECHA_DE_VENTA, FECHA_DE_SUSCRIPCION, FECHA_DE_EMISION, FECHA_DE_COBRANZA, FECHA_DE_GENERACION_PDF, CANAL_AP, RAMO_AP, POLIZA_AP, CANAL_RC, RAMO_RC, POLIZA_RC, CORREO_ELECTRONICO, TELEFONO_CELULAR, SEGMENTO, APLICATIVO, GIRO, SECTOR, INI_VIGENCIA, FIN_VIGENCIA, (select concat(canal,ramo, poliza) as id), (select if (SEGMENTO=\"SE\", \"SCONFIRMACION\", \"CCONFIRMACION\") AS SEGMENTO) from detalle WHERE CANAL=\"2\" AND CAMPAÑA!=\"112\" AND CAMPAÑA!=\"212\" AND CAMPAÑA!=\"214\" AND CAMPAÑA!=\"216\" AND RAMO!=\"97\" AND FECHA_DE_EMISION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND FECHA_DE_EMISION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y');";
            ArrayList<String> datos_tlmk = new ArrayList();
            datos_tlmk=consulta(query_tlmk);
            salida_csv(datos_tlmk, tlmk);
            /////////////////////////MAIL
            String query_mail="select DETALLE.CANAL, DETALLE.RAMO, DETALLE.POLIZA, DETALLE.PRODUCTO, (SELECT IF (DETALLE.RAMO=\"26\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"73\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"74\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"83\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"97\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (select DETALLE.RAMO) )) )) )) )) )) AS RENRAMOS, DETALLE.TELEFONO_CELULAR as CELULAR, DETALLE.CORREO_ELECTRONICO as EMAIL, DETALLE.NOMBRE_DEL_DESTINATARIO as NOMBRE, DETALLE.SUCURSAL, DETALLE.FECHA_DE_EMISION as FECHA_EMISION, (DATE_FORMAT(CURDATE(), '%d/%m/%Y')) as FECHA, (select BENEFICIOS.NOMBRE_RAMO from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS NOMBRE_RAMO, (select concat(DETALLE.canal ,'-', DETALLE.ramo, '-', DETALLE.poliza)) as POLIZA_CONCATENADO, (select BENEFICIOS.BENEFICIOS from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS BENEFICIOS, (select LIGA.URL_IMAGEN from LIGA where RENRAMOS=LIGA.RAMO) AS LIGA, CODIGO_IDENTIFICADOR as ARCHIVO, (select concat(ARCHIVO,'.pdf')) as ARCHIVO1, (SELECT IF (DETALLE.RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_1 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO2, (SELECT IF (DETALLE.RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_2 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO3, (SELECT IF (DETALLE.RAMO=\"97\",\" \", (SELECT WK.NOMBRE_WELCOME_KIT FROM WK WHERE RENRAMOS=WK.RENRAMO))) as ARCHIVO4, (select concat('-')) as ARCHIVOS, (select if (DETALLE.SEGMENTO= \"SE\",  \"SCONFIRMACION\",  \"CCONFIRMACION\")) as ENVIO, (select concat(DETALLE.CANAL, RENRAMOS, ENVIO)) as C_ENVIO, (SELECT PLANTILLA.NOMBRE FROM PLANTILLA WHERE C_ENVIO=PLANTILLA.CODIGO) as PLANTILLA from detalle WHERE CANAL= \"2\" AND CAMPAÑA!= \"112\" AND CAMPAÑA!= \"212 \" AND CAMPAÑA!= \"214\" AND CAMPAÑA!= \"216\" AND RAMO!= \"97\" AND FECHA_DE_EMISION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND FECHA_DE_EMISION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND CORREO_ELECTRONICO NOT IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND CORREO_ELECTRONICO NOT LIKE '%ZURICH%' AND CORREO_ELECTRONICO NOT LIKE '%SUBDIR%' AND CORREO_ELECTRONICO NOT LIKE '%DIRE%' AND CORREO_ELECTRONICO!=\"\" UNION select DETALLE.CANAL, DETALLE.RAMO, DETALLE.POLIZA, DETALLE.PRODUCTO, (SELECT IF (DETALLE.RAMO=\"26\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"73\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"74\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"83\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"97\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (select DETALLE.RAMO) )) )) )) )) )) AS RENRAMOS, DETALLE.TELEFONO_CELULAR as CELULAR, DETALLE.CORREO_ELECTRONICO as EMAIL, DETALLE.NOMBRE_DEL_DESTINATARIO as NOMBRE, DETALLE.SUCURSAL, DETALLE.FECHA_DE_EMISION as FECHA_EMISION, (DATE_FORMAT(CURDATE(), '%d/%m/%Y')) as FECHA, (select BENEFICIOS.NOMBRE_RAMO from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS NOMBRE_RAMO, (select concat(DETALLE.canal ,'-', DETALLE.ramo, '-', DETALLE.poliza)) as POLIZA_CONCATENADO, (select BENEFICIOS.BENEFICIOS from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS BENEFICIOS, (select LIGA.URL_IMAGEN from LIGA where RENRAMOS=LIGA.RAMO) AS LIGA, CODIGO_IDENTIFICADOR as ARCHIVO, (select concat(ARCHIVO,'.pdf')) as ARCHIVO1, (SELECT IF (DETALLE.RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_1 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO2, (SELECT IF (DETALLE.RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_2 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO3, (SELECT IF (DETALLE.RAMO=\"97\",\" \", (SELECT WK.NOMBRE_WELCOME_KIT FROM WK WHERE RENRAMOS=WK.RENRAMO))) as ARCHIVO4, (select concat('-')) as ARCHIVOS, (select if (DETALLE.SEGMENTO= \"SE\",  \"SCONFIRMACION\",  \"CCONFIRMACION\")) as ENVIO, (select concat(DETALLE.CANAL, RENRAMOS, ENVIO)) as C_ENVIO, (SELECT PLANTILLA.NOMBRE FROM PLANTILLA WHERE C_ENVIO=PLANTILLA.CODIGO) as PLANTILLA from detalle WHERE CANAL=\"2\" AND CAMPAÑA=\"112\" AND CAMPAÑA=\"212\" AND CAMPAÑA= \"214 \" AND CAMPAÑA=\"216\" AND RAMO=\"97\" AND FECHA_DE_EMISION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND FECHA_DE_EMISION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND CORREO_ELECTRONICO NOT IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND CORREO_ELECTRONICO NOT LIKE '%ZURICH%' AND CORREO_ELECTRONICO NOT LIKE '%DIRE%' AND CORREO_ELECTRONICO NOT LIKE '%SUBDIR%' AND CORREO_ELECTRONICO!=\"\" UNION select DETALLE.CANAL, DETALLE.RAMO, DETALLE.POLIZA, DETALLE.PRODUCTO, (SELECT IF (DETALLE.RAMO=\"26\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"73\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"74\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"83\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (SELECT IF (DETALLE.RAMO=\"97\",(select RENRAMO.RENOMBRADO_RAMO from renramo where DETALLE.PRODUCTO=renramo.renombrado_ramo or DETALLE.PRODUCTO=renramo.clave_producto), (select DETALLE.RAMO) )) )) )) )) )) AS RENRAMOS, DETALLE.TELEFONO_CELULAR as CELULAR, DETALLE.CORREO_ELECTRONICO as EMAIL, DETALLE.NOMBRE_DEL_DESTINATARIO as NOMBRE, DETALLE.SUCURSAL, DETALLE.FECHA_DE_EMISION as FECHA_EMISION, (DATE_FORMAT(CURDATE(), '%d/%m/%Y')) as FECHA, (select BENEFICIOS.NOMBRE_RAMO from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS NOMBRE_RAMO, (select concat(DETALLE.canal ,'-', DETALLE.ramo, '-', DETALLE.poliza)) as POLIZA_CONCATENADO, (select BENEFICIOS.BENEFICIOS from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS BENEFICIOS, (select LIGA.URL_IMAGEN from LIGA where RENRAMOS=LIGA.RAMO) AS LIGA, CODIGO_IDENTIFICADOR as ARCHIVO, (select concat(ARCHIVO,'.pdf')) as ARCHIVO1, (SELECT IF (DETALLE.RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_1 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO2, (SELECT IF (DETALLE.RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_2 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO3, (SELECT IF (DETALLE.RAMO=\"97\",\" \", (SELECT WK.NOMBRE_WELCOME_KIT FROM WK WHERE RENRAMOS=WK.RENRAMO))) as ARCHIVO4, (select concat('-')) as ARCHIVOS, (select if (DETALLE.SEGMENTO= \"SE\",  \"SMAIL\",  \"CMAIL\")) as ENVIO, (select concat(DETALLE.CANAL, RENRAMOS, ENVIO)) as C_ENVIO, (SELECT PLANTILLA.NOMBRE FROM PLANTILLA WHERE C_ENVIO=PLANTILLA.CODIGO) as PLANTILLA  from detalle WHERE CANAL!= \"2 \" AND FECHA_DE_EMISION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND FECHA_DE_EMISION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND CORREO_ELECTRONICO NOT IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND CORREO_ELECTRONICO NOT LIKE '%DIRE%' AND CORREO_ELECTRONICO NOT LIKE '%SUBDIR%' AND CORREO_ELECTRONICO NOT LIKE '%ZURICH%' AND CORREO_ELECTRONICO NOT LIKE '%PYME%' AND CORREO_ELECTRONICO!=\"\";";
            ArrayList<String> datos_mail = new ArrayList();
            datos_mail=consulta_mail(query_mail);
            salida_csv(datos_mail, mail);
            //////////////////////SMS
            String query_sms="select D.CANAL, D.RAMO, D.POLIZA, (select concat(D.canal ,'-', D.ramo, '-', D.poliza)) as POLIZA_CONFIRMAR, D.TELEFONO_CELULAR, D.NOMBRE_DEL_DESTINATARIO, R.NOMBRE_RAMO from detalle D, NOMBRERAMO R WHERE D.CANAL=\"2\" AND D.CAMPAÑA!=\"112\" AND D.CAMPAÑA!=\"212\" AND D.CAMPAÑA!=\"214\" AND D.CAMPAÑA!=\"216\" AND D.RAMO!=\"97\" AND D.FECHA_DE_EMISION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND D.FECHA_DE_EMISION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND CHAR_LENGTH(D.TELEFONO_CELULAR)>=10 AND  D.TELEFONO_CELULAR!=\"1111111111\" AND D.TELEFONO_CELULAR!=\"2222222222\" AND D.TELEFONO_CELULAR!=\"3333333333\" AND D.TELEFONO_CELULAR!=\"4444444444\" AND D.TELEFONO_CELULAR!=\"5555555555\" AND D.TELEFONO_CELULAR!=\"6666666666\" AND D.TELEFONO_CELULAR!=\"7777777777\" AND D.TELEFONO_CELULAR!=\"8888888888\" AND D.TELEFONO_CELULAR!=\"9999999999\" AND D.TELEFONO_CELULAR NOT LIKE '0%' AND D.CORREO_ELECTRONICO IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND D.CORREO_ELECTRONICO NOT LIKE '@' AND D.RAMO=R.RAMO;";
            ArrayList<String> datos_sms = new ArrayList();
            datos_sms=consulta_sms(query_sms);
            salida_csv(datos_sms, sms);
            //////////////////////Fisico
            String query_fisico="select D.CODIGO_IDENTIFICADOR, D.CANAL, D.RAMO, D.POLIZA, D.NOMBRE_DEL_DESTINATARIO, D.DIRECCION, D.COLONIA, D.POBLACION, D.ESTADO, D.CODIGO_POSTAL, D.PRODUCTO, (SELECT PRODUCTOS.DESC__RAMO FROM PRODUCTOS WHERE D.RAMO=PRODUCTOS.RAMO) AS APLICATIVO from detalle D, NOMBRERAMO R WHERE D.CANAL=\"2\" AND D.CAMPAÑA!=\"112\" AND D.CAMPAÑA!=\"212\" AND D.CAMPAÑA!=\"214\" AND D.CAMPAÑA!=\"216\" AND D.RAMO!=\"97\" AND D.FECHA_DE_EMISION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND D.FECHA_DE_EMISION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND CHAR_LENGTH(D.TELEFONO_CELULAR)>=10 AND  D.TELEFONO_CELULAR!=\"1111111111\" AND D.TELEFONO_CELULAR!=\"2222222222\" AND D.TELEFONO_CELULAR!=\"3333333333\" AND D.TELEFONO_CELULAR!=\"4444444444\" AND D.TELEFONO_CELULAR!=\"5555555555\" AND D.TELEFONO_CELULAR!=\"6666666666\" AND D.TELEFONO_CELULAR!=\"7777777777\" AND D.TELEFONO_CELULAR!=\"8888888888\" AND D.TELEFONO_CELULAR!=\"9999999999\" AND D.TELEFONO_CELULAR NOT LIKE '0%' AND D.CORREO_ELECTRONICO IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND D.CORREO_ELECTRONICO NOT LIKE '@' AND D.RAMO=R.RAMO;";
            ArrayList<String> datos_fisico = new ArrayList();
            datos_fisico=consulta_sms(query_fisico);
            salida_csv(datos_fisico, "EnvioFiscoIM");
            
            
            cmail=(datos_mail.size()/26)-1;
            csms=(datos_sms.size()/8)-1;
            cdescartados=cregistros-(cmail+csms);
            
            sb.append("********************************************************");
            sb.append(NEWLINE);
            sb.append("Los registros leidos del detalle son:\t"+cregistros);
            sb.append(NEWLINE);
            sb.append("Para el layout mail se enviaron:\t"+cmail);
            sb.append(NEWLINE);
            sb.append("Para el layaout sms se enviaron:\t"+csms);
            sb.append(NEWLINE);
            sb.append("Los registros descartados son:\t\t"+cdescartados);
            sb.append(NEWLINE);
            sb.append("********************************************************");
            sb.append(NEWLINE);
            /////////////////////////sms
            String query_smsReportes="select R.CANAL, R._RAMO, R._POLIZA, (select concat(R.canal, '-', R._ramo, '-', R._poliza)) as POLIZA_CONFIRMAR, R._NUMERO_CELULAR_CONTRATANTE, R._NOMBRE_CLIENTE, NR.NOMBRE_RAMO from reportes R, NOMBRERAMO NR WHERE R.CANAL=\"2\" AND R._EJECUTIVO!=\"111111\" AND R._NOMBRE_EJECUTIVO NOT LIKE '%CENTRAL DE RETENCION%' AND R._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND R._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND CHAR_LENGTH(R._NUMERO_CELULAR_CONTRATANTE)=10 AND R._NUMERO_CELULAR_CONTRATANTE!=\"1111111111\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"2222222222\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"3333333333\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"4444444444\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"5555555555\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"6666666666\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"7777777777\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"8888888888\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"9999999999\" AND R._NUMERO_CELULAR_CONTRATANTE NOT LIKE '0%' AND R._CORREO_ELECTRONICO_CONTRATANTE IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND R._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '@' AND R._RAMO=NR.RAMO;";
            ArrayList<String> datos_smsReportes = new ArrayList();
            ArrayList<String> salida = new ArrayList();
            datos_smsReportes=consulta_sms(query_smsReportes);
            salida=cambioNombre(datos_smsReportes);
            salida_csv(salida, smsR);
            /////////////////////////mail
            //ramo  57 y 71 String query_mailReportes="select  REPORTES.CANAL, REPORTES._RAMO, REPORTES._POLIZA, REPORTES._CODIGO_PRODUCTO, (SELECT IF (REPORTES._RAMO=\"26\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"73\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"74\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"83\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"97\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (select reportes._ramo) )) )) )) )) )) AS RENRAMOS,REPORTES._NUMERO_CELULAR_CONTRATANTE as CELULAR, REPORTES._CORREO_ELECTRONICO_CONTRATANTE as EMAIL, REPORTES._NOMBRE_CLIENTE as NOMBRE, REPORTES._SUCURSAL, REPORTES._FEC__SUSCRIPCION as FECHA_EMISION, (DATE_FORMAT(CURDATE(), '%d/%m/%Y')) as FECHA, (select BENEFICIOS.NOMBRE_RAMO from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS NOMBRE_RAMO, (select concat(REPORTES.canal,'-', REPORTES._ramo, '-', REPORTES._poliza)) as POLIZA_CONCATENADO, (select BENEFICIOS.BENEFICIOS from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS BENEFICIOS, (select LIGA.URL_IMAGEN from LIGA where RENRAMOS=LIGA.RAMO) AS LIGA, (select concat(POLIZA_CONCATENADO,'-', (DATE_FORMAT(CURDATE(), '%d%m%Y')))) as ARCHIVO, (select concat(ARCHIVO,'.pdf')) as ARCHIVO1, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_1 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO2, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_2 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO3, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT WK.NOMBRE_WELCOME_KIT FROM WK WHERE RENRAMOS=WK.RENRAMO))) as ARCHIVO4, (select concat('-')) as ARCHIVOS, (select concat('CCONFIRMACION')) as ENVIO, (select concat(CANAL, RENRAMOS, ENVIO)) as C_ENVIO, (SELECT PLANTILLA.NOMBRE FROM PLANTILLA WHERE C_ENVIO=PLANTILLA.CODIGO) as PLANTILLA from reportes WHERE REPORTES.CANAL= \"2\" AND REPORTES._EJECUTIVO!=\"111111\" AND REPORTES._NOMBRE_EJECUTIVO NOT LIKE '%CENTRAL DE RETENCION%' AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%ZURICH%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%SUBDIR%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%DIRE%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE!=\"\" UNION   select  REPORTES.CANAL, REPORTES._RAMO, REPORTES._POLIZA, REPORTES._CODIGO_PRODUCTO, (SELECT IF (REPORTES._RAMO=\"26\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"73\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"74\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"83\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"97\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (select reportes._ramo) )) )) )) )) )) AS RENRAMOS, REPORTES._NUMERO_CELULAR_CONTRATANTE as CELULAR, REPORTES._CORREO_ELECTRONICO_CONTRATANTE as EMAIL, REPORTES._NOMBRE_CLIENTE as NOMBRE, REPORTES._SUCURSAL, REPORTES._FEC__SUSCRIPCION as FECHA_EMISION, (DATE_FORMAT(CURDATE(), '%d/%m/%Y')) as FECHA, (select BENEFICIOS.NOMBRE_RAMO from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS NOMBRE_RAMO, (select concat(REPORTES.canal,'-', REPORTES._ramo, '-', REPORTES._poliza)) as POLIZA_CONCATENADO, (select BENEFICIOS.BENEFICIOS from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS BENEFICIOS, (select LIGA.URL_IMAGEN from LIGA where RENRAMOS=LIGA.RAMO) AS LIGA, (select concat(POLIZA_CONCATENADO,'-', (DATE_FORMAT(CURDATE(), '%d%m%Y')))) as ARCHIVO, (select concat(ARCHIVO,'.pdf')) as ARCHIVO1, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_1 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO2, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_2 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO3, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT WK.NOMBRE_WELCOME_KIT FROM WK WHERE RENRAMOS=WK.RENRAMO))) as ARCHIVO4, (select concat('-')) as ARCHIVOS, (select concat('CMAIL')) as ENVIO, (select concat(CANAL, RENRAMOS, ENVIO)) as C_ENVIO, (SELECT PLANTILLA.NOMBRE FROM PLANTILLA WHERE C_ENVIO=PLANTILLA.CODIGO) as PLANTILLA   from reportes WHERE REPORTES.CANAL=\"2\" AND REPORTES._EJECUTIVO!=\"111111\" AND REPORTES._NOMBRE_EJECUTIVO NOT LIKE '%CENTRAL DE RETENCION%' AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%ZURICH%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%DIRE%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%SUBDIR%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE=\"\" UNION select  REPORTES.CANAL, REPORTES._RAMO, REPORTES._POLIZA, REPORTES._CODIGO_PRODUCTO, (SELECT IF (REPORTES._RAMO=\"26\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"73\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"74\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"83\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"97\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (select reportes._ramo) )) )) )) )) )) AS RENRAMOS, REPORTES._NUMERO_CELULAR_CONTRATANTE as CELULAR, REPORTES._CORREO_ELECTRONICO_CONTRATANTE as EMAIL, REPORTES._NOMBRE_CLIENTE as NOMBRE, REPORTES._SUCURSAL, REPORTES._FEC__SUSCRIPCION as FECHA_EMISION, (DATE_FORMAT(CURDATE(), '%d/%m/%Y')) as FECHA, (select BENEFICIOS.NOMBRE_RAMO from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS NOMBRE_RAMO, (select concat(REPORTES.canal,'-', REPORTES._ramo, '-', REPORTES._poliza)) as POLIZA_CONCATENADO, (select BENEFICIOS.BENEFICIOS from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS BENEFICIOS, (select LIGA.URL_IMAGEN from LIGA where RENRAMOS=LIGA.RAMO) AS LIGA, (select concat(POLIZA_CONCATENADO,'-', (DATE_FORMAT(CURDATE(), '%d%m%Y')))) as ARCHIVO, (select concat(ARCHIVO,'.pdf')) as ARCHIVO1, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_1 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO2, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_2 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO3, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT WK.NOMBRE_WELCOME_KIT FROM WK WHERE RENRAMOS=WK.RENRAMO))) as ARCHIVO4, (select concat('-')) as ARCHIVOS, (select concat('CMAIL')) as ENVIO, (select concat(CANAL, RENRAMOS, ENVIO)) as C_ENVIO, (SELECT PLANTILLA.NOMBRE FROM PLANTILLA WHERE C_ENVIO=PLANTILLA.CODIGO) as PLANTILLA  from reportes WHERE REPORTES.CANAL!= \"2\" AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%DIRE%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%SUBDIR%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%ZURICH%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%PYME%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE!=\"\";";
            String query_mailReportes="select  REPORTES.CANAL, REPORTES._RAMO, REPORTES._POLIZA, REPORTES._CODIGO_PRODUCTO, (SELECT IF (REPORTES._RAMO=\"26\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"73\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"74\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"83\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"97\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (select reportes._ramo) )) )) )) )) )) AS RENRAMOS,REPORTES._NUMERO_CELULAR_CONTRATANTE as CELULAR, REPORTES._CORREO_ELECTRONICO_CONTRATANTE as EMAIL, REPORTES._NOMBRE_CLIENTE as NOMBRE, REPORTES._SUCURSAL, REPORTES._FEC__SUSCRIPCION as FECHA_EMISION, (DATE_FORMAT(CURDATE(), '%d/%m/%Y')) as FECHA, (select BENEFICIOS.NOMBRE_RAMO from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS NOMBRE_RAMO, (select concat(REPORTES.canal,'-', REPORTES._ramo, '-', REPORTES._poliza)) as POLIZA_CONCATENADO, (select BENEFICIOS.BENEFICIOS from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS BENEFICIOS, (select LIGA.URL_IMAGEN from LIGA where RENRAMOS=LIGA.RAMO) AS LIGA, (select concat(POLIZA_CONCATENADO,'-', (DATE_FORMAT(CURDATE(), '%d%m%Y')))) as ARCHIVO, (select concat(ARCHIVO,'.pdf')) as ARCHIVO1, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_1 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO2, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_2 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO3, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT WK.NOMBRE_WELCOME_KIT FROM WK WHERE RENRAMOS=WK.RENRAMO))) as ARCHIVO4, (select concat('-')) as ARCHIVOS, (select concat('CCONFIRMACION')) as ENVIO, (select concat(CANAL, RENRAMOS, ENVIO)) as C_ENVIO, (SELECT PLANTILLA.NOMBRE FROM PLANTILLA WHERE C_ENVIO=PLANTILLA.CODIGO) as PLANTILLA from reportes WHERE REPORTES.CANAL= \"2\" AND REPORTES._RAMO!=\"57\" AND REPORTES._RAMO!=\"71\" AND REPORTES._EJECUTIVO!=\"111111\" AND REPORTES._NOMBRE_EJECUTIVO NOT LIKE '%CENTRAL DE RETENCION%' AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%ZURICH%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%SUBDIR%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%DIRE%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE!=\"\" UNION   select  REPORTES.CANAL, REPORTES._RAMO, REPORTES._POLIZA, REPORTES._CODIGO_PRODUCTO, (SELECT IF (REPORTES._RAMO=\"26\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"73\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"74\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"83\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"97\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (select reportes._ramo) )) )) )) )) )) AS RENRAMOS, REPORTES._NUMERO_CELULAR_CONTRATANTE as CELULAR, REPORTES._CORREO_ELECTRONICO_CONTRATANTE as EMAIL, REPORTES._NOMBRE_CLIENTE as NOMBRE, REPORTES._SUCURSAL, REPORTES._FEC__SUSCRIPCION as FECHA_EMISION, (DATE_FORMAT(CURDATE(), '%d/%m/%Y')) as FECHA, (select BENEFICIOS.NOMBRE_RAMO from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS NOMBRE_RAMO, (select concat(REPORTES.canal,'-', REPORTES._ramo, '-', REPORTES._poliza)) as POLIZA_CONCATENADO, (select BENEFICIOS.BENEFICIOS from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS BENEFICIOS, (select LIGA.URL_IMAGEN from LIGA where RENRAMOS=LIGA.RAMO) AS LIGA, (select concat(POLIZA_CONCATENADO,'-', (DATE_FORMAT(CURDATE(), '%d%m%Y')))) as ARCHIVO, (select concat(ARCHIVO,'.pdf')) as ARCHIVO1, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_1 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO2, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_2 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO3, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT WK.NOMBRE_WELCOME_KIT FROM WK WHERE RENRAMOS=WK.RENRAMO))) as ARCHIVO4, (select concat('-')) as ARCHIVOS, (select concat('CMAIL')) as ENVIO, (select concat(CANAL, RENRAMOS, ENVIO)) as C_ENVIO, (SELECT PLANTILLA.NOMBRE FROM PLANTILLA WHERE C_ENVIO=PLANTILLA.CODIGO) as PLANTILLA from reportes WHERE REPORTES.CANAL=\"2\" AND REPORTES._RAMO!=\"57\" AND REPORTES._RAMO!=\"71\" AND REPORTES._EJECUTIVO!=\"111111\" AND REPORTES._NOMBRE_EJECUTIVO NOT LIKE '%CENTRAL DE RETENCION%' AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%ZURICH%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%DIRE%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%SUBDIR%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE=\"\" UNION select  REPORTES.CANAL, REPORTES._RAMO, REPORTES._POLIZA, REPORTES._CODIGO_PRODUCTO, (SELECT IF (REPORTES._RAMO=\"26\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"73\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"74\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"83\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (SELECT IF (REPORTES._RAMO=\"97\",(select RENRAMO.RENOMBRADO_RAMO from renramo where reportes._codigo_producto=renramo.renombrado_ramo or reportes._codigo_producto=renramo.clave_producto), (select reportes._ramo) )) )) )) )) )) AS RENRAMOS, REPORTES._NUMERO_CELULAR_CONTRATANTE as CELULAR, REPORTES._CORREO_ELECTRONICO_CONTRATANTE as EMAIL, REPORTES._NOMBRE_CLIENTE as NOMBRE, REPORTES._SUCURSAL, REPORTES._FEC__SUSCRIPCION as FECHA_EMISION, (DATE_FORMAT(CURDATE(), '%d/%m/%Y')) as FECHA, (select BENEFICIOS.NOMBRE_RAMO from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS NOMBRE_RAMO, (select concat(REPORTES.canal,'-', REPORTES._ramo, '-', REPORTES._poliza)) as POLIZA_CONCATENADO, (select BENEFICIOS.BENEFICIOS from BENEFICIOS where RENRAMOS=BENEFICIOS.RENRAMO OR RENRAMOS=BENEFICIOS.NOMBRE_RAMO) AS BENEFICIOS, (select LIGA.URL_IMAGEN from LIGA where RENRAMOS=LIGA.RAMO) AS LIGA, (select concat(POLIZA_CONCATENADO,'-', (DATE_FORMAT(CURDATE(), '%d%m%Y')))) as ARCHIVO, (select concat(ARCHIVO,'.pdf')) as ARCHIVO1, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_1 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO2, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT CG.NOMBRE_CONDICIONES_GENERALES_2 FROM CG WHERE RENRAMOS=CG.RENRAMO))) as ARCHIVO3, (SELECT IF (REPORTES._RAMO=\"97\",\" \", (SELECT WK.NOMBRE_WELCOME_KIT FROM WK WHERE RENRAMOS=WK.RENRAMO))) as ARCHIVO4, (select concat('-')) as ARCHIVOS, (select concat('CMAIL')) as ENVIO, (select concat(CANAL, RENRAMOS, ENVIO)) as C_ENVIO, (SELECT PLANTILLA.NOMBRE FROM PLANTILLA WHERE C_ENVIO=PLANTILLA.CODIGO) as PLANTILLA  from reportes WHERE REPORTES.CANAL!=\"2\" AND REPORTES._RAMO!=\"57\" AND REPORTES._RAMO!=\"71\" AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND REPORTES._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%DIRE%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%SUBDIR%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%ZURICH%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '%PYME%' AND REPORTES._CORREO_ELECTRONICO_CONTRATANTE!=\"\";";
            ArrayList<String> datos_mailReportes = new ArrayList();
            datos_mailReportes=consulta_mail(query_mailReportes);
            ArrayList<String> salidamail = new ArrayList();
            salidamail=cambioNombreMail(datos_mailReportes);
            salida_csv(salidamail, mailR);
            //////////////////////FISICO
            String query_smsFisico="select (select concat(R.CANAL,'-', R._RAMO,'-', R._POLIZA,'-', (DATE_FORMAT(CURDATE(), '%d%m%Y')))) as CODIGO_IDENTIFICADOR, R.CANAL, R._RAMO, R._POLIZA, R._NOMBRE_CLIENTE AS NOMBRE_CLIENTE, (select concat('-')) as DIRECCION, (select concat('-')) as COLONIA, (select concat('-')) as POBLACION, (select concat('-')) as ESTADO, (select concat('-')) as CODIGO_POSTAL, R._CODIGO_PRODUCTO, (SELECT PRODUCTOS.DESC__RAMO FROM PRODUCTOS WHERE R._RAMO=PRODUCTOS.RAMO) AS APLICATIVO from reportes R, NOMBRERAMO NR WHERE R.CANAL=\"2\" AND R._EJECUTIVO!=\"111111\" AND R._NOMBRE_EJECUTIVO NOT LIKE '%CENTRAL DE RETENCION%' AND R._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE(), '%d/%m/%Y') AND R._FEC__SUSCRIPCION!=DATE_FORMAT(CURDATE()-1, '%d/%m/%Y') AND CHAR_LENGTH(R._NUMERO_CELULAR_CONTRATANTE)=10 AND R._NUMERO_CELULAR_CONTRATANTE!=\"1111111111\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"2222222222\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"3333333333\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"4444444444\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"5555555555\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"6666666666\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"7777777777\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"8888888888\" AND R._NUMERO_CELULAR_CONTRATANTE!=\"9999999999\" AND R._NUMERO_CELULAR_CONTRATANTE NOT LIKE '0%' AND R._CORREO_ELECTRONICO_CONTRATANTE IN (SELECT LISTANEGRA.LISTA FROM LISTANEGRA) AND R._CORREO_ELECTRONICO_CONTRATANTE NOT LIKE '@' AND R._RAMO=NR.RAMO;";
            ArrayList<String> datos_smsFisico = new ArrayList();
            ArrayList<String> salidaFisico = new ArrayList();
            datos_smsFisico=consulta_sms(query_smsFisico);
            salidaFisico=cambioNombre(datos_smsFisico);
            //salida_csv(salidaFisico, "EnvioFisicoComisiones");
            
            
            
            ///LAYOUTFINAL COMISONES
            layoutSMS=layoutFinalSMS(datos_sms, salida);
            salida_csv(layoutSMS, "LayoutFinalSMS");
            layoutMAIL=layoutFinalMAIL(datos_mail, salidamail);
            salida_csv(layoutMAIL, "LayoutFinalMAIL");
            formaEnvio(layoutMAIL, layoutSMS);
            
            comisiones_csms=(layoutSMS.size()/8)-1;
            comisiones_cmail=(datos_mailReportes.size()/26)-1;
            comisiones_cdescartados=comisiones_cregistros-(comisiones_cmail+comisiones_csms);
            sb.append(NEWLINE);
            sb.append("********************************************************");
            sb.append(NEWLINE);
            sb.append("Los registros leidos de comisiones son:\t"+comisiones_cregistros);
            sb.append(NEWLINE);
            sb.append("Para el layout mail se enviaron:\t"+comisiones_cmail);
            sb.append(NEWLINE);
            sb.append("Para el layaout sms se enviaron:\t"+comisiones_csms);
            sb.append(NEWLINE);
            sb.append("Los registros descartados son:\t\t"+comisiones_cdescartados);
            sb.append(NEWLINE);
            sb.append("********************************************************");
            
    	}catch(Exception e) {
    		System.out.println("Ocurrio un error " + e.getStackTrace());
    	}
    	
    	return sb.toString();
    }
    
    public static void main(String[] args) throws Exception {
    	doProcess();
    }
    
}
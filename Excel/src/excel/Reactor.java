/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import util.DateUtil;

/**
 *
 * @author QuatSolutions SC
 */
public class Reactor {
    public static ArrayList<String> reac=new ArrayList();
    public static String outFolder = "C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto Zurich - Evidencia\\exportar\\18-02-2019\\Excel\\" + DateUtil.dateToString() + "\\out\\";
    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, Exception {
        BufferedReader mail = new BufferedReader (new InputStreamReader(new FileInputStream("C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto Zurich - Evidencia\\exportar\\18-02-2019\\Excel\\20190305\\out\\MAIL5-3-2019--20-9-8.csv"), "ISO-8859-1"));
        BufferedReader sms = new BufferedReader (new InputStreamReader(new FileInputStream("C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto Zurich - Evidencia\\exportar\\18-02-2019\\Excel\\20190305\\out\\LayoutFinalSMS5-3-2019--20-9-53.csv"), "ISO-8859-1"));
        BufferedReader archivo = new BufferedReader (new InputStreamReader(new FileInputStream("C:\\Users\\QuatSolutions SC\\Desktop\\Proyecto Zurich - Evidencia\\exportar\\18-02-2019\\Excel\\Resultado Final.CSV"), "ISO-8859-1"));
        if (mail.lines()!=null || sms.lines()!=null || archivo.lines()!=null ) {
            ArrayList<String> correo=new ArrayList();
            correo=validarVigencia(mail, archivo);
            ArrayList<String> mensaje=new ArrayList();
            mensaje=validarVigenciaMensaje(sms);
        }else{
            System.out.println("Un archivo esta vacio, Verificar");
        }
        

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
    public static ArrayList<String> validarVigenciaMensaje ( BufferedReader layout) throws Exception{
            ArrayList<String> validar = new ArrayList();         
            try {
                String lineaL = layout.readLine();
             
                validar.add("CANAL,RAMO,POLIZA,POLIZA_CONFIRMAR,TELEFONO_CELULAR,NOMBRE_DEL_DESTINATARIO,NOMBRE_RAMO,\r\n");
                for (int i = 0; i < reac.size(); i++) {
                }
               while (lineaL != null) {               
                    String[] campos = lineaL.split(","); 
                    String pal=Arrays.toString(campos).replace("   ", "");                    
                    if (campos.length>1) {
                            String polizal= limpiaPoliza(campos[3]);
                            for (int i = 0; i < reac.size(); i++) {
                                if (reac.get(i).equals(polizal)) {
                                    validar.add(pal.replace("[", "").replace("]", "")+"\r\n");
                                }
                        }
                   }
                    lineaL = layout.readLine();
                }
               salida_csv(validar, "Layout_SMS_WS");
                
            }catch (IOException e){
                    System.out.println("Error al leer el archivo");
                    }
        return validar;  
   }
    
    
    public static ArrayList<String> validarVigencia ( BufferedReader layout, BufferedReader salidaReactor) throws Exception{
            ArrayList<String> reactor = new ArrayList();
            ArrayList<String> validar = new ArrayList();         
            try {
                String linea = salidaReactor.readLine();
                while (linea != null) {
                    String[] campos = linea.split(",");               
                        if (campos[5].contains("VIGENTE")) {
                            String poliza=campos[0].replace(" ", "")+"-"+campos[1].replace(" ", "")+"-"+campos[2].replace(" ", "");
                            //System.out.println(poliza);
                            reactor.add(poliza);
                            reac.add(poliza);
                        }
                    linea = salidaReactor.readLine();
                  }
                String lineaL = layout.readLine();
                int count=0;
              //  System.out.println("++++");
                int cont=0;
                validar.add("CANAL,RAMO,POLIZA,PRODUCTO,RENRAMOS,CELULAR,EMAIL,NOMBRE,SUCURSAL,FECHA_EMISION,FECHA,NOMBRE_RAMO,POLIZA_CONCATENADO,BENEFICIOS,LIGA,ARCHIVO,ARCHIVO1,ARCHIVO2,ARCHIVO3,ARCHIVO4,ARCHIVOS,ENVIO,C_ENVIO,PLANTILLA,\r\n");
               while (lineaL != null) {               
                    String[] campos = lineaL.split(","); 
                    String pal=Arrays.toString(campos).replace("   ", "");
                    if (campos.length>1) {
                            String polizal= limpiaPoliza(campos[12]);
                            for (int i = 0; i < reactor.size(); i++) {
                                if (reactor.get(i).equals(polizal)) {
                                    validar.add(pal.replace("[", "").replace("]", "")+"\r\n");
                                }
                        }
                   }
                    lineaL = layout.readLine();
                       count++;      
                }
                unirPolizas(validar);
            }catch (IOException e){
                    System.out.println("Error al leer el archivo");
                    }
        return validar;  
   }
    public static void unirPolizas ( ArrayList<String> arreglo){
        ArrayList<String> salida= new ArrayList(arreglo);
        ArrayList<String> copia= new ArrayList(arreglo);
        ArrayList<String> repe= new ArrayList();
        ArrayList<Integer> posiciones= new ArrayList();
        for (int i = 0; i < arreglo.size(); i++) {
            String[] campos = arreglo.get(i).split(","); 
            for (int j = 0; j < copia.size(); j++) {
                String[] camposc = copia.get(j).split(","); 
                if (campos[6].equals(camposc[6]) && i!=j ) {
                    posiciones.add(i);
                    repe.add(arreglo.get(i));
                }
            }
        }
        
         ArrayList<String> salidas= new ArrayList();
         salidas=unirRepetidos(repe);
         ArrayList<String> salidaWeb= new ArrayList();
         salidaWeb=webService(salida, posiciones, salidas );
         salida_csv(salidaWeb, "Layout_MAIL_WS");
         for (int i = 0; i < salidaWeb.size(); i++) {
            // System.out.println(i+"\t"+salidaWeb.get(i));
        }
    }
    public static ArrayList<String> webService (ArrayList<String> vig, ArrayList<Integer> repetidos, ArrayList<String> union){
        ArrayList<String> salida= new ArrayList();
        ArrayList<String> nuevo= new ArrayList();
        ArrayList<String> correo= new ArrayList();
        for (int i = 0; i < repetidos.size(); i++) {
            correo.add(vig.get(repetidos.get(i)));
        }
        for (int i = 0; i < vig.size(); i++) {
            int au=correo.size();
            if (!correo.contains(vig.get(i))) {
                salida.add(vig.get(i));
            }
            
        }
        for (int i = 0; i < union.size(); i++) {
            salida.add(union.get(i));
        }
        
        return salida;
                
    }
    public static String limpiaPoliza(String poliza){
        char [] palabra= poliza.toCharArray();
        ArrayList<String> agrega= new ArrayList();
        for (int i = 0; i < palabra.length; i++) {
            if (palabra[i]=='0' || palabra[i]=='1' || palabra[i]=='2' || palabra[i]=='3' || palabra[i]=='4'  || palabra[i]=='5'  || palabra[i]=='6'  || palabra[i]=='7' || palabra[i]=='8' || palabra[i]=='9' || palabra[i]=='-') {
               agrega.add(String.valueOf(palabra[i]));
            }
        }
        String salida=agrega.toString().replace("[", "").replace("]", "").replace(",", "").replace(" ", "");
        return salida;
    }
    
    public static ArrayList<String> unirRepetidos(ArrayList<String> repe){
        ArrayList<String> salida= new ArrayList();
        ArrayList<String> copia= new ArrayList(repe);
        ArrayList<String> uni= new ArrayList();
        for (int i = 0; i < repe.size(); i++) {
            ArrayList<String> unidos= new ArrayList();
            String[] campos = repe.get(i).split(","); 
            String poliza=campos[0].replace(" ", "")+","+campos[1].replace(" ", "")+","+campos[2].replace(" ", "");
            for (int j = 0; j < copia.size(); j++) {
                String[] camposc = copia.get(j).split(","); 
                String polizaCopia=campos[0].replace(" ", "")+","+campos[1].replace(" ", "")+","+campos[2].replace(" ", "");
                if (campos[6].equals(camposc[6]) && i!=j ) {
                    for (int k = 0; k < campos.length; k++) {
                        if (k==16) {
                            unidos.add(campos[16]+":"+camposc[16]);
                        }else{
                            unidos.add(campos[k]);
                        }
                    }
                }
            }
            uni.add(unidos.toString().replace("[", "").replace("]", ""));
        }
        ArrayList<String> copy=new ArrayList(uni);
        ArrayList<String> correo=new ArrayList();
        for (int i = 0; i < uni.size(); i++) {
            String [] word=uni.get(i).split(",");
                String c1=word[6];
            correo.add(c1);
        }
        HashSet<String> hashSet = new HashSet<String>(correo);
        copy.clear();
        copy.addAll(hashSet);
        for (int i = 0; i < copy.size(); i++) {
            int aux=0;
            for (int j = 0; j < uni.size(); j++) {
                String [] word=uni.get(j).split(",");
                String c1=word[6];
                if (copy.get(i).equals(c1)) {
                    aux++;
                    if (aux==1) {
                        salida.add(uni.get(j));
                    }
                }
            }
        }
        return salida;
    }   
}

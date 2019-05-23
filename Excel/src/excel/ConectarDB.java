/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author omart
 */
public class ConectarDB {
    
            ArrayList <String> segunda=new ArrayList();

   public static Connection conexion() throws Exception{
       try{
            String driver= "com.mysql.jdbc.Driver";
            String usuario= "root";
            String pass= "torres340";
            String direccion = "jdbc:mysql://localhost:3306/proyecto_Zurich";
            Class.forName(driver);
            Connection cone= DriverManager.getConnection(direccion, usuario, pass);
            System.out.println("Conexion exitosa");
            return cone;
       }catch(Exception e){
           System.out.println(e);
       }
       return null;
   }
   public static void cargarDatos(ArrayList campos, ArrayList datos) throws Exception{

        String cadena="INSERT INTO prueba_2 (";
        StringBuffer lista=new StringBuffer();
        StringBuffer datosarr=new StringBuffer();
        ArrayList<String> cade=new ArrayList();
        ArrayList<String> dat=new ArrayList();
        for (int i = 0; i < campos.size(); i++) {
            if (i<campos.size()-1) {
                String dato=campos.get(i)+", ";
                System.out.println(campos.get(i));
                cade.add(dato);
                }else{
                String dato=campos.get(i)+") VALUES ";
                System.out.println(campos.get(i));
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
            if(aux==3){
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
        String lis=String.valueOf(cade);
        String que=cadena+lista+datosarr;
        String selec="SELECT *FROM DETALLE;";
        String det="DESCRIBE DETALLE;";
        System.out.println(que);
        try{
            
           Connection con =conexion();
           PreparedStatement crear =con.prepareStatement(que);
           crear.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
    }
   
   public static ArrayList consulta (String consul) throws Exception{
       String consulta=consul;
       ArrayList lista=new ArrayList();
       try{
           Connection con =conexion();
           PreparedStatement crear =con.prepareStatement(consulta);
           ResultSet resultado= crear.executeQuery();
           System.out.println(resultado);
           for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                   System.out.print(resultado.getMetaData().getColumnName(i)+ ",");
                   lista.add(resultado.getMetaData().getColumnName(i)+ ",");
                   lista.add("\r\n");
               }
           int tam=0;
           System.out.println("");
           while(resultado.next()){
               for (int i = 1; i <= resultado.getMetaData().getColumnCount(); i++) {
                   System.out.print(resultado.getString(i)+",");
                   lista.add(resultado.getString(i)+",");
               }
               System.out.println("");
               lista.add("\r\n");
               
           }
       }catch(Exception e){
           System.out.println("Error: "+e);
       }
       return lista;
   }
   
    public static void crearTabla(ArrayList campos) throws Exception{
        String var="varchar(255)";
        String var2="PRIMARY KEY (DETALLE_ID));";
        String cadena="CREATE TABLE IF NOT EXISTS PRUEBA_2(DETALLE_ID INT NOT NULL AUTO_INCREMENT, ";
        StringBuffer lista=new StringBuffer();
        ArrayList<String> cade=new ArrayList();
        for (int i = 0; i < campos.size(); i++) {
            String dato=campos.get(i)+" "+var+", ";
            System.out.println(campos.get(i));
            cade.add(dato);
        }
        for(String string:cade)
             lista=lista.append(String.valueOf(string)); 
        String lis=String.valueOf(cade);
        String que=cadena+lista+var2;
        String selec="SELECT *FROM DETALLE;";
        String det="DESCRIBE DETALLE;";
        System.out.println(que);
        try{
            
           Connection con =conexion();
           PreparedStatement crear =con.prepareStatement(que);
           crear.executeUpdate();
        }catch(Exception e){
            System.out.println(e);
        }
    }
    public static void main(String[] args) throws Exception {
        ArrayList<String> cp = new ArrayList();
        ArrayList<String> dt = new ArrayList();
        ArrayList<String> pase = new ArrayList();
        dt.add("1");
        dt.add("2");
        dt.add("3");
        dt.add("4");
        dt.add("5");
        dt.add("6");
        dt.add("7");
        dt.add("8");
        dt.add("9");
        cp.add("Hola");
        cp.add("uno");
        cp.add("tres");
        crearTabla(cp);
        System.out.println("**");
        //cargarDatos(cp,dt);
        String query=(String) JOptionPane.showInputDialog(null,"Ingrese una consulta");
        pase=consulta(query);
    }
    
}

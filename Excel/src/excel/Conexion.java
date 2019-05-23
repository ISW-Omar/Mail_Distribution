/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author QuatSolutions SC
 */
public class Conexion {
    public static Connection conexion;
    public static final String driver= "com.mysql.jdbc.Driver";
    public static String usuario= "root";
    public static String pass= "torres340";
    public static String direccion = "jdbc:mysql://localhost:3306/proyecto_Zurich";
    
    public Conexion(){
        conexion=null;
        try{
            Class.forName(driver);
            conexion= DriverManager.getConnection(direccion, usuario, pass);
            if(conexion!=null){
                System.out.println("Conexion establecida con exito");
            }
        }catch(ClassNotFoundException | SQLException e){
            System.out.println("Error de conexion"+ e);
        }
    }
    public Connection  getConexion(){
        return conexion;
    }
    public void desconectar(){
        conexion=null;
        if(conexion==null){
            System.out.println("Conexion terminada");
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mediciones1;

import java.sql.*;
import java.util.ArrayList;

public class Conexion {
       final String controlador = "com.mysql.jdbc.Driver";
    final String bd = "jdbc:mysql://localhost/proyecto";
    final String usuario = "root";
    final String password = "";
    
    private Connection conexion = null;
    
    Statement instruccion_select = null;
    PreparedStatement instruccion_insert_update_delate = null;
    
    ResultSet conjuntoResultados;
    ResultSetMetaData metaDatos;
    String sql = "";
    
    final String consulta_predeterminada = "SELECT * FROM datos";
    
    public ArrayList <String> id = new ArrayList <String> ();
    public ArrayList <String> lectura = new ArrayList <String> ();
    public ArrayList <String> litros = new ArrayList <String> ();
    public ArrayList <String> tiempo = new ArrayList <String> ();
    public ArrayList <String> lecturas = new ArrayList <String> ();
   
    
    // Metodo que permite abrir una conexion a la BD
    public void conectar () throws Exception{
        
        try{
            Class.forName(controlador).newInstance();
            conexion = DriverManager.getConnection(bd,usuario,password);
        }

        catch(ClassNotFoundException e){
            conexion = null;
            System.out.println("Error al cargar driver");
        }

        catch(SQLException e){
            conexion = null;
            System.out.println("Error en la conexion");
        }
    }   
    
    // Metodo que nos permite hacer una consulta a la BD    
    public void select_datos(String consulta){

        try{
            instruccion_select = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            conjuntoResultados = instruccion_select.executeQuery(consulta);
            

            while(conjuntoResultados.next())
            {
                id.add(conjuntoResultados.getString(1));
                lectura.add(conjuntoResultados.getString(2));
                litros.add(conjuntoResultados.getString(3));
                tiempo.add(conjuntoResultados.getString(4));
            }
        }
            
        catch(SQLException e){
            System.out.println("Ocurrio un error al ejecutar la consulta de datos x3" + e);
        }
        
        finally{
            
            try{
                conexion.close();
            }
            
            catch(Exception e){
                System.out.println("Error al cerrar la base de datos");
            }
        }
    }
     
    public void insert_datos (String lectura, double litros, double tiempo){
        
        sql = "insert into datos (lectura,litros,tiempo) values (?,?,?)";

        try{         
            instruccion_insert_update_delate = conexion.prepareStatement(sql);
            instruccion_insert_update_delate.setString(1,lectura);
            instruccion_insert_update_delate.setDouble(2,litros);
            instruccion_insert_update_delate.setDouble(3,tiempo);

            instruccion_insert_update_delate.executeUpdate();
        }
        
        catch(SQLException e){
            System.out.println("Ocurrio un error al insertar los datos");
        }

        finally{
           
            try{
                conexion.close();
            }
           
           catch(Exception e){
               System.out.println("Error al cerrar la base de datos");
           }
        }
    }
        
    public void select_datos_lecturas(String consulta){
        
        try{
            instruccion_select = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            conjuntoResultados = instruccion_select.executeQuery(consulta);

            while(conjuntoResultados.next())
            {
                lecturas.add(conjuntoResultados.getString(1));
            }
        }
            
        catch(SQLException e){
            System.out.println("Ocurrio un error al ejecutar la consulta de datos hey");
        }
        
        finally{
            
            try{
                conexion.close();
            }
            
            catch(Exception e){
                System.out.println("Error al cerrar la base de datos");
            }
        }
    }
}

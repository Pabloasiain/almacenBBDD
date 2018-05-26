
/**
 * Clase basica que gestiona una conexion JDBC a MySQL ModeloDB here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.util.Iterator;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ModeloDB extends ModeloAbs
{
    // instance variables - replace the example below with your own
        private Connection conexion = null;
        private Statement stmt = null;
        private String servidor = "jdbc:mysql://localhost/Productosdb";
        private String usuario = "root";
        private String contraseña = "root";
    
        /**
     * Constructor for objects of class ModeloDB
     * Establece la conexion a la base de datos
     */
   public ModeloDB()
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e) {
            System.err.println("Error al registrar driver");
            System.exit(0);
        }
        
        try {
            conexion = DriverManager.getConnection(servidor, usuario, contraseña);
            stmt = conexion.createStatement();
        }
        catch (SQLException e){
            System.err.println("Error al conectar al servidor");
            System.exit(0);
        }
    }

    // INSERT
    public boolean insertarProducto ( Producto p){
        String sqlStr = "INSERT INTO `Productos` (`CODIGO`, `NOMBRE`, `STOCK`, `STOCK_MIN`, `PRECIO`) VALUES (?,?,?,?,?);";
        PreparedStatement sentenciapreparada = null;
        int nfilas = 0;
        
        //creo una sentecnia preparada
        try{
            sentenciapreparada = conexion.prepareStatement(sqlStr);
        } catch (SQLException e) {
            System.out.println("ERROR en la instruccion de SQL");
            e.printStackTrace();
            return false;    
        }   
        try {
            sentenciapreparada.setInt   (1,p.getCodigo());
	    sentenciapreparada.setString(2,p.getNombre());
	    sentenciapreparada.setInt   (3,p.getStock());
	    sentenciapreparada.setInt   (4,p.getStock_min());
	    sentenciapreparada.setFloat (5,p.getPrecio());
	    nfilas = sentenciapreparada.executeUpdate();
	   } catch (Exception ex) {
	    ex.printStackTrace();
	    return false;
	   }
	  return (nfilas== 1);
    }

    // DELETE
    boolean borrarProducto ( int codigo ){
        int nfilas = 0;
        String sqlStr = "DELETE FROM Productos where CODIGO = " + codigo;
        try {
            nfilas = stmt.executeUpdate(sqlStr);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return (nfilas == 1);
     }
    
    // SELECT
    public Producto buscarProducto ( int codigo){
        String sqlStr = "select * from Productos WHERE CODIGO = "+codigo;
        Producto resu = null;
        try{
            ResultSet rset = stmt.executeQuery(sqlStr);
            if (rset.next()){
                resu = new Producto();
                resu.setCodigo    (rset.getInt("CODIGO"));
                resu.setNombre    (rset.getString("NOMBRE"));
                resu.setStock     (rset.getInt("STOCK"));
                resu.setStock_min (rset.getInt("STOCK_MIN"));
                resu.setPrecio    (rset.getFloat("PRECIO"));
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            resu = null;
        }
        return resu;
    }
    
    
    //SELECT
    void listarProductos (){
       String sqlStr = "select * from Productos ";
       try {
	    ResultSet rset = stmt.executeQuery(sqlStr);
	    // Solo de debe existir uno;
            while ( rset.next ()){
                System.out.printf("%5s %-30s %5s %5s  %s \n",rset.getString(1),rset.getString(2),rset.getString(3),
                   rset.getString(4),rset.getString(5));
            }	
	} catch (Exception ex) {
	  ex.printStackTrace();
	}
    }
    
    //UPDATE
    boolean modificarProducto (Producto nuevo){  
        String sqlStr = "UPDATE `Productos` SET CODIGO = ?, NOMBRE = ?, STOCK = ?, STOCK_MIN = ?, PRECIO = ? "+
                       " WHERE CODIGO = ?";
	PreparedStatement sentenciapreparada = null;
	int nfilas = 0;
 
	 // Creo una sentencia preparada
	 try {
	      sentenciapreparada = conexion.prepareStatement(sqlStr);
          } catch (SQLException e) {
	      System.err.println("ERROR en la instrucción de SQL");
	      e.printStackTrace();
	      return false;
          }
 
	try {
	    sentenciapreparada.setInt   (1,nuevo.getCodigo());
	    sentenciapreparada.setString(2,nuevo.getNombre());
	    sentenciapreparada.setInt   (3,nuevo.getStock());
	    sentenciapreparada.setInt   (4,nuevo.getStock_min());
	    sentenciapreparada.setFloat (5,nuevo.getPrecio());
	    sentenciapreparada.setInt   (6,nuevo.getCodigo()); // Where
	    nfilas = sentenciapreparada.executeUpdate();
	    } catch (Exception ex) {
	    ex.printStackTrace();
	    return false;
	}
        return (nfilas == 1); // true si se ha producido la modificacion
      
    }
    
    // Devuelvo un Iterador de una ArrayList con los resultados
    // copiados de Rset al ArrayList
   Iterator <Producto> getIterator(){
       ArrayList <Producto> lista = new ArrayList<Producto>();
       // Relleno el array list con los resultados de al consulta
       String sqlStr = "select * from Productos ";
       try {
	    ResultSet rset = stmt.executeQuery(sqlStr);
	    // Solo de debe existir uno;
            while ( rset.next ()){
                Producto p = new Producto();
                p.setCodigo    (rset.getInt   ("CODIGO"));
                p.setNombre    (rset.getString("NOMBRE"));
                p.setStock     (rset.getInt   ("STOCK"));
                p.setStock_min (rset.getInt   ("STOCK_MIN"));
                p.setPrecio    (rset.getFloat ("PRECIO"));
                lista.add(p); // Añado el objeto a la coleccion
            }	
	} catch (Exception ex) {
	  ex.printStackTrace();
	}
       return lista.iterator();
     }
}

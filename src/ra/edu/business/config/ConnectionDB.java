package ra.edu.business.config;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3306/phone_manager";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "1234@";
    public static Connection openConnection() {
        Connection conn = null;
        try{
            conn =  DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (SQLException e){
            System.err.println("Loi ket noi CSDL do: "+e.getMessage());
        } catch (Exception e){
            System.err.println("Loi ko xac dinh khi ket noi CSDL: "+e.getMessage());
        }
        return conn;
    }
    public static void closeConnection(Connection conn, CallableStatement callSt){
        if(conn != null){
            try{
                conn.close();
            }catch(SQLException e){
                throw  new RuntimeException(e);
            }
        }
        if(callSt!=null){
            try{
                callSt.close();
            }catch(SQLException e){
                throw  new RuntimeException(e);
            }
        }
    }
}

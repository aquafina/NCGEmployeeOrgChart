package nishat.net.Helper;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import javax.sql.DataSource;

public class ConnectionProvider {
    private static DataSource myDS = null;
    static {
        try {
            Context ctx = new InitialContext();
            //myDS = (DataSource)ctx.lookup("java:jdbc/prodDS");
            myDS = (DataSource)ctx.lookup("jdbc/ebsDS");
            // your datasource jndi name as defined during configuration
            if (ctx != null)
                ctx.close();
        } catch (NamingException ne) {
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            System.out.println("ConnectionProvider.java: " + ne.getMessage());
            System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
            //ne.printStackTrace();//ideally you should log it
            //throw new RuntimeException(ne);
        }
    }

    private ConnectionProvider() {
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        if (myDS == null){
            throw new IllegalStateException("AppsDatasource is not properly initialized or available");
        }
        return myDS.getConnection();
    }

    public static String connectionState() {
        String state = "";
        try {
            state = (myDS.getConnection() == null ? "is null" : "is not null");
        } catch (SQLException e) {
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            System.out.println("ConnectionProvider.java: " + e.getMessage());
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        }
        return state;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beam.scottypointsticker.Stores;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author Administrator
 */
public class CentralStore {

    public static String MySQLDatabase = null;
    public static String MySQLUser = null;
    public static String MySQLPass = null;
    public static ComboPooledDataSource MySQLCon = null;

    public static Connection ConMySQL() throws SQLException {

        return MySQLCon.getConnection();

    }

}

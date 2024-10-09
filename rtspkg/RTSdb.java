package rtspkg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static rtspkg.RTSdebug.showArray;

/*
 *
 *
 */
public class RTSdb {



    /*
     * DB test method: connect + create + populate table + query
     */
    public static void sqlTest(String dbName, Boolean debugMode) {
        Connection connection = null;
        try {
            // db connect
            // connection = DriverManager.getConnection("jdbc:sqlite:./data/RTS.db");
            connection = DriverManager.getConnection("jdbc:sqlite:"+dbName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(3); //set timeout in seconds

            // SQL: DDL, DML
            statement.executeUpdate("create table if not exists RTSraw (date date, country varchar(2), plate nvarchar(10));");

            statement.executeUpdate("drop table if exists TempFlexType;");
            statement.executeUpdate("create table TempFlexType (field TEXT primary key, value ANYTHING, type varchar(50));");
            statement.executeUpdate("insert into TempFlexType (field, value) values ('text', 'zażółć gęślą jaźń');");
            statement.executeUpdate("insert into TempFlexType (field, value) values ('lorem', 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.');");
            statement.executeUpdate("insert into TempFlexType (field, value) values ('int', -123);");
            statement.executeUpdate("insert into TempFlexType (field, value) values ('real', 10.0/3);");
            statement.executeUpdate("insert into TempFlexType select 'pi', pi(), null;");
            statement.executeUpdate("update TempFlexType set type = typeof(value);");

            //DEBUG_MODE: execute SQL query and print resultset
            if (debugMode) {
                System.out.println("<DEBUG_MODE=true LogSrc=RTSdb>");
                System.out.println("### TempFlexType table content ###");
                ResultSet rs = statement.executeQuery("select * from TempFlexType order by rowid;");
                while(rs.next()) {
                    System.out.println("field [" + rs.getString("field") + "] is type [" 
                                        + rs.getString("type") + "] and has a value = " 
                                        + rs.getString("value"));
                }
            }
        } catch(SQLException e) {
            // Failed to connect/create a file
            System.err.println(e.getMessage());
        }

        finally {
            try {
            if(connection != null){
                connection.close();
            }
            } catch(SQLException e) {
            // Failed to disconnect the file
            System.err.println(e.getMessage());
            }
        }
    }

    public static boolean isDBready(String dbName, Boolean debugMode) {
        Connection connection = null;
        int rownum = -2;

        try {
            // db connect
            //connection = DriverManager.getConnection("jdbc:sqlite:./data/RTS.db");
            connection = DriverManager.getConnection("jdbc:sqlite:"+dbName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(1); //set timeout in seconds
            //ResultSet rs = statement.executeQuery("select count(1) as NumOfRows from RTSraw;");
            ResultSet rs = statement.executeQuery("SELECT count(1) as cnt FROM sqlite_master WHERE type='table' AND name='RTSraw';");
            if (rs.getString("cnt").length()>0) {
                rownum = Integer.parseInt(rs.getString("cnt"));
            } else { 
                rownum = 0;
            }
        } catch(SQLException e) {
            rownum = -110;
            System.err.println("ERROR [" + rownum + "] " + e.getMessage());
        }

        finally {
            try {
            if(connection != null){
                connection.close();
            }
            } catch(SQLException e) {
            // Failed to disconnect the file
            rownum = -120;
            System.err.println("ERROR [" + rownum + "] " + e.getMessage());
            System.err.println(e.getMessage());
            }
        }
        return rownum > 0;       
    }

    public static void resetDB(String dbName, Boolean debugMode) {
        Connection connection = null;
        try {
            // db connect
            // connection = DriverManager.getConnection("jdbc:sqlite:./data/RTS.db");
            connection = DriverManager.getConnection("jdbc:sqlite:"+dbName);
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(3); //set timeout in seconds
            statement.executeQuery("drop table if exists RTSraw;"
                                  +"drop table if exists FlexType;");
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        }

        finally {
            try {
            if(connection != null){
                connection.close();
            }
            } catch(SQLException e) {
            // Failed to disconnect the file
            System.err.println(e.getMessage());
            }
        }
    }

    public static void sqlLoadPlates(String dbName, Boolean debugMode, String code, String plates[][]) {

        for(int i =0, aggr_sum=0; i< plates.length; i++, aggr_sum=0){
            // plateSet[0][i][0] = plates[i][0];
            for(int aggr = 0; aggr <= i; aggr++){
                // increasing sum of previous population values
                aggr_sum += Integer.valueOf(plates[aggr][2]);
            }
            plates[i][0] = code;
            plates[i][3] = String.valueOf(aggr_sum);
        }

        //DEBUG_MODE: print country code + array [plates] values
        if (debugMode) {
            System.out.println("<DEBUG_MODE=true LogSrc=RTSdb>");
            System.out.println(">>> COUNTRY: "+code);
            showArray(plates);
        }
    }

}
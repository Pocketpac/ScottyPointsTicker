/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beam.scottypointsticker.Stores;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.json.simple.JSONObject;

/**
 *
 * @author Administrator
 */
public class CentralStore {

    public static String MySQLDatabase = null;
    public static String MySQLUser = null;
    public static String MySQLPass = null;
    public static ComboPooledDataSource MySQLCon = null;
    public static int RankQueue = 0;
    public static int PointsQueue = 0;
    final public static int cores = Runtime.getRuntime().availableProcessors();
    public static ExecutorService ThreadQueue = Executors.newFixedThreadPool(2);
    public static ExecutorService MiscThreads = Executors.newFixedThreadPool(cores * 5);
    public static JSONObject LastTweet = new JSONObject();
    public static int TweetQueue = 0;

    public static Long GetLastTweet(Long ChanID) {
        return (Long) LastTweet.getOrDefault(ChanID, 0L);
    }

    public static void UpdateLastTweet(Long ChanID) {

        Long NextTweet = System.currentTimeMillis() * (8 * 60 * 60 * 1000);
        LastTweet.put(ChanID, NextTweet);
    }

    public static void ClearLastTweet(Long ChanID) {
        if (LastTweet.containsKey(ChanID)) {
            LastTweet.remove(ChanID);
        }
    }

    public static Connection ConMySQL() throws SQLException {

        return MySQLCon.getConnection();

    }

}

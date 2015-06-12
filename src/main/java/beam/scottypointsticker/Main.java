/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beam.scottypointsticker;

import beam.scottypointsticker.Stores.CentralStore;
import static beam.scottypointsticker.Stores.CentralStore.MySQLCon;
import static beam.scottypointsticker.Stores.CentralStore.MySQLDatabase;
import static beam.scottypointsticker.Stores.CentralStore.MySQLPass;
import static beam.scottypointsticker.Stores.CentralStore.MySQLUser;
import beam.scottypointsticker.utils.Points;
import beam.scottypointsticker.utils.autoTweeter;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tjhasty
 */
public class Main {

    public static boolean DebugMode = false;

    private static String getPid() {
        File proc_self = new File("/proc/self");
        if (proc_self.exists()) {
            try {
                return proc_self.getCanonicalFile().getName();
            } catch (IOException e) {
                /// Continue on fall-back
            }
        }
        File bash = new File("/bin/bash");
        if (bash.exists()) {
            ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", "echo $PPID");
            try {
                Process p = pb.start();
                BufferedReader rd = new BufferedReader(new InputStreamReader(p.getInputStream()));
                return rd.readLine();
            } catch (IOException e) {
                return String.valueOf(Thread.currentThread().getId());
            }
        }
        // This is a cop-out to return something when we don't have BASH
        return String.valueOf(Thread.currentThread().getId());
    }

    public static void main(String[] args) {
        PrintStream PID = null;
        try {
            PID = new PrintStream(new FileOutputStream("ScottyPointsTicker.pid"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        PID.print(getPid());
        PID.close();
        try {
            getPropValues();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (!DebugMode) {
            PrintStream out = null;
            try {
                out = new PrintStream(new FileOutputStream("PointsTickerConsole.Log", true));
            } catch (Exception e) {
            }

            System.setOut(out);
            PrintStream Errorout = null;
            try {
                Errorout = new PrintStream(new FileOutputStream("PointsTickerErrorLog.Log", true));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
//            PrintStream out = new PrintStream(new FileOutputStream("Console_Output.Log", true));
            System.setErr(Errorout);

        }

        try {
            PrepMysqlPool();
        } catch (PropertyVetoException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        PrepTimers();
        CentralStore.MiscThreads.execute(new startTweeter());

    }

    public static void PrepTimers() {
        Timer timer = new Timer();
        timer.schedule(new PointsLoopTimer(), 5000, 900000);
        System.out.println("Starting Points Timer Thread");
    }

    public static void PrepMysqlPool() throws PropertyVetoException {
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
        cpds.setJdbcUrl(MySQLDatabase);
        cpds.setUser(MySQLUser);
        cpds.setPassword(MySQLPass);
        cpds.setAcquireIncrement(5);
        cpds.setMaxPoolSize(100);
        cpds.setMaxIdleTime(3600);
        cpds.setMaxIdleTimeExcessConnections(300);
        cpds.setMinPoolSize(20);
        cpds.setNumHelperThreads(6);
        cpds.setUnreturnedConnectionTimeout(120);
        cpds.setPreferredTestQuery("select 1;");
        cpds.setIdleConnectionTestPeriod(10);
        cpds.setCheckoutTimeout(5000);
        cpds.setUnreturnedConnectionTimeout(30);
        MySQLCon = cpds;
    }

    public static void getPropValues() throws IOException {

        Properties prop = new Properties();
        prop.load(new FileInputStream("config.properties"));

        MySQLDatabase = prop.getProperty("mysqldatabase");
        MySQLUser = prop.getProperty("mysqluser");
        MySQLPass = prop.getProperty("mysqlpassword");
        DebugMode = Boolean.parseBoolean(prop.getProperty("Debug"));
    }

    static class PointsLoopTimer extends TimerTask {

        public void run() {
            try {
                new Points().StartPointsLoop();
            } catch (Exception ex) {

            }

        }
    }

    static class startTweeter implements Runnable {

        @Override
        public void run() {
            new autoTweeter().TweetLoop();
        }

    }

}

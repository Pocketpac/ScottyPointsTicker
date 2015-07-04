
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beam.scottypointsticker.utils;

import beam.scottypointsticker.Stores.CentralStore;
import static beam.scottypointsticker.Stores.CentralStore.ConMySQL;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Administrator
 */
public class sql {

    public Map<String, String> getTwitterAuth(Long ChanID) throws SQLException {
        Map<String, String> toSend = new HashMap();
        Connection con = CentralStore.ConMySQL();
        Statement statement = con.createStatement();
        statement.setQueryTimeout(15);
        ResultSet results = statement.executeQuery("SELECT * FROM twitterauthcreds where ChanID=" + ChanID);
        while (results.next()) {
            toSend.put("CKey", results.getString("CKey"));
            toSend.put("CSecret", results.getString("CSecret"));
            toSend.put("AToken", results.getString("AToken"));
            toSend.put("ATokenSecret", results.getString("ATokenSecret"));
        }
        statement.close();
        con.close();
        return toSend;
    }

    public JSONObject GetTweeters() throws SQLException {
        JSONObject Tweeters = new JSONObject();
        Connection con = ConMySQL();
        Statement statement = con.createStatement();
        ResultSet result = statement.executeQuery("SELECT ChanID,AutoTweetMsg FROM settings WHERE UseBot=1 AND useAutoTweet=1");

        while (result.next()) {
            Long chanid = result.getLong("ChanID");
            String TweetMSG = result.getString("AutoTweetMsg");
            Tweeters.put(chanid, TweetMSG);

        }
        result.close();
        statement.close();
        con.close();
        return Tweeters;
    }

    public void TickTimeWatched(Long ChanID) throws ClassNotFoundException, SQLException, IOException {

        JSONArray ToTick = new JSONArray();
        int page = 0;
        while (true) {
            try {
                JSONArray toAdd = new JSONArray();
                toAdd.addAll((JSONArray) new JSONParser().parse(new HTTP().getRemoteContent("https://beam.pro/api/v1/chats/" + ChanID + "/users?limit=50&page=" + page)));
                if (toAdd.isEmpty()) {
                    break;
                }
                ToTick.addAll(toAdd);
                page++;
            } catch (ParseException ex) {
                try {
                    sleep(1500);
                } catch (InterruptedException ex1) {
                    Logger.getLogger(sql.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        Connection con = ConMySQL();
        Statement statement = con.createStatement();
        ResultSet curHours = statement.executeQuery("SELECT * FROM rankingtable WHERE ChanID=" + ChanID);
        Map<Long, Long> curPoints = new HashMap();

        while (curHours.next()) {
            curPoints.put(curHours.getLong("UserID"), curHours.getLong("hoursWatched"));
        }
        statement.close();
        con.close();
        Long TickTime = 15L * 60 * 1000;
        Connection con2 = ConMySQL();
        Statement statement2 = con2.createStatement();

        for (Object T : ToTick) {
            JSONObject obj = (JSONObject) T;
            Long UserID = (Long) obj.get("userId");
            Long Points = null;
            try {
                Points = curPoints.get(UserID);
            } catch (Exception e) {
            }
            if (Points == null) {
                Points = 0L;
            }
            Long finalHours = Points + TickTime;

            int Updated = statement2.executeUpdate("UPDATE rankingtable set hoursWatched=" + finalHours + " WHERE ChanID=" + ChanID + " AND UserID=" + UserID);
            if (Updated == 0) {
                statement2.executeUpdate("INSERT INTO rankingtable (UserID, ChanID, hoursWatched) VALUES (" + UserID + "," + ChanID + "," + finalHours + ")");
            }
        }

        statement2.close();
        con2.close();
    }

    public void AddPoints(Float Points, Long ChanID, Long UserID) throws ClassNotFoundException, SQLException, IOException {
        Connection con = ConMySQL();
        Statement statement = con.createStatement();

        statement.setQueryTimeout(15);
        try {

            statement.setQueryTimeout(15);  // set timeout to 30 sec.
            // load the sql-JDBC driver using the current class loader

            // create a database connection
            ResultSet curPoints = statement.executeQuery("select * from points where UserID='" + UserID + "' and ChanID='" + ChanID + "'");
            curPoints.next();
            Float FinalPoints = curPoints.getLong("points") + Points;
            //statement.execute("create table if not exists points (id INTEGER PRIMARY KEY, points INTEGER, person STRING, Channel STRING)");
            statement.execute("update points set points=" + FinalPoints + " where UserID='" + UserID + "' and ChanID='" + ChanID + "'");

        } catch (SQLException ex) {
            //statement.execute("create table if not exists points (id INTEGER PRIMARY KEY, points INTEGER, person STRING, Channel STRING)");
            int startpoints = getSettingint(ChanID, "startpoints");
            statement.executeUpdate("insert into points (points,UserID,ChanID) values (" + startpoints + ",'" + UserID + "','" + ChanID + "')");
            ResultSet curPoints = statement.executeQuery("select * from points where UserID='" + UserID + "' and ChanID='" + ChanID + "'");
            curPoints.next();
            Float FinalPoints = curPoints.getFloat("points") + Points;
            statement.execute("update points set points=" + FinalPoints + " where UserID='" + UserID + "' and ChanID='" + ChanID + "'");

        } finally {
            statement.close();
            con.close();
        }
    }

    public void AddPoints(Long Points, Long ChanID, Long UserID) throws ClassNotFoundException, SQLException, IOException {
        Connection con = CentralStore.ConMySQL();
        Statement statement = con.createStatement();
        //Statement statement = con.createStatement();

        statement.setQueryTimeout(15);
        try {

            statement.setQueryTimeout(15);  // set timeout to 30 sec.
            // load the sql-JDBC driver using the current class loader

            // create a database connection
            ResultSet curPoints = statement.executeQuery("select * from points where UserID='" + UserID + "' and ChanID='" + ChanID + "'");
            curPoints.next();
            Long FinalPoints = curPoints.getLong("points") + Points;
            //statement.execute("create table if not exists points (id INTEGER PRIMARY KEY, points INTEGER, person STRING, Channel STRING)");
            statement.execute("update points set points=" + FinalPoints + " where UserID='" + UserID + "' and ChanID='" + ChanID + "'");

        } catch (SQLException ex) {
            //statement.execute("create table if not exists points (id INTEGER PRIMARY KEY, points INTEGER, person STRING, Channel STRING)");
            int startpoints = getSettingint(ChanID, "startpoints");
            statement.executeUpdate("insert into points (points,UserID,ChanID) values (" + startpoints + ",'" + UserID + "','" + ChanID + "')");
            ResultSet curPoints = statement.executeQuery("select * from points where UserID='" + UserID + "' and ChanID='" + ChanID + "'");
            curPoints.next();
            Long FinalPoints = curPoints.getLong("points") + Points;
            statement.execute("update points set points=" + FinalPoints + " where UserID='" + UserID + "' and ChanID='" + ChanID + "'");

        } finally {
            statement.close();
            con.close();
        }
    }

    public int getSettingint(Long ChanID, String getSetting) throws ClassNotFoundException, SQLException, IOException {
        Connection con = CentralStore.ConMySQL();
        Statement statement = con.createStatement();
        statement.setQueryTimeout(15);
        ResultSet setting;
        setting = null;
        int send = 0;
        try {
            setting = statement.executeQuery("select " + getSetting.toLowerCase() + " from settings where ChanID='" + ChanID + "' and " + getSetting.toLowerCase() + " is not null");

            setting.next();
            send = setting.getInt(getSetting.toLowerCase());

        } catch (SQLException ex) {
            //System.out.println(ex);
            setting = statement.executeQuery("select * from settings where Channel='default'");
            setting.next();
            send = setting.getInt(getSetting.toLowerCase());

        } finally {
            statement.close();
            con.close();
        }
        return send;
    }

    public Long getSettingLong(Long ChanID, String getSetting) throws ClassNotFoundException, SQLException, IOException {
        Connection con = CentralStore.ConMySQL();
        Statement statement = con.createStatement();
        statement.setQueryTimeout(15);
        ResultSet setting;
        setting = null;
        Long send = null;
        try {
            setting = statement.executeQuery("select " + getSetting.toLowerCase() + " from settings where ChanID='" + ChanID + "' and " + getSetting.toLowerCase() + " is not null");

            setting.next();
            send = setting.getLong(getSetting.toLowerCase());

        } catch (SQLException ex) {
            //System.out.println(ex);
            setting = statement.executeQuery("select * from settings where Channel='default'");
            setting.next();
            send = setting.getLong(getSetting.toLowerCase());

        } finally {
            statement.close();
            con.close();
        }
        return send;
    }

    public String getSetting(Long ChanID, String getSetting) throws ClassNotFoundException, SQLException, IOException {
        Connection con = CentralStore.ConMySQL();
        Statement statement = con.createStatement();
        statement.setQueryTimeout(15);
        ResultSet setting = null;
        String send;
        try {
            setting = statement.executeQuery("select " + getSetting.toLowerCase() + " from settings where ChanID='" + ChanID + "' and " + getSetting.toLowerCase() + " is not null");
            setting.next();
            send = setting.getString(getSetting.toLowerCase());

        } catch (SQLException ex) {
            //System.out.println(ex);
            setting = statement.executeQuery("select " + getSetting.toLowerCase() + " from settings where Channel='default'");
            setting.next();
            send = setting.getString(getSetting.toLowerCase());

        } finally {
            statement.close();
            con.close();
        }
        return send;
    }

    public List<Long> GetJoinChannels() throws ClassNotFoundException, SQLException, IOException {
        Connection con = CentralStore.ConMySQL();
        Statement statement = con.createStatement();
        statement.setQueryTimeout(15);
        ResultSet setting = null;
        List<Long> send = new ArrayList();
        try {
            setting = statement.executeQuery("select ChanID from settings where UseBot=1 order by Donated DESC;");
            while (setting.next()) {
                send.add(setting.getLong("ChanID"));

            }
        } catch (SQLException e) {

        } finally {
            statement.close();
            con.close();
            return send;
        }
    }

    public Map<String, Long> GetChanList() throws SQLException, ClassNotFoundException, IOException {
        Connection con = CentralStore.ConMySQL();
        Statement statement = con.createStatement();
        statement.setQueryTimeout(15);
        Map<String, Long> Send = new HashMap();
        try {
            ResultSet Got = statement.executeQuery("select Channel, ChanID from settings where Points='1' and UseBot='1' order by Donated DESC;");

            while (Got.next()) {
                Long ID = Got.getLong("ChanID");
                String Chan = Got.getString("Channel");
                Send.put(Chan, ID);
            }
        } catch (SQLException e) {

        } finally {
            statement.close();
            con.close();

            return Send;
        }

    }
}

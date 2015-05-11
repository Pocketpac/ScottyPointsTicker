
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beam.scottypointsticker.utils;

import beam.scottypointsticker.Stores.CentralStore;
import static beam.scottypointsticker.Stores.CentralStore.ConMySQL;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Administrator
 */
public class sql {

    public void AddPoints(Float Points, Long ChanID, Long UserID) throws ClassNotFoundException, SQLException, IOException {
        Connection con = ConMySQL();
        Statement statement = con.createStatement();
        //Statement statement = con.createStatement();

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

//    public void CMDCounter(Long ChanID, String cmd) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        Long FinalCount = (long) 1;
//        try {
//
//            ResultSet Count = statement.executeQuery("select count from commands where ChanID='" + ChanID + "' and comcall='" + cmd + "'");
//            Count.next();
//            FinalCount = Count.getLong("count") + 1;
//            //statement.execute("create table if not exists points (id INTEGER PRIMARY KEY, points INTEGER, person STRING, Channel STRING)");
//            statement.execute("update commands set count=" + FinalCount + " where ChanID='" + ChanID + "'and comcall='" + cmd + "'");
//        } catch (SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//        }
//
//    }
//
//    public void DelPoints(Long Points, Long ChanID, Long UserID) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        try {
//
//            ResultSet curPoints = statement.executeQuery("select * from points where UserID='" + UserID + "' and ChanID='" + ChanID + "'");
//            curPoints.next();
//            Long FinalPoints = curPoints.getLong("points") - Points;
//            //statement.execute("create table if not exists points (id INTEGER PRIMARY KEY, points INTEGER, person STRING, Channel STRING)");
//            statement.execute("update points set points=" + FinalPoints + " where UserID='" + UserID + "' and ChanID='" + ChanID + "'");
//        } catch (SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//        }
//
//    }
//
//    public String DelCommand(Long ChanID, String comcall) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        int remove = 0;
//        String send = null;
//        try {
//            statement.executeUpdate("delete from repeatcmds where ChanID='" + ChanID + "' and command='" + comcall + "';");
//            remove = statement.executeUpdate("DELETE FROM `commands` WHERE `ChanID`='" + ChanID + "' and `comcall`='" + comcall.toLowerCase() + "'");
//            if (remove == 1) {
//                send = "Command removed";
//            } else {
//                send = "Command not found or error occured";
//            }
//        } catch (SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//        }
//
//        return send;
//    }
//
//    public String ChangComPermLevel(Long ChanID, String comcall, String permlevel) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//
//        String send = null;
//        try {
//
//            statement.setQueryTimeout(15);
//            int updated = 0;
//            try {
//                updated = statement.executeUpdate("UPDATE `commands` SET `permlevel`='" + permlevel + "' WHERE ChanID='" + ChanID + "' and comcall='" + comcall.toLowerCase() + "'");
//            } catch (SQLException e) {
//                send = "Command not found or error";
//            }
//            if (updated == 1) {
//                send = "Permissions level updated";
//            } else {
//                send = "Command not found or error";
//            }
//
//        } catch (SQLException ex) {
//            send = "Command not found or error";
//        } finally {
//            statement.close();
//            con.close();
//        }
//
//        return send;
//    }
//
//    public String GetRepeatList(Long ChanID) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException, Exception {
//
//        Connection con = CentralStore.ConMySQL();
//        String newline = System.lineSeparator();
//        String Comcall = null;
//        String Comtext = null;
//        Map<String, String> ToGist = new HashMap();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        String Stage1 = "";
//        String Stage2 = "";
//
//        try {
//            ResultSet Got = statement.executeQuery("SELECT command FROM repeatcmds WHERE ChanID = '" + ChanID + "'");
//
//            while (Got.next()) {
//                String cmd = Got.getString("command");
//                Stage1 = Stage1 + newline + newline + cmd + " - " + GetCommand(ChanID, cmd);
//            }
//
//        } catch (IOException | ClassNotFoundException | SQLException e) {
//
//        } finally {
//
//            statement.close();
//            con.close();
//            String send = Stage1;
//            if (Stage1 != "") {
//                HTTP HTTP = new HTTP();
//                return "Repeating List: " + HTTP.striked("CommandList", "", send);
//            } else {
//                return "No commands repeating";
//            }
//        }
//
//    }
//
//    public boolean addrepeat(Long ChanID, String command) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        int added = 0;
//        //comtext = comtext.replace("'", "''");
//        if (CommandExists(ChanID, command, 5)) {
//            try {
//
//                added = statement.executeUpdate("INSERT INTO `repeatcmds`(`ChanID`, `command`) VALUES ('" + ChanID + "','" + command + "')");
//
//            } catch (SQLException ex) {
//                ErrorReport(ChanID, ex);
//            } finally {
//                statement.close();
//                con.close();
//                if (added == 1) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        }
//        return false;
//    }
//
//    public String DelRepeat(Long ChanID, String Command) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        int remove = 0;
//        String send = null;
//        try {
//            remove = statement.executeUpdate("DELETE FROM `repeatcmds` WHERE `ChanID`='" + ChanID + "' and `command`='" + Command + "'");
//            if (remove == 1) {
//                send = "Repeating command removed";
//            } else {
//                send = "Repeating command not found or error occured";
//            }
//        } catch (SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//        }
//
//        return send;
//    }
//
//    public void addquote(Long ChanID, String quote) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        String equote = StringEscapeUtils.escapeSql(quote);
//        //comtext = comtext.replace("'", "''");
//        try {
//
//            statement.setQueryTimeout(15);
//            int updated = 0;
//            if (updated == 0) {
//                statement.executeUpdate("INSERT INTO `quotes`(`ChanID`, `quotes`) VALUES ('" + ChanID + "','" + equote + "')");
//            }
//
//        } catch (SQLException ex) {
//
//        } finally {
//            statement.close();
//            con.close();
//        }
//    }
//
//    public String DelQuote(Long ChanID, Integer id) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        int Removed = 0;
//        //   statement.execute("create table if not exists ChannelsToTick (id INTEGER PRIMARY KEY,Channel STRING)");
//        try {
//            Removed = statement.executeUpdate("delete from quotes where ChanID='" + ChanID + "' and id='" + id + "'");
//
//        } catch (SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//            if (Removed == 1) {
//                return "Quote Removed";
//            } else {
//                return "Quote not found or error occured";
//            }
//
//        }
//    }
//
//    public List<String> PopRepeatList(Long ChanID) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException, Exception {
//        Connection con = CentralStore.ConMySQL();
//        List<String> ToList = new ArrayList();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        try {
//
//            ResultSet Got = statement.executeQuery("SELECT command FROM repeatcmds WHERE ChanID = '" + ChanID + "'");
//
//            while (Got.next()) {
//                String cmd = Got.getString("command");
//                //String output = GetCommand(ChanID, cmd);
//                ToList.add(cmd);
//
//            }
//        } catch (SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//
//        }
//        return ToList;
//    }
//
//    public String getquotelist(Long ChanID) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException, Exception {
//        Connection con = CentralStore.ConMySQL();
//        String newline = System.lineSeparator();
//        String Comcall = null;
//        String Comtext = null;
//        Map<String, String> ToGist = new HashMap();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        String Stage1 = null;
//        String Stage2 = null;
//        try {
//            ResultSet Got = statement.executeQuery("SELECT * FROM quotes WHERE ChanID = '" + ChanID + "'");
//
//            while (Got.next()) {
//                if (Stage1 == null) {
//                    Stage1 = "ID: " + Got.getString("id") + " - " + Got.getString("quotes");
//
//                } else if (Stage1 != null) {
//                    Stage2 = Stage1 + newline + newline + "ID: " + Got.getString("id") + " - " + Got.getString("quotes");
//                    Stage1 = Stage2;
//                }
//
//            }
////            if (Stage1 == null) {
////                Stage1 = "No Commands to list";
////            }
//        } catch (SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//            if (Stage1 != null) {
//                HTTP HTTP = new HTTP();
//                return "Link to quotes list: " + HTTP.striked("QuoteList", "", Stage1);
//            } else {
//                return "No quotes to list";
//            }
//        }
//    }
//
//    public String getquote(Long ChanID, Integer ID) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        String send = null;
//        ResultSet Got = null;
//        try {
//            //System.out.println("SELECT quotes FROM quotes WHERE Channel='" + Channel + "' and id='" + ID + "'");
//            Got = statement.executeQuery("SELECT quotes FROM quotes WHERE ChanID='" + ChanID + "' and id='" + ID + "'");
//
//        } catch (SQLException e) {
//
//        } finally {
//            Got.next();
//            send = Got.getString("quotes");
//            statement.close();
//            con.close();
//            return send;
//        }
//    }
//
//    public String getrandomquote(Long ChanID) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        String send = null;
//        List<String> RanQuote = new ArrayList();
//        try {
//            ResultSet Got = statement.executeQuery("SELECT quotes FROM quotes WHERE ChanID='" + ChanID + "'");
//
//            while (Got.next()) {
//                RanQuote.add(Got.getString("quotes"));
//            }
//        } catch (SQLException e) {
//
//        } finally {
//            Random randomGenerator = new Random();
//            int index = randomGenerator.nextInt(RanQuote.size());
//            send = RanQuote.get(index);
//            statement.close();
//            con.close();
//            if (send == null) {
//                send = "No quotes found";
//            }
//            return send;
//        }
//    }
//
//    public void addCommand(Long ChanID, String comcall, String comtext, Integer permlevel) throws ClassNotFoundException, SQLException, IOException {
//        if (!comcall.contains("!")) {
//            return;
//        }
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        //String UnComtext = StringEscapeUtils.unescapeJava(comtext);
//        String Ecomtext = StringEscapeUtils.escapeSql(comtext).trim();
//        String Ecomcall = StringEscapeUtils.escapeSql(comcall).trim();
//
//        //comtext = comtext.replace("'", "''");
//        try {
//
//            statement.setQueryTimeout(15);
//            int updated = 0;
//            try {
//                updated = statement.executeUpdate("UPDATE `commands` SET `comtext`='" + Ecomtext.trim() + "' WHERE ChanID='" + ChanID + "' and comcall='" + Ecomcall.toLowerCase().trim() + "'");
//            } catch (SQLException e) {
//                System.out.println(e);
//            }
//            if (updated == 0) {
//                statement.executeUpdate("INSERT INTO `commands`(`ChanID`, `comcall`, `comtext`, `permlevel`) VALUES ('" + ChanID + "','" + Ecomcall.toLowerCase().trim() + "','" + Ecomtext.trim() + "','0')");
//            }
//
//        } catch (SQLException ex) {
//
//        } finally {
//            statement.close();
//            con.close();
//        }
//
////System.out.println("insert into settings (" + Channel + "," + toChange + ") valuesconv (" + Channel + "','" + setting + "')");
//    }
//
//    public void setSetting(Long ChanID, String toChange, String setting) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//
//        try {
//
//            //System.out.println("update settings set " + toChange.toLowerCase() + "='" + setting.toLowerCase() + "' where Channel='" + Channel + "'");
//            statement.setQueryTimeout(15);
//            int updated = statement.executeUpdate("update settings set " + toChange + "='" + setting + "' where ChanID='" + ChanID + "'");
//            if (updated == 0) {
//
//                statement.executeUpdate("insert into settings (ChanID," + toChange + ") values ('" + ChanID + "','" + setting + "')");
//
//            }
//        } catch (SQLException ex) {
//
////System.out.println("insert into settings (" + Channel + "," + toChange + ") values (" + Channel + "','" + setting + "')");
//        } finally {
//            statement.close();
//            con.close();
//        }
//    }
//
//    public void setSetting(Long ChanID, String toChange, int setting) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        try {
//
//            statement.setQueryTimeout(15);
//            int updated = statement.executeUpdate("update settings set " + toChange.toLowerCase() + "=" + setting + " where ChanID='" + ChanID + "'");
//            if (updated == 0) {
//
//                statement.executeUpdate("insert into settings (ChanID," + toChange.toLowerCase() + ") values ('" + ChanID + "','" + setting + "')");
//                statement.close();
//                con.close();
//            }
//        } catch (SQLException ex) {
//
////System.out.println("insert into settings (" + Channel + "," + toChange + ") values (" + Channel + "','" + setting + "')");
//        } finally {
//            statement.close();
//            con.close();
//        }
//    }
//
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
//
//    public Long GetPlayerPoints(Long ChanID, Long UserID) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//
//        try {
//
//            statement.setQueryTimeout(15);
//            ResultSet Points = statement.executeQuery("select * from points where UserID='" + UserID + "' and ChanID='" + ChanID + "'");
//            List<Long> Send = new ArrayList<Long>();
//
//            int HasPoints = 0;
//            while (Points.next()) {
//                HasPoints += 1;
//                Send.add(Points.getLong("points"));
//
//            }
//            if (HasPoints == 0) {
//                int startpoints = getSettingint(ChanID, "startpoints");
//                statement.executeUpdate("insert into points (points,UserID,ChanID) values (" + startpoints + ",'" + UserID + "','" + ChanID + "')");
//                Points = statement.executeQuery("select * from points where UserID='" + UserID + "' and ChanID='" + ChanID + "'");
//                Send.removeAll(Send);
//                while (Points.next()) {
//                    Send.add(Points.getLong("points"));
//                }
//
//            }
//            return Send.get(0);
//
//        } catch (SQLException ex) {
//
//            //  statement.execute("create table if not exists points (id INTEGER PRIMARY KEY, points INTEGER, person STRING, Channel STRING)");
//            int startpoints = getSettingint(ChanID, "startpoints");
//            statement.executeUpdate("insert into points (points,UserID,Channel) values (" + startpoints + ",'" + UserID + "','" + ChanID + "')");
//            ResultSet Points = statement.executeQuery("select * from points where ChanID=" + "'" + ChanID + "'");
//
//            return Points.getLong("points");
//        } finally {
//            statement.close();
//            con.close();
//        }
//
//    }
//
//    public int GetPlayerPoints(Long ChanID, Integer UserID) throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//
//        try {
//
//            statement.setQueryTimeout(15);
//            ResultSet Points = statement.executeQuery("select * from points where UserID='" + UserID + "' and ChanID='" + ChanID + "'");
//            List<Integer> Send = new ArrayList<Integer>();
//
//            int HasPoints = 0;
//            while (Points.next()) {
//                HasPoints += 1;
//                Send.add(Points.getInt("points"));
//
//            }
//            if (HasPoints == 0) {
//                int startpoints = getSettingint(ChanID, "startpoints");
//                statement.executeUpdate("insert into points (points,UserID,ChanID) values (" + startpoints + ",'" + UserID + "','" + ChanID + "')");
//                Points = statement.executeQuery("select * from points where UserID='" + UserID + "' and ChanID='" + ChanID + "'");
//                Send.removeAll(Send);
//                while (Points.next()) {
//                    Send.add(Points.getInt("points"));
//                }
//
//            }
//            return Send.get(0);
//
//        } catch (SQLException ex) {
//
//            //  statement.execute("create table if not exists points (id INTEGER PRIMARY KEY, points INTEGER, person STRING, Channel STRING)");
//            int startpoints = getSettingint(ChanID, "startpoints");
//            statement.executeUpdate("insert into points (points,UserID,Channel) values (" + startpoints + ",'" + UserID + "','" + ChanID + "')");
//            ResultSet Points = statement.executeQuery("select * from points where ChanID=" + "'" + ChanID + "'");
//
//            return Points.getInt("points");
//        } finally {
//            statement.close();
//            con.close();
//        }
//
//    }
//
//    public String TopPoints(Long ChanID, int count) throws ClassNotFoundException, SQLException, IOException {
//        NumberFormat Format = NumberFormat.getNumberInstance();
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        String toplist = "";
//
//        try {
//
//            ResultSet top = statement.executeQuery("select * from points where ChanID='" + ChanID + "' order by ChanID,points desc;");
//            int loop = 0;
//            JSONUtil json = new JSONUtil();
//            while (loop < count + 1) {
//                top.next();
//                long points = top.getLong("points");
//                Long getperson = top.getLong("UserID");
//                String person = CentralStore.GetUserName(getperson);
//                loop++;
//                if ("".equals(toplist)) {
//
//                    if (!CentralStore.Username.equalsIgnoreCase(person)) {
//                        toplist = person + " - " + Format.format(points);
//                    }
//                } else {
//                    if (!CentralStore.Username.equalsIgnoreCase(person)) {
//                        toplist = toplist + ", " + person + " - " + Format.format(points);
//                    }
//                }
//
//            }
//        } catch (SQLException e) {
//
//        } finally {
//
//            statement.close();
//            con.close();
//            return toplist;
//        }
//    }
//
//    public boolean CommandExists(Long ChanID, String Command, Integer PermLevel) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        Boolean send = false;
//
//        try {
//            ResultSet Got = statement.executeQuery("SELECT comtext, permlevel FROM commands WHERE ChanID='" + ChanID + "' AND comcall='" + Command.toLowerCase() + "'");
//
//            while (Got.next()) {
//                if (PermLevel >= Got.getInt("permlevel")) {
//                    send = true;
//                }
//
//            }
//        } catch (SQLException e) {
//
//        }
//        statement.close();
//        con.close();
//        return send;
//    }
//
//    public String GetCommand(Long ChanID, String Command) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        String send = null;
//        try {
//            ResultSet Got = statement.executeQuery("SELECT comtext FROM commands WHERE ChanID='" + ChanID + "' AND comcall='" + Command.toLowerCase() + "'");
//
//            while (Got.next()) {
//
//                send = Got.getString("comtext");
//
//            }
//        } catch (SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//            //http http = new http();
//            return send;
//        }
//    }
//
//    public List<Long> GetJoinChannels() throws ClassNotFoundException, SQLException, IOException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        ResultSet setting = null;
//        List<Long> send = new ArrayList();
//        try {
//            setting = statement.executeQuery("select ChanID from settings where UseBot=1 order by Donated DESC;");
//            while (setting.next()) {
//                send.add(setting.getLong("ChanID"));
//
//            }
//        } catch (SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//            return send;
//        }
//    }
//
//    public void GetCommandList(Long ChanID) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException, Exception {
//        Connection con = CentralStore.ConMySQL();
//        String newline = System.lineSeparator();
//        String Comcall = null;
//        String Comtext = null;
//        Map<String, String> ToGist = new HashMap();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        String Stage1 = "";
//        String Stage2 = "";
//        try {
//            ResultSet Got = statement.executeQuery("SELECT * FROM commands WHERE ChanID = '" + ChanID + "'");
//
//            while (Got.next()) {
//                String restrictlevel = null;
//                int level = Got.getInt("permlevel");
//                switch (level) {
//                    case 0:
//                        restrictlevel = "Everyone";
//                        break;
//                    case 1:
//                        restrictlevel = "Mods";
//                        break;
//                    case 2:
//                        restrictlevel = "Streamer";
//                        break;
//                    case 3:
//                        restrictlevel = "Admin";
//                        break;
//                }
//
//                Stage1 = Stage1 + newline + newline + Got.getString("comcall") + " - Level: " + restrictlevel + " - " + Got.getString("comtext") + " - Count: " + Got.getString("count");
//
//            }
//
//        } catch (SQLException e) {
//
//        } finally {
//
//            statement.close();
//            con.close();
//
//            con.close();
//            HTTP HTTP = new HTTP();
//            if (!"".equals(Stage1)) {
//                clients.get(ChanID).getBasicRemote().sendText(SendMSG("Commands List: " + HTTP.striked("CommandList", "", Stage1), ChanID));
//            } else {
//                clients.get(ChanID).getBasicRemote().sendText(SendMSG("No commands found.", ChanID));
//            }
//        }
//    }
//
//    public Boolean IsRegular(Long ChanID, Long UserID) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        ResultSet Got = statement.executeQuery("SELECT * FROM regulars WHERE ChanID = '" + ChanID + "' and UserID = '" + UserID + "'");
//        boolean send = false;
//        try {
//            while (Got.next()) {
//                send = true;
//            }
//        } catch (SQLException e) {
//
//        } finally {
//
//            statement.close();
//            con.close();
//            return send;
//        }
//
//        //System.out.println(Send);
//    }
//
//    public Boolean AddRegular(Long ChanID, Long UserID) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        boolean send = false;
//        try {
//            if (!IsRegular(ChanID, UserID)) {
//                int did = statement.executeUpdate("INSERT INTO `regulars`(`ChanID`, `UserID`) VALUES ('" + ChanID + "','" + UserID + "')");
//                if (did == 1) {
//                    send = true;
//                }
//            }
//        } catch (IOException | ClassNotFoundException | SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//        }
//
//        //System.out.println(Send);
//        return send;
//    }
//
//    public Boolean DelRegular(Long ChanID, Long UserID) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        statement.setQueryTimeout(15);
//        boolean send = false;
//        try {
//            int did = statement.executeUpdate("DELETE FROM `regulars` WHERE `ChanID` = '" + ChanID + "' and `UserID` = '" + UserID + "'");
//            if (did == 1) {
//                send = true;
//            }
//        } catch (SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//            return send;
//        }
//        //System.out.println(Send);
//
//    }
//
//    public void GiveAllPoints(Long ChanID, Long Points) throws ClassNotFoundException, IOException, SQLException, InterruptedException, ParseException {
//        JSONParser jsonParser = new JSONParser();
//        JSONUtil json = new JSONUtil();
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        Statement statement2 = con.createStatement();
//        statement.setQueryTimeout(15);
//        statement.setQueryTimeout(15);
//        HTTP bm = new HTTP();
//        Object users = jsonParser.parse(bm.getRemoteContent("https://beam.pro/api/v1/chats/" + ChanID + "/users"));
//        JSONArray ToParse = (JSONArray) users;
//        Iterator ParseMe = ToParse.iterator();
//        List<Long> UserIDList = new ArrayList();
//        String CUsername = getSetting(ChanID, "CUsername");
//        Long CUserID = Long.parseLong(json.GetUserID(CUsername));
//        try {
//            for (Object obj : ToParse) {
//                JSONObject t = (JSONObject) obj;
//                Long ID = Long.parseLong(t.get("user_id").toString());
//                if (!Objects.equals(ID, CUserID)) {
//                    UserIDList.add(ID);
//                }
//            }
//
//            ResultSet curPoints;
//            Map<Long, Long> ViewerPoints = new HashMap();
//            for (Long t : UserIDList) {
//                curPoints = statement.executeQuery("select * from points where ChanID='" + ChanID + "' and UserID='" + t + "'");
//                while (curPoints.next()) {
//                    Long UID = curPoints.getLong("UserID");
//                    Long PTS = curPoints.getLong("points");
//                    //System.out.println(UID + ":" + PTS);
//                    ViewerPoints.put(UID, PTS);
//                }
//            }
//            for (Long t : ViewerPoints.keySet()) {
//                Long CurPoints = ViewerPoints.get(t);
//                Long FinalPoints = CurPoints + Points;
//                statement2.addBatch("update points set points='" + FinalPoints + "' where UserID='" + t + "' and ChanID='" + ChanID + "';");
//            }
//
//        } catch (SQLException ex) {
//            ErrorReport(ChanID, ex);
//        } finally {
//            statement.close();
//            statement2.executeBatch();
//            statement2.close();
//            con.close();
//        }
//    }
//
//    public void PurgePoints(Long ChanID) throws ClassNotFoundException, SQLException, IOException {
//        //int StartPoints = getSettingint(ChanID, "Startpoints");
//        Connection con = CentralStore.ConMySQL();
//        Statement statement = con.createStatement();
//        try {
//
//            statement.setQueryTimeout(15);
//            statement.executeUpdate("DELETE FROM `points` WHERE chanid='" + ChanID + "'");
//            //statement.executeUpdate("delete from points where Channel='" + Channel + "'");
//
//        } catch (SQLException e) {
//
//        } finally {
//            statement.close();
//            con.close();
//
//        }
//    }
//

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

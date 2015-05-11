/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beam.scottypointsticker.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Points {

    static String curPoints;
    static int curPointsIndex;
    static sql sql = new sql();

    public static void StartPointsLoop() throws ClassNotFoundException, SQLException, IOException, Exception {
        Map<String, Long> ChanToTick = sql.GetChanList();
        //List<String> users = JSONUtil.GetUserList(ChanID);
        int TickTime = Math.round(600000 / ChanToTick.size());
        for (String Chan : ChanToTick.keySet()) {
            System.out.println("Ticking for " + Chan);
            Long ChanID = ChanToTick.get(Chan);
            //System.out.println("Ticking points for channel " + Chan + ":" + ChanID);
            PointLoopThread(Chan, ChanID);
            Thread.sleep(TickTime);
        }

    }

    public static void PointLoopThread(final String channel, final Long ChanID) {

        new Thread(channel + " Points Ticker") {

            @Override
            public void run() {
                HTTP bot = new HTTP();
                try {
                    Object obj = null;
                    //System.out.println("Ticking Points for channel " + channel);
                    boolean got = false;
                    JSONParser parser = new JSONParser();
                    while (!got) {

                        try {
                            obj = parser.parse(bot.getRemoteContent("https://beam.pro/api/v1/chats/" + ChanID + "/users"));
                            got = true;
                        } catch (org.json.simple.parser.ParseException ex) {
                            Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    JSONArray ToList = (JSONArray) obj;
                    Iterator List = ToList.iterator();
                    List<String> Chatters = new ArrayList();
                    String CUsername = sql.getSetting(ChanID, "CUsername");
                    while (List.hasNext()) {
                        JSONObject chan = (JSONObject) List.next();
                        String user = String.valueOf(chan.get("user_id"));
                        if (!Chatters.contains(user) && !user.equalsIgnoreCase(CUsername)) {
                            Chatters.add(user);
                        }
                    }

                    JSONUtil json = new JSONUtil();
                    boolean live = json.IsLive(ChanID);
                    for (String Players : Chatters) {
                        if (!live) {

                            try {
                                if (sql.getSettingLong(ChanID, "idlepoints") < 1) {
                                    return;
                                }
                                sql.AddPoints(sql.getSettingLong(ChanID, "idlepoints"), ChanID, Long.valueOf(Players));
                            } catch (ClassNotFoundException | SQLException | IOException ex) {
                                Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        } else {

                            try {
                                sql.AddPoints(sql.getSettingLong(ChanID, "notidlepoints"), ChanID, Long.valueOf(Players));
                            } catch (ClassNotFoundException | SQLException | IOException ex) {
                                Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    }
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }.start();

    }
//
//    public Points(String server, int port) {
//        super(server, port);
//
//    }
//
//    static class PointLooper
//            extends TimerTask {
//
//        @Override
//        public void run() {
//            try {
//                PointLoop();
//            } catch (SQLException ex) {
//                Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (ClassNotFoundException ex) {
//                Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (IOException ex) {
//                Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//    }
}

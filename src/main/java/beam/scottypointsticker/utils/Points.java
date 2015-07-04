/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beam.scottypointsticker.utils;

import static beam.scottypointsticker.Stores.CentralStore.PointsQueue;
import static beam.scottypointsticker.Stores.CentralStore.RankQueue;
import static beam.scottypointsticker.Stores.CentralStore.threadPool;
import java.io.IOException;
import static java.lang.Thread.sleep;
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
import org.json.simple.parser.ParseException;

public class Points {

    static String curPoints;
    static int curPointsIndex;
    static sql sql = new sql();

    public void StartPointsLoop() throws ClassNotFoundException, SQLException, IOException, Exception {

        new Thread("RankThread") {
            @Override
            public void run() {
                List<Long> ToRank = null;
                try {
                    ToRank = new sql().GetJoinChannels();
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex);
                }
                int RankTime = Math.round(600000 / ToRank.size());

                for (Long t : ToRank) {
                    RankQueue++;

                    Runnable RL = new RankLoop(t);
                    threadPool.execute(RL);
                    while (RankQueue > 9) {
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                RankQueue = 0;
            }
        }.start();

        Map<String, Long> ChanToTick = sql.GetChanList();
        //int TickTime = Math.round(600000 / ChanToTick.size());
        for (String Chan : ChanToTick.keySet()) {
            PointsQueue++;
            System.out.println("Ticking for " + Chan);
            Long ChanID = ChanToTick.get(Chan);
            //System.out.println("Ticking points for channel " + Chan + ":" + ChanID);
            Runnable PLT = new PointLoop(Chan, ChanID);
            threadPool.execute(PLT);
            while (PointsQueue > 9) {
                Thread.sleep(250);
            }
            Thread.sleep(50);

        }
        PointsQueue = 0;

    }

    public class RankLoop implements Runnable {

        Long ChanID = null;

        RankLoop(Long chanid) {
            this.ChanID = chanid;
        }

        @Override
        public void run() {

            boolean live = new JSONUtil().IsLive(ChanID);
            if (live) {
                try {
                    System.out.println("Ticking Ranking for " + ChanID);
                    new sql().TickTimeWatched(ChanID);
                } catch (ClassNotFoundException | SQLException | IOException ex) {
                    Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            RankQueue--;
        }

    }

    public class PointLoop implements Runnable {

        String Channel = "";
        Long ChanID = null;

        PointLoop(String channel, Long chanid) {
            this.ChanID = chanid;
            this.Channel = channel;
        }

        @Override
        public void run() {
            HTTP bot = new HTTP();
            try {
                boolean got = false;
                JSONParser parser = new JSONParser();
                JSONArray result = new JSONArray();
                int page = 0;
                while (true) {
                    try {
                        JSONArray toAdd = new JSONArray();
                        toAdd.addAll((JSONArray) parser.parse(new HTTP().getRemoteContent("https://beam.pro/api/v1/chats/" + ChanID + "/users?limit=50&page=" + page)));
                        if (toAdd.isEmpty()) {
                            break;
                        }
                        result.addAll(toAdd);
                        page++;
                    } catch (ParseException ex) {
                        try {
                            sleep(1500);
                        } catch (InterruptedException ex1) {
                            Logger.getLogger(Points.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                    }
                }

                Iterator List = result.iterator();
                List<String> Chatters = new ArrayList();
                String CUsername = sql.getSetting(ChanID, "CUsername");
                while (List.hasNext()) {
                    JSONObject chan = (JSONObject) List.next();
                    String user = String.valueOf(chan.get("userId"));
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
                                PointsQueue--;
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
            PointsQueue--;
        }

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

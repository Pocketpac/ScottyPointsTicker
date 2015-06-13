/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beam.scottypointsticker.utils;

import beam.scottypointsticker.Stores.CentralStore;
import static beam.scottypointsticker.Stores.CentralStore.GetLastTweet;
import static beam.scottypointsticker.Stores.CentralStore.MiscThreads;
import static beam.scottypointsticker.Stores.CentralStore.TweetQueue;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

/**
 *
 * @author tjhasty
 */
public class autoTweeter {

    public void TweetLoop() {
        while (true) {
            try {
                JSONObject ToTweet = new JSONObject();
                try {
                    ToTweet.putAll(new sql().GetTweeters());
                } catch (SQLException ex) {
                    Logger.getLogger(autoTweeter.class.getName()).log(Level.SEVERE, null, ex);
                }
                for (Object t : ToTweet.keySet()) {
                    Long ChanID = (Long) t;
                    boolean isLive = new JSONUtil().IsLive(ChanID);
                    if (GetLastTweet(ChanID) < System.currentTimeMillis()) {
                        if (isLive) {
                            TweetQueue++;
                            String MSG = (String) ToTweet.get(t);
                            if (MSG.contains("(_channame_)")) {
                                MSG = MSG.replace("(_channame_)", new JSONUtil().GetChanName(ChanID));
                            }
                            if (MSG.contains("(_status_)")) {
                                MSG = MSG.replace("(_status_)", new JSONUtil().GetStatus(ChanID));
                            }
                            Runnable tweet = new Tweet(ChanID, MSG);
                            MiscThreads.execute(tweet);
                            CentralStore.UpdateLastTweet(ChanID);
                        } else {
                            CentralStore.ClearLastTweet(ChanID);
                        }

                    }
                    if (TweetQueue > 10) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(autoTweeter.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    Thread.sleep(50);
                }
                Thread.sleep(5 * 60 * 60 * 1000);
            } catch (Exception e) {

            }
        }

    }

    public class Tweet implements Runnable {

        Long ChanID = null;
        String MSG = "";

        Tweet(Long chanid, String msg) {
            this.ChanID = chanid;
            this.MSG = msg;
        }

        @Override
        public void run() {
            try {
                Map<String, String> AuthInfo = new HashMap();
                AuthInfo.putAll(new sql().getTwitterAuth(ChanID));
                if (AuthInfo.isEmpty()) {
                    TweetQueue--;
                    return;
                }
                String CKey = AuthInfo.get("CKey");
                String CSecret = AuthInfo.get("CSecret");
                String AToken = AuthInfo.get("AToken");
                String ATokenSecret = AuthInfo.get("ATokenSecret");
                ConfigurationBuilder builder = new ConfigurationBuilder();
                builder.setOAuthConsumerKey(CKey);
                builder.setOAuthConsumerSecret(CSecret);
                Configuration configuration = builder.build();
                TwitterFactory factory = new TwitterFactory(configuration);
                Twitter twitter = factory.getInstance();
                AccessToken accessToken = new AccessToken(AToken, ATokenSecret);
                twitter.setOAuthAccessToken(accessToken);

                twitter.updateStatus(MSG);
                TweetQueue--;
                System.out.println("Tweet sent for " + ChanID);
            } catch (Exception e) {
                TweetQueue--;
            }
        }

    }

}

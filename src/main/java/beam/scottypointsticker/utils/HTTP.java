/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beam.scottypointsticker.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author tjhasty
 */
public class HTTP {

    JSONParser jsonParser = new JSONParser();
    List<String> EndPoints = new ArrayList();
    Long ChanID;
    String Username = "";
    String Password = "";
    sql sql = new sql();

    public String getRemoteContent(String urlString) {
        String dataIn = "";
        int TimesToTry = 0;
        while (TimesToTry < 6) {
            try {
                URL url = new URL(urlString);
                // System.out.println("DEBUG: Getting data from " + url.toString());
                URLConnection conn = url.openConnection();
                conn.setRequestProperty("User-Agent", "ScottyBot");
                try (BufferedReader in = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()))) {
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        dataIn += inputLine;
                    }
                }
                break;
            } catch (IOException | OutOfMemoryError ex) {
                TimesToTry++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex1) {
                    Logger.getLogger(HTTP.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }

        return dataIn;
    }
}

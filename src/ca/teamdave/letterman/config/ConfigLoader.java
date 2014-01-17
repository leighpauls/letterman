package ca.teamdave.letterman.config;

import com.sun.squawk.util.LineReader;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import javax.microedition.io.Connector;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Loads configuration files from disk
 */
public class ConfigLoader {
    public static void openFile() {
        InputStream is = null;
        try {
            is = Connector.openDataInputStream("file:///letterman_config.json");
            LineReader reader = new LineReader(new InputStreamReader(is));
            Vector lines = new Vector();
            reader.readLines(lines);
            String fileContents = "";
            for (int i = 0; i < lines.size(); ++i) {
                // System.out.println((String)lines.elementAt(i));
                fileContents += (String)lines.elementAt(i) + "\n";
            }
            System.out.println(fileContents);
            try {
                JSONObject jsonObject = new JSONObject(fileContents);
                System.out.println(jsonObject.toString(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            System.err.println("Failed to open file: /letterman_config.json");
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

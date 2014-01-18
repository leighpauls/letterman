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
    private static ConfigLoader sInstance = null;
    public static ConfigLoader getInstance() {
        if (sInstance == null) {
            sInstance = new ConfigLoader();
        }
        return sInstance;
    }

    private JSONObject mRootObject;

    public void loadConfigFromFile() {
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
            try {
                mRootObject = new JSONObject(fileContents);
                // System.out.println(mRootObject.toString(1));
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

    public JSONObject getConfigObject(String topLevelKey) throws JSONException {
        return mRootObject.getJSONObject(topLevelKey);
    }
}

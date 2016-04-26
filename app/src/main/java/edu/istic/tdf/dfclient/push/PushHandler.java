package edu.istic.tdf.dfclient.push;

import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

/**
 *  Custom GCM listener - handles push reception
 */

public class PushHandler {

    private static String TAG = "PUSH_HANDLER";

    /**
     * The bundle key for the Topic
     */
    public static final String TOPIC_BUNDLE_KEY = "topic";

    /**
     * List of push catchers
     */
    private HashMap<String, IPushCommand> catchers = new HashMap<>();

    /**
     * The default catcher
     */
    private DefaultCatcher defaultCatcher = new DefaultCatcher();



    /**
     * Called by application when a push is received
     * @param sender The push sender
     * @param data The push data
     */
    public void handlePush(String sender, Bundle data) {

        // Check that the push contains a topic
        if(data.containsKey(TOPIC_BUNDLE_KEY)
                && data.getString(TOPIC_BUNDLE_KEY) != null) {

            // Fetch the catcher for the topic
            IPushCommand interceptor = catchers.get(data.getString(TOPIC_BUNDLE_KEY));

            // If we have an catcher for this topic
            if(interceptor != null) {
                interceptor.execute(data);
            } else { // Else, we run the default catcher
                defaultCatcher.execute(data);
            }

        }
    }

    public HashMap<String, IPushCommand> getCatchers() {
        return catchers;
    }

    public void addCatcher(String key, IPushCommand command) {
        catchers.put(key, command);
    }

    public void removeCatcher(String key) {
        catchers.remove(key);
    }

    private class DefaultCatcher implements IPushCommand {

        @Override
        public void execute(Bundle bundle) {
            Log.e(TAG, "Cannot handle a received push : no catcher for its topic");
        }
    }
}

package edu.ucla.loni.LOVE;

import java.util.*;

//import org.apache.log4j.*;

/**
 * Provides central control of various environment related
 * settings or user preferences.
 */
public class Preference {
    private HashMap _preferences;
    ////////////////////////////////////////////////////////////
    //following are some basic preferences or helper functions

    /**
     * Get the home directory of this program.
     * Reason to use this method instead of
     * <code>System.getProperty("user.dir")</code> directly is
     * that we may use an environment setting for this parameter.
     * User may use: "LOVE_HOME = /usr/bin/LOVE" to specify
     * this.
     *
     * @return Home directory of LOVE
     */
    public static String getHomeDir() {
        return System.getProperty("user.dir");
    }
}

package edu.uwi.mona.mobileourvle.app.Classes.TransportLayer;

/**
 * The Interface APIEndpoints.
 */
public interface APIEndpoints {

    /**
     * The Interface Shared.
     */
    public static interface Shared {
        // host to point android to my local host
        /**
         * The Constant API_HOST.
         */
        final public static String API_HOST = "http://ourvle.mona.uwi.edu/"; // need to point it to ourvle 2
        // "http://192.168.0.103/moodle/";
        // Used when you need android
        // to talk to the localhost from the my wifi
        // "http://10.0.2.2:8000/"; // Used when you need android to talk to the
        // localhost from the emulator
        final public static String REST_API_ENDPOINT = "webservice/rest/server.php";
    }

    /**
     * The Interface Authentication.
     */
    public static interface Authentication {
        // authentication
        /**
         * The Constant LOGIN_ENDPOINT.
         */
        final public static String LOGIN_ENDPOINT = "login/token.php";
    }
}
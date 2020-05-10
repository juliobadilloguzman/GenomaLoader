package selenium.util;

import java.util.HashMap;

public class AsyncStorage {

    private static AsyncStorage instance;
    private HashMap<String,String> storage;

    public static synchronized AsyncStorage getInstance(){
        if (instance == null) instance = new AsyncStorage();
        return instance;
    }

    private AsyncStorage(){
        storage = new HashMap<>();
    }

    public HashMap<String, String> getStorage() {
        return storage;
    }
}

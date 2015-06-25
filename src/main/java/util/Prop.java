package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class Prop {

    private static Prop prop;
    private static HashMap<String, String> map;

    private Prop() {
    }

    public static Prop getInstance() {
        if (null == prop) {
            map = new HashMap<String, String>();
            prop = new Prop();
            prop.init();
        }
        return prop;
    }

    public String get(String name) {
        return map.get(name);
    }

    public void init() {
        InputStream pInStream = this.getClass().getResourceAsStream("/node.properties");
        Properties p = new Properties();
        try {
            p.load(pInStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Enumeration enu = p.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = p.getProperty(key);
            map.put(key, value);
        }
    }
}

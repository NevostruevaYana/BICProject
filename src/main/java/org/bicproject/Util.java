package org.bicproject;

import java.io.File;
import java.util.*;

public class Util {

    public static final String SEPARATOR = File.separator;
    public static final String FILE_NAME = "src"+ SEPARATOR + "main" +
            SEPARATOR + "resources" + SEPARATOR + "lng.txt";
    public static final String BIG_FILE_NAME = "src"+ SEPARATOR + "main" +
            SEPARATOR + "resources" + SEPARATOR + "lng-big.csv";
    public static final String OUT_DIR = "src"+ SEPARATOR + "output";
    public static final String OUT_FILE_NAME = SEPARATOR + "result.txt";
    public static final String regex = "^\"[0-9]*([0-9]*\";\"[0-9]*)*\"";

    public static boolean checkCorrectLine(String line) {
        return line.matches(regex);
    }
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

}

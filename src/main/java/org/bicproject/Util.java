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
    public static final String regex = "(\"[^\"]*\")|([^\"]*)";

    public static boolean checkWrongElements(String[] lines) {
        for (String line: lines) {
            if (!line.matches(regex)) {
                return true;
            }
        }
        return false;
    }

}

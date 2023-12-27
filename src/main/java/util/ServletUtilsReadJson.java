package util;

import java.io.BufferedReader;
import java.io.IOException;

public class ServletUtilsReadJson {
    public static String readJson(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
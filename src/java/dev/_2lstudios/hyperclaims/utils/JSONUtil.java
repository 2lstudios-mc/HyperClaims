package dev._2lstudios.hyperclaims.utils;

import java.io.Reader;
import java.io.FileReader;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import org.json.simple.JSONObject;
import java.io.IOException;
import java.io.File;

public class JSONUtil {
    private static String dataFolder;

    public static void initialize(final String dataFolder) {
        JSONUtil.dataFolder = dataFolder;
    }

    public static File createJSON(final String path) {
        if (path.endsWith(".json")) {
            final File file = new File(path.replace("%datafolder%", JSONUtil.dataFolder));
            if (!file.exists()) {
                try {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file;
        }
        return null;
    }

    public static void saveJSON(final String path, final JSONObject jsonObject) {
        if (path.endsWith(".json")) {
            File file = new File(path.replace("%datafolder%", JSONUtil.dataFolder));
            if (!file.exists()) {
                file = createJSON(file.toPath().toString());
            }

            try {
                final FileWriter fileWriter = new FileWriter(file);

                try {
                    fileWriter.write(jsonObject.toJSONString());
                    fileWriter.flush();
                    fileWriter.close();
                } finally {
                    if (fileWriter != null) {
                        fileWriter.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static JSONObject getJSON(final String path) {
        if (path.endsWith(".json")) {
            final File file = new File(path.replace("%datafolder%", JSONUtil.dataFolder));
            if (file.exists() && file.isFile()) {
                final JSONParser jsonParser = new JSONParser();
                FileReader reader = null;

                try {
                    reader = new FileReader(file);
                    final Object object = jsonParser.parse((Reader) reader);
                    return (JSONObject) object;
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return new JSONObject();
    }

    public static void deleteJSON(final String path) {
        if (path.endsWith(".json")) {
            final File file = new File(path.replace("%datafolder%", JSONUtil.dataFolder));
            if (file.exists() && file.isFile()) {
                file.delete();
            }
        }
    }
}

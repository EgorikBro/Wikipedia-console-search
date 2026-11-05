package wikisearch.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class AppConfig {

    private static final String FILE_NAME = "application.properties";

    private final Properties properties = new Properties();

    public AppConfig() {
        InputStream stream = AppConfig.class.getClassLoader().getResourceAsStream(FILE_NAME);
        if (stream == null) {
            throw new IllegalStateException("Не найден файл настроек " + FILE_NAME);
        }
        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            properties.load(reader);
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать файл настроек " + FILE_NAME, e);
        }
    }

    public String getApiUrl() {
        return getString("api.url");
    }

    public String getArticleUrl() {
        return getString("article.url");
    }

    public int getSearchLimit() {
        return getInt("search.limit");
    }

    public int getTimeoutSeconds() {
        return getInt("request.timeout.seconds");
    }

    public String getUserAgent() {
        return getString("user.agent");
    }

    private String getString(String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("В файле настроек нет параметра " + key);
        }
        return value.trim();
    }

    private int getInt(String key) {
        String value = getString(key);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Параметр " + key + " должен быть целым числом");
        }
    }
}
package wikisearch.api;

import wikisearch.config.AppConfig;
import wikisearch.exception.WikiSearchException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

public class WikipediaClient {

    private final AppConfig config;
    private final HttpClient httpClient;

    public WikipediaClient(AppConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Настройки не могут быть пустыми");
        }
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(config.getTimeoutSeconds()))
                .build();
    }

    // Возвращает ответ сервера в формате JSON
    public String search(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("Поисковый запрос не может быть пустым");
        }
        HttpRequest request = HttpRequest.newBuilder(buildSearchUri(query))
                .timeout(Duration.ofSeconds(config.getTimeoutSeconds()))
                .header("User-Agent", config.getUserAgent())
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        } catch (HttpTimeoutException e) {
            throw new WikiSearchException("Википедия не ответила вовремя, попробуйте ещё раз", e);
        } catch (IOException e) {
            throw new WikiSearchException("Не удалось подключиться к Википедии. Проверьте интернет", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new WikiSearchException("Запрос к Википедии был прерван", e);
        }

        if (response.statusCode() != 200) {
            throw new WikiSearchException("Википедия вернула ошибку, код ответа " + response.statusCode());
        }
        return response.body();
    }

    private URI buildSearchUri(String query) {
        String url = config.getApiUrl()
                + "?action=query&list=search&utf8=&format=json"
                + "&srlimit=" + config.getSearchLimit()
                + "&srsearch=" + URLEncoder.encode(query, StandardCharsets.UTF_8);
        return URI.create(url);
    }
}
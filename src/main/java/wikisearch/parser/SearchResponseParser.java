package wikisearch.parser;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;
import wikisearch.exception.WikiSearchException;
import wikisearch.model.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class SearchResponseParser {

    private final Gson gson = new Gson();

    public List<SearchResult> parse(String json) {
        if (json == null || json.isBlank()) {
            throw new WikiSearchException("Википедия вернула пустой ответ");
        }

        SearchResponse response;
        try {
            response = gson.fromJson(json, SearchResponse.class);
        } catch (JsonParseException e) {
            throw new WikiSearchException("Не удалось разобрать ответ Википедии", e);
        }

        if (response == null) {
            throw new WikiSearchException("Википедия вернула пустой ответ");
        }
        if (response.error != null) {
            throw new WikiSearchException("Ошибка API Википедии: " + response.error.info);
        }
        if (response.query == null || response.query.search == null) {
            throw new WikiSearchException("В ответе Википедии нет результатов поиска");
        }

        List<SearchResult> results = new ArrayList<>();
        for (SearchItem item : response.query.search) {
            // Пропускаем неполные записи, чтобы одна испорченная статья не ломала весь поиск
            if (item == null || item.pageId <= 0 || item.title == null || item.title.isBlank()) {
                continue;
            }
            results.add(new SearchResult(item.pageId, item.title, cleanSnippet(item.snippet)));
        }
        return results;
    }

    private String cleanSnippet(String snippet) {
        if (snippet == null) {
            return "";
        }
        return snippet
                .replaceAll("<[^>]+>", "")
                .replace("&quot;", "\"")
                .replace("&#039;", "'")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .trim();
    }

    // Классы ниже повторяют структуру JSON-ответа, Gson заполняет их сам

    private static class SearchResponse {
        private Query query;
        private ApiError error;
    }

    private static class Query {
        private List<SearchItem> search;
    }

    private static class SearchItem {
        @SerializedName("pageid")
        private long pageId;
        private String title;
        private String snippet;
    }

    private static class ApiError {
        private String info;
    }
}
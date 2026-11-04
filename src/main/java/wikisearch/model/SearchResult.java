package wikisearch.model;

public class SearchResult {

    private final long pageId;
    private final String title;
    private final String snippet;

    public SearchResult(long pageId, String title, String snippet) {
        if (pageId <= 0) {
            throw new IllegalArgumentException("Некорректный номер статьи: " + pageId);
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Заголовок статьи не может быть пустым");
        }
        this.pageId = pageId;
        this.title = title;
        this.snippet = snippet == null ? "" : snippet;
    }

    public long getPageId() {
        return pageId;
    }

    public String getTitle() {
        return title;
    }

    public String getSnippet() {
        return snippet;
    }
}
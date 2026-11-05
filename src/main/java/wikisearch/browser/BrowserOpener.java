package wikisearch.browser;

import wikisearch.config.AppConfig;
import wikisearch.exception.WikiSearchException;
import wikisearch.model.SearchResult;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

public class BrowserOpener {

    private final AppConfig config;

    public BrowserOpener(AppConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Настройки не могут быть пустыми");
        }
        this.config = config;
    }

    public String getArticleUrl(SearchResult result) {
        if (result == null) {
            throw new IllegalArgumentException("Статья не может быть пустой");
        }
        return config.getArticleUrl() + result.getPageId();
    }

    public void open(SearchResult result) {
        String url = getArticleUrl(result);
        if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            throw new WikiSearchException("На этом компьютере нельзя открыть браузер из программы");
        }
        try {
            Desktop.getDesktop().browse(URI.create(url));
        } catch (IOException e) {
            throw new WikiSearchException("Не удалось открыть браузер", e);
        }
    }
}

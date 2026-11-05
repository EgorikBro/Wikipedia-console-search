package wikisearch.app;

import wikisearch.api.WikipediaClient;
import wikisearch.browser.BrowserOpener;
import wikisearch.exception.WikiSearchException;
import wikisearch.input.ConsoleInput;
import wikisearch.model.SearchResult;
import wikisearch.output.ResultPrinter;
import wikisearch.parser.SearchResponseParser;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class WikiSearchApp {

    private final ConsoleInput input;
    private final WikipediaClient client;
    private final SearchResponseParser parser;
    private final ResultPrinter printer;
    private final BrowserOpener browserOpener;

    public WikiSearchApp(ConsoleInput input, WikipediaClient client, SearchResponseParser parser,
                         ResultPrinter printer, BrowserOpener browserOpener) {
        this.input = input;
        this.client = client;
        this.parser = parser;
        this.printer = printer;
        this.browserOpener = browserOpener;
    }

    public void run() {
        printer.printWelcome();
        while (true) {
            Optional<String> query = input.readQuery();
            if (query.isEmpty()) {
                printer.printGoodbye();
                return;
            }
            try {
                search(query.get());
            } catch (WikiSearchException e) {
                printer.printError(e.getMessage());
            }
        }
    }

    private void search(String query) {
        String json = client.search(query);
        List<SearchResult> results = parser.parse(json);
        if (results.isEmpty()) {
            printer.printNoResults(query);
            return;
        }
        printer.printResults(results);

        OptionalInt choice = input.readChoice(results.size());
        if (choice.isEmpty()) {
            return;
        }
        openArticle(results.get(choice.getAsInt() - 1));
    }

    private void openArticle(SearchResult article) {
        printer.printOpening(article);
        try {
            browserOpener.open(article);
        } catch (WikiSearchException e) {
            // Если браузер не открылся, даём ссылку, чтобы пользователь открыл её сам
            printer.printError(e.getMessage());
            printer.printArticleLink(browserOpener.getArticleUrl(article));
        }
    }
}
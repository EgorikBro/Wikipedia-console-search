package wikisearch;

import wikisearch.api.WikipediaClient;
import wikisearch.app.WikiSearchApp;
import wikisearch.browser.BrowserOpener;
import wikisearch.config.AppConfig;
import wikisearch.input.ConsoleInput;
import wikisearch.output.ResultPrinter;
import wikisearch.parser.SearchResponseParser;

public class Main {

    public static void main(String[] args) {
        WikiSearchApp app;
        try {
            AppConfig config = new AppConfig();
            app = new WikiSearchApp(
                    new ConsoleInput(),
                    new WikipediaClient(config),
                    new SearchResponseParser(),
                    new ResultPrinter(),
                    new BrowserOpener(config)
            );
        } catch (IllegalStateException e) {
            System.out.println("Ошибка настроек: " + e.getMessage());
            return;
        }
        app.run();
    }
}
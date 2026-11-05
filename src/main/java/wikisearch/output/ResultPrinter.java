package wikisearch.output;

import wikisearch.model.SearchResult;

import java.util.List;

public class ResultPrinter {

    public void printWelcome() {
        System.out.println("===== Поиск по Википедии =====");
    }

    public void printGoodbye() {
        System.out.println("До свидания!");
    }

    public void printResults(List<SearchResult> results) {
        System.out.println();
        System.out.println("Найдено статей: " + results.size());
        for (int i = 0; i < results.size(); i++) {
            SearchResult result = results.get(i);
            System.out.println((i + 1) + ". " + result.getTitle());
            if (!result.getSnippet().isEmpty()) {
                System.out.println("   " + result.getSnippet());
            }
        }
        System.out.println();
    }

    public void printNoResults(String query) {
        System.out.println("По запросу «" + query + "» ничего не найдено, попробуйте другой запрос");
    }

    public void printOpening(SearchResult result) {
        System.out.println("Открываю статью «" + result.getTitle() + "» в браузере...");
    }

    public void printArticleLink(String url) {
        System.out.println("Ссылка на статью: " + url);
    }

    public void printError(String message) {
        System.out.println("Ошибка: " + message);
    }
}

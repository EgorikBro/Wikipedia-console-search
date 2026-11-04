package wikisearch.input;

import java.nio.charset.Charset;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Scanner;

public class ConsoleInput {

    private static final String EXIT_COMMAND = "выход";

    private final Scanner scanner = new Scanner(System.in, consoleCharset());

    // Возвращает пустой Optional, если пользователь решил выйти
    public Optional<String> readQuery() {
        while (true) {
            System.out.print("Введите запрос (или «" + EXIT_COMMAND + "» для завершения): ");
            if (!scanner.hasNextLine()) {
                return Optional.empty();
            }
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase(EXIT_COMMAND)) {
                return Optional.empty();
            }
            if (line.isEmpty()) {
                System.out.println("Запрос не может быть пустым, попробуйте ещё раз");
                continue;
            }
            return Optional.of(line);
        }
    }

    // Возвращает номер статьи от 1 до max или пустой OptionalInt, если пользователь вернулся к поиску
    public OptionalInt readChoice(int max) {
        if (max < 1) {
            throw new IllegalArgumentException("Нет статей для выбора");
        }
        while (true) {
            System.out.print("Номер статьи (1-" + max + ") или 0, чтобы вернуться к поиску: ");
            if (!scanner.hasNextLine()) {
                return OptionalInt.empty();
            }
            String line = scanner.nextLine().trim();
            int number;
            try {
                number = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Введите целое число");
                continue;
            }
            if (number == 0) {
                return OptionalInt.empty();
            }
            if (number < 1 || number > max) {
                System.out.println("Статьи с номером " + number + " нет, выберите от 1 до " + max);
                continue;
            }
            return OptionalInt.of(number);
        }
    }

    // В консоли Windows кодировка не UTF-8, без этого русские буквы прочитаются неправильно
    private static Charset consoleCharset() {
        String name = System.getProperty("stdin.encoding", System.getProperty("stdout.encoding"));
        if (name == null) {
            return Charset.defaultCharset();
        }
        return Charset.forName(name);
    }
}

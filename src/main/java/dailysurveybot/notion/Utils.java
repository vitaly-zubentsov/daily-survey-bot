package dailysurveybot.notion;

import dailysurveybot.notion.model.api.ColumnInfo;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    //Регулярка для поиска цифр стоящих вначале строки
    private static final String REGEX_FOR_SEARCH_FIRST_DIGITS = "^(\\d+)";
    //Паттерн для поиска цифр стоящих вначале строки
    private static final Pattern PATTERN = Pattern.compile(REGEX_FOR_SEARCH_FIRST_DIGITS);

    private Utils() {
    }

    /**
     * Получение первых цифры из поля name объекта Column info
     *
     * @param columnInfo - свойства колонки таблицы
     * @return число или 0, если цифры осутствуют
     */
    public static int extractNameOrderFromColumnInfo(@Nonnull ColumnInfo columnInfo) {
        Objects.requireNonNull(columnInfo);
        Matcher matcher = PATTERN.matcher(columnInfo.getNameWithOrderPrefix());
        String number = "";
        if (matcher.find()) {
            number = matcher.group(0);
        }
        return number.isEmpty() ? Integer.MAX_VALUE : Integer.parseInt(number);
    }

    /**
     * Удаление префиксных цифр из названия колоноок
     *
     * @param columnInfo - свойства колонки таблицы
     * @return columnInfo с name не содержащим префиксных цифр
     */
    @Nonnull
    public static ColumnInfo addNameWithoutOrderDigits(@Nonnull ColumnInfo columnInfo) {
        Objects.requireNonNull(columnInfo);
        columnInfo.setName(columnInfo.getNameWithOrderPrefix().replaceAll(REGEX_FOR_SEARCH_FIRST_DIGITS, ""));
        return columnInfo;
    }
}

package dailysurveybot.notion;

import dailysurveybot.notion.model.Property;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.List;

/**
 * Интерфейс для взаимодействия с notion
 */
public interface NotionService {

    /**
     * Отпарвка заполненой строки в notion
     *
     * @param columnsForFill - имена колонок заполняемой таблицы
     * @param valuesForFill  - значение введенные пользователем для имен колонок таблицы
     */
    void saveRow(@Nonnull List<String> columnsForFill, @Nonnull List<String> valuesForFill);

    /**
     * Получение списка колонок из таблицы в notion
     *
     * @return cписок колонок таблицы
     */
    List<Property> getProperties() throws IOException, InterruptedException;

}

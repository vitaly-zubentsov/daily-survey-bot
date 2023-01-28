package dailysurveybot.notion;

import dailysurveybot.notion.model.Column;

import java.io.IOException;
import java.util.List;

/**
 * Интерфейс для взаимодействия с notion
 */
public interface NotionService {

    void saveRow(String text) throws IOException, InterruptedException;

    /**
     * Получение списка колонок из таблицы в notion
     *
     * @return cписок колонок таблицы
     */
    List<Column> getColumns() throws IOException, InterruptedException;
}

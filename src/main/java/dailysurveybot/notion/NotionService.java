package dailysurveybot.notion;

import dailysurveybot.notion.model.api.ColumnInfo;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Интерфейс для взаимодействия с notion
 */
public interface NotionService {

    /**
     * Отпарвка заполненой строки в notion
     *
     * @param properties - заполненные колонки таблицы
     */
    void saveRow(@Nonnull List<ColumnInfo> properties);

    /**
     * Получение списка данных о колонках в таблице notion
     *
     * @return cписок колонок таблицы
     */
    List<ColumnInfo> getColumnsInfo();

}

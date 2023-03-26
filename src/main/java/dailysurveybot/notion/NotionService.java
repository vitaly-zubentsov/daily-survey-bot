package dailysurveybot.notion;

import dailysurveybot.notion.model.api.ColumnInfo;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Интерфейс взаимодействия с notion
 */
public interface NotionService {

    /**
     * Отпарвка заполненой строки в notion
     *
     * @param properties - заполненные колонки таблицы
     * @param databaseId уникальный идентификатор таблицы в сервисе notion
     * @param apiToken   api токен, необходим для доступа к таблиицам
     */
    void saveRow(@Nonnull List<ColumnInfo> properties, @Nonnull String databaseId, @Nonnull String apiToken);

    /**
     * Получение списка данных о колонках в таблице notion
     *
     * @param databaseId уникальный идентификатор таблицы в сервисе notion
     * @param apiToken   api токен, необходим для доступа к таблиицам
     * @return cписок колонок таблицы
     */
    List<ColumnInfo> getColumnsInfo(@Nonnull String databaseId, @Nonnull String apiToken);

}

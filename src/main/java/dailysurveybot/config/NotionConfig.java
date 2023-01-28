package dailysurveybot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки для работты с notion таблицами
 *
 * @param apiUrl     url для осуществления api запросов в notion
 * @param apiToken   api токен, необходим для доступа к таблиицам
 * @param databaseId уникальный идентификатор таблицы в сервиси notion
 * @param apiVersion версия api
 */
@ConfigurationProperties("notion")
public record NotionConfig(String apiUrl,
                           String apiToken,
                           String databaseId,
                           String apiVersion) {
}
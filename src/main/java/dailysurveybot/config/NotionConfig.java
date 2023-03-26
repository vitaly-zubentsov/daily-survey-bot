package dailysurveybot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки для работты с notion таблицами
 *
 * @param apiUrl     url для осуществления api запросов в notion
 * @param apiVersion версия api
 */
@ConfigurationProperties("notion")
public record NotionConfig(String apiUrl,
                           String apiVersion) {
}
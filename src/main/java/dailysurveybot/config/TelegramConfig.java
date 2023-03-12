package dailysurveybot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки для работы с api telegram
 *
 * @param botName  имя бота
 * @param botToken токен для доступа к функциям бота
 */
@ConfigurationProperties("telegram")
public record TelegramConfig(String botName,
                             String botToken,
                             Long userId,
                             Long chatId) {

}

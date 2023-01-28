package dailysurveybot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Настройки для работы с api telegram
 *
 * @param webhookPath url вебхука, c которым взаимодействует api telegram
 * @param botName     имя бота
 * @param botToken    токен для доступа к функциям бота
 * @param apiUrl      url api telegram
 */
@ConfigurationProperties("telegram")
public record TelegramConfig(String webhookPath,
                             String botName,
                             String botToken,
                             String apiUrl) {

}
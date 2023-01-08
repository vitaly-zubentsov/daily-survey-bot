package dailysurveybot.config;

import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.handlers.CallbackQueryHandler;
import dailysurveybot.telegram.handlers.MessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class SpringConfig {
    private final TelegramConfig telegramConfig;

    public SpringConfig(TelegramConfig telegramConfig) {
        this.telegramConfig = telegramConfig;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramConfig.getWebhookPath()).build();
    }

    @Bean
    public DailySurveyBot springWebhookBot(SetWebhook setWebhook,
                                           MessageHandler messageHandler,
                                           CallbackQueryHandler callbackQueryHandler) {

        return new DailySurveyBot(setWebhook,
                messageHandler,
                callbackQueryHandler,
                telegramConfig.getWebhookPath(),
                telegramConfig.getBotName(),
                telegramConfig.getBotToken());
    }
}

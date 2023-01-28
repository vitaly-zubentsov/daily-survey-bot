package dailysurveybot.config;

import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.services.CallbackQueryServiceImpl;
import dailysurveybot.telegram.services.MessageServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;

@Configuration
public class SpringConfig {
    private final TelegramConfig telegramConfig;

    public SpringConfig(TelegramConfig telegramConfig) {
        this.telegramConfig = telegramConfig;
    }

    @Bean
    public SetWebhook setWebhookInstance() {
        return SetWebhook.builder().url(telegramConfig.webhookPath()).build();
    }

    @Bean
    public DailySurveyBot springWebhookBot(SetWebhook setWebhook,
                                           MessageServiceImpl messageService,
                                           CallbackQueryServiceImpl callbackQueryHandler) {

        return new DailySurveyBot(setWebhook,
                messageService,
                callbackQueryHandler,
                telegramConfig.webhookPath(),
                telegramConfig.botName(),
                telegramConfig.botToken());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

package dailysurveybot;

import dailysurveybot.config.NotionConfig;
import dailysurveybot.config.TelegramConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({NotionConfig.class, TelegramConfig.class})
public class DailySurveyBotApp {

    public static void main(String[] args) {
        SpringApplication.run(DailySurveyBotApp.class, args);
    }
}
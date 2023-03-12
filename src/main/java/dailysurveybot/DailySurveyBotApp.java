package dailysurveybot;

import dailysurveybot.config.NotionConfig;
import dailysurveybot.config.TelegramConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({NotionConfig.class, TelegramConfig.class})
@EnableScheduling
public class DailySurveyBotApp {

    public static void main(String[] args) {
        SpringApplication.run(DailySurveyBotApp.class, args);
    }
}
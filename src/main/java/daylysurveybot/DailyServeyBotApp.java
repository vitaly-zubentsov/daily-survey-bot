package daylysurveybot;

import daylysurveybot.telegram.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class DailyServeyBotApp {

    public static void main(String[] args) {
        try {
            //получаем имя бота и токен из файла настроек bot.properties
            String rootPath = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath();
            String appConfigPath = rootPath + "bot.properties";
            Properties appProps = new Properties();
            loadFileProperties(appProps, appConfigPath);

            //запускаем бота
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(new Bot(appProps.getProperty("bot.name"), appProps.getProperty("bot.token")));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private static void loadFileProperties(Properties appProps, String appConfigPath) {
        try {
            appProps.load(new FileInputStream(appConfigPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
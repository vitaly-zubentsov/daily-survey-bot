package daylysurveybot.telegram;

import daylysurveybot.telegram.command.operation.GreetingsCommand;
import daylysurveybot.telegram.command.services.HelpCommand;
import daylysurveybot.telegram.command.services.SettingsCommand;
import daylysurveybot.telegram.command.services.StartCommand;
import daylysurveybot.telegram.noncommand.NonCommand;
import daylysurveybot.telegram.noncommand.Settings;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

public class Bot extends TelegramLongPollingCommandBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;

    private static final Settings defaultSettings = new Settings("Hello world!");

    // Настройки файла для разных пользователей. Ключ - уникальный id чата
    private static Map<Long, Settings> userSettings;

    private final NonCommand nonCommand;

    public Bot(String botName, String botToken) {
        super();
        BOT_NAME = botName;
        BOT_TOKEN = botToken;
        this.nonCommand = new NonCommand();
        userSettings = new HashMap<>();
        //регистрируем команды
        register(new StartCommand("start", "Старт"));
        register(new SettingsCommand("settings", "Отображениие настроек"));
        register(new GreetingsCommand("hi", "Приветствие"));
        register(new HelpCommand("help", "Помощь"));
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    public static Map<Long, Settings> getUserSettings() {
        return userSettings;
    }

    public static Settings getDefaultSettings() {
        return defaultSettings;
    }

    /**
     * Ответ на запрос, не являющийся командой
     */
    @Override
    public void processNonCommandUpdate(Update update) {
        final Message msg = update.getMessage();
        final Long chatId = msg.getChatId();
        final String userName = getUserName(msg);

        final String answer = nonCommand.execute(chatId, userName, msg.getText());
        setAnswer(chatId, userName, answer);
    }

    /**
     * Получение настроек по id чата. Если ранее для этого чата в ходе сеанса работы бота настройки не были установлены, используются настройки по умолчанию
     */
    public static Settings getUserSettings(Long chatId) {
        Map<Long, Settings> userSettings = Bot.getUserSettings();
        Settings settings = userSettings.get(chatId);
        if (settings == null) {
            return defaultSettings;
        }
        return settings;
    }

    /**
     * Формирование имени пользователя
     *
     * @param msg сообщение
     */
    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    /**
     * Отправка ответа
     *
     * @param chatId   id чата
     * @param userName имя пользователя
     * @param text     текст ответа
     */
    private void setAnswer(Long chatId, String userName, String text) {
        SendMessage answer = new SendMessage();
        answer.setText(text);
        answer.setChatId(chatId.toString());
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            //логируем сбой Telegram telegram.Bot API, используя userName
        }
    }


}

package dailysurveybot.telegram;

import dailysurveybot.config.TelegramConfig;
import dailysurveybot.telegram.commands.operations.AddRowToTableCommand;
import dailysurveybot.telegram.commands.services.HelpCommand;
import dailysurveybot.telegram.commands.services.SettingsCommand;
import dailysurveybot.telegram.commands.services.StartCommand;
import dailysurveybot.telegram.entity.Settings;
import dailysurveybot.telegram.noncommands.NonCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс телеграмм бота
 */
@Component
public class DailySurveyBot extends TelegramLongPollingCommandBot {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final Settings defaultSettings = new Settings("Hello world!");

    // Настройки файла для разных пользователей. Ключ - уникальный id чата
    private static Map<Long, Settings> userSettings;

    private final String botName;
    private final String botToken;
    private final NonCommand nonCommand;
    private final TelegramConfig telegramConfig;
    private final StartCommand startCommand;
    private final SettingsCommand settingsCommand;
    private final AddRowToTableCommand addRowToTableCommand;
    private final HelpCommand helpCommand;

    public DailySurveyBot(TelegramConfig telegramConfig,
                          StartCommand startCommand,
                          SettingsCommand settingsCommand,
                          HelpCommand helpCommand,
                          AddRowToTableCommand addRowToTableCommand) {
        super();
        this.telegramConfig = telegramConfig;
        this.botName = telegramConfig.botName();
        this.botToken = telegramConfig.botToken();
        this.startCommand = startCommand;
        this.settingsCommand = settingsCommand;
        this.helpCommand = helpCommand;
        this.addRowToTableCommand = addRowToTableCommand;
        this.nonCommand = new NonCommand();
        userSettings = new HashMap<>();

        //регистрируем команды
        register(this.startCommand);
        logger.debug("Команда start была зарегистрирована");
        register(this.settingsCommand);
        logger.debug("Команда settings была зарегистрирована");
        register(this.helpCommand);
        logger.debug("Команда help была зарегистрирована");
        register(this.addRowToTableCommand);
        logger.debug("Команда AddRowToTableCommand(/t) была зарегистрирована");

        logger.debug("Бот был создан");
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
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
        Map<Long, Settings> userSettings = DailySurveyBot.getUserSettings();
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


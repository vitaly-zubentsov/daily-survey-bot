package dailysurveybot.telegram.services;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

/**
 * Интерфейс взаимодействия с настройками пользователя
 */
public interface SettingsService {

    /**
     * Установка параметров для пользователя
     *
     * @param chatId         id чата
     * @param helloWorldText что будет отвечать бот на приветствие
     * @return результат установки параметров
     */
    SendMessage setSettings(Long chatId, String helloWorldText);

}

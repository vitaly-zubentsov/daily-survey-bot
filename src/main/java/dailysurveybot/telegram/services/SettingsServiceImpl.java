package dailysurveybot.telegram.services;

import dailysurveybot.telegram.constants.SettingsEnum;
import dailysurveybot.telegram.entity.Settings;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.HashMap;
import java.util.Map;

@Service
public class SettingsServiceImpl implements SettingsService {

    private static final Settings DEFAULT_SETTINGS = new Settings(SettingsEnum.HI_PROGRAMMERS.getText());
    // Настройки файла для разных пользователей. Ключ - уникальный id чата
    private static final Map<Long, Settings> userSettings = new HashMap<>(); //TODO убрать потенциальная утечка памяти

    @Override
    public SendMessage setSettings(Long chatId, String helloWorldText) {
        String answer;
        try {
            final Settings settings = getUserSettings(chatId);
            if (!helloWorldText.equals(settings.getHelloWorldAnswer())) {
                settings.setHelloWorldAnswer(helloWorldText);
                saveUserSettings(chatId, settings);
                answer = "Настройки обновлены.";
            } else {
                answer = "Нечего обновлять, применяемые настройки те же, что были выбранны ранее";
            }

            //TODO логируем событие, используя chatId
        } catch (Exception e) {
            answer = "Простите, я не понимаю Вас. Возможно, Вам поможет /help";
            //TODO логируем событие, используя chatId
        }

        return new SendMessage(chatId.toString(), answer);
    }

    /**
     * Получение настроек по id чата. Если ранее для этого чата в ходе сеанса работы бота
     * настройки не были установлены, используются настройки по умолчанию
     */
    public static Settings getUserSettings(Long chatId) {
        final Settings settings = userSettings.get(chatId);
        if (settings == null) {
            return DEFAULT_SETTINGS;
        }
        return settings;
    }

    /**
     * Добавление настроек пользователя в мапу, чтобы потом их использовать для этого пользователя при генерации файла
     * Если настройки совпадают с дефолтными, они не сохраняются и удаляются, чтобы впустую не раздувать мапу
     *
     * @param chatId   id чата
     * @param settings настройки
     */
    private void saveUserSettings(Long chatId, Settings settings) {
        if (!DEFAULT_SETTINGS.equals(settings)) {
            userSettings.put(chatId, settings);
        } else {
            userSettings.remove(chatId);
        }
    }


}

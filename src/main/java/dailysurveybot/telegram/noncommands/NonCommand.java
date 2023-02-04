package dailysurveybot.telegram.noncommands;


import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.entity.Settings;
import org.apache.commons.lang3.StringUtils;

public class NonCommand {

    /**
     * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
     */
    public String execute(Long chatId, String userName, String text) {
        String answer;
        try {
            //создаём настройки из сообщения пользователя
            final Settings settings = createSettings(text);
            //добавляем настройки в мапу, чтобы потом их использовать для этого пользователя при генерации файла
            saveUserSettings(chatId, settings);
            answer = "Настройки обновлены. Вы всегда можете их посмотреть с помощью /settings";
            //TODO логируем событие, используя userName
        } catch (RuntimeException e) {
            //TODO сделать exception именно для ошибки установки обработчик
            answer = e.getMessage() +
                    "\n\n Настройки не были изменены. Вы всегда можете их посмотреть с помощью /settings";
            //TODO логируем событие, используя userName
        } catch (Exception e) {
            answer = "Простите, я не понимаю Вас. Возможно, Вам поможет /help";
            //TODO логируем событие, используя userName
        }
        return answer;
    }

    /**
     * Создание настроек из полученного пользователем сообщения
     *
     * @param text текст сообщения
     * @throws IllegalArgumentException пробрасывается, если сообщение пользователя не соответствует формату
     */
    private Settings createSettings(String text) throws IllegalArgumentException {
        //отсекаем файлы, стикеры, гифки и прочий мусор
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("Сообщение не является текстом");
        }
        //создаём из сообщения пользователя 3 числа-настройки (min, max, listCount) либо пробрасываем исключение о несоответствии сообщения требуемому формату
        return new Settings(text);
    }

    /**
     * Добавление настроек пользователя в мапу, чтобы потом их использовать для этого пользователя при генерации файла
     * Если настройки совпадают с дефолтными, они не сохраняются, чтобы впустую не раздувать мапу
     *
     * @param chatId   id чата
     * @param settings настройки
     */
    private void saveUserSettings(Long chatId, Settings settings) {
        if (!DailySurveyBot.getDefaultSettings().equals(settings)) {
            DailySurveyBot.getUserSettings().put(chatId, settings);
        } else {
            DailySurveyBot.getUserSettings().remove(chatId);
        }
    }
}

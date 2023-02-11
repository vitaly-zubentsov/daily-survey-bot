package dailysurveybot.telegram.noncommands;

import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.entity.UserData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 * Используется для заполнения таблицы данными
 */
@Component
public class NonCommand {

    public String execute(Long chatId, String userName, String text) {
        String answerToUser;
        try {
            UserData userData = DailySurveyBot.getUserData(chatId);
            if (userData.getColumnsForFill().isEmpty()) {
                answerToUser = "Для обновления таблицы введите /t";
            } else {
                addTextToValueForFill(text, userData);
                userData.setFilledColumnsCounter(userData.getFilledColumnsCounter() + 1);
                //TODO поэкспериментировать примитив в итоге локально обновленный будет ли изменияться в
                // самом классе если нет то надо просто взятть обертку
                int filledColumnsCounter = userData.getFilledColumnsCounter();
                if (filledColumnsCounter > userData.getColumnsForFill().size()) {
                    //todo send data to notion
                    answerToUser = "таблица заполнена";
                    userData.getColumnsForFill().clear();
                    userData.setFilledColumnsCounter(1);
                    userData.getValuesForFill().clear();
                } else {
                    answerToUser = userData.getColumnsForFill().get(filledColumnsCounter - 1);
                }
                //TODO логируем событие, используя userName
            }
        } catch (RuntimeException e) {
            //TODO сделать exception именно для ошибки установки обработчик
            answerToUser = e.getMessage() +
                    "\n\n Настройки не были изменены. Вы всегда можете их посмотреть с помощью /settings";
            //TODO логируем событие, используя userName
        } catch (Exception e) {
            answerToUser = "Простите, я не понимаю Вас. Возможно, Вам поможет /help";
            //TODO логируем событие, используя userName
        }
        return answerToUser;
    }


    private void addTextToValueForFill(String text, UserData userData) {
        //отсекаем файлы, стикеры, гифки и прочий мусор
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("Сообщение не является текстом");
        }
        userData.getValuesForFill().add(text);
    }

}

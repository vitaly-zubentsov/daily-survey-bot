package dailysurveybot.telegram.noncommands;

import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.entity.UserData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static dailysurveybot.notion.model.enums.PropertyType.SELECT;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 * Используется для заполнения таблицы данными
 */
@Component
public class NonCommand {

    private final NotionService notionService;

    public NonCommand(NotionService notionService) {
        this.notionService = notionService;
    }


    public String execute(Long chatId, String userName, String text) {
        String answerToUser;
        try {
            UserData userData = DailySurveyBot.getUserData(chatId);
            if (userData.getColumnInfoList().isEmpty()) {
                answerToUser = "Для обновления таблицы введите /t";
            } else {
                addTextToColumn(text, userData);

                userData.setFilledColumnsCounter(userData.getFilledColumnsCounter() + 1);
                int filledColumnsCounter = userData.getFilledColumnsCounter();

                if (filledColumnsCounter == userData.getColumnInfoList().size()) {
                    notionService.saveRow(userData.getColumnInfoList());
                    answerToUser = "таблица заполнена";
                    userData.getColumnInfoList().clear();
                    userData.setFilledColumnsCounter(0);
                } else {
                    ColumnInfo columnInfo = userData.getColumnInfoList().get(filledColumnsCounter);
                    answerToUser = columnInfo.getName();
                    if (SELECT.getValue().equals(columnInfo.getType())) {
                        //TODO сделать клавиатуру клавиатуру на селекты
                        answerToUser += columnInfo.getSelectOptions();
                    }
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

    private void addTextToColumn(String text, UserData userData) {
        //отсекаем файлы, стикеры, гифки и прочий мусор
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException("Сообщение не является текстом");
        }
        ColumnInfo columnInfo = userData.getColumnInfoList().get(userData.getFilledColumnsCounter());
        columnInfo.setTextFromUser(text);
    }
}

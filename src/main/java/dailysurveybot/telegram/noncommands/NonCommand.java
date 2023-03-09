package dailysurveybot.telegram.noncommands;

import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.entity.UserData;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static dailysurveybot.notion.model.enums.PropertyType.SELECT;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 * Используется для заполнения таблицы данными
 */
@Component
public class NonCommand {

    protected final Logger logger = LoggerFactory.getLogger(NonCommand.class);

    private final NotionService notionService;

    public NonCommand(NotionService notionService) {
        this.notionService = notionService;
    }


    public String execute(Long chatId, String userName, String text) {
        logger.debug("Пользователь {}. Запуск обработки сообщения \"{}\", не являющегося командой",
                userName, text);
        String answer;
        UserData userData = null;
        try {
            userData = DailySurveyBot.getUserData(chatId);
            logger.debug("Пользователь {}. При обработке сообщения пользователем получены следующие данные {}", userName, userData);
            if (userData.getColumnInfoList().isEmpty()) {
                answer = "Для обновления таблицы введите /t";
            } else {
                addTextToColumn(text, userData);
                userData.setFilledColumnsCounter(userData.getFilledColumnsCounter() + 1);
                int filledColumnsCounter = userData.getFilledColumnsCounter();

                if (filledColumnsCounter == userData.getColumnInfoList().size()) {
                    notionService.saveRow(userData.getColumnInfoList());
                    answer = "Таблица заполнена";
                    userData.getColumnInfoList().clear();
                    userData.setFilledColumnsCounter(0);
                } else {
                    ColumnInfo columnInfo = userData.getColumnInfoList().get(filledColumnsCounter);
                    answer = columnInfo.getName();
                    if (SELECT.getValue().equals(columnInfo.getType())) {
                        //TODO сделать клавиатуру клавиатуру на селекты
                        answer += columnInfo.getSelectOptions();
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error("Пользователь {}. Не удалось обработать сообщение пользователя {} , данные пользователя {}, ошибка {}", userName, text, userData, e.getMessage());
            answer = "Сообщение не является текстом. Я могу сохранять только текстовые сообщения";
        } catch (RuntimeException e) {
            logger.error("Пользователь {}. Не удалось обработать сообщение пользователя {} , данные пользователя {}, ошибка {}", userName, text, userData, e.getMessage());
            answer = "Простите, я не понимаю Вас. Возможно, Вам поможет /help";
        }
        logger.debug("Пользователь {}. Завершена обработка сообщения \"{}\", не являющегося командой",
                userName, text);
        return answer;
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

package dailysurveybot.telegram.noncommands;

import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.entity.UserData;
import dailysurveybot.telegram.keyboards.InlineKeyboard;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static dailysurveybot.notion.model.enums.PropertyType.SELECT;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 * Используется для заполнения таблицы данными
 */
@Component
public class NonCommand {

    protected final Logger logger = LoggerFactory.getLogger(NonCommand.class);

    private final NotionService notionService;
    private final InlineKeyboard inlineKeyboard;

    public NonCommand(NotionService notionService,
                      InlineKeyboard inlineKeyboard) {
        this.notionService = notionService;
        this.inlineKeyboard = inlineKeyboard;
    }


    public SendMessage execute(Long chatId, String userName, String text) {
        logger.debug("Пользователь {}. Запуск обработки сообщения \"{}\", не являющегося командой",
                userName, text);
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());
        UserData userData = null;
        try {
            userData = DailySurveyBot.getUserData(chatId);
            logger.debug("Пользователь {}. При обработке сообщения пользователем получены следующие данные {}", userName, userData);
            if (userData.getColumnInfoList().isEmpty()) {
                answer.setText("Для обновления таблицы введите /t");
            } else {
                addTextToColumn(text, userData);
                userData.setFilledColumnsCounter(userData.getFilledColumnsCounter() + 1);
                int filledColumnsCounter = userData.getFilledColumnsCounter();

                if (filledColumnsCounter == userData.getColumnInfoList().size()) {
                    notionService.saveRow(userData.getColumnInfoList());
                    answer.setText("Таблица заполнена");
                    userData.getColumnInfoList().clear();
                    userData.setFilledColumnsCounter(0);
                } else {
                    ColumnInfo columnInfo = userData.getColumnInfoList().get(filledColumnsCounter);
                    answer.setText(columnInfo.getName());
                    if (SELECT.getValue().equals(columnInfo.getType())) {
                        answer.setReplyMarkup(inlineKeyboard.getInlineMessageButtons(columnInfo.getSelectOptions()));
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            logger.error("Пользователь {}. Не удалось обработать сообщение пользователя {} , данные пользователя {}, ошибка {}", userName, text, userData, e.getMessage());
            answer.setText("Сообщение не является текстом. Я могу сохранять только текстовые сообщения");
        } catch (RuntimeException e) {
            logger.error("Пользователь {}. Не удалось обработать сообщение пользователя {} , данные пользователя {}, ошибка {}", userName, text, userData, e.getMessage());
            answer.setText("Простите, я не понимаю Вас. Возможно, Вам поможет /help");
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

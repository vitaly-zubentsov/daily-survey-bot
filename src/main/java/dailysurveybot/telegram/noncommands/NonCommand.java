package dailysurveybot.telegram.noncommands;

import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.entity.User;
import dailysurveybot.telegram.entity.UserData;
import dailysurveybot.telegram.exceptions.FailToFillUserDbExceptions;
import dailysurveybot.telegram.keyboards.InlineKeyboard;
import dailysurveybot.telegram.repos.UserRepo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import static dailysurveybot.notion.model.enums.PropertyType.SELECT;
import static dailysurveybot.telegram.Utils.checkMandatoryParam;

/**
 * Обработка сообщения, не являющегося командой (т.е. обычного текста не начинающегося с "/")
 * Используется для заполнения таблицы данными
 */
@Component
public class NonCommand {

    public static final String TEXT_FOR_LOGGING_ERROR = "Пользователь {}. Не удалось обработать сообщение пользователя {} , данные пользователя {}, ошибка {}";
    protected final Logger logger = LoggerFactory.getLogger(NonCommand.class);

    private final NotionService notionService;
    private final InlineKeyboard inlineKeyboard;
    private final UserRepo userRepo;

    public NonCommand(NotionService notionService,
                      InlineKeyboard inlineKeyboard,
                      UserRepo userRepo) {
        this.notionService = notionService;
        this.inlineKeyboard = inlineKeyboard;
        this.userRepo = userRepo;
    }


    public SendMessage execute(Long chatId, Long userId, String userName, String text) {
        logger.debug("Пользователь {}. Запуск обработки сообщения {}, не являющегося командой",
                userName, text);
        SendMessage answer = new SendMessage();
        answer.setDisableWebPagePreview(true);
        answer.setChatId(chatId.toString());
        UserData userData = null;
        try {
            if (StringUtils.isBlank(text)) {
                throw new IllegalArgumentException("Сообщение не является текстом");
            }
            userData = DailySurveyBot.getUserData(userId);
            logger.debug("Пользователь {}. При обработке сообщения пользователем получены следующие данные {}", userName, userData);

            User userFromDb = userRepo.findById(userId).orElse(null);
            if (userFromDb == null) {
                answer.setText("Для заполнения таблицы установите первоначальные настройки, введя команду /start");
            } else if (Boolean.FALSE.equals(userFromDb.getFilled())) {
                fillUserInDbAndSetAnswer(userFromDb, answer, text);
            } else if (userData.getColumnInfoList().isEmpty()) {
                answer.setText("Для обновления таблицы введите /t");
            } else {
                performOperationsToFillTable(text, answer, userData, userFromDb);
            }
        } catch (IllegalArgumentException e) {
            logger.error(TEXT_FOR_LOGGING_ERROR, userName, text, userData, e.getMessage());
            answer.setText("Сообщение не является текстом. Я могу работать только с текстовыми сообщения");
        } catch (RuntimeException e) {
            logger.error(TEXT_FOR_LOGGING_ERROR, userName, text, userData, e.getMessage());
            answer.setText("Простите, я не понимаю Вас. Возможно, Вам поможет /help");
        }
        logger.debug("Пользователь {}. Завершена обработка сообщения {}, не являющегося командой",
                userName, text);

        return answer;
    }

    private void fillUserInDbAndSetAnswer(User userFromDb, SendMessage answer, String text) {
        if (userFromDb.getNotionDatabaseId() == null) {
            userFromDb.setNotionDatabaseId(text);
            userRepo.save(userFromDb);
            answer.setText("Создайте интеграцию для своего workspace в notion и добавьте доступ на запись и чтения данных в таблице для интеграции, следуя инструкции (сделать нужно только первый шаг и второй шаг): https://developers.notion.com/docs/create-a-notion-integration . Далее введите в ответе мне Internal Integration Token. Его можно получить перейдя по ссылке https://www.notion.so/my-integrations и нажав View integration");
        } else if (userFromDb.getNotionApiToken() == null) {
            userFromDb.setNotionApiToken(text);
            userFromDb.setFilled(true);
            userRepo.save(userFromDb);
            answer.setText("Настройки успешно сохранены. Попробуйте ввести строку таблицы воспользовавшись командой /t. Если порядок при заполнении таблицы важен, то добавьте в начало названия колонки порядковый номер. Если возникнут сложности введите /help");
        } else {
            throw new FailToFillUserDbExceptions();
        }
    }

    private void performOperationsToFillTable(String text, SendMessage answer, UserData userData, User userFromDb) {
        addTextToColumn(text, userData);
        userData.setFilledColumnsCounter(userData.getFilledColumnsCounter() + 1);
        int filledColumnsCounter = userData.getFilledColumnsCounter();

        if (filledColumnsCounter == userData.getColumnInfoList().size()) {
            checkMandatoryParam(userFromDb);
            notionService.saveRow(userData.getColumnInfoList(),
                    userFromDb.getNotionDatabaseId(),
                    userFromDb.getNotionApiToken());
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

    private void addTextToColumn(String text, UserData userData) {
        ColumnInfo columnInfo = userData.getColumnInfoList().get(userData.getFilledColumnsCounter());
        columnInfo.setTextFromUser(text);
    }

}

package dailysurveybot.telegram.commands.operations;

import dailysurveybot.Utils;
import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.entity.UserData;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static dailysurveybot.notion.model.enums.PropertyType.SELECT;
import static dailysurveybot.telegram.constants.CommandsEnum.ADD_ROW_TO_TABLE;

@Component
public class AddRowToTableCommand extends OperationCommand {

    private final NotionService notionService;

    public AddRowToTableCommand(NotionService notionService) {
        super(ADD_ROW_TO_TABLE.getIdentifier(), ADD_ROW_TO_TABLE.getDescription());
        this.notionService = notionService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        String answerToUser;

        try {
            UserData userData = DailySurveyBot.getUserData(chat.getId());
            userData.setColumnInfoList(notionService.getColumnsInfo());
            userData.setFilledColumnsCounter(0);
            //Отправляем пользователю имя первого столбца для заполнениия.
            // Последующие столбыцы обрабатываются после ввода пользователем текста (не команд) в ответ боту
            ColumnInfo columnInfo = userData.getColumnInfoList().get(0);
            answerToUser = columnInfo.getName();
            if (SELECT.getValue().equals(columnInfo.getType())) {
                //TODO сделать клавиатуру клавиатуру на селекты
                answerToUser += columnInfo.getSelectOptions();
            }
        } catch (Exception e) { //TODO  добавить классы ошибок
            answerToUser = "Что то пошло не так. Я не могу получить имена столбоцов таблицы";
            logger.debug(e.getMessage());
        }

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName, answerToUser);
    }

}

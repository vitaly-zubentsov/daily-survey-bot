package dailysurveybot.telegram.commands.operation;

import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.Utils;
import dailysurveybot.telegram.entity.UserData;
import dailysurveybot.telegram.exceptions.ColumnsInfoListIsEmptyException;
import dailysurveybot.telegram.exceptions.NoUserInDbExceptions;
import dailysurveybot.telegram.keyboards.InlineKeyboard;
import dailysurveybot.telegram.repos.UserRepo;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

import static dailysurveybot.notion.model.enums.PropertyType.SELECT;
import static dailysurveybot.telegram.Utils.checkMandatoryParam;
import static dailysurveybot.telegram.constants.CommandsEnum.ADD_ROW_TO_TABLE;

@Component
public class AddRowToTableCommand extends OperationCommand {

    private final NotionService notionService;
    private final InlineKeyboard inlineKeyboard;
    private final UserRepo userRepo;

    public AddRowToTableCommand(NotionService notionService,
                                InlineKeyboard inlineKeyboard,
                                UserRepo userRepo) {
        super(ADD_ROW_TO_TABLE.getIdentifier(), ADD_ROW_TO_TABLE.getDescription());
        this.notionService = notionService;
        this.inlineKeyboard = inlineKeyboard;
        this.userRepo = userRepo;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        SendMessage answer = new SendMessage();
        String userName = Utils.getUserName(user);
        answer.enableMarkdown(true);
        answer.setChatId(chat.getId().toString());
        UserData userData = null;
        try {
            dailysurveybot.telegram.entity.User userFromDb = userRepo.findById(user.getId())
                    .orElseThrow(NoUserInDbExceptions::new);
            checkMandatoryParam(userFromDb);
            List<ColumnInfo> columnsInfos = notionService.getColumnsInfo(userFromDb.getNotionDatabaseId(),
                    userFromDb.getNotionApiToken());

            if (columnsInfos.isEmpty()) {
                throw new ColumnsInfoListIsEmptyException();
            }
            userData = DailySurveyBot.getUserData(chat.getId());
            userData.setColumnInfoList(columnsInfos);
            userData.setFilledColumnsCounter(0);

            //Отправляем пользователю имя первого столбца для заполнениия
            // Последующие столбцы обрабатываются после ответов пользователя (не команд)
            ColumnInfo columnInfo = userData.getColumnInfoList().get(0);
            answer.setText(columnInfo.getName());
            if (SELECT.getValue().equals(columnInfo.getType())) {
                answer.setReplyMarkup(inlineKeyboard.getInlineMessageButtons(columnInfo.getSelectOptions()));
            }
        } catch (RuntimeException e) {
            answer.setText("Что то пошло не так. Я не могу получить имена столбоцов таблицы");
            logger.error("Пользователь {}, ошибка {}, userData {}", userName, e.getMessage(), userData);
            e.printStackTrace();
        }

        sendAnswer(absSender, this.getCommandIdentifier(), userName, answer);
    }

}

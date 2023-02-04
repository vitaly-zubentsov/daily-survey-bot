package dailysurveybot.telegram.commands.operations;

import dailysurveybot.Utils;
import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.Column;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;
import java.util.stream.Collectors;

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
            List<Column> columns = notionService.getColumns();
            answerToUser = columns.stream().map(Column::getName).collect(Collectors.joining(", "));
        } catch (Exception e) { //TODO  добавить классы ошибок
            answerToUser = "Что то пошло не так. Я не могу получить имена столбоцов таблицы";
            logger.error(e.getMessage());
        }

        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                answerToUser);
    }

}

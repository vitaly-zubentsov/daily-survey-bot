package dailysurveybot.telegram.commands.operation;


import dailysurveybot.telegram.Utils;
import dailysurveybot.telegram.repos.UserRepo;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import static dailysurveybot.telegram.constants.CommandsEnum.START;

@Component
public class StartCommand extends OperationCommand {

    private final UserRepo userRepo;

    public StartCommand(UserRepo userRepo) {
        super(START.getIdentifier(), START.getDescription());
        this.userRepo = userRepo;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        SendMessage answer = new SendMessage();
        answer.setText("Приветствую! Я могу заполнять таблицу в notion.\n Таблица должна состоять из столбцов содержащих текст или выпадающий список(select).\n Для начала работы необходимо провести настройку таблицы в notion.\n Введите мне в ответе Id заполняемой таблицы. Если вы не знаете что такое id и где его искать, то воспользуйтесь информацией данной на 3 шаге(Step 3: Save the database ID) данной инструкции https://developers.notion.com/docs/create-a-notion-integration#step-1-create-an-integration");
        answer.enableMarkdown(true);
        answer.setChatId(chat.getId().toString());
        answer.setDisableWebPagePreview(true);
        dailysurveybot.telegram.entity.User newUser = new dailysurveybot.telegram.entity.User();
        newUser.setFilled(false);
        newUser.setId(user.getId());
        newUser.setChatId(chat.getId());
        userRepo.save(newUser);
        sendAnswer(absSender, this.getCommandIdentifier(), userName, answer);
    }
}

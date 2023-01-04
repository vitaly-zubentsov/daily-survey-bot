package daylysurveybot.telegram.command.operation;

import daylysurveybot.Utils;
import daylysurveybot.telegram.Bot;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

public class GreetingsCommand extends OperationCommand {

    public GreetingsCommand(String identifier, String description) {
        super(identifier, description);
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String userName = Utils.getUserName(user);
        sendAnswer(absSender, chat.getId(), this.getCommandIdentifier(), userName,
                Bot.getUserSettings(chat.getId()).getHelloWorldAnswer());
    }
}
package dailysurveybot.telegram.schedulers;

import dailysurveybot.config.TelegramConfig;
import dailysurveybot.telegram.DailySurveyBot;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.List;

@Component
public class Scheduler {

    private final DailySurveyBot dailySurveyBot;
    private final long userId;
    private final long chatId;


    public Scheduler(DailySurveyBot dailySurveyBot,
                     TelegramConfig telegramConfig) {
        this.dailySurveyBot = dailySurveyBot;
        this.userId = telegramConfig.userId();
        this.chatId = telegramConfig.chatId();
    }

    @Scheduled(cron = "0 0 18 * * *")
    public void startAddRowToTable() {
        Update update = new Update();
        User user = new User();
        user.setUserName("userName");
        user.setId(userId);
        Chat chat = new Chat();
        chat.setId(chatId);
        Message message = new Message();
        message.setChat(chat);
        message.setFrom(user);
        message.setText("/t");
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setType(EntityType.BOTCOMMAND);
        messageEntity.setOffset(0);
        message.setEntities(List.of(messageEntity));
        update.setMessage(message);
        dailySurveyBot.onUpdateReceived(update);
    }

}

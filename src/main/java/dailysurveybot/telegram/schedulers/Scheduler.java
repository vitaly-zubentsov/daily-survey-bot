package dailysurveybot.telegram.schedulers;

import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.repos.UserRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.List;

@Component
public class Scheduler {

    private final DailySurveyBot dailySurveyBot;
    private final UserRepo userRepo;


    public Scheduler(DailySurveyBot dailySurveyBot,
                     UserRepo userRepo) {
        this.dailySurveyBot = dailySurveyBot;
        this.userRepo = userRepo;
    }

    @Scheduled(cron = "0 0 18 * * *")
    public void startAddRowToTable() {
        Iterable<dailysurveybot.telegram.entity.User> all = userRepo.findAll();
        for (dailysurveybot.telegram.entity.User user : all) {
            if (Boolean.TRUE.equals(user.getFilled())) {
                executeUpdate(user.getId(), user.getChatId());
            }
        }
    }

    private void executeUpdate(Long userId, Long chatId) {
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

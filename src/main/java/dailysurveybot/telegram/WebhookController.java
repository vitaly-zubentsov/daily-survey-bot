package dailysurveybot.telegram;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebhookController {
    private final DailySurveyBot dailySurveyBot;

    public WebhookController(DailySurveyBot dailySurveyBot) {
        this.dailySurveyBot = dailySurveyBot;
    }

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return dailySurveyBot.onWebhookUpdateReceived(update);
    }

}

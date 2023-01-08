package dailysurveybot.telegram.handlers;

import dailysurveybot.telegram.constants.SettingsEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

@Component
public class CallbackQueryHandler {

    private final SettingsHandler settingsHandler;

    public CallbackQueryHandler(SettingsHandler settingsHandler) {
        this.settingsHandler = settingsHandler;
    }

    public BotApiMethod<? extends Serializable> processCallbackQuery(CallbackQuery buttonQuery) {
        final Long chatId = buttonQuery.getMessage().getChatId();

        final String data = buttonQuery.getData();

        if (SettingsEnum.HI_RUSSIA.getData().equals(data)) {
            return settingsHandler.setSettings(chatId, SettingsEnum.HI_RUSSIA.getText());
        } else if (SettingsEnum.HI_GEORGIAN.getData().equals(data)) {
            return settingsHandler.setSettings(chatId, SettingsEnum.HI_GEORGIAN.getText());
        } else if (SettingsEnum.HI_ENGLISH.getData().equals(data)) {
            return settingsHandler.setSettings(chatId, SettingsEnum.HI_ENGLISH.getText());
        } else {
            return settingsHandler.setSettings(chatId, SettingsEnum.HI_PROGRAMMERS.getText());
        }
    }
}

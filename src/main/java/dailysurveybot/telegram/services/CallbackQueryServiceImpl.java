package dailysurveybot.telegram.services;

import dailysurveybot.telegram.constants.SettingsEnum;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

@Service
public class CallbackQueryServiceImpl implements CallbackQueryService {

    private final SettingsService settingsHandler;

    public CallbackQueryServiceImpl(SettingsService settingsHandler) {
        this.settingsHandler = settingsHandler;
    }

    @Override
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

package dailysurveybot.telegram.keyboards;

import dailysurveybot.telegram.constants.SettingsEnum;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Клавиатуры, формируемые в ленте Telegram для выбора ностроек
 */
@Component
public class InlineKeyboardMaker {

    public InlineKeyboardMarkup getInlineMessageButtons() {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(getButton(SettingsEnum.HI_ENGLISH));
        rowList.add(getButton(SettingsEnum.HI_RUSSIA));
        rowList.add(getButton(SettingsEnum.HI_GEORGIAN));
        rowList.add(getButton(SettingsEnum.HI_PROGRAMMERS));

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> getButton(SettingsEnum settingsEnum) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(settingsEnum.getText());
        button.setCallbackData(settingsEnum.getData());

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }
}
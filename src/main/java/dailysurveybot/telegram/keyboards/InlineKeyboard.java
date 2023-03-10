package dailysurveybot.telegram.keyboards;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Клавиатура, формируемые в ленте Telegram для заполнения select
 */
@Component
public class InlineKeyboard {

    public InlineKeyboardMarkup getInlineMessageButtons(@Nonnull List<String> selectOptions) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        for (String selectOption : selectOptions) {
            rowList.add(getButton(selectOption));
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private List<InlineKeyboardButton> getButton(String selectOption) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(selectOption);
        button.setCallbackData(selectOption);

        List<InlineKeyboardButton> keyboardButtonsRow = new ArrayList<>();
        keyboardButtonsRow.add(button);
        return keyboardButtonsRow;
    }
}

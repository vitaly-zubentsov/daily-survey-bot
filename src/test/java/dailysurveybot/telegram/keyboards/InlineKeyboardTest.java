package dailysurveybot.telegram.keyboards;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InlineKeyboardTest {

    private final InlineKeyboard inlineKeyboard = new InlineKeyboard();

    @Test
    @DisplayName("Получение клавиатуры")
    void getInlineMessageButtons_ColumnInfoIsNotEmpty_ReturnInlineKeyboardMarkup() {
        //given
        List<String> selectOptions = List.of("yes", "no");

        //when
        InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeyboard.getInlineMessageButtons(selectOptions);

        //then
        assertEquals(2, inlineKeyboardMarkup.getKeyboard().size());
        assertEquals(selectOptions.get(0), inlineKeyboardMarkup.getKeyboard().get(0).get(0).getText());
        assertEquals(selectOptions.get(1), inlineKeyboardMarkup.getKeyboard().get(1).get(0).getText());
    }

    @Test
    @DisplayName("Получение клавиатуры, список опций пустой")
    void getInlineMessageButtons_ColumnInfoIsEmpty_ReturnEmptyInlineKeyboardMarkup() {
        //given
        List<String> selectOptions = Collections.emptyList();

        //when
        InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeyboard.getInlineMessageButtons(selectOptions);

        //then
        assertEquals(0, inlineKeyboardMarkup.getKeyboard().size());
    }

}
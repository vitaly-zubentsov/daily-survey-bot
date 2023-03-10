package dailysurveybot.telegram.noncommands;

import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.entity.UserData;
import dailysurveybot.telegram.keyboards.InlineKeyboard;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

import static dailysurveybot.notion.model.enums.PropertyType.SELECT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Проверка NonCommand")
class NonCommandTest {

    private static final String USER_NAME = "userName";
    private static final String TEXT = "text";

    @InjectMocks
    NonCommand nonCommand;

    @Mock
    NotionService notionService;

    @Spy
    InlineKeyboard inlineKeyboard;

    @Test
    @DisplayName("Пользователь отправляет сообщение не введя команду /t")
    void execute_EmptyColumnInfoForUser_ReturnAnswerWithClue() {
        assertEquals("Для обновления таблицы введите /t", nonCommand.execute(1L, USER_NAME, TEXT).getText());
    }

    @Test
    @DisplayName("Заполнение таблицы. Следующая колонка текст")
    void execute_ColumnInfoIsNotEmptyAndNextColumnIsText_ReturnNextValueForFill() {
        //given
        Long userId = 123L;
        UserData userData = DailySurveyBot.getUserData(userId);
        int filledColumnsCounter = 1;
        userData.setFilledColumnsCounter(filledColumnsCounter);
        ColumnInfo columnInfo1 = new ColumnInfo();
        ColumnInfo columnInfo2 = new ColumnInfo();
        ColumnInfo columnInfo3 = new ColumnInfo();
        String nextColumnName = "Next column name";
        columnInfo3.setName(nextColumnName);
        userData.setColumnInfoList(List.of(columnInfo1, columnInfo2, columnInfo3));

        //when
        SendMessage answer = nonCommand.execute(userId, USER_NAME, TEXT);

        //then
        assertEquals(nextColumnName, answer.getText());
        assertEquals(TEXT, columnInfo2.getTextFromUser());
        assertEquals(filledColumnsCounter + 1, userData.getFilledColumnsCounter());
    }

    @Test
    @DisplayName("Заполнение таблицы. Следующая колонка select")
    void execute_ColumnInfoIsNotEmptyAndNextColumnIsSelect_ReturnNextValueForFill() {
        //given
        Long userId = 123L;
        UserData userData = DailySurveyBot.getUserData(userId);
        int filledColumnsCounter = 1;
        userData.setFilledColumnsCounter(filledColumnsCounter);
        ColumnInfo columnInfo1 = new ColumnInfo();
        ColumnInfo columnInfo2 = new ColumnInfo();
        ColumnInfo columnInfo3 = new ColumnInfo();
        String nextColumnName = "Next column name";
        columnInfo3.setName(nextColumnName);
        columnInfo3.setType(SELECT.getValue());
        columnInfo3.setSelectOptions(List.of("1", "2"));
        userData.setColumnInfoList(List.of(columnInfo1, columnInfo2, columnInfo3));

        //when
        SendMessage answer = nonCommand.execute(userId, USER_NAME, TEXT);

        //then
        assertEquals(nextColumnName, answer.getText());
        assertNotNull(answer.getReplyMarkup());
        assertEquals(TEXT, columnInfo2.getTextFromUser());
        assertEquals(filledColumnsCounter + 1, userData.getFilledColumnsCounter());
    }

    @Test
    @DisplayName("Заполнение таблицы. Значения колонок заполнены")
    void execute_ColumnInfoIsNotEmptyAndFinishFill_ReturnCompleteFillMessage() {
        //given
        Long userId = 123L;
        UserData userData = DailySurveyBot.getUserData(userId);
        int filledColumnsCounter = 1;
        userData.setFilledColumnsCounter(filledColumnsCounter);
        ColumnInfo columnInfo1 = new ColumnInfo();
        ColumnInfo columnInfo2 = new ColumnInfo();
        List<ColumnInfo> columnInfos = new ArrayList<>();
        columnInfos.add(columnInfo1);
        columnInfos.add(columnInfo2);
        userData.setColumnInfoList(columnInfos);

        //when
        SendMessage answer = nonCommand.execute(userId, USER_NAME, TEXT);

        //then
        verify(notionService, times(1)).saveRow(any());
        assertEquals("Таблица заполнена", answer.getText());
        assertTrue(userData.getColumnInfoList().isEmpty());
        assertEquals(0, userData.getFilledColumnsCounter());
    }

    @Test
    @DisplayName("Заполнение таблицы. Значения колонок заполнены. При отправке сообщения в notion получена ошибка")
    void execute_ErrorWhenSaveRowToNotion_ReturnError() {
        //given
        Long userId = 123L;
        UserData userData = DailySurveyBot.getUserData(userId);
        int filledColumnsCounter = 1;
        userData.setFilledColumnsCounter(filledColumnsCounter);
        ColumnInfo columnInfo1 = new ColumnInfo();
        ColumnInfo columnInfo2 = new ColumnInfo();
        List<ColumnInfo> columnInfos = new ArrayList<>();
        columnInfos.add(columnInfo1);
        columnInfos.add(columnInfo2);
        userData.setColumnInfoList(columnInfos);
        doThrow(new RuntimeException()).when(notionService).saveRow(any());

        //when
        SendMessage answer = nonCommand.execute(userId, USER_NAME, TEXT);

        //then
        assertEquals("Простите, я не понимаю Вас. Возможно, Вам поможет /help", answer.getText());
    }

    @Test
    @DisplayName("Заполнение таблицы. Пользователь отправил не текст")
    void execute_userInputIsNotAText_ReturnError() {
        //given
        Long userId = 123L;
        UserData userData = DailySurveyBot.getUserData(userId);
        int filledColumnsCounter = 1;
        userData.setFilledColumnsCounter(filledColumnsCounter);
        ColumnInfo columnInfo1 = new ColumnInfo();
        ColumnInfo columnInfo2 = new ColumnInfo();
        userData.setColumnInfoList(List.of(columnInfo1, columnInfo2));

        //when
        SendMessage answer = nonCommand.execute(userId, USER_NAME, "     ");

        //then
        assertEquals("Сообщение не является текстом. Я могу сохранять только текстовые сообщения",
                answer.getText());
    }

}
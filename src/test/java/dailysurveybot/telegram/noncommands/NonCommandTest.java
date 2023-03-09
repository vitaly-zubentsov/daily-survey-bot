package dailysurveybot.telegram.noncommands;

import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.entity.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static dailysurveybot.notion.model.enums.PropertyType.SELECT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NonCommandTest {

    private static final String USER_NAME = "userName";
    private static final String TEXT = "text";

    @InjectMocks
    NonCommand nonCommand;

    @Mock
    NotionService notionService;

    @Test
    @DisplayName("Пользователь отправляет сообщение не введя команду /t")
    void execute_EmptyColumnInfoForUser_ReturnAnswerWithClue() {
        assertEquals("Для обновления таблицы введите /t", nonCommand.execute(1L, USER_NAME, TEXT));
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
        String answer = nonCommand.execute(userId, USER_NAME, TEXT);

        //then
        assertEquals(nextColumnName, answer);
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
        String answer = nonCommand.execute(userId, USER_NAME, TEXT);

        //then
        assertEquals(nextColumnName + "[1, 2]", answer);
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
        String answer = nonCommand.execute(userId, USER_NAME, TEXT);

        //then
        verify(notionService, times(1)).saveRow(any());
        assertEquals("Таблица заполнена", answer);
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
        String answer = nonCommand.execute(userId, USER_NAME, TEXT);

        //then
        assertEquals("Простите, я не понимаю Вас. Возможно, Вам поможет /help", answer);
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
        String answer = nonCommand.execute(userId, USER_NAME, "     ");

        //then
        assertEquals("Сообщение не является текстом. Я могу сохранять только текстовые сообщения", answer);
    }


}
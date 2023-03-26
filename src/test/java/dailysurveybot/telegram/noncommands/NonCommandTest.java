package dailysurveybot.telegram.noncommands;

import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.entity.User;
import dailysurveybot.telegram.entity.UserData;
import dailysurveybot.telegram.keyboards.InlineKeyboard;
import dailysurveybot.telegram.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static dailysurveybot.notion.model.enums.PropertyType.SELECT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Проверка NonCommand")
class NonCommandTest {

    private static final long CHAT_ID = 123L;
    private static final long USER_ID = 231L;
    private static final String USER_NAME = "userName";
    private static final String TEXT = "text";

    @InjectMocks
    private NonCommand nonCommand;

    @Mock
    private NotionService notionService;

    @Mock
    private UserRepo userRepo;

    @Spy
    private InlineKeyboard inlineKeyboard;

    private User user;

    @BeforeEach
    void init() {
        user = new User();
        user.setChatId(CHAT_ID);
        user.setId(USER_ID);
        user.setFilled(true);
        user.setNotionDatabaseId("databaseId");
        user.setNotionApiToken("apiToken");
    }

    @Test
    @DisplayName("Пользователь отправляет сообщение не введя команду /t")
    void execute_EmptyColumnInfoForUser_ReturnAnswerWithClue() {
        //given
        when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));
        //when then
        assertEquals("Для обновления таблицы введите /t", nonCommand.execute(CHAT_ID, USER_ID, USER_NAME, TEXT).getText());
    }

    @Test
    @DisplayName("Заполнение таблицы. Следующая колонка текст")
    void execute_ColumnInfoIsNotEmptyAndNextColumnIsText_ReturnNextValueForFill() {
        //given
        UserData userData = DailySurveyBot.getUserData(USER_ID);
        int filledColumnsCounter = 1;
        userData.setFilledColumnsCounter(filledColumnsCounter);
        ColumnInfo columnInfo1 = new ColumnInfo();
        ColumnInfo columnInfo2 = new ColumnInfo();
        ColumnInfo columnInfo3 = new ColumnInfo();
        String nextColumnName = "Next column name";
        columnInfo3.setName(nextColumnName);
        userData.setColumnInfoList(List.of(columnInfo1, columnInfo2, columnInfo3));
        when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));

        //when
        SendMessage answer = nonCommand.execute(CHAT_ID, USER_ID, USER_NAME, TEXT);

        //then
        assertEquals(nextColumnName, answer.getText());
        assertEquals(TEXT, columnInfo2.getTextFromUser());
        assertEquals(filledColumnsCounter + 1, userData.getFilledColumnsCounter());
    }

    @Test
    @DisplayName("Заполнение таблицы. Следующая колонка select")
    void execute_ColumnInfoIsNotEmptyAndNextColumnIsSelect_ReturnNextValueForFill() {
        //given
        UserData userData = DailySurveyBot.getUserData(USER_ID);
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
        when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));

        //when
        SendMessage answer = nonCommand.execute(CHAT_ID, USER_ID, USER_NAME, TEXT);

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
        UserData userData = DailySurveyBot.getUserData(USER_ID);
        int filledColumnsCounter = 1;
        userData.setFilledColumnsCounter(filledColumnsCounter);
        ColumnInfo columnInfo1 = new ColumnInfo();
        ColumnInfo columnInfo2 = new ColumnInfo();
        List<ColumnInfo> columnInfos = new ArrayList<>();
        columnInfos.add(columnInfo1);
        columnInfos.add(columnInfo2);
        userData.setColumnInfoList(columnInfos);
        when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));

        //when
        SendMessage answer = nonCommand.execute(CHAT_ID, USER_ID, USER_NAME, TEXT);

        //then
        verify(notionService, times(1)).saveRow(any(), any(), any());
        assertEquals("Таблица заполнена", answer.getText());
        assertTrue(userData.getColumnInfoList().isEmpty());
        assertEquals(0, userData.getFilledColumnsCounter());
    }

    @Test
    @DisplayName("Заполнение таблицы. Значения колонок заполнены. При отправке сообщения в notion получена ошибка")
    void execute_ErrorWhenSaveRowToNotion_ReturnError() {
        //given
        UserData userData = DailySurveyBot.getUserData(USER_ID);
        int filledColumnsCounter = 1;
        userData.setFilledColumnsCounter(filledColumnsCounter);
        ColumnInfo columnInfo1 = new ColumnInfo();
        ColumnInfo columnInfo2 = new ColumnInfo();
        List<ColumnInfo> columnInfos = new ArrayList<>();
        columnInfos.add(columnInfo1);
        columnInfos.add(columnInfo2);
        userData.setColumnInfoList(columnInfos);
        doThrow(new RuntimeException()).when(notionService).saveRow(any(), any(), any());
        when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));

        //when
        SendMessage answer = nonCommand.execute(CHAT_ID, USER_ID, USER_NAME, TEXT);

        //then
        assertEquals("Простите, я не понимаю Вас. Возможно, Вам поможет /help", answer.getText());
    }

    @Test
    @DisplayName("Заполнение таблицы. Пользователь отправил не текст")
    void execute_userInputIsNotAText_ReturnError() {
        SendMessage answer = nonCommand.execute(CHAT_ID, USER_ID, USER_NAME, "     ");

        assertEquals("Сообщение не является текстом. Я могу работать только с текстовыми сообщения",
                answer.getText());
    }

    @Test
    @DisplayName("Пользователь отправляет сообщение не дав команду /start")
    void execute_EmptyUser_ReturnAnswerWithAskToInputStart() {
        assertEquals("Для заполнения таблицы установите первоначальные настройки, введя команду /start",
                nonCommand.execute(CHAT_ID, USER_ID, USER_NAME, TEXT).getText());
    }

    @Test
    @DisplayName("Ввод первоначальных настроек, пользователь уже ввел databaseId")
    void execute_userHasDatabaseId_returnAnswerWithAskToSetToken() {
        //given
        user.setNotionDatabaseId(null);
        user.setNotionApiToken(null);
        user.setFilled(false);
        when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));

        //when
        SendMessage answer = nonCommand.execute(CHAT_ID, USER_ID, USER_NAME, TEXT);

        //then
        assertTrue(answer.getText().contains("Создайте интеграцию для своего workspace в notion и добавьте доступ на запись и чтения данных в таблице для интеграции"));

    }

    @Test
    @DisplayName("Ввод первоначальных настроек, пользователь ввел token")
    void execute_userHasToken_returnAnswerWithFinishStartText() {
        //given
        user.setNotionApiToken(null);
        user.setFilled(false);
        when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));

        //when
        SendMessage answer = nonCommand.execute(CHAT_ID, USER_ID, USER_NAME, TEXT);

        //then
        assertTrue(answer.getText().contains("Настройки успешно сохранены. Попробуйте ввести строку таблицы воспользовавшись командой /t"));
        assertTrue(user.getFilled());
    }

    @Test
    @DisplayName("Ввод первоначальных настроек, ошибка при заполнении")
    void execute_userAlreadyHaveFilledValue_returnAnswerWithError() {
        //given
        user.setFilled(false);
        when(userRepo.findById(USER_ID)).thenReturn(Optional.of(user));

        //when
        SendMessage answer = nonCommand.execute(CHAT_ID, USER_ID, USER_NAME, TEXT);

        //then
        assertEquals("Простите, я не понимаю Вас. Возможно, Вам поможет /help", answer.getText());
    }

}
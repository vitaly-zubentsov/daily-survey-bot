package dailysurveybot.telegram.commands.operation;

import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.keyboards.InlineKeyboard;
import dailysurveybot.telegram.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static dailysurveybot.notion.model.enums.PropertyType.SELECT;
import static dailysurveybot.notion.model.enums.PropertyType.TITLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Проверка AddRowToTableCommand")
class AddRowToTableCommandTest {

    private static final long CHAT_ID = 123L;
    private static final long USER_ID = 231L;
    private static final String NOTION_DATABASE_ID = "databaseId";
    private static final String NOTION_API_TOKEN = "apiToken";

    @InjectMocks
    private AddRowToTableCommand addRowToTableCommand;

    @Mock
    private NotionService notionService;
    @Spy
    private InlineKeyboard inlineKeyboard;
    @Mock
    private DailySurveyBot dailySurveyBot;
    @Mock
    private UserRepo userRepo;

    private dailysurveybot.telegram.entity.User userFromDb;

    @BeforeEach
    void init() {
        userFromDb = new dailysurveybot.telegram.entity.User();
        userFromDb.setChatId(CHAT_ID);
        userFromDb.setId(USER_ID);
        userFromDb.setFilled(true);
        userFromDb.setNotionDatabaseId(NOTION_DATABASE_ID);
        userFromDb.setNotionApiToken(NOTION_API_TOKEN);
    }

    @Test
    @DisplayName("Заполнение таблицы. Первая колонка текст")
    void execute_ColumnInfoIsNotEmptyAndFirstColumnIsText_SendMessageWithFirstColumnName() throws TelegramApiException {
        //given
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        User user = new User(USER_ID, "firstName", false);
        Long chatId = 11L;
        Chat chat = new Chat(chatId, "type");
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setName("columnInfo1Name");
        columnInfo.setType(TITLE.getValue());
        List<ColumnInfo> columnInfos = List.of(columnInfo, new ColumnInfo(), new ColumnInfo());
        String[] strings = new String[1];
        when(notionService.getColumnsInfo(NOTION_DATABASE_ID, NOTION_API_TOKEN)).thenReturn(columnInfos);
        when(userRepo.findById(USER_ID)).thenReturn(Optional.ofNullable(userFromDb));

        //when
        addRowToTableCommand.execute(dailySurveyBot, user, chat, strings);

        //then
        verify(dailySurveyBot, only()).execute(argumentCaptor.capture());
        SendMessage sendMessage = argumentCaptor.getValue();
        assertEquals(chatId.toString(), sendMessage.getChatId());
        assertEquals(columnInfo.getName(), sendMessage.getText());
        assertEquals(ParseMode.MARKDOWN, sendMessage.getParseMode());
        assertNull(sendMessage.getReplyMarkup());
    }

    @Test
    @DisplayName("Заполнение таблицы. Следующая колонка select")
    void execute_ColumnInfoIsNotEmptyAndNextColumnIsSelect_SendMessageWithFirstColumnNameAndKeyboard() throws TelegramApiException {
        //given
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        User user = new User(USER_ID, "firstName", false);
        Long chatId = 11L;
        Chat chat = new Chat(chatId, "type");
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setName("columnInfo1Name");
        columnInfo.setType(SELECT.getValue());
        columnInfo.setSelectOptions(List.of("1", "2"));
        List<ColumnInfo> columnInfos = List.of(columnInfo, new ColumnInfo(), new ColumnInfo());
        String[] strings = new String[1];
        when(notionService.getColumnsInfo(NOTION_DATABASE_ID, NOTION_API_TOKEN)).thenReturn(columnInfos);
        when(userRepo.findById(USER_ID)).thenReturn(Optional.ofNullable(userFromDb));

        //when
        addRowToTableCommand.execute(dailySurveyBot, user, chat, strings);

        //then
        verify(dailySurveyBot, only()).execute(argumentCaptor.capture());
        SendMessage sendMessage = argumentCaptor.getValue();
        assertEquals(chatId.toString(), sendMessage.getChatId());
        assertEquals(columnInfo.getName(), sendMessage.getText());
        assertEquals(ParseMode.MARKDOWN, sendMessage.getParseMode());
        assertNotNull(sendMessage.getReplyMarkup());
    }

    @Test
    @DisplayName("Заполнение таблицы. Не удалось получить данные о столбцах таблицы")
    void execute_ColumnInfoIsEmpty_SendMessageWithError() throws TelegramApiException {
        //given
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        User user = new User(USER_ID, "firstName", false);
        Long chatId = 11L;
        Chat chat = new Chat(chatId, "type");
        String[] strings = new String[1];
        when(notionService.getColumnsInfo(NOTION_DATABASE_ID, NOTION_API_TOKEN)).thenReturn(Collections.emptyList());
        when(userRepo.findById(USER_ID)).thenReturn(Optional.ofNullable(userFromDb));

        //when
        addRowToTableCommand.execute(dailySurveyBot, user, chat, strings);

        //then
        verify(dailySurveyBot, only()).execute(argumentCaptor.capture());
        SendMessage sendMessage = argumentCaptor.getValue();
        assertEquals(chatId.toString(), sendMessage.getChatId());
        assertEquals("Что то пошло не так. Я не могу получить имена столбоцов таблицы", sendMessage.getText());
        assertEquals(ParseMode.MARKDOWN, sendMessage.getParseMode());
        assertNull(sendMessage.getReplyMarkup());
    }

}
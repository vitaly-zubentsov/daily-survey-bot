package dailysurveybot.telegram.commands;

import dailysurveybot.notion.NotionService;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.commands.operation.AddRowToTableCommand;
import dailysurveybot.telegram.keyboards.InlineKeyboard;
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

import static dailysurveybot.notion.model.enums.PropertyType.SELECT;
import static dailysurveybot.notion.model.enums.PropertyType.TITLE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Проверка AddRowToTableCommand")
class AddRowToTableCommandTest {

    @InjectMocks
    private AddRowToTableCommand addRowToTableCommand;

    @Mock
    private NotionService notionService;
    @Spy
    private InlineKeyboard inlineKeyboard;
    @Mock
    private DailySurveyBot dailySurveyBot;

    @Test
    @DisplayName("Заполнение таблицы. Первая колонка текст")
    void execute_ColumnInfoIsNotEmptyAndFirstColumnIsText_SendMessageWithFirstColumnName() throws TelegramApiException {
        //given
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        User user = new User(123L, "firstName", false);
        Long chatId = 11L;
        Chat chat = new Chat(chatId, "type");
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setName("columnInfo1Name");
        columnInfo.setType(TITLE.getValue());
        List<ColumnInfo> columnInfos = List.of(columnInfo, new ColumnInfo(), new ColumnInfo());
        String[] strings = new String[1];
        when(notionService.getColumnsInfo()).thenReturn(columnInfos);

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
        User user = new User(123L, "firstName", false);
        Long chatId = 11L;
        Chat chat = new Chat(chatId, "type");
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setName("columnInfo1Name");
        columnInfo.setType(SELECT.getValue());
        columnInfo.setSelectOptions(List.of("1", "2"));
        List<ColumnInfo> columnInfos = List.of(columnInfo, new ColumnInfo(), new ColumnInfo());
        String[] strings = new String[1];
        when(notionService.getColumnsInfo()).thenReturn(columnInfos);

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
        User user = new User(123L, "firstName", false);
        Long chatId = 11L;
        Chat chat = new Chat(chatId, "type");
        String[] strings = new String[1];
        when(notionService.getColumnsInfo()).thenReturn(Collections.emptyList());

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
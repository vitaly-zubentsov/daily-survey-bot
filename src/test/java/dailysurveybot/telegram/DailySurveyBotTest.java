package dailysurveybot.telegram;

import dailysurveybot.config.TelegramConfig;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.commands.info.HelpCommand;
import dailysurveybot.telegram.commands.info.SettingsCommand;
import dailysurveybot.telegram.commands.operation.AddRowToTableCommand;
import dailysurveybot.telegram.commands.operation.StartCommand;
import dailysurveybot.telegram.entity.UserData;
import dailysurveybot.telegram.noncommands.NonCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailySurveyBotTest {

    @Mock
    private TelegramConfig telegramConfig;
    @Mock
    private StartCommand startCommand;
    @Mock
    private SettingsCommand settingsCommand;
    @Mock
    private HelpCommand helpCommand;
    @Mock
    private NonCommand nonCommand;
    @Mock
    private AddRowToTableCommand addRowToTableCommand;

    @Test
    @DisplayName("Получение сообщения от пользователя")
    void processNonCommandUpdate_UpdateIsMessage_sendAnswer() throws TelegramApiException {
        //given
        DailySurveyBot dailySurveyBot = spy(new DailySurveyBot(telegramConfig,
                startCommand,
                settingsCommand,
                helpCommand,
                nonCommand,
                addRowToTableCommand));
        ArgumentCaptor<SendMessage> messageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Update update = new Update();
        update.setMessage(new Message());
        String text = "text";
        update.getMessage().setText(text);
        update.getMessage().setFrom(new User());
        String userName = "userName";
        long userId = 321L;
        update.getMessage().getFrom().setId(userId);
        update.getMessage().getFrom().setUserName(userName);
        update.getMessage().setChat(new Chat());
        Long chatId = 123L;
        update.getMessage().getChat().setId(chatId);
        String answerToUser = "Answer to user";
        SendMessage answer = new SendMessage();
        answer.setText(answerToUser);
        answer.setChatId(chatId.toString());
        when(nonCommand.execute(chatId, userId, userName, text)).thenReturn(answer);

        //when
        dailySurveyBot.processNonCommandUpdate(update);

        //then
        verify(dailySurveyBot, times(1)).execute(messageArgumentCaptor.capture());
        SendMessage sendMessage = messageArgumentCaptor.getValue();
        assertEquals(answerToUser, sendMessage.getText());
        assertEquals(chatId.toString(), sendMessage.getChatId());
    }

    @Test
    @DisplayName("Получение CallbackQuery от пользователя")
    void processNonCommandUpdate_UpdateIsCallbackQuery_SendAnswerClearInlineKeyboard() throws TelegramApiException {
        //given
        DailySurveyBot dailySurveyBot = spy(new DailySurveyBot(telegramConfig,
                startCommand,
                settingsCommand,
                helpCommand,
                nonCommand,
                addRowToTableCommand));
        ArgumentCaptor<SendMessage> messageArgumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        String text = "text";
        update.getCallbackQuery().setData(text);
        update.getCallbackQuery().setFrom(new User());
        String userName = "userName";
        long userId = 321L;
        update.getCallbackQuery().getFrom().setId(userId);
        update.getCallbackQuery().getFrom().setUserName(userName);
        Long chatId = 123L;
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(chatId);
        String answerToUser = "Answer to user";
        SendMessage answer = new SendMessage();
        answer.setText(answerToUser);
        answer.setChatId(chatId.toString());
        when(nonCommand.execute(chatId, userId, userName, text)).thenReturn(answer);

        //when
        dailySurveyBot.processNonCommandUpdate(update);

        //then
        verify(dailySurveyBot, times(1)).execute(messageArgumentCaptor.capture());
        SendMessage sendMessage = messageArgumentCaptor.getValue();
        assertEquals(answerToUser, sendMessage.getText());
        assertEquals(chatId.toString(), sendMessage.getChatId());
    }

    @Test
    @DisplayName("Получение CallbackQuery от пользователя, проверка удаления клавитуры")
    void processNonCommandUpdate_UpdateIsCallbackQueryCheckClearInlineKeyboard_SendAnswerClearInlineKeyboard() throws TelegramApiException {
        //given
        DailySurveyBot dailySurveyBot = spy(new DailySurveyBot(telegramConfig,
                startCommand,
                settingsCommand,
                helpCommand,
                nonCommand,
                addRowToTableCommand));
        ArgumentCaptor<EditMessageReplyMarkup> editMessageReplyMarkupArgumentCaptor = ArgumentCaptor.forClass(EditMessageReplyMarkup.class);
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        String text = "text";
        update.getCallbackQuery().setData(text);
        update.getCallbackQuery().setFrom(new User());
        String userName = "userName";
        update.getCallbackQuery().getFrom().setUserName(userName);
        long userId = 321L;
        update.getCallbackQuery().getFrom().setId(userId);
        Long chatId = 123L;
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(chatId);
        SendMessage answer = new SendMessage();
        when(nonCommand.execute(chatId, userId, userName, text)).thenReturn(answer);
        Message messageFromTelegram = new Message();
        messageFromTelegram.setMessageId(321);
        doReturn(messageFromTelegram).when(dailySurveyBot).execute(any(SendMessage.class));

        //when
        dailySurveyBot.processNonCommandUpdate(update);

        //then
        verify(dailySurveyBot, times(2)).execute(editMessageReplyMarkupArgumentCaptor.capture());
        EditMessageReplyMarkup editMessageReplyMarkup = editMessageReplyMarkupArgumentCaptor.getValue();
        assertEquals(320, editMessageReplyMarkup.getMessageId());
        assertEquals(chatId.toString(), editMessageReplyMarkup.getChatId());
    }

    @Test
    @DisplayName("Получение имени бота")
    void getBotUsername_ReturnBotUserName() {
        //given
        String botName = "botName";
        TelegramConfig telegramConfig = new TelegramConfig(botName, "botToken", 1L, 1L);
        DailySurveyBot dailySurveyBot = spy(new DailySurveyBot(telegramConfig,
                startCommand,
                settingsCommand,
                helpCommand,
                nonCommand,
                addRowToTableCommand));

        //when then
        assertEquals(botName, dailySurveyBot.getBotUsername());
    }

    @Test
    @DisplayName("Получение токена бота")
    void getBotToken_ReturnBotToken() {
        //given
        String botToken = "botToken";
        TelegramConfig telegramConfig = new TelegramConfig("botName", botToken, 1L, 1L);
        DailySurveyBot dailySurveyBot = spy(new DailySurveyBot(telegramConfig,
                startCommand,
                settingsCommand,
                helpCommand,
                nonCommand,
                addRowToTableCommand));

        //when then
        assertEquals(botToken, dailySurveyBot.getBotToken());
    }

    @Test
    @DisplayName("Получение данных пользователя")
    void getUserData_UserDataExist_ReturnUserData() {
        //given
        Long userId = 123L;
        UserData userData = DailySurveyBot.getUserData(userId);
        int filledColumnsCounter = 5;
        userData.setFilledColumnsCounter(filledColumnsCounter);
        ColumnInfo columnInfo = new ColumnInfo();
        userData.getColumnInfoList().add(columnInfo);

        //when
        UserData userDataAfter = DailySurveyBot.getUserData(userId);

        //then
        assertNotNull(userDataAfter);
        assertEquals(userData, userDataAfter);
    }

    @Test
    @DisplayName("Получение данных пользователя, данные пользователя пусты")
    void getUserData_UserDataExist_ReturnDefaultData() {
        //given when
        UserData userData = DailySurveyBot.getUserData(13L);

        //then
        assertNotNull(userData);
        assertEquals(0, userData.getFilledColumnsCounter());
        assertNotNull(userData.getColumnInfoList());
        assertTrue(userData.getColumnInfoList().isEmpty());
    }

}
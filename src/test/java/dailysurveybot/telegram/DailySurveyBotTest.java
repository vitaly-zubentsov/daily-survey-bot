package dailysurveybot.telegram;

import dailysurveybot.config.TelegramConfig;
import dailysurveybot.notion.model.api.ColumnInfo;
import dailysurveybot.telegram.commands.AddRowToTableCommand;
import dailysurveybot.telegram.commands.HelpCommand;
import dailysurveybot.telegram.commands.SettingsCommand;
import dailysurveybot.telegram.commands.StartCommand;
import dailysurveybot.telegram.entity.UserData;
import dailysurveybot.telegram.noncommands.NonCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DailySurveyBotTest {

    @InjectMocks
    DailySurveyBot dailySurveyBot;

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
    @DisplayName("Отправка сообщения от пользователя")
    void processNonCommandUpdate_setAnswer() throws TelegramApiException {
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
        update.getMessage().getFrom().setUserName(userName);
        update.getMessage().setChat(new Chat());
        Long chatId = 123L;
        update.getMessage().getChat().setId(chatId);
        String answerToUser = "Answer to user";
        when(nonCommand.execute(chatId, userName, text)).thenReturn(answerToUser);

        //when
        dailySurveyBot.processNonCommandUpdate(update);

        //then
        verify(dailySurveyBot, times(1)).execute(messageArgumentCaptor.capture());
        SendMessage sendMessage = messageArgumentCaptor.getValue();
        assertEquals(answerToUser, sendMessage.getText());
        assertEquals(chatId.toString(), sendMessage.getChatId());
    }

    @Test
    @DisplayName("Получение имени бота")
    void getBotUsername_ReturnBotUserName() {
        //given
        String botName = "botName";
        TelegramConfig telegramConfig = new TelegramConfig(botName, "botToken");
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
        TelegramConfig telegramConfig = new TelegramConfig("botName", botToken);
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
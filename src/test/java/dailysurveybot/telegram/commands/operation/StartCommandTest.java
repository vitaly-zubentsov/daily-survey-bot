package dailysurveybot.telegram.commands.operation;

import dailysurveybot.notion.NotionService;
import dailysurveybot.telegram.DailySurveyBot;
import dailysurveybot.telegram.repos.UserRepo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Проверка StartCommand")
class StartCommandTest {


    @InjectMocks
    private StartCommand startCommand;

    @Mock
    private NotionService notionService;
    @Mock
    private DailySurveyBot dailySurveyBot;
    @Mock
    private UserRepo userRepo;

    @Test
    @DisplayName("Запуск сохранения настроек")
    void execute_saveUserAndSendAnswer() throws TelegramApiException {
        //given
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Long userId = 123L;
        User user = new User(userId, "firstName", false);
        Long chatId = 11L;
        Chat chat = new Chat(chatId, "type");
        String[] strings = new String[1];
        dailysurveybot.telegram.entity.User userFromDb = new dailysurveybot.telegram.entity.User();
        userFromDb.setFilled(false);
        userFromDb.setId(userId);
        userFromDb.setChatId(chatId);

        //when
        startCommand.execute(dailySurveyBot, user, chat, strings);

        //then
        verify(userRepo, only()).save(userFromDb);
        verify(dailySurveyBot, only()).execute(argumentCaptor.capture());
        SendMessage sendMessage = argumentCaptor.getValue();
        assertEquals(chatId.toString(), sendMessage.getChatId());
        assertTrue(sendMessage.getText().contains("Приветствую! Я могу заполнять таблицу в not"));
        assertEquals(ParseMode.MARKDOWN, sendMessage.getParseMode());
        assertTrue(sendMessage.getDisableWebPagePreview());
        assertNull(sendMessage.getReplyMarkup());
    }

}
package dailysurveybot.telegram.handlers;

import dailysurveybot.telegram.constants.BotMessageEnum;
import dailysurveybot.telegram.constants.ButtonNameEnum;
import dailysurveybot.telegram.entity.Settings;
import dailysurveybot.telegram.keyboards.InlineKeyboardMaker;
import dailysurveybot.telegram.keyboards.ReplyKeyboardMaker;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;

import static dailysurveybot.Utils.getUserName;

@Component
public class MessageHandler {
    private static final String START_COMMAND = "/start";
    private static final String HELP_COMMAND = "/help";

    private final ReplyKeyboardMaker replyKeyboardMaker;
    private final InlineKeyboardMaker inlineKeyboardMaker;

    public MessageHandler(ReplyKeyboardMaker replyKeyboardMaker,
                          InlineKeyboardMaker inlineKeyboardMaker) {
        this.replyKeyboardMaker = replyKeyboardMaker;
        this.inlineKeyboardMaker = inlineKeyboardMaker;
    }

    public BotApiMethod<? extends Serializable> answerMessage(Message message) {
        final String chatId = message.getChatId().toString();
        final String inputText = message.getText();

        if (inputText == null) {
            throw new IllegalArgumentException();
        } else if (START_COMMAND.equals(inputText)) {
            return getStartMessage(chatId);
        } else if (HELP_COMMAND.equals(inputText)) {
            return new SendMessage(chatId, getUserName(message.getFrom()) + BotMessageEnum.HELP_MESSAGE.getMessage());
        } else if (ButtonNameEnum.GREETINGS_BUTTON.getButtonName().equals(inputText)) {
            return getGreetingsMessage(message.getChatId());
        } else if (ButtonNameEnum.SETTING_BUTTON.getButtonName().equals(inputText)) {
            return getSettingMessage(chatId);
        } else {
            return new SendMessage(chatId, BotMessageEnum.NON_COMMAND_MESSAGE.getMessage());
        }
    }

    private SendMessage getStartMessage(String chatId) {
        final SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.START_MESSAGE.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

    private SendMessage getGreetingsMessage(Long chatId) {
        final Settings settings = SettingsHandler.getUserSettings(chatId);
        final SendMessage sendMessage = new SendMessage(chatId.toString(), settings.getHelloWorldAnswer());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(replyKeyboardMaker.getMainMenuKeyboard());
        return sendMessage;
    }

    private SendMessage getSettingMessage(String chatId) {
        final SendMessage sendMessage = new SendMessage(chatId, BotMessageEnum.SETTINGS_MESSAGE.getMessage());
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(inlineKeyboardMaker.getInlineMessageButtons());
        return sendMessage;
    }
}
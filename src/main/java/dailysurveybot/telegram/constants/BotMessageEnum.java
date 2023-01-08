package dailysurveybot.telegram.constants;

public enum BotMessageEnum {
    //ответы на команды с клавиатуры
    START_MESSAGE("\uD83D\uDC4B Привет, я helloWorld бот , и я почти ничего не умею, но могу поздороваться с Вами\n\n" +
            "Воспользуйтесь клавиатурой, чтобы начать работу\uD83D\uDC47"),
    NON_COMMAND_MESSAGE("Пожалуйста, воспользуйтесь клавиатурой или обратитесь к /help\uD83D\uDC47"),
    SETTINGS_MESSAGE("Выбирете приветствие для бота"),
    HELP_MESSAGE(" попробуйте все же воспользоваться клавиатурой\uD83D\uDC47"),
    ;

    private final String message;

    BotMessageEnum(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

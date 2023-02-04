package dailysurveybot.telegram.constants;

public enum CommandsEnum {
    START("start", "Старт"),
    SETTINGS("settings", "Отображениие настроек"),
    HELP("help", "Помощь"),
    ADD_ROW_TO_TABLE("t", "Заполнение строки таблицы");

    private final String identifier;
    private final String description;

    CommandsEnum(String identifier, String description) {
        this.identifier = identifier;
        this.description = description;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }
}

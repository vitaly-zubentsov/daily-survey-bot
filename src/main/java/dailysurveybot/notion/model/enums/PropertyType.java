package dailysurveybot.notion.model.enums;

public enum PropertyType {
    TITLE("title"),
    RICH_TEXT("rich_text"),
    SELECT("select");

    private final String value;

    PropertyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

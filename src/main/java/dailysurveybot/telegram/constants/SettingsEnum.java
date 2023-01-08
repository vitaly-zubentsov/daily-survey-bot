package dailysurveybot.telegram.constants;

public enum SettingsEnum {
    HI_ENGLISH("Hi", "english"),
    HI_RUSSIA("Привет", "russia"),
    HI_GEORGIAN("Гомарджоба", "georgia"),
    HI_PROGRAMMERS("Hello World!", "programmers");

    private final String text;


    private final String data;

    SettingsEnum(String text, String data) {
        this.text = text;
        this.data = data;
    }

    public String getText() {
        return text;
    }

    public String getData() {
        return data;
    }
}

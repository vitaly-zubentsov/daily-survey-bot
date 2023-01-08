package dailysurveybot.telegram.constants;

/**
 * Названия кнопок основной клавиатуры
 */
public enum ButtonNameEnum {
    GREETINGS_BUTTON("Поздороваться"),
    SETTING_BUTTON("Настройки");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}

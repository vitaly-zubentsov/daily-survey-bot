package dailysurveybot.telegram.constants;

/**
 * Названия кнопок основной клавиатуры
 */
public enum ButtonNameEnum {
    SEND_NEW_ROW_TO_NOTION_BUTTON("Добавить строку"),
    SETTING_BUTTON("Настройки");

    private final String buttonName;

    ButtonNameEnum(String buttonName) {
        this.buttonName = buttonName;
    }

    public String getButtonName() {
        return buttonName;
    }
}

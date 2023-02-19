package dailysurveybot.notion.model;

import com.google.common.base.Objects;

/**
 * Данные о заголовке(Title) БД. В notion один столбец БД обязательно должен быть с типом Title
 */
public class Title {

    private Text text;

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Title title = (Title) o;
        return Objects.equal(text, title.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }

    @Override
    public String toString() {
        return "Title{" +
                "text=" + text +
                '}';
    }
}

package dailysurveybot.notion.model;

import com.google.common.base.Objects;

/**
 * Данные rtf
 */
public class RichText {

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
        RichText richText = (RichText) o;
        return Objects.equal(text, richText.text);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(text);
    }

    @Override
    public String toString() {
        return "RichText{" +
                "text=" + text +
                '}';
    }
}

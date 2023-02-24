package dailysurveybot.notion.model;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.Objects;

/**
 * Текст
 */
@JsonRootName(value = "text")
public class Text {

    private String content;

    public String getContent() {
        return content;
    }

    public Text() {
    }

    public Text(String content) {
        this.content = content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Text text = (Text) o;
        return Objects.equal(content, text.content);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(content);
    }

    @Override
    public String toString() {
        return "Text{" +
                "content='" + content + '\'' +
                '}';
    }
}

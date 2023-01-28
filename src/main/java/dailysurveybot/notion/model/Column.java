package dailysurveybot.notion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;

/**
 * Класс содержащий описание колонки таблицы
 */
public class Column {

    private String id;
    private String name;
    private String type;
    @JsonProperty("select")
    private JsonNode select;
    @JsonProperty("created_time")
    private JsonNode createdTime;
    private Object title;
    @JsonProperty("rich_text")
    private Object richText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JsonNode getSelect() {
        return select;
    }

    public void setSelect(JsonNode select) {
        this.select = select;
    }

    public JsonNode getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(JsonNode createdTime) {
        this.createdTime = createdTime;
    }

    public Object getTitle() {
        return title;
    }

    public void setTitle(Object title) {
        this.title = title;
    }

    public Object getRichText() {
        return richText;
    }

    public void setRichText(Object richText) {
        this.richText = richText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Column column = (Column) o;
        return Objects.equal(id, column.id)
                && Objects.equal(name, column.name)
                && Objects.equal(type, column.type)
                && Objects.equal(select, column.select)
                && Objects.equal(createdTime, column.createdTime)
                && Objects.equal(title, column.title)
                && Objects.equal(richText, column.richText);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, type, select, createdTime, title, richText);
    }

    @Override
    public String toString() {
        return "Column{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", select=" + select +
                ", createdTime=" + createdTime +
                ", title=" + title +
                ", richText=" + richText +
                '}';
    }
}

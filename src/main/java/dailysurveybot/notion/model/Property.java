package dailysurveybot.notion.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Objects;

import java.util.List;

/**
 * Данные о колонке таблицы
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Property {

    private String id;
    private String name;
    private String type;
    private Select select;
    @JsonProperty("created_time")
    private JsonNode createdTime;
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<Title> title;
    @JsonProperty("rich_text")
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<RichText> richTexts;

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

    public Select getSelect() {
        return select;
    }

    public void setSelect(Select select) {
        this.select = select;
    }

    public JsonNode getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(JsonNode createdTime) {
        this.createdTime = createdTime;
    }

    public List<Title> getTitle() {
        return title;
    }

    public void setTitle(List<Title> title) {
        this.title = title;
    }

    public List<RichText> getRichTexts() {
        return richTexts;
    }

    public void setRichTexts(List<RichText> richTexts) {
        this.richTexts = richTexts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return Objects.equal(id, property.id)
                && Objects.equal(name, property.name)
                && Objects.equal(type, property.type)
                && Objects.equal(select, property.select)
                && Objects.equal(createdTime, property.createdTime)
                && Objects.equal(title, property.title)
                && Objects.equal(richTexts, property.richTexts);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id,
                name,
                type,
                select,
                createdTime,
                title,
                richTexts);
    }

    @Override
    public String toString() {
        return "Property{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", select=" + select +
                ", createdTime=" + createdTime +
                ", title=" + title +
                ", richTexts=" + richTexts +
                '}';
    }
}

package dailysurveybot.notion.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

import java.util.Map;

/**
 * Таблица со столбцамии, в терминологии notion Database
 */
@JsonIgnoreProperties(value = {"description", "object", "id", "cover", "icon", "icon", "last_edited_by", "created_by", "last_edited_time", "created_time", "title", "is_inline", "parent", "url", "archived"})
public class Database {

    private Map<String, Property> properties;

    @JsonAnyGetter
    public Map<String, Property> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Property> properties) {
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Database database = (Database) o;
        return Objects.equal(properties, database.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(properties);
    }

    @Override
    public String toString() {
        return "Database{" +
                "properties=" + properties +
                '}';
    }
}

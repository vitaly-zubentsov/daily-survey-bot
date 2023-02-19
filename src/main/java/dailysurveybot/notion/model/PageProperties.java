package dailysurveybot.notion.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.google.common.base.Objects;

import java.util.Map;

/**
 * Свойства строки, содержит данные о колонках таблицы
 */
public class PageProperties {

    /**
     * Свойства столбцов таблицы, ключ - имя строки
     */
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
        PageProperties that = (PageProperties) o;
        return Objects.equal(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(properties);
    }

    @Override
    public String toString() {
        return "PageProperties{" +
                "properties=" + properties +
                '}';
    }
}

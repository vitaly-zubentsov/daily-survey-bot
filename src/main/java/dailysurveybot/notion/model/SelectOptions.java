package dailysurveybot.notion.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

/**
 * Опция для выбора из выпадающего списка(select)
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SelectOptions {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SelectOptions that = (SelectOptions) o;
        return Objects.equal(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

    @Override
    public String toString() {
        return "SelectOptions{" +
                "name='" + name + '\'' +
                '}';
    }
}

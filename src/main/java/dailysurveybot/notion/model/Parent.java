package dailysurveybot.notion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * Родитель page
 */
public class Parent {

    /**
     * Идентификатор базы данных, к которой принадлежит строка
     */
    @JsonProperty("database_id")
    private String databaseId;

    public String getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(String databaseId) {
        this.databaseId = databaseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Parent parent = (Parent) o;
        return Objects.equal(databaseId, parent.databaseId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(databaseId);
    }

    @Override
    public String toString() {
        return "Parent{" +
                "databaseId='" + databaseId + '\'' +
                '}';
    }
}

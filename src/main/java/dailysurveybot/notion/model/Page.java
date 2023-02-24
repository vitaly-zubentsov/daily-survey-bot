package dailysurveybot.notion.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * Строка таблицы, в терминологии notion - page
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Page {

    private Parent parent;

    @JsonProperty("properties")
    private PageProperties pageProperties;

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public PageProperties getPageProperties() {
        return pageProperties;
    }

    public void setPageProperties(PageProperties pageProperties) {
        this.pageProperties = pageProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Page page = (Page) o;
        return Objects.equal(parent, page.parent)
                && Objects.equal(pageProperties, page.pageProperties);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(parent,
                pageProperties);
    }

    @Override
    public String toString() {
        return "Page{" +
                "parent=" + parent +
                ", pageProperties=" + pageProperties +
                '}';
    }
}

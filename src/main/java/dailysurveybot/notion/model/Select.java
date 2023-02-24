package dailysurveybot.notion.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.List;

/**
 * Список опций для выбора
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Select {

    @JsonProperty("options")
    private List<SelectOptions> selectOptions;

    private String name;

    public List<SelectOptions> getSelectOptions() {
        return selectOptions;
    }

    public void setSelectOptions(List<SelectOptions> selectOptions) {
        this.selectOptions = selectOptions;
    }

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
        Select select = (Select) o;
        return Objects.equal(selectOptions, select.selectOptions)
                && Objects.equal(name, select.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(selectOptions, name);
    }
}

package dailysurveybot.notion.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

import java.util.List;

/**
 * Список опций для выбора
 */
public class Select {

    @JsonProperty("options")
    private List<SelectOptions> selectOptions;

    public List<SelectOptions> getSelectOptions() {
        return selectOptions;
    }

    public void setSelectOptions(List<SelectOptions> selectOptions) {
        this.selectOptions = selectOptions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Select select = (Select) o;
        return Objects.equal(selectOptions, select.selectOptions);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(selectOptions);
    }

    @Override
    public String toString() {
        return "Select{" +
                "selectOptions=" + selectOptions +
                '}';
    }
}

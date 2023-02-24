package dailysurveybot.notion.model.api;

import com.google.common.base.Objects;

import java.util.List;

public class ColumnInfo {

    private String name;
    private String type;
    private List<String> selectOptions;
    private String textFromUser;

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

    public List<String> getSelectOptions() {
        return selectOptions;
    }

    public void setSelectOptions(List<String> selectOptions) {
        this.selectOptions = selectOptions;
    }

    public String getTextFromUser() {
        return textFromUser;
    }

    public void setTextFromUser(String textFromUser) {
        this.textFromUser = textFromUser;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnInfo that = (ColumnInfo) o;
        return Objects.equal(name, that.name)
                && Objects.equal(type, that.type)
                && Objects.equal(selectOptions, that.selectOptions)
                && Objects.equal(textFromUser, that.textFromUser);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name,
                type,
                selectOptions,
                textFromUser);
    }

    @Override
    public String toString() {
        return "ColumnsInfo{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", selectOptions=" + selectOptions +
                ", textFromUser='" + textFromUser + '\'' +
                '}';
    }
}

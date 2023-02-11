package dailysurveybot.telegram.entity;

import com.google.common.base.Objects;

import java.util.List;

/**
 * Класс содержащий дынные пользователя
 */
public class UserData {

    //Имена колонок в заполняемой таблице
    private List<String> columnsForFill;
    //Значения введённые пользователем для заполнения таблицы
    private List<String> valuesForFill;
    //Счетчик заполненых полей заполняемой таблицы
    private int filledColumnsCounter;

    public UserData(List<String> columnsForFill, List<String> valuesForFill, int filledColumnsCounter) {
        this.columnsForFill = columnsForFill;
        this.valuesForFill = valuesForFill;
        this.filledColumnsCounter = filledColumnsCounter;
    }

    public List<String> getColumnsForFill() {
        return columnsForFill;
    }

    public void setColumnsForFill(List<String> columnsForFill) {
        this.columnsForFill = columnsForFill;
    }

    public List<String> getValuesForFill() {
        return valuesForFill;
    }

    public void setValuesForFill(List<String> valuesForFill) {
        this.valuesForFill = valuesForFill;
    }

    public int getFilledColumnsCounter() {
        return filledColumnsCounter;
    }

    public void setFilledColumnsCounter(int filledColumnsCounter) {
        this.filledColumnsCounter = filledColumnsCounter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserData userData = (UserData) o;
        return filledColumnsCounter == userData.filledColumnsCounter
                && Objects.equal(columnsForFill, userData.columnsForFill)
                && Objects.equal(valuesForFill, userData.valuesForFill);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(columnsForFill, valuesForFill, filledColumnsCounter);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "columnsForFill=" + columnsForFill +
                ", valuesForFill=" + valuesForFill +
                ", filledColumnsCounter=" + filledColumnsCounter +
                '}';
    }
}

package dailysurveybot.telegram.entity;

import com.google.common.base.Objects;
import dailysurveybot.notion.model.api.ColumnInfo;

import java.util.List;

/**
 * Класс содержащий дынные пользователя
 */
public class UserData {

    //Имена колонок в заполняемой таблице
    private List<ColumnInfo> columnInfoList;
    //Счетчик заполненых полей заполняемой таблицы
    private int filledColumnsCounter;

    public UserData(List<ColumnInfo> columnInfoList, int filledColumnsCounter) {
        this.columnInfoList = columnInfoList;
        this.filledColumnsCounter = filledColumnsCounter;
    }

    public List<ColumnInfo> getColumnInfoList() {
        return columnInfoList;
    }

    public void setColumnInfoList(List<ColumnInfo> columnInfoList) {
        this.columnInfoList = columnInfoList;
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
                && Objects.equal(columnInfoList, userData.columnInfoList);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(columnInfoList, filledColumnsCounter);
    }

    @Override
    public String toString() {
        return "UserData{" +
                "columnInfoList=" + columnInfoList +
                ", filledColumnsCounter=" + filledColumnsCounter +
                '}';
    }
}

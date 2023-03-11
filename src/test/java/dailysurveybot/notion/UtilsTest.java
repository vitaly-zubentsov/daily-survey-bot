package dailysurveybot.notion;

import dailysurveybot.notion.model.api.ColumnInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Проверка utils")
class UtilsTest {

    @Test
    @DisplayName("Получение цифр из columnInfo, name содержит 1 цифру вначале")
    void extractNameOrderFromColumnInfo_NameHasOneDigit_ReturnOneDigit() {
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setNameWithOrderPrefix("2ColumnName");
        int number = Utils.extractNameOrderFromColumnInfo(columnInfo);
        assertEquals(2, number);
    }

    @Test
    @DisplayName("Получение цифр из columnInfo, name содержит более одной цифры вначале")
    void extractNameOrderFromColumnInfo_NameHasTreeDigit_ReturnTwoDigit() {
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setNameWithOrderPrefix("123456ColumnName");
        int number = Utils.extractNameOrderFromColumnInfo(columnInfo);
        assertEquals(123456, number);
    }

    @Test
    @DisplayName("Получение цифр из columnInfo, name не содержит цифр вначале")
    void extractNameOrderFromColumnInfo_NameHasNoDigit_ReturnZero() {
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setNameWithOrderPrefix("ColumnName");
        int number = Utils.extractNameOrderFromColumnInfo(columnInfo);
        assertEquals(Integer.MAX_VALUE, number);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    @DisplayName("Получение цифр из columnInfo, передали null вместо columnInfo")
    void extractNameOrderFromColumnInfo_ColumnInfoIsNull_ReturnError() {
        assertThrows(NullPointerException.class, () -> Utils.extractNameOrderFromColumnInfo(null));
    }

    @Test
    @DisplayName("Удаление префиксных цифр из названия колонки")
    void removePrefixNumberFromNameColumnInfo_ColumnInfoNameHasDigitalPrefix_ReturnColumn() {
        ColumnInfo columnInfo = new ColumnInfo();
        columnInfo.setNameWithOrderPrefix("123ColumnName");
        ColumnInfo columnInfoAfterRemove = Utils.addNameWithoutOrderDigits(columnInfo);
        assertEquals("ColumnName", columnInfoAfterRemove.getName());
    }

}
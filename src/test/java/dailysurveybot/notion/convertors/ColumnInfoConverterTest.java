package dailysurveybot.notion.convertors;

import dailysurveybot.notion.model.*;
import dailysurveybot.notion.model.api.ColumnInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dailysurveybot.notion.model.enums.PropertyType.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Проверка ColumnInfoConverter")
class ColumnInfoConverterTest {

    private final ColumnInfoConverter columnInfoConverter = new ColumnInfoConverter();

    @Test
    @DisplayName("Конвертация из database в columnInfoList")
    void convertDatabaseToColumnsInfoList_DatabaseHasProperties_ReturnColumnInfoList() {
        //given
        Database database = new Database();
        database.setProperties(new HashMap<>());
        Property propertyRichText = new Property();
        propertyRichText.setName("RichText");
        propertyRichText.setType(RICH_TEXT.getValue());
        Property propertyTitle = new Property();
        propertyTitle.setName("Title");
        propertyTitle.setType(TITLE.getValue());
        Property propertySelect = new Property();
        propertySelect.setName("Select");
        propertySelect.setType(SELECT.getValue());
        Select select = new Select();
        SelectOptions selectOptions1 = new SelectOptions();
        selectOptions1.setName("yes");
        SelectOptions selectOptions2 = new SelectOptions();
        selectOptions2.setName("no");
        select.setSelectOptions(List.of(selectOptions1, selectOptions2));
        propertySelect.setSelect(select);
        database.getProperties().put("RichText", propertyRichText);
        database.getProperties().put("Title", propertyTitle);
        database.getProperties().put("Select", propertySelect);

        //when
        List<ColumnInfo> columnInfos = columnInfoConverter.convertDatabaseToColumnsInfoList(database);

        //then
        assertEquals(3, columnInfos.size());
        assertEquals(propertyRichText.getName(), columnInfos.get(0).getName());
        assertEquals(propertyRichText.getType(), columnInfos.get(0).getType());
        assertNull(columnInfos.get(0).getTextFromUser());
        assertEquals(propertyTitle.getName(), columnInfos.get(2).getName());
        assertEquals(propertyTitle.getType(), columnInfos.get(2).getType());
        assertNull(columnInfos.get(2).getTextFromUser());
        assertEquals(propertySelect.getName(), columnInfos.get(1).getName());
        assertEquals(propertySelect.getType(), columnInfos.get(1).getType());
        assertEquals(selectOptions1.getName(), columnInfos.get(1).getSelectOptions().get(0));
        assertEquals(selectOptions2.getName(), columnInfos.get(1).getSelectOptions().get(1));
        assertNull(columnInfos.get(1).getTextFromUser());
    }

    @Test
    @DisplayName("Конвертация из database в columnInfoList, database не содержит описание столбцов таблицы")
    void convertDatabaseToColumnsInfoList_DatabaseHasNoProperties_ReturnError() {
        Database database = new Database();
        assertThrows(NullPointerException.class, () -> columnInfoConverter.convertDatabaseToColumnsInfoList(database));
    }

    @Test
    @DisplayName("Конвертация из columnInfoList в pageProperties")
    void convertColumnsInfoListToPageProperties_ReturnPageProperties() {
        //given
        ColumnInfo columnInfo1 = new ColumnInfo();
        columnInfo1.setName("RichText");
        columnInfo1.setType(RICH_TEXT.getValue());
        columnInfo1.setTextFromUser("first");
        ColumnInfo columnInfo2 = new ColumnInfo();
        columnInfo2.setName("Title");
        columnInfo2.setType(TITLE.getValue());
        columnInfo2.setTextFromUser("second");
        ColumnInfo columnInfo3 = new ColumnInfo();
        columnInfo3.setName("Select");
        columnInfo3.setType(SELECT.getValue());
        columnInfo3.setTextFromUser("third");
        ColumnInfo columnInfo4 = new ColumnInfo();
        columnInfo4.setName("Time");
        columnInfo4.setType("created_time");
        columnInfo4.setTextFromUser("bla-bla");
        List<ColumnInfo> columnInfos = List.of(columnInfo1, columnInfo2, columnInfo3, columnInfo4);

        //when
        PageProperties pageProperties = columnInfoConverter.convertColumnsInfoListToPageProperties(columnInfos);

        //then
        Map<String, Property> properties = pageProperties.getProperties();
        assertEquals(3, properties.size());
        Property richText = properties.get(columnInfo1.getName());
        assertEquals(columnInfo1.getTextFromUser(), richText.getRichTexts().get(0).getText().getContent());
        Property title = properties.get(columnInfo2.getName());
        assertEquals(columnInfo2.getTextFromUser(), title.getTitle().get(0).getText().getContent());
        Property select = properties.get(columnInfo3.getName());
        assertEquals(columnInfo3.getTextFromUser(), select.getSelect().getName());
    }

}
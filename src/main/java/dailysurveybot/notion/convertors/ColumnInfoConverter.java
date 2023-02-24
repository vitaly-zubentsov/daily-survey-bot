package dailysurveybot.notion.convertors;

import dailysurveybot.notion.model.*;
import dailysurveybot.notion.model.api.ColumnInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.*;

import static dailysurveybot.notion.model.enums.PropertyType.*;

@Component
public class ColumnInfoConverter {

    @Nonnull
    public List<ColumnInfo> convertDatabaseToColumnsInfoList(@Nonnull Database database) {
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        List<Property> properties = Objects.requireNonNull(database.getProperties()).values().stream().toList();
        for (Property property : properties) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setName(property.getName());
            columnInfo.setType(property.getType());
            if (property.getSelect() != null) {
                List<String> selectOptions = property.getSelect().getSelectOptions()
                        .stream().map(SelectOptions::getName).toList();
                columnInfo.setSelectOptions(selectOptions);
            }
            columnInfoList.add(columnInfo);
        }
        return columnInfoList;
    }

    @Nonnull
    public PageProperties convertColumnsInfoListToPageProperties(@Nonnull List<ColumnInfo> columnInfoList) {
        PageProperties pageProperties = new PageProperties();
        Map<String, Property> propertyMap = new HashMap<>();
        for (ColumnInfo columnInfo : columnInfoList) {
            Property property = new Property();
            if (RICH_TEXT.getValue().equals(columnInfo.getType())) {
                setTextToRichText(property, columnInfo.getTextFromUser());
            } else if (SELECT.getValue().equals(columnInfo.getType())) {
                setTextToSelect(property, columnInfo.getTextFromUser());
            } else if (TITLE.getValue().equals(columnInfo.getType())) {
                setTextToTitle(property, columnInfo.getTextFromUser());
            } else {
                continue;
            }
            propertyMap.put(columnInfo.getName(), property);
        }
        pageProperties.setProperties(propertyMap);
        return pageProperties;
    }

    private void setTextToRichText(Property property, String text) {
        List<RichText> richTexts = new ArrayList<>();
        RichText richText = new RichText();
        richText.setText(new Text(text));
        richTexts.add(richText);
        property.setRichTexts(richTexts);
    }

    private void setTextToTitle(Property titleProperty, String text) {
        Title title = new Title();
        title.setText(new Text(text));
        List<Title> titles = new ArrayList<>();
        titles.add(title);
        titleProperty.setTitle(titles);
    }

    private void setTextToSelect(Property titleProperty, String text) {
        Select select = new Select();
        select.setName(text);
        titleProperty.setSelect(select);
    }
}

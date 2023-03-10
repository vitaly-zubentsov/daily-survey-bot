package dailysurveybot.notion.convertors;

import dailysurveybot.notion.Utils;
import dailysurveybot.notion.model.*;
import dailysurveybot.notion.model.api.ColumnInfo;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import java.util.*;

import static dailysurveybot.notion.Utils.addNameWithoutOrderDigits;
import static dailysurveybot.notion.model.enums.PropertyType.*;

@Component
public class ColumnInfoConverter {

    @Nonnull
    public List<ColumnInfo> convertDatabaseToColumnsInfoList(@Nonnull Database database) {
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        List<Property> properties = Objects.requireNonNull(database.getProperties()).values().stream().toList();
        for (Property property : properties) {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setNameWithOrderPrefix(property.getName());
            columnInfo.setType(property.getType());
            if (property.getSelect() != null) {
                List<String> selectOptions = property.getSelect().getSelectOptions()
                        .stream().map(SelectOptions::getName).toList();
                columnInfo.setSelectOptions(selectOptions);
            }
            columnInfoList.add(columnInfo);
        }

        //TODO сделать возможность сортировки настраиваемой
        //Сортируем колонки по названиям в соотвествии с их числовыми префиксами,
        // вследствии того, что api notion возвращает колонки в произвольном порядке,
        // а не так как отображает на ui на сайте notion.
        columnInfoList.sort(Comparator.comparing(Utils::extractNameOrderFromColumnInfo));

        //Убираем цифры префикса из названий чтобы пользователь при заполнении видел только необходимую информацию
        for (ColumnInfo columnInfo : columnInfoList) {
            addNameWithoutOrderDigits(columnInfo);
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
            propertyMap.put(columnInfo.getNameWithOrderPrefix(), property);
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

    private void setTextToTitle(Property property, String text) {
        Title title = new Title();
        title.setText(new Text(text));
        List<Title> titles = new ArrayList<>();
        titles.add(title);
        property.setTitle(titles);
    }

    private void setTextToSelect(Property property, String text) {
        Select select = new Select();
        select.setName(text);
        property.setSelect(select);
    }
}

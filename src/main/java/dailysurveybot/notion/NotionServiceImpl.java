package dailysurveybot.notion;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dailysurveybot.config.NotionConfig;
import dailysurveybot.notion.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@Service
public class NotionServiceImpl implements NotionService {

    private static final String PAGES_URL = "pages";
    private static final String DATABASES_URL = "databases/";

    private final Logger logger = LoggerFactory.getLogger(NotionServiceImpl.class);

    private final ObjectMapper objectMapper;
    private final NotionConfig notionConfig;
    private final RestTemplate restTemplate;


    public NotionServiceImpl(NotionConfig notionConfig,
                             RestTemplate restTemplate) {
        this.notionConfig = notionConfig;
        this.restTemplate = restTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void saveRow(@Nonnull List<String> columnsForFill, @Nonnull List<String> valuesForFill) {
        logger.debug("Вызов saveRow");
        //заполняем строку
        if (columnsForFill.size() != valuesForFill.size()) {
            logger.warn("Ошибка при заполнении, columnsForFill :{}, valuesForFill : {}", columnsForFill, valuesForFill);
            throw new RuntimeException("Что то пошло не так"); //TODO сделать отдельный класс ошибок
        }
        Page page = new Page();
        page.setParent(new Parent());
        page.getParent().setDatabaseId(notionConfig.databaseId());
        HashMap<String, Property> stringColumnHashMap = new HashMap<>();
        //заполняем title
        Property titleProperty = getTitleProperty(valuesForFill.get(0));
        stringColumnHashMap.put(columnsForFill.get(0), titleProperty);
        //заполняем текстовые поля
        for (int i = 1; i < columnsForFill.size(); i++) {
            Property property = getPropertyWithRichText(valuesForFill, i);
            stringColumnHashMap.put(columnsForFill.get(i), property);
        }
        PageProperties pageProperties = new PageProperties();
        pageProperties.setProperties(stringColumnHashMap);
        page.setPageProperties(pageProperties);

        restTemplate.exchange(notionConfig.apiUrl() + PAGES_URL,
                HttpMethod.POST,
                new HttpEntity<>(page, getDefaultHeaders()),
                Page.class);

        logger.info("Завершение вызова saveRow");
    }

    @Override
    public List<Property> getProperties() throws IOException {
        String url = notionConfig.apiUrl() + DATABASES_URL + notionConfig.databaseId();
        logger.info("Вызов метода getColumns: {}", url);

        //Запрос на получение данных о таблице в notion
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(getDefaultHeaders()),
                String.class);

        //Получаем из ответа данные о свойствах колонок таблицы
        JsonNode jsonNode = objectMapper.readTree(response.getBody()).get("properties");
        List<Property> properties = new ArrayList<>();
        for (JsonNode next : jsonNode) {
            properties.add(objectMapper.treeToValue(next, Property.class));
        }

        //title являющийся первым столбцом в таблице в json'е находится в конце, ставим его вперед
        Property titleProperty = properties.get(properties.size() - 1);
        properties.remove(titleProperty);
        properties.add(0, titleProperty);

        return properties;
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + notionConfig.apiToken());
        headers.set("Notion-Version", notionConfig.apiVersion());
        return headers;
    }

    private Property getPropertyWithRichText(List<String> valuesForFill, int i) {
        Property property = new Property();
        List<RichText> richTexts = new ArrayList<>();
        RichText richText = new RichText();
        Text text = new Text();
        text.setContent(valuesForFill.get(i));
        richText.setText(text);
        richTexts.add(richText);
        property.setRichTexts(richTexts);
        return property;
    }

    private Property getTitleProperty(String text) {
        Property titleProperty = new Property();
        Title titleOne = new Title();
        Text textTitle = new Text();
        textTitle.setContent(text);
        titleOne.setText(textTitle);
        List<Title> title = new ArrayList<>();
        title.add(titleOne);
        titleProperty.setTitle(title);
        return titleProperty;
    }

}

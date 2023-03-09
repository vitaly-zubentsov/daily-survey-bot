package dailysurveybot.notion;

import dailysurveybot.config.NotionConfig;
import dailysurveybot.notion.convertors.ColumnInfoConverter;
import dailysurveybot.notion.model.Database;
import dailysurveybot.notion.model.Page;
import dailysurveybot.notion.model.PageProperties;
import dailysurveybot.notion.model.Parent;
import dailysurveybot.notion.model.api.ColumnInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;


@Service
public class NotionServiceImpl implements NotionService {

    private static final String PAGES_URL = "pages";
    private static final String DATABASES_URL = "databases/";

    private final Logger logger = LoggerFactory.getLogger(NotionServiceImpl.class);

    private final NotionConfig notionConfig;
    private final RestTemplate restTemplate;
    private final ColumnInfoConverter columnInfoConverter;


    public NotionServiceImpl(NotionConfig notionConfig,
                             RestTemplate restTemplate,
                             ColumnInfoConverter columnInfoConverter) {
        this.notionConfig = notionConfig;
        this.restTemplate = restTemplate;
        this.columnInfoConverter = columnInfoConverter;
    }

    @Override
    public void saveRow(@Nonnull List<ColumnInfo> columnInfoList) {
        logger.debug("Вызов saveRow: {}", columnInfoList);

        restTemplate.exchange(notionConfig.apiUrl() + PAGES_URL,
                HttpMethod.POST,
                new HttpEntity<>(createPage(columnInfoList), getDefaultHeaders()),
                Page.class);

        logger.debug("Завершение вызова saveRow");
    }

    @Override
    public List<ColumnInfo> getColumnsInfo() {
        String url = notionConfig.apiUrl() + DATABASES_URL + notionConfig.databaseId();
        logger.debug("Вызов метода getColumnsInfo. url {}", url);

        //Запрос на получение данных о таблице в notion
        ResponseEntity<Database> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(getDefaultHeaders()),
                Database.class);
        logger.debug("getColumnsInfo получен ответ от сервера: {}", response.getBody());

        return columnInfoConverter.convertDatabaseToColumnsInfoList(requireNonNull(response.getBody()));
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + notionConfig.apiToken());
        headers.set("Notion-Version", notionConfig.apiVersion());
        return headers;
    }

    private Page createPage(List<ColumnInfo> columnInfoList) {
        Page page = new Page();

        page.setParent(new Parent());
        page.getParent().setDatabaseId(notionConfig.databaseId());

        PageProperties pageProperties = columnInfoConverter.convertColumnsInfoListToPageProperties(columnInfoList);
        page.setPageProperties(pageProperties);

        return page;
    }
}

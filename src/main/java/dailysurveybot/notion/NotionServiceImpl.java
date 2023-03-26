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

    private final RestTemplate restTemplate;
    private final ColumnInfoConverter columnInfoConverter;
    private final String apiUrl;
    private final String apiVersion;


    public NotionServiceImpl(NotionConfig notionConfig,
                             RestTemplate restTemplate,
                             ColumnInfoConverter columnInfoConverter) {
        apiUrl = notionConfig.apiUrl();
        apiVersion = notionConfig.apiVersion();
        this.restTemplate = restTemplate;
        this.columnInfoConverter = columnInfoConverter;
    }

    @Override
    public void saveRow(@Nonnull List<ColumnInfo> columnInfoList, @Nonnull String databaseId, @Nonnull String apiToken) {
        logger.debug("Вызов saveRow: {}", columnInfoList);

        restTemplate.exchange(apiUrl + PAGES_URL,
                HttpMethod.POST,
                new HttpEntity<>(createPage(columnInfoList, databaseId), getDefaultHeaders(apiToken)),
                Page.class);

        logger.debug("Завершение вызова saveRow");
    }

    @Override
    public List<ColumnInfo> getColumnsInfo(@Nonnull String databaseId, @Nonnull String apiToken) {
        String url = apiUrl + DATABASES_URL + databaseId;
        logger.debug("Вызов метода getColumnsInfo. url {}", url);

        //Запрос на получение данных о таблице в notion
        ResponseEntity<Database> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(getDefaultHeaders(apiToken)),
                Database.class);
        logger.debug("getColumnsInfo получен ответ от сервера: {}", response.getBody());

        return columnInfoConverter.convertDatabaseToColumnsInfoList(requireNonNull(response.getBody()));
    }

    private HttpHeaders getDefaultHeaders(@Nonnull String apiToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + apiToken);
        headers.set("Notion-Version", apiVersion);
        return headers;
    }

    private Page createPage(List<ColumnInfo> columnInfoList, String databaseId) {
        Page page = new Page();

        page.setParent(new Parent());
        page.getParent().setDatabaseId(databaseId);

        PageProperties pageProperties = columnInfoConverter.convertColumnsInfoListToPageProperties(columnInfoList);
        page.setPageProperties(pageProperties);

        return page;
    }
}

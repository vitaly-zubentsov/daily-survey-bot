package dailysurveybot.notion;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dailysurveybot.config.NotionConfig;
import dailysurveybot.notion.model.Column;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

@Service
public class NotionServiceImpl implements NotionService {

    private static final String BODY_TEMPLATE = "{\"parent\": { \"database_id\": \"%s\" },\"properties\": {\"title\": {\"title\": [{\"text\": {\"content\": \"%s\"}}]}}}";
    private static final String PAGES_URL = "pages";
    private static final String DATABASES_URL = "databases/";

    private final Logger logger = LoggerFactory.getLogger(NotionServiceImpl.class);

    private final NotionConfig notionConfig;
    private final RestTemplate restTemplate;


    public NotionServiceImpl(NotionConfig notionConfig,
                             RestTemplate restTemplate) {
        this.notionConfig = notionConfig;
        this.restTemplate = restTemplate;
    }

    @Override
    public void saveRow(String text) throws IOException, InterruptedException {
        logger.info("Вызов saveRow");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(notionConfig + PAGES_URL))
                .header("Authorization", "Bearer " + notionConfig.apiToken())
                .header("accept", "application/json")
                .header("Notion-Version", notionConfig.apiVersion())
                .header("content-type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(format(BODY_TEMPLATE, notionConfig.databaseId(), text)))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        logger.info("Завершение вызова saveRow");
    }

    @Override
    public List<Column> getColumns() throws IOException {
        String url = notionConfig.apiUrl() + DATABASES_URL + notionConfig.databaseId();
        logger.info("Вызов метода getColumns: {}", url);

        //Запрос на получение данных о таблице в notion
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(getDefaultHeaders()),
                String.class);

        //Получаем из ответа данные о свойствах колонок таблицы
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response.getBody()).get("properties");
        List<Column> columns = new ArrayList<>();
        for (JsonNode next : jsonNode) {
            columns.add(objectMapper.treeToValue(next, Column.class));
        }

        return columns;
    }

    private HttpHeaders getDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Bearer " + notionConfig.apiToken());
        headers.set("Notion-Version", notionConfig.apiVersion());
        return headers;
    }

}

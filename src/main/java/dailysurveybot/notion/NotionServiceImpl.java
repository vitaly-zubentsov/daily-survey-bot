package dailysurveybot.notion;


import dailysurveybot.config.NotionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.lang.String.format;

@Service
public class NotionServiceImpl implements NotionService {

    private static final String BODY_TEMPLATE = "{\"parent\": { \"database_id\": \"%s\" },\"properties\": {\"title\": {\"title\": [{\"text\": {\"content\": \"%s\"}}]}}}";
    private final Logger logger = LoggerFactory.getLogger(NotionServiceImpl.class);


    private final String notionApiUrl;
    private final String notionApiToken;
    private final String notionDatabaseId;

    public NotionServiceImpl(NotionConfig notionConfig) {
        this.notionApiUrl = notionConfig.getNotionApiUrl();
        this.notionApiToken = notionConfig.getNotionApiToken();
        this.notionDatabaseId = notionConfig.getNotionDatabaseId();
        logger.info("Успешно создан NotionServiceImpl, загруженны даныне: notionApiUrl = {}, notionApiToken = {} , notionDatabaseId = {}",
                notionApiUrl,
                notionApiToken,
                notionDatabaseId);
    }

    @Override
    public void saveRow(String text) throws IOException, InterruptedException {
        logger.info("вызов saveRow");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(notionApiUrl))
                .header("Authorization", "Bearer " + notionApiToken)
                .header("accept", "application/json")
                .header("Notion-Version", "2022-06-28")
                .header("content-type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString(format(BODY_TEMPLATE, notionDatabaseId, text)))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

    }
}

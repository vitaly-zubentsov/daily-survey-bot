package dailysurveybot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotionConfig {
    @Value("${notion.api-url}")
    private String notionApiUrl;
    @Value("${notion.api-token}")
    private String notionApiToken;
    @Value("${notion.database-id}")
    private String notionDatabaseId;

    public String getNotionApiUrl() {
        return notionApiUrl;
    }

    public String getNotionApiToken() {
        return notionApiToken;
    }

    public String getNotionDatabaseId() {
        return notionDatabaseId;
    }
}
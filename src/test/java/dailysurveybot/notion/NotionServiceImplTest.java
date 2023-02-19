package dailysurveybot.notion;

import dailysurveybot.config.NotionConfig;
import dailysurveybot.notion.model.Property;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static dailysurveybot.TestUtils.readFileAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class NotionServiceImplTest {

    @InjectMocks
    private NotionServiceImpl notionService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private NotionConfig notionConfig;

    @Test
    @DisplayName("Проверка получения списка колонок")
    void getPropertiesTest() throws Exception {
        String json = readFileAsString("database.json");
        String expected = "[Property{id='title', name='Что ты делал сегодня?', type='title', select=null, createdTime=null, title=[Title{text=null}], richTexts=null}, Property{id='%3Dexl', name='Время', type='created_time', select=null, createdTime={}, title=null, richTexts=null}, Property{id='MqVL', name='Зарядка', type='select', select=Select{selectOptions=[SelectOptions{name='да'}, SelectOptions{name='нет'}]}, createdTime=null, title=null, richTexts=null}, Property{id='eF%3AK', name='Сон', type='rich_text', select=null, createdTime=null, title=null, richTexts=[RichText{text=null}]}]";

        ResponseEntity<String> response = new ResponseEntity<>(json, HttpStatus.ACCEPTED);

        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(String.class)))
                .thenReturn(response);
        when(notionConfig.apiUrl()).thenReturn("https://url.ru");
        when(notionConfig.databaseId()).thenReturn("databaseId");
        when(notionConfig.apiToken()).thenReturn("token");
        when(notionConfig.apiVersion()).thenReturn("version");

        List<Property> propertyList = notionService.getProperties();

        assertEquals(expected, propertyList.toString());
    }

}

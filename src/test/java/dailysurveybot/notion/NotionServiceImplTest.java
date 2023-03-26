package dailysurveybot.notion;

import dailysurveybot.config.NotionConfig;
import dailysurveybot.notion.convertors.ColumnInfoConverter;
import dailysurveybot.notion.model.Database;
import dailysurveybot.notion.model.Page;
import dailysurveybot.notion.model.PageProperties;
import dailysurveybot.notion.model.api.ColumnInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@SuppressWarnings({"ConstantConditions", "rawtypes"})
@ExtendWith(MockitoExtension.class)
class NotionServiceImplTest {

    private static final String DATABASE_ID = "databaseId";
    private static final String API_TOKEN = "apiToken";

    private NotionServiceImpl notionService;
    @Mock
    private RestTemplate restTemplate;
    @Mock
    private NotionConfig notionConfig;
    @Mock
    private ColumnInfoConverter columnInfoConverter;

    @BeforeEach
    void init() {
        when(notionConfig.apiUrl()).thenReturn("https://url.ru/");
        when(notionConfig.apiVersion()).thenReturn("version");
        notionService = new NotionServiceImpl(notionConfig, restTemplate, columnInfoConverter);
    }

    @Test
    @DisplayName("Получение списка колонок")
    void getColumnsInfo_ResponseBodyNotNull_ReturnColumns() {
        //given
        ArgumentCaptor<HttpEntity> argumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        ResponseEntity<Database> response = new ResponseEntity<>(new Database(), HttpStatus.ACCEPTED);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Database.class)))
                .thenReturn(response);
        when(columnInfoConverter.convertDatabaseToColumnsInfoList(any())).thenReturn(List.of(new ColumnInfo()));

        //when
        List<ColumnInfo> columnsInfo = notionService.getColumnsInfo(DATABASE_ID, API_TOKEN);

        //then
        verify(restTemplate, only()).exchange(eq("https://url.ru/databases/databaseId"),
                eq(HttpMethod.GET),
                argumentCaptor.capture(),
                eq(Database.class));
        HttpHeaders headers = argumentCaptor.getValue().getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals(Collections.singletonList(MediaType.APPLICATION_JSON), headers.getAccept());
        assertEquals("Bearer apiToken", headers.get("Authorization").get(0));
        assertEquals("version", headers.get("Notion-Version").get(0));
        assertNotNull(columnsInfo);
    }

    @Test
    @DisplayName("Получение списка колонок, ответ не содержиит body")
    void getColumnsInfo_ResponseBodyNull_ReturnColumns() {
        //given
        ResponseEntity<Database> response = new ResponseEntity<>(null, HttpStatus.ACCEPTED);
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(Database.class)))
                .thenReturn(response);

        //when //then
        assertThrows(NullPointerException.class, () -> notionService.getColumnsInfo(DATABASE_ID, API_TOKEN));
    }

    @Test
    @DisplayName("Сохранение строки")
    void saveRow() {
        //given
        ArgumentCaptor<HttpEntity> argumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
        when(columnInfoConverter.convertColumnsInfoListToPageProperties(any())).thenReturn(new PageProperties());

        //when
        notionService.saveRow(List.of(new ColumnInfo()), DATABASE_ID, API_TOKEN);

        //then
        verify(restTemplate, only()).exchange(eq("https://url.ru/pages"),
                eq(HttpMethod.POST),
                argumentCaptor.capture(),
                eq(Page.class));
        HttpHeaders headers = argumentCaptor.getValue().getHeaders();
        assertEquals(MediaType.APPLICATION_JSON, headers.getContentType());
        assertEquals(Collections.singletonList(MediaType.APPLICATION_JSON), headers.getAccept());
        assertEquals("Bearer apiToken", headers.get("Authorization").get(0));
        assertEquals("version", headers.get("Notion-Version").get(0));
        assertTrue(argumentCaptor.getValue().hasBody());
    }

}

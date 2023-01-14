package dailysurveybot.notion;

import java.io.IOException;

public interface NotionService {

    void saveRow(String text) throws IOException, InterruptedException;
}

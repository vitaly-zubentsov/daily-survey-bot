package dailysurveybot.telegram;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Проверка utils")
class UtilsTest {

    public static final String USER_NAME = "userName";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    private User user;

    @BeforeEach
    public void init() {
        user = new User();
        user.setUserName(USER_NAME);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
    }

    @Test
    @DisplayName("Получение имени пользователя")
    void getUserName_UserNameNotNull_ReturnUserName() {
        String result = Utils.getUserName(user);
        assertEquals(USER_NAME, result);
    }

    @Test
    @DisplayName("Получение имени пользователя, при отсутствии userName")
    void getUserName_UserNameNull_ReturnFirstNameAndLastName() {
        user.setUserName(null);
        String result = Utils.getUserName(user);
        assertEquals(LAST_NAME + " " + FIRST_NAME, result);
    }

}
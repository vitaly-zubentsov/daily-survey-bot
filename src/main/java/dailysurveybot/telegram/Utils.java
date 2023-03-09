package dailysurveybot.telegram;

import org.telegram.telegrambots.meta.api.objects.User;

public class Utils {

    private Utils() {
    }

    /**
     * Формирование имени пользователя. Если заполнен никнейм, используем его. Если нет - используем фамилию и имя
     *
     * @param user пользователь
     * @return никнейм или фамалия и имя пользователя
     */
    public static String getUserName(User user) {
        return (user.getUserName() != null) ? user.getUserName() :
                String.format("%s %s", user.getLastName(), user.getFirstName());
    }
}
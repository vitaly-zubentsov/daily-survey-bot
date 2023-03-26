package dailysurveybot.telegram;

import dailysurveybot.telegram.exceptions.MandatoryFieldInUserFromDbIsEmpty;
import org.apache.commons.lang3.StringUtils;
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

    /**
     * Валидация данных пользователя
     *
     * @param userFromDb данные о пользователе полученные из БД
     */
    public static void checkMandatoryParam(dailysurveybot.telegram.entity.User userFromDb) {
        if (StringUtils.isBlank(userFromDb.getNotionDatabaseId())
                || StringUtils.isBlank(userFromDb.getNotionApiToken())) {
            throw new MandatoryFieldInUserFromDbIsEmpty();
        }
    }
}
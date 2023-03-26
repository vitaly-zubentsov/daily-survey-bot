package dailysurveybot.telegram.repos;

import dailysurveybot.telegram.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
}

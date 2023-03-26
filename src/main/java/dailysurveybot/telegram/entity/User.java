package dailysurveybot.telegram.entity;

import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "USERS")
public class User {

    @Id
    private Long id;
    @Column(name = "CHAT_ID", length = 50)
    private Long chatId;
    @Column(name = "NOTION_API_TOKEN", length = 50)
    private String notionApiToken;
    @Column(name = "NOTION_DATABASE_ID", length = 50)
    private String notionDatabaseId;
    @Column(name = "IS_FILLED")
    private Boolean isFilled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getNotionApiToken() {
        return notionApiToken;
    }

    public void setNotionApiToken(String notionApiToken) {
        this.notionApiToken = notionApiToken;
    }

    public String getNotionDatabaseId() {
        return notionDatabaseId;
    }

    public void setNotionDatabaseId(String notionDatabaseId) {
        this.notionDatabaseId = notionDatabaseId;
    }

    public Boolean getFilled() {
        return isFilled;
    }

    public void setFilled(Boolean filled) {
        isFilled = filled;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equal(id, user.id) && Objects.equal(chatId, user.chatId) && Objects.equal(notionApiToken, user.notionApiToken) && Objects.equal(notionDatabaseId, user.notionDatabaseId) && Objects.equal(isFilled, user.isFilled);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, chatId, notionApiToken, notionDatabaseId, isFilled);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", notionApiToken='" + notionApiToken + '\'' +
                ", notionDatabaseId='" + notionDatabaseId + '\'' +
                ", isFilled=" + isFilled +
                '}';
    }
}

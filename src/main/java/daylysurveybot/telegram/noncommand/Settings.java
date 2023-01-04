package daylysurveybot.telegram.noncommand;

import com.google.common.base.Objects;

public class Settings {

    private String helloWorldAnswer;

    public Settings(String helloWorldAnswer) {
        this.helloWorldAnswer = helloWorldAnswer;
    }

    public String getHelloWorldAnswer() {
        return helloWorldAnswer;
    }

    public void setHelloWorldAnswer(String helloWorldAnswer) {
        this.helloWorldAnswer = helloWorldAnswer;
    }

    @Override
    public String toString() {
        return "Settings{" +
                "helloWorldAnswer='" + helloWorldAnswer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Settings settings = (Settings) o;
        return Objects.equal(helloWorldAnswer, settings.helloWorldAnswer);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(helloWorldAnswer);
    }
}
package hexlet.code;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import static org.assertj.core.api.Assertions.assertThat;

class AppTest {

    Javalin app;

    @BeforeEach
    public void setUpMock() throws IOException {
        app = App.getApp();
    }

    @Test
    public void testMainPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get("/");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }

    @Test
    public void testNotFoundUrlById() {
        JavalinTest.test(app, (server, client) -> {
            client.delete("/test/delete/777");
            var response =  client.get("/urls/777");
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testNotCorrectUrl() {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=notCorrectUrl";
            var response = client.post("/urls", requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }


    @Test
    public void testNullUrlValidation() {

        JavalinTest.test(app, (server, client) -> {
            var response = client.post("/urls", "");
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Анализатор страниц");
        });
    }
}

package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.NamedRoutes;
import hexlet.code.util.Time;

import io.javalin.Javalin;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.StringJoiner;

public final class AppTest {
    private static Javalin app;
    private static MockWebServer mockServer;
    private static String urlName;
    private static final String HTML_PATH = "src/test/resources/index.html";

    public static String getContentOfHtmlFile() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(HTML_PATH));
        String lineOfFile = reader.readLine();
        var result = new StringJoiner("\n");

        while (lineOfFile != null) {
            result.add(lineOfFile);
            lineOfFile = reader.readLine();
        }
        return result.toString();
    }

    @BeforeAll
    public static void beforeAll() throws IOException {
        mockServer = new MockWebServer();
        urlName = mockServer.url("/").toString();
        var mockResponse = new MockResponse().setBody(getContentOfHtmlFile());
        mockServer.enqueue(mockResponse);
//        mockServer.start();
    }

    @BeforeEach
    public void beforeEach() throws SQLException, IOException {
        app = App.getApp();
    }

    @AfterAll
    public static void afterAll() throws IOException {
        mockServer.shutdown();
    }

    @Test
    public void testRootPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.rootPath());
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("Page analyzer");
        });
    }

    @Test
    public void testPagesPage() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlsPath());
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testCreatePage() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
            assertThat(UrlRepository.getEntities()).hasSize(1);
        });
    }

    @Test
    public void testCreateTheSamePage() throws SQLException {
        var url = new Url("https://www.example.com", Time.getCurrentTime());
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=https://www.example.com";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(response.body().string()).contains("https://www.example.com");
            assertThat(UrlRepository.getEntities()).hasSize(1);
        });
    }

    @Test
    public void testCreateIncorrectPage() throws SQLException {
        JavalinTest.test(app, (server, client) -> {
            var requestBody = "url=12345";
            var response = client.post(NamedRoutes.urlsPath(), requestBody);
            assertThat(response.code()).isEqualTo(200);
            assertThat(UrlRepository.getEntities()).hasSize(0);
        });
    }

    @Test
    public void testPagePage() throws SQLException {
        var url = new Url("https://www.example.com", Time.getCurrentTime());
        UrlRepository.save(url);
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);
        });
    }

    @Test
    public void testUrlNotFound() {
        JavalinTest.test(app, (server, client) -> {
            var response = client.get(NamedRoutes.urlPath(999999999L));
            assertThat(response.code()).isEqualTo(404);
        });
    }

    @Test
    public void testCheckUrl() throws IOException, SQLException {
        var url = new Url(urlName, Time.getCurrentTime());
        UrlRepository.save(url);

        JavalinTest.test(app, (server1, client) -> {
            var response = client.post(NamedRoutes.urlChecksPath(url.getId()));
            assertThat(response.code()).isEqualTo(200);

            var urlCheck = UrlCheckRepository.getLastCheck(url.getId()).get();
            var id = String.valueOf(urlCheck.getId());
            var statusCode = String.valueOf(urlCheck.getStatusCode());
            var title = urlCheck.getTitle();
            var h1 = urlCheck.getH1();
            var description = urlCheck.getDescription();

            assertThat(title).isEqualTo("This is a title");
            assertThat(h1).isEqualTo("This is a header");
            assertThat(description).isEqualTo("This is a description");
        });
    }
}

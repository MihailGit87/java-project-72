package hexlet.code;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.controller.UrlCheckController;
import hexlet.code.controller.UrlController;
import hexlet.code.controller.RootController;
import hexlet.code.repository.BaseRepository;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.javalin.rendering.template.JavalinJte;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.util.stream.Collectors;

@Slf4j
public final class App {

    public static void main(String[] args) throws IOException, SQLException {
        Javalin app = getApp();
        app.start(getPort());
    }

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    private static final String JDBC_URL_H2 = "jdbc:h2:mem:project";

    static String jdbcUrlCurrent = getJdbcDatabaseUrl();

    public static String getJdbcDatabaseUrl() {
        // Получаем значение переменной окружения JDBC_DATABASE_URL
        String jdbcUrl = System.getenv("JDBC_DATABASE_URL");

        // Если переменная окружения не установлена, устанавливаем значение по умолчанию
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            jdbcUrl = JDBC_URL_H2; // Значение по умолчанию
        }

        return jdbcUrl;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    public static Javalin getApp() throws IOException, SQLException {

        var hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrlCurrent);

        var dataSource = new HikariDataSource(hikariConfig);

        var inputStream = App.class.getClassLoader().getResourceAsStream("schema.sql");
        var reader = new BufferedReader(new InputStreamReader(inputStream));
        var sql = reader.lines().collect(Collectors.joining("\n"));

        log.info(sql);
        try (var connection = dataSource.getConnection();
             var statement = connection.createStatement()) {
            statement.execute(sql);
        }
        BaseRepository.dataSource = dataSource;

        var app = Javalin.create(config -> {
            config.plugins.enableDevLogging();
        });

        JavalinJte.init(createTemplateEngine());

        app.get(NamedRoutes.rootPath(), RootController::index);
        app.get(NamedRoutes.urlsPath(), UrlController::index);
        app.get(NamedRoutes.urlPath("{id}"), UrlController::show);
        app.post(NamedRoutes.urlsPath(), UrlController::create);
        app.post(NamedRoutes.urlChecksPath("{id}"), UrlCheckController::create);

        return app;
    }
}

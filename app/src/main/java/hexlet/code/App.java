package hexlet.code;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App {
    public static void main(String[] args) {
        var app = getApp();
    }

    public static Javalin getApp() {
        var app = Javalin.create(config -> config.plugins.enableDevLogging());

        app.get("/", ctx -> ctx.result("Hello World"))
                .start(7070);

        return app;
    }
}

package hexlet.code;

import io.javalin.Javalin;
import lombok.extern.slf4j.Slf4j;
import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import io.javalin.rendering.template.JavalinJte;
import gg.jte.resolve.ResourceCodeResolver;

@Slf4j
public class App {

    private static int getPort() {
        String port = System.getenv().getOrDefault("PORT", "7070");
        return Integer.valueOf(port);
    }

    public static void main(String[] args) {
        var app = getApp();

        app.start(getPort());
    }

    public static Javalin getApp() {
        var app = Javalin.create(config -> config.plugins.enableDevLogging());

        app.get("/", ctx -> ctx.render("index.jte"));
        app.get("/urls", ctx -> ctx.render("urls/show.jte"));
        JavalinJte.init(createTemplateEngine());
        return app;
    }

    private static TemplateEngine createTemplateEngine() {
        ClassLoader classLoader = App.class.getClassLoader();
        ResourceCodeResolver codeResolver = new ResourceCodeResolver("templates", classLoader);
        TemplateEngine templateEngine = TemplateEngine.create(codeResolver, ContentType.Html);
        return templateEngine;
    }
}

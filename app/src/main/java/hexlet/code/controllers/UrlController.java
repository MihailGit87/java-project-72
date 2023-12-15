package hexlet.code.controllers;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlsRepository;
import io.javalin.http.Handler;
import java.net.URL;

import static hexlet.code.repository.UrlsRepository.isUrlExists;

public class UrlController {

    public static Handler createUrl = ctx -> {
        var inputUrl = ctx.formParam("url");
        URL parsedUrl;
        try {
            parsedUrl = new URL(inputUrl);
        } catch (Exception e) {
            ctx.sessionAttribute("flash", "Некорректный адрес");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect("/");
            return;
        }

        String normalizedUrl = String
                .format(
                        "%s://%s%s",
                        parsedUrl.getProtocol(),
                        parsedUrl.getHost(),
                        parsedUrl.getPort() == -1 ? "" : ":" + parsedUrl.getPort()
                )
                .toLowerCase();

        if (isUrlExists(normalizedUrl)) {
            ctx.sessionAttribute("flash", "Страница уже существует");
            ctx.sessionAttribute("flash-type", "danger");
            ctx.redirect("/urls");
            return;
        }

        Url url = new Url(normalizedUrl);
        UrlsRepository.save(url);
        ctx.sessionAttribute("flash", "Страница успешно создана");
        ctx.sessionAttribute("flash-type", "success");
        ctx.redirect("/urls");
    };
}

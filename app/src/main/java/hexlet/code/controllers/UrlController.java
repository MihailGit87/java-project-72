package hexlet.code.controllers;

import hexlet.code.dto.UrlPage;
import hexlet.code.dto.UrlsPage;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlChecksRepository;
import hexlet.code.repository.UrlsRepository;
import io.javalin.http.Handler;
import io.javalin.http.NotFoundResponse;
import java.net.URL;
import java.util.List;
import java.util.Map;

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


    public static Handler getUrls = ctx -> {
        String flash = ctx.consumeSessionAttribute("flash");
        String flashType = ctx.consumeSessionAttribute("flash-type");
        flash =  flash == null ? "" : flash;
        flashType =  flashType == null ? "" : flashType;

        List<Url> urls = UrlsRepository.getUrls();
        Map<Long, UrlCheck> urlChecks = UrlChecksRepository.findLatestChecks();
        var urlsPage = new UrlsPage(urls, urlChecks);
        urlsPage.setFlashType(flashType);
        urlsPage.setFlash(flash);
        ctx.render("urls/index.jte",  Map.of("urlsPage", urlsPage));
    };

    public static Handler showUrl = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        String flash = ctx.consumeSessionAttribute("flash");
        String flashType = ctx.consumeSessionAttribute("flash-type");
        flash =  flash == null ? "" : flash;
        flashType =  flashType == null ? "" : flashType;
        Url url = UrlsRepository.getUrlById(id)
                .orElseThrow(() -> new NotFoundResponse("Url with id = " + id + " not found"));
        List<UrlCheck> urlChecks = UrlChecksRepository.getUrlChecks(url.getId());
        UrlPage urlPage = new UrlPage(url, urlChecks);
        urlPage.setFlash(flash);
        urlPage.setFlashType(flashType);

        ctx.render("urls/show.jte",
                Map.of("urlPage", urlPage));
    };

    public static Handler deleteUrl = ctx -> {
        long id = ctx.pathParamAsClass("id", Long.class).getOrDefault(null);
        UrlsRepository.deleteUrl(id);
    };
}

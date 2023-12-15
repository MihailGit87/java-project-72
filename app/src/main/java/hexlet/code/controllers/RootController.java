package hexlet.code.controllers;

import io.javalin.http.Handler;
import java.util.Map;

public class RootController {

    public static Handler index = ctx -> {
        String flash = ctx.consumeSessionAttribute("flash");
        String flashType = ctx.consumeSessionAttribute("flash-type");
        flash =  flash == null ? "" : flash;
        flashType =  flashType == null ? "" : flashType;
        ctx.render("index.jte", Map.of("flash", flash, "flashType", flashType));

    };
}

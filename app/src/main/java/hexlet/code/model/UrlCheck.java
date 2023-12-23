package hexlet.code.model;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
public class UrlCheck {

    public UrlCheck(int statusCode, String title, String h1, String description) {
        this.statusCode = statusCode;
        this.title = title;
        this.h1 = h1;
        this.description = description;
    }


    public UrlCheck(int statusCode, String description, long urlId, String title, String h1) {
        this.statusCode = statusCode;
        this.description = description;
        this.urlId = urlId;
        this.title = title;
        this.h1 = h1;
    }

    private long id;

    private Timestamp createdAt;

    private Integer statusCode;

    private String title;

    private String description;

    private long urlId;

    private String h1;


}

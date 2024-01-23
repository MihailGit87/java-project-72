package hexlet.code.dto.urls;

import hexlet.code.dto.BasePage;
import hexlet.code.model.UrlCheck;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@Getter
public final class UrlPage extends BasePage {
    private Long id;
    private String name;
    private Timestamp createdAt;
    private List<UrlCheck> urlChecks;
}

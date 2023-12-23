package hexlet.code.repository;

import hexlet.code.model.UrlCheck;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlChecksRepository extends BaseRepository {
    public static Boolean save(UrlCheck urlCheck) throws SQLException {
        var sql = """
            INSERT INTO url_checks (created_at, status_code, description, url_id, title, h1) VALUES (?, ?, ?, ?, ?, ?)
            """;
        var datetime = new Timestamp(System.currentTimeMillis());
        try (var conn = BaseRepository.dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setTimestamp(1, datetime);
            preparedStatement.setInt(2, urlCheck.getStatusCode());
            preparedStatement.setString(3, urlCheck.getDescription());
            preparedStatement.setLong(4, urlCheck.getUrlId());
            preparedStatement.setString(5, urlCheck.getTitle());
            preparedStatement.setString(6, urlCheck.getH1());

            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                urlCheck.setId(generatedKeys.getLong(1));
                return true;
            }
        }
        return false;
    }


    public static List<UrlCheck> getUrlChecks(long urlId) throws SQLException {
        var sql = "SELECT * FROM url_checks WHERE url_id=? ORDER BY id DESC LIMIT 30";
        try (var conn = BaseRepository.dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, urlId);
            ResultSet resultSet = preparedStatement.executeQuery();

            List<UrlCheck> urlChecks = new ArrayList<>();
            while (resultSet.next()) {
                // Получить данные из результата запроса
                int id = resultSet.getInt("id");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                int statusCode = resultSet.getInt("status_code");
                String description = resultSet.getString("description");
                String title = resultSet.getString("title");
                String h1 = resultSet.getString("h1");
                UrlCheck urlCheck = new UrlCheck(statusCode, description, urlId, title, h1);
                urlCheck.setId(id);
                urlCheck.setCreatedAt(createdAt);
                urlChecks.add(urlCheck);
                // Обработать полученные данные
            }
            return urlChecks;
        }
    }

    public static Map<Long, UrlCheck> findLatestChecks() throws SQLException {
        var sql = "SELECT DISTINCT ON (url_id) * from url_checks order by url_id DESC, id DESC";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            var resultSet = stmt.executeQuery();
            var result = new HashMap<Long, UrlCheck>();
            while (resultSet.next()) {
                var id = resultSet.getLong("id");
                var urlId = resultSet.getLong("url_id");
                var statusCode = resultSet.getInt("status_code");
                var title = resultSet.getString("title");
                var h1 = resultSet.getString("h1");
                var description = resultSet.getString("description");
                var createdAt = resultSet.getTimestamp("created_at");
                var check = new UrlCheck(statusCode, title, h1, description);
                check.setId(id);
                check.setUrlId(urlId);
                check.setCreatedAt(createdAt);
                result.put(urlId, check);
            }
            return result;
        }
    }
}

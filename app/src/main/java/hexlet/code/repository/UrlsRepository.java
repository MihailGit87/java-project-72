package hexlet.code.repository;

import hexlet.code.model.Url;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UrlsRepository extends BaseRepository {

    public static void save(Url url) throws SQLException {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        var datetime = new Timestamp(System.currentTimeMillis());
        url.setCreatedAt(datetime);
        try (var conn = BaseRepository.dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, url.getCreatedAt());
            preparedStatement.executeUpdate();
            var generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                url.setId(generatedKeys.getLong(1));
            }
        }
    }

    public static boolean isUrlExists(String urlName) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name= ?";
        try (var conn = BaseRepository.dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, urlName);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        }
    }


    public static Optional<Url> getUrlById(Long id) throws SQLException {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            var resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlId = resultSet.getLong("id");
                var url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);

                return Optional.of(url);
            }
            return Optional.empty();
        }
    }


    public static List<Url> getUrls() throws SQLException {
        var sql = "SELECT * FROM urls";
        try (var conn = BaseRepository.dataSource.getConnection();
             Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);

            List<Url> urls = new ArrayList<>();
            while (resultSet.next()) {
                // Получить данные из результата запроса
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Timestamp createdAt = resultSet.getTimestamp("created_at");
                Url url = new Url(name);
                url.setId(id);
                url.setCreatedAt(createdAt);
                urls.add(url);
                // Обработать полученные данные
            }
            return urls;
        }
    }

    public static void deleteUrl(Long id) throws SQLException {
        var sql = "DELETE FROM urls WHERE id = ?";
        try (var conn = dataSource.getConnection();
             var stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public static Optional<Url> findByName(String urlName) throws SQLException {
        var sql = "SELECT * FROM urls WHERE name= ?";
        try (var conn = BaseRepository.dataSource.getConnection();
             var preparedStatement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, urlName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("name");
                var createdAt = resultSet.getTimestamp("created_at");
                var urlId = resultSet.getLong("id");
                var url = new Url(name);
                url.setId(urlId);
                url.setCreatedAt(createdAt);

                return Optional.of(url);
            }
            return Optional.empty();
        }
    }
}

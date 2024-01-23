package hexlet.code.util;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;

import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class NormalizedData {
    public static String getNormalizedURL(URL url) {
        String protocol = url.getProtocol();
        String symbols = "://";
        String host = url.getHost();
        String colonBeforePort = url.getPort() == -1 ? "" : ":";
        String port = url.getPort() == -1 ? "" : String.valueOf(url.getPort());

        return protocol + symbols + host + colonBeforePort + port;
    }

    public static Map<Long, UrlCheck> getListOfLastChecks() throws SQLException {
        var urls = UrlRepository.getEntities();
        Map<Long, UrlCheck> result = new HashMap<>();

        for (var url: urls) {
            var id = url.getId();
            UrlCheck lastCheck;
            if (UrlCheckRepository.getLastCheck(id).isPresent()) {
                lastCheck = UrlCheckRepository.getLastCheck(id).get();
            } else {
                lastCheck = null;
            }
            result.put(id, lastCheck);
        }

        return result;
    }
}

package hexlet.code.util;

import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;

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
        var urlChecks = UrlCheckRepository.getAllLastChecks();
        Map<Long, UrlCheck> result = new HashMap<>();

        for (var urlCheck : urlChecks) {
            result.put(urlCheck.getUrlId(), urlCheck);
        }

        return result;
    }
}

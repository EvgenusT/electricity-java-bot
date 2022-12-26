
package electricityjavabot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

@Slf4j
@Service
public class HttpsChecker {

    public boolean urlConnection(String url) {
        boolean available;
        try {
            final URLConnection connection = new URL(url).openConnection();
            connection.connect();
            log.info("Service " + url + " available, yeah!");
            available = true;
        } catch (final MalformedURLException e) {
            throw new IllegalStateException("Bad URL: " + url, e);
        } catch (final IOException e) {
            log.info("Service " + url + " unavailable, oh no!", e);
            available = false;
        }
        return available;
    }
}

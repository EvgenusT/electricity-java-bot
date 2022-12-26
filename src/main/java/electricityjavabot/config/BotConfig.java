package electricityjavabot.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("application.properties")
public class BotConfig {
    @Value("${bot.name}")
    String botName;
    @Value("${bot.key}")
    String token;
    @Value("${bot.chat_id}")
    Long chatId;
    @Value("${bot.url}")
    String url;
}

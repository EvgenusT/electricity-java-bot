package electricityjavabot.service;

import electricityjavabot.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Optional;

@Slf4j
@Component
@EnableScheduling
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final HttpsChecker httpsChecker;
    static final String ERROR_TEXT = "Error occurred: ";
    private boolean isEnabled;
    private boolean globalFailed;
    private boolean isDisable;

    public TelegramBot(BotConfig botConfig, HttpsChecker httpsChecker) {
        this.botConfig = botConfig;
        this.httpsChecker = httpsChecker;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.info("No message");
    }

    @Scheduled(cron = "${cron.scheduler}")
    private void checkHTTPS() {
        int countFailed = 0;
        boolean connection;
        connection = isConnection();
        if (!connection && !globalFailed) {
            while (!connection && countFailed < 3) {
                connection = isConnection();
                if (!connection) {
                    try {
                        countFailed++;
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }
            globalFailed = true;
        }

        if (connection && !isEnabled && !restartException()) {
            prepareAndSendMessage(botConfig.getChatId(), "Світло з'явилося \uD83C\uDF15");
            isEnabled = true;
            globalFailed = false;
            isDisable = false;
        } else if (!connection && !isDisable) {
            prepareAndSendMessage(botConfig.getChatId(), "Світло пропало \uD83C\uDF11");
            isEnabled = false;
            isDisable = true;
        } else {
            log.info("Без змін");
        }
    }

    private boolean isConnection() {
        return Optional.of(httpsChecker.urlConnection(botConfig.getUrl())).orElse(false);
    }

    private void prepareAndSendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }

    private void executeMessage(SendMessage message) {
        try {
            execute(message);
            log.info("message send: " + message.getText());
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private boolean restartException() {
        ZoneId zoneId = ZoneId.of("Europe/Kiev");
        boolean check = false;
        LocalTime start = LocalTime.of(3, 0, 0);
        LocalTime stop = LocalTime.of(3, 5, 0);
        LocalTime now = LocalTime.now(zoneId);
        if (now.isAfter(start) && now.isBefore(stop)) {
            check = true;
        }
        return check;
    }

}

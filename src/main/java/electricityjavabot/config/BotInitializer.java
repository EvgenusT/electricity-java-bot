//package electricityjavabot.config;
//
//import electricityjavabot.service.TelegramBot;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//
//@Component
//@Slf4j
//public class BotInitializer {
//    @Autowired
//    TelegramBot bot;
//
//    public void init() throws TelegramApiException {
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//        try {
//            telegramBotsApi.registerBot(bot);
//        } catch (TelegramApiException e) {
//            log.error("Error occurred: " + e.getMessage());
//        }
//
//    }
//}

package ge.market.flea.scheduler;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class Scheduler {
    //867782230

    /*@Scheduled(fixedDelay = 1000)
    public void scheduleFixedDelayTask() throws TelegramApiException {
        System.out.println(
                "Fixed delay task - " + System.currentTimeMillis() / 1000);
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(867782230L);
        sendMessage.setText("xyi");
        sender.execute(sendMessage);
    }*/
}

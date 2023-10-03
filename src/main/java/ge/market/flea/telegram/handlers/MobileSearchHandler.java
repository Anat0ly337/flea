package ge.market.flea.telegram.handlers;

import ge.market.flea.data.entity.Goods;
import ge.market.flea.service.MobileService;
import ge.market.flea.telegram.UserState;
import lombok.extern.slf4j.Slf4j;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

@Slf4j
public class MobileSearchHandler extends KeyboardAbstract {

    private final SilentSender sender;
    private final Map<Long, UserState> chatStates;

    private final MobileService mobileService;

    public MobileSearchHandler(SilentSender sender, Map<Long, UserState> chatStates, MobileService mobileService) {
        super(sender, chatStates);
        this.sender = sender;
        this.chatStates = chatStates;
        this.mobileService = mobileService;
    }

    @Override
    public void messageResolver(Update update) {
        SendMessage message = new SendMessage();
        String msg = getMessage(update);

       List<Goods> goods = mobileService.find(msg);

        log.info("MobileSearch handler");

        StringBuilder builder = new StringBuilder();

        goods.forEach(goods1 -> builder.append("\uD83D\uDCF1 " + goods1.getGoodName() +" " +goods1.getDescription() + "\n"));

        message.setText(builder.toString());

        chatStates.put(getChatId(update), UserState.MOBILE_SEARCH);
        message.setChatId(getChatId(update));

        messageSender(message);
    }
}

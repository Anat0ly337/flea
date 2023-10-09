package ge.market.flea.telegram.handlers;

import ge.market.flea.constants.GoodConstant;
import ge.market.flea.data.entity.Goods;
import ge.market.flea.service.MobileService;
import ge.market.flea.telegram.UserState;
import lombok.extern.slf4j.Slf4j;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;
import java.util.Map;

@Slf4j
public class MobileSearchHandler extends KeyboardAbstract {

    private final SilentSender sender;
    private final Map<Long, UserState> chatStates;
    private final Map<Long,Integer> pageState;
    private final MobileService mobileService;

    public MobileSearchHandler(SilentSender sender, Map<Long, UserState> chatStates,
                               MobileService mobileService, Map<Long, Integer> page) {
        super(sender, chatStates);
        this.sender = sender;
        this.chatStates = chatStates;
        this.mobileService = mobileService;
        this.pageState = page;
    }

    @Override
    public void messageResolver(Update update) {
        log.info("MobileSearch handler");

        int page = 0;
        if (isFirstVisit(update)) {
            pageState.put(getChatId(update), page);
        }

        chatStates.put(getChatId(update), UserState.MOBILE_SEARCH);
        pageState.put(getChatId(update), 1);

        buildAndSendMessage(update, page);
    }

    private void buildAndSendMessage(Update update, Integer page) {
        String msg = getMessage(update);
        List<Goods> goods = mobileService.find(msg, page);

        SendMessage message = new SendMessage();

        message.setText(responseBuilder(goods));
        message.setChatId(getChatId(update));
        buildBackForwardButtons(message);

        messageSender(message);
    }

    private boolean isFirstVisit(Update update) {
        long id = getChatId(update);
        UserState userState = chatStates.get(id);
        return  (userState != UserState.MOBILE_SEARCH);
    }

    private void buildBackForwardButtons(SendMessage message) {
        List<List<InlineKeyboardButton>> rowsInline = getRowsLine(message);

        InlineKeyboardButton back = getInlineKeyBoardButton("🔙", GoodConstant.BACK);

        InlineKeyboardButton forward = getInlineKeyBoardButton("➡" , GoodConstant.FORWARD);

        rowsInline.add(List.of(back, forward));

    }

    private String responseBuilder (List<Goods> goods) {

        if (goods.isEmpty()) {
            return "Ничо не найдено";
        }

        StringBuilder builder = new StringBuilder();
        goods.forEach(goods1 -> builder.append("\uD83D\uDCF1 " + goods1.getGoodName() +" " +goods1.getDescription() + "\n"
        + goods1.getNumber() + "\n"));

        return builder.toString();
    }
}

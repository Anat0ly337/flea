package ge.market.flea.telegram;import java.util.List;import java.util.Map;import ge.market.flea.service.MobileService;import ge.market.flea.telegram.handlers.*;import org.apache.commons.lang3.NotImplementedException;import org.springframework.beans.factory.annotation.Autowired;import org.telegram.abilitybots.api.db.DBContext;import org.telegram.abilitybots.api.sender.SilentSender;import org.telegram.telegrambots.meta.api.objects.Update;public class MessageReceiver {    private final SilentSender sender;    private final Map<Long, UserState> chatStates;    private final Map<Long,Integer> page;    @Autowired    private MobileService mobileService;    public MessageReceiver(SilentSender sender, DBContext db) {        db.clear();        this.sender = sender;        this.chatStates = db.getMap("chatStates");        this.page = db.getMap("page");    }    protected void receiveMessage(List<Update> updates) {        for (Update update : updates) {            long chatId = getChatId(update);            UserState userState = chatStates.get(chatId);            if (userState == null) {                continue;            }            KeyboardAbstract keyboardAbstract = keyboardChooser(userState);            keyboardAbstract.messageResolver(update);        }    }    private KeyboardAbstract keyboardChooser(UserState userState) {       //String action = update.getCallbackQuery().getData();        switch (userState) {            case COMMON_SEARCH -> {                return new CommonSearchInlineHandler(sender, chatStates);            }            case LANGUAGE_CHOOSE -> {                return new InitialResponseHandler(sender, chatStates);            }            case POST_SEARCH -> {                return new PostSearchInlineHandler(sender, chatStates);            }            case MOBILE_SEARCH -> {                return new MobileSearchHandler(sender, chatStates, mobileService, page);            }            default -> new NotImplementedException();        }        return null;    }    private long getChatId (Update update) {        Long chatId;        if (update.getMessage() == null) {            chatId =  update.getCallbackQuery().getMessage().getChatId();        } else {            chatId = update.getMessage().getChat().getId();        }        return chatId;    }}
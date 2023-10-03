package ge.market.flea.telegram.handlers;

import ge.market.flea.constants.GoodConstant;
import ge.market.flea.telegram.UserState;
import org.telegram.abilitybots.api.sender.SilentSender;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

public class PostSearchInlineHandler extends KeyboardAbstract {


    private final SilentSender sender;
    private final Map<Long, UserState> chatStates;

    public PostSearchInlineHandler(SilentSender sender, Map<Long, UserState> chatStates) {
        super(sender, chatStates);
        this.sender = sender;
        this.chatStates = chatStates;
    }

    @Override
    public void messageResolver(Update update) {
        SendMessage message = new SendMessage();
        message.setChatId(getChatId(update));

        String callBackData = update.getCallbackQuery().getData();

        message.setText("Выберите \uD83D\uDCF1" + callBackData + "\n" +
                "По примеру" +  "\n" +
        "iPhone 13 pro max");

        chatStates.put(getChatId(update), userStateByCallBackData.get(callBackData));

        messageSender(message);
    }


}

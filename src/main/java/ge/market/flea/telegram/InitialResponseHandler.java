package ge.market.flea.telegram;import static ge.market.flea.telegram.UserState.AWAITING_CONFIRMATION;import static ge.market.flea.telegram.UserState.COMMON_SEARCH;import static ge.market.flea.telegram.UserState.LANGUAGE_CHOOSE;import static ge.market.flea.telegram.UserState.PIZZA_TOPPINGS;import java.util.ArrayList;import java.util.List;import java.util.Map;import org.telegram.abilitybots.api.sender.SilentSender;import org.telegram.telegrambots.meta.api.methods.send.SendMessage;import org.telegram.telegrambots.meta.api.objects.Message;import org.telegram.telegrambots.meta.api.objects.Update;import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;public class InitialResponseHandler extends KeyboardAbstract {    private final SilentSender sender;    private final Map<Long, UserState> chatStates;    public InitialResponseHandler(SilentSender sender, Map<Long, UserState> chatStates) {        super(sender, chatStates);        this.sender = sender;        this.chatStates = chatStates;    }    @Override    public void messageResolver(Update update) {        Message message = update.getMessage();        long chatId = getChatId(update);        if (message != null) {            if (message.getText().equalsIgnoreCase("/stop")) {                stopChat(chatId);            }        }        switch (chatStates.get(chatId)) {            case AWAITING_NAME -> replyToName(chatId, message);            case LANGUAGE_CHOOSE -> replyToChooseLanguage(chatId, message);            default -> unexpectedMessage(chatId);        }    }    public void replyToStart(long chatId) {        System.out.println("-------------");        SendMessage message = new SendMessage();        message.setChatId(chatId);        message.setText("Choose the language");        message.setReplyMarkup(KeyboardFactory.getChooseLanguage());        sender.execute(message);        chatStates.put(chatId, LANGUAGE_CHOOSE);    }    public void replyToButtons(long chatId, Message message) {        if (message.getText().equalsIgnoreCase("/stop")) {            stopChat(chatId);        }    }    private void unexpectedMessage(long chatId) {        SendMessage sendMessage = new SendMessage();        sendMessage.setChatId(chatId);        sendMessage.setText("I did not expect that.");        sender.execute(sendMessage);    }    private void stopChat(long chatId) {        SendMessage sendMessage = new SendMessage();        sendMessage.setChatId(chatId);        sendMessage.setText("Thank you for your order. See you soon!\nPress /start to order again");        chatStates.remove(chatId);        sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));        sender.execute(sendMessage);    }    private void replyToChooseLanguage(long chatId, Message message) {        SendMessage sendMessage = new SendMessage();        sendMessage.setChatId(chatId);        if ("Русский".equalsIgnoreCase(message.getText())) {            sendMessage.setText("Ваш язык Русский.:)");            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();            List<InlineKeyboardButton> rowInline = new ArrayList<>();            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("Выбрать категорию");            inlineKeyboardButton.setCallbackData("3");            rowInline.add(inlineKeyboardButton);            rowsInline.add(rowInline);            markupInline.setKeyboard(rowsInline);            sendMessage.setReplyMarkup(markupInline);            chatStates.put(chatId, COMMON_SEARCH);            sender.execute(sendMessage);        } else if ("English".equalsIgnoreCase(message.getText())) {            sendMessage.setText("English is your language.!");            InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();            List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();            List<InlineKeyboardButton> rowInline = new ArrayList<>();            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton("opt1");            inlineKeyboardButton.setCallbackData("321312321312312321231213231231231231231213213");            rowInline.add(inlineKeyboardButton);            rowsInline.add(rowInline);            markupInline.setKeyboard(rowsInline);            sendMessage.setReplyMarkup(new ReplyKeyboardRemove(true));            chatStates.put(chatId, COMMON_SEARCH);            sender.execute(sendMessage);        } else {            sendMessage.setText("We don't sell " + message.getText() + ". Please select from the options below.");            sendMessage.setReplyMarkup(KeyboardFactory.getChooseLanguage());            sender.execute(sendMessage);        }    }    private void replyToName(long chatId, Message message) {        /*promptWithKeyboardForState(chatId, "Hello " + message.getText() + ". What would you like to have?",            KeyboardFactory.getChooseLanguage(),            LANGUAGE_CHOOSE);*/    }    public boolean userIsActive(Long chatId) {        return chatStates.containsKey(chatId);    }}
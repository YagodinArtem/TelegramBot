import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    //уникальный номер бота только для владельца
    //1361557806:AAFqfYC5pOKnHoh8mZtiz0xTHiSuXaiox1E
    @Override
    public String getBotToken() {
        return "1361557806:AAFqfYC5pOKnHoh8mZtiz0xTHiSuXaiox1E";
    }

    //имя бота при создании
    @Override
    public String getBotUsername() {
        return "YAR_WeatherBot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        //для получения обновлений и обработки - сообщений
        Message message = update.getMessage();
        Model model = new Model();

        if (message != null && message.hasText()) {
            switch (message.getText()) {
                case "/help" -> sendMsg(message, """
                         1. Для того чтобы узнать погоду, просто напишите в чат название населенного пункта
                         2. Если желаете узнать свежие новости, нажмите на кнопку /News""");
                case "/settings" -> sendMsg(message, "Что необходимо настроить?");
                //bitcoin news method
                case "/News" -> sendNews(message, News.getNews());


                default -> {
                    try {
                        sendMsg(message, Weather.getWeather(message.getText(), model));
                    } catch (IOException e) {
                        sendMsg(message, "Такой город не найден");
                    }
                }
            }
        }
    }

    public void setButtons(SendMessage sendMessage) {
        //создание и редактирование клавиатуры
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow firstRow = new KeyboardRow(); //первая строка кнопок
        KeyboardRow secondRow = new KeyboardRow(); //вторая строка кнопок


        firstRow.add(new KeyboardButton("/help"));
        firstRow.add(new KeyboardButton("/settings"));

        secondRow.add(new KeyboardButton("/News"));
        secondRow.add(new KeyboardButton("Москва"));

        keyboardRowList.add(secondRow);
        keyboardRowList.add(firstRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);
    }

    public void sendNews(Message message, List<String> news) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(false);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());

        try {
            setButtons(sendMessage);
            for (int i = 0; i < News.getNewsQuantity(); i++) {
                sendMessage.setText(news.get(i));
                sendMessage(sendMessage);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    public void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            setButtons(sendMessage);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new Bot());
        } catch (TelegramApiException e) {
            //TelegramBotException
            e.printStackTrace();
        }

    }
}

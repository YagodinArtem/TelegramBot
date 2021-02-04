import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class Weather {

    //da76c2c969b4f571e848d02626f34153
    //token ^ weather

    public static String getWeather(String message, Model model) throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + message + "&units=metric&appid=da76c2c969b4f571e848d02626f34153");
        Scanner sc = new Scanner((InputStream) url.getContent());
        String result = "";

        while (sc.hasNext()) {
            result += sc.nextLine();
        }

        System.out.println(result);
        JSONObject object = new JSONObject(result);

        model.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        model.setTemp(main.getDouble("temp"));
        model.setHumidity(main.getDouble("humidity"));

        JSONObject wind = object.getJSONObject("wind");
        model.setSpeed(wind.getDouble("speed"));

        JSONObject clouds = object.getJSONObject("clouds");
        model.setClouds(getClouds(clouds.getDouble("all")));


        JSONArray getArray = object.getJSONArray("weather");

        for (int i = 0; i < getArray.length(); i++) {
            JSONObject j = getArray.getJSONObject(i);
            model.setIcon((String) j.get("icon"));
            model.setMain((String) j.get("main"));
        }

        String res =
                        "Город: " + model.getName() + "\n" +
                        "Температура воздуха: " + model.getTemp() + "C" + "\n" +
                        "Облачность: " + model.getClouds() + "\n" +
                        "Влажность: " + model.getHumidity() + "%" + "\n" +
                        "Скорость ветра: " + model.getSpeed() + "м/с\n" +
                        "http://openweathermap.org/img/w/" + model.getIcon() + ".png";

        System.out.println(res);
        System.out.println(object.getString("name"));

        return res;
    }

    public static String getClouds(Double all) {
        String result = "";
        if (all < 5) {
            result = "Ясное небо";
        }
        if (all > 5 && all <= 25) {
            result = "Небольшая облачность";
        }
        if (all > 25 && all <= 70) {
            result = "Высокая облачность";
        }
        if (all > 70) {
            result = "И даже солнца не видно, здесь не хуй ловить нам...";
        }

        return result;
    }
}

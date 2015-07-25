package com.github.lecho.mobilization.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class Break {

    public static final String JSON = "{\"break-registration\":{\"title\":\"Registration\",\"description_html\":\"If " +
            "you do not want to miss any presentation please register between 7:30-9:10. However, registration will " +
            "be opened all day!\"},\"break-hello-world\":{\"title\":\"Hello World\",\"description_html\":\"This is " +
            "couple of words about Mobilization. For community by community.\"},\"break-coffee\":{\"title\":\"Coffee " +
            "Break\",\"description_html\":\"\"},\"break-lunch\":{\"title\":\"Lunch Break\"," +
            "\"description_html\":\"There is Solider soup - \\\"Grochówka\\\" for everyone! VIPs (Speakers, Sponsors)" +
            " have lunch in Congresowa Restaurant on base floor.\"},\"break-finally\":{\"title\":\"Finally\"," +
            "\"description_html\":\"\"}}";

    String key;
    String title;
    String descriptionHtml;

    public static Map<String, Break> beaksFromJson(String json) {
        Type genericType = new TypeToken<Map<String, Break>>() {
        }.getType();

        Gson gson = new Gson();
        Map<String, Break> breaks = gson.fromJson(json, genericType);

        return breaks;
    }
}

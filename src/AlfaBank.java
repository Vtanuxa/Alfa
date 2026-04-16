import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

public class AlfaBank {
    public static void main(String[] args) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        String day = "0"+String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = "0"+String.valueOf(calendar.get(Calendar.MONTH)+1);
        month = month.substring(month.length()-2);
        day = day.substring(day.length()-2);
        int year = calendar.get(Calendar.YEAR);
        double today = getRub(null);
        double yesterday = getRub(year+"-"+month+"-"+day);

        System.out.println(today+" "+yesterday);

        String searchWord;
        if (today > yesterday) {
            searchWord = "rich";
        } else {
            searchWord = "broke";
        }

        String gifUrl = getGif(searchWord);
        System.out.println("GIF URL: " + gifUrl);
    }

    static double getRub(String date){
        double rub = 0;
        final String APP_ID = "ad1c3a856f32428db34c670bfefe84c1";
        String spec = "https://openexchangerates.org/api/latest.json?app_id="+APP_ID;
        if (date != null){
            spec = "https://openexchangerates.org/api/historical/"+date+".json?app_id="+APP_ID;
        }
        try {
            URL url = new URL(spec);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null){
                result.append(line);
            }

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONObject jsonRates = (JSONObject) jsonObject.get("rates");
            rub = (double) jsonRates.get("RUB");

        }catch (Exception e){
            e.printStackTrace();
        }

        return rub;
    }

    static String getGif(String q) {
        String gif = "";
        final String APP_KEY = "yogxacUoXdKZX0wkD0depV72uwdbz3gj";
        String spec = "https://api.giphy.com/v1/gifs/search?api_key=" + APP_KEY + "&q=" + q + "&limit=1";

        try {
            URL url = new URL(spec);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result.toString());
            JSONArray data = (JSONArray) jsonObject.get("data");

            if (data != null && data.size() > 0) {
                JSONObject firstGif = (JSONObject) data.get(0);
                JSONObject images = (JSONObject) firstGif.get("images");
                JSONObject original = (JSONObject) images.get("original");
                gif = (String) original.get("url");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return gif;
    }
}

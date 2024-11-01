import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class API{
    private static String URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-01-02";
    public static void main(String[] args) {
        try {
            URL url = new URL(URL);
            // Abrir conexión
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Error: HTTP response code " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                response.append(line);
            }

            conn.disconnect();

            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray features = jsonResponse.getJSONArray("features");

            System.out.println("Número de terremotos encontrados: " + features.length());

            for (int i = 0; i < features.length(); i++) {
                JSONObject feature = features.getJSONObject(i);
                JSONObject properties = feature.getJSONObject("properties");

                String place = properties.getString("place");
                double magnitude = properties.getDouble("mag");
                long time = properties.getLong("time");

                System.out.println("Terremoto #" + (i + 1));
                System.out.println("Lugar: " + place);
                System.out.println("Magnitud: " + magnitude);
                System.out.println("Tiempo: " + new java.util.Date(time));
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package fr.esaip.metho;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // Déclaration de tous les TextView
    private TextView cityNameTextView, temperatureTextView, weatherDescriptionTextView,
            tempMinMaxTextView, coordinatesTextView, humidityTextView,
            pressureTextView, windSpeedTextView, windDirectionTextView;

    // Nouveaux éléments UI
    private EditText cityEditText;
    private Button searchButton;

    private RequestQueue requestQueue;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation des Vues
        cityNameTextView = findViewById(R.id.cityName);
        temperatureTextView = findViewById(R.id.temperature);
        weatherDescriptionTextView = findViewById(R.id.weatherDescription);
        tempMinMaxTextView = findViewById(R.id.tempMinMax);
        coordinatesTextView = findViewById(R.id.coordinates);
        humidityTextView = findViewById(R.id.humidity);
        pressureTextView = findViewById(R.id.pressure);
        windSpeedTextView = findViewById(R.id.windSpeed);
        windDirectionTextView = findViewById(R.id.windDirection);

        // Initialisation des nouveaux éléments
        cityEditText = findViewById(R.id.cityEditText);
        searchButton = findViewById(R.id.searchButton);

        // Initialisation de Volley
        requestQueue = Volley.newRequestQueue(this);

        // Configuration du listener pour le bouton de recherche
        searchButton.setOnClickListener(view -> {
            String city = cityEditText.getText().toString().trim();
            if (!city.isEmpty()) {
                fetchWeatherData(city);
                hideKeyboard(view); // Masquer le clavier après la recherche
            } else {
                Toast.makeText(MainActivity.this, "Veuillez entrer un nom de ville", Toast.LENGTH_SHORT).show();
            }
        });

        // Récupération des données météo pour une ville par défaut au lancement
        fetchWeatherData("Angers");
    }

    private void fetchWeatherData(String city) {
        String apiKey = "c0bbff4a824cce23670fa594dfa7e8b1";
        // L'URL reste la même, car elle contient déjà toutes les informations nécessaires
        String url = "https://api.openweathermap.org/data/2.5/weather?units=metric&lang=fr&APPID=" + apiKey + "&q=" + city;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                this::parseAndDisplayWeatherData, // Utilisation d'une référence de méthode
                error -> {
                    Toast.makeText(MainActivity.this, "Ville non trouvée ou erreur réseau", Toast.LENGTH_LONG).show();
                    error.printStackTrace();
                });

        requestQueue.add(stringRequest);
    }

    private void parseAndDisplayWeatherData(String response) {
        try {
            JSONObject jsonResponse = new JSONObject(response);
            DecimalFormat df = new DecimalFormat("#.#");

            // 1. Coordonnées (lat, lon)
            JSONObject coord = jsonResponse.getJSONObject("coord");
            double lat = coord.getDouble("lat");
            double lon = coord.getDouble("lon");
            coordinatesTextView.setText(String.format(Locale.FRANCE, "Lat: %.2f, Lon: %.2f", lat, lon));

            // 2. Météo (description)
            JSONArray weatherArray = jsonResponse.getJSONArray("weather");
            String description = "";
            if (weatherArray.length() > 0) {
                JSONObject weather = weatherArray.getJSONObject(0);
                description = weather.getString("description");
                description = description.substring(0, 1).toUpperCase() + description.substring(1);
            }
            weatherDescriptionTextView.setText(description);

            // 3. Données principales (temp, min, max, pression, humidité)
            JSONObject main = jsonResponse.getJSONObject("main");
            double temp = main.getDouble("temp");
            double tempMin = main.getDouble("temp_min");
            double tempMax = main.getDouble("temp_max");
            int pressure = main.getInt("pressure");
            int humidity = main.getInt("humidity");

            temperatureTextView.setText(String.format(Locale.FRANCE, "%s°C", df.format(temp)));
            tempMinMaxTextView.setText(String.format(Locale.FRANCE, "Max: %s°C  Min: %s°C", df.format(tempMax), df.format(tempMin)));
            pressureTextView.setText(String.format(Locale.FRANCE, "%d hPa", pressure));
            humidityTextView.setText(String.format(Locale.FRANCE, "%d%%", humidity));

            // 4. Vent (vitesse, direction)
            JSONObject wind = jsonResponse.getJSONObject("wind");
            double windSpeed = wind.getDouble("speed"); // Vitesse en m/s
            int windDeg = wind.getInt("deg");
            double windSpeedKmh = windSpeed * 3.6; // Conversion en km/h

            windSpeedTextView.setText(String.format(Locale.FRANCE, "%s km/h", df.format(windSpeedKmh)));
            windDirectionTextView.setText(String.format(Locale.FRANCE, "%d° (%s)", windDeg, convertDegToCardinal(windDeg)));

            // 5. Nom de la ville
            String cityName = jsonResponse.getString("name");
            cityNameTextView.setText(cityName);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Erreur lors de l'analyse des données", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Cache le clavier virtuel.
     * @param view La vue qui a déclenché l'événement (par exemple, le bouton).
     */
    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Convertit la direction du vent en degrés vers un point cardinal.
     * @param deg Direction en degrés (0-360)
     * @return Chaîne de caractères représentant le point cardinal (ex: "N", "NE", "E", ...)
     */
    private String convertDegToCardinal(int deg) {
        String[] cardinals = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSO", "SO", "OSO", "O", "ONO", "NO", "NNO", "N"};
        return cardinals[(int)Math.round((double) deg % 360 / 22.5)];
    }
}

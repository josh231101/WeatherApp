package app.android.joshjosh.clima;


import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ConstraintLayout constraintLayout;
    private EditText user;
    private TextView tvDay;
    private TextView tvHour;
    private TextView tvWeather;
    private TextView tvPlace;
    private TextView tvGrados;
    private ImageView imgView;
    private Button search;
    private WeatherService service;
    private Call<City> cityCall;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        bindUI();
        getActualDay();
        getActualHour();

        service = API.getApi().create(WeatherService.class);
        search.setOnClickListener(this);


    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorSormyLight));
        toolbar.setTitle("Weather");
        toolbar.setTitleTextColor(getResources().getColor(R.color.blank));
        toolbar.setLogo(getDrawable(R.drawable.ic_weather));
        setSupportActionBar(toolbar);

    }


    private void getActualHour() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentDateandTime = sdf.format(new Date());
        tvHour.setText(currentDateandTime);
    }

    private void getActualDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SUNDAY:
                // Current day is Sunday
                tvDay.setText("Domingo");
                break;
            case Calendar.MONDAY:
                // Current day is Monday
                tvDay.setText("Lunes");
                break;
            case Calendar.TUESDAY:
                // etc.
                tvDay.setText("Martes");
                break;
            case Calendar.WEDNESDAY:
                tvDay.setText("Miércoles");
                break;

            case Calendar.THURSDAY:
                tvDay.setText("Jueves");
                break;
            case Calendar.FRIDAY:
                tvDay.setText("Viernes");
                break;
            case Calendar.SATURDAY:
                tvDay.setText("Sábado");

                break;

        }
    }

    private void bindUI() {
        search = findViewById(R.id.search);
        user = findViewById(R.id.editTextUser);
        tvDay = findViewById(R.id.tvDay);
        tvHour = findViewById(R.id.tvHour);
        tvPlace = findViewById(R.id.tvPlace);
        tvWeather = findViewById(R.id.tvWeather);
        tvGrados = findViewById(R.id.tvGrados);
        imgView = findViewById(R.id.iVState);
        constraintLayout = findViewById(R.id.constraintbg);

    }

    @Override
    public void onClick(View v) {
        user.onEditorAction(EditorInfo.IME_ACTION_DONE);
        String city = user.getText().toString();
        if(city != ""){
            cityCall = service.getCity(city,API.APPKEY,"metric");
            cityCall.enqueue(new Callback<City>() {
                @Override
                public void onResponse(Call<City> call, Response<City> response) {
                   // Toast.makeText(MainActivity.this,"EXITO",Toast.LENGTH_SHORT).show();
                    City city = response.body();
                    setResult(city);
                }

                @Override
                public void onFailure(Call<City> call, Throwable t) {
                    Toast.makeText(MainActivity.this,"UPS!Something wrong",Toast.LENGTH_SHORT).show();

                }
            });
        }



    }

    private void setResult(City city) {
        tvPlace.setText(city.getName()+ " , " + city.getCountry());
        tvWeather.setText(city.getDescription());
        tvGrados.setText(String.valueOf(city.getTemperature()) + "°");
        //TODO: CHANGE BACKGROUND CONSTRAINT
        setImage(city);
        setBackgroundWeather(city);


    }

    private void setBackgroundWeather(City city) {
         switch (city.getDescription()){
             case "Thunderstorm":
                 constraintLayout.setBackground(getDrawable(R.drawable.stormygradient));
                 setToolbarColor(R.color.colorSormyLight);

                 break;
             case "Drizzle":
             case "Rain":
                 constraintLayout.setBackground(getDrawable(R.drawable.rainygradient));
                 setToolbarColor(R.color.colorRainnyStrong);
                 break;
             case "Snow":
                 constraintLayout.setBackground(getDrawable(R.drawable.snowy));
                 setToolbarColor(R.color.colorSnowyStrong);
                 break;
             case "Mist":
             case "Smoke":
             case "Haze":
             case "Dust":
             case "Fog":
             case "Sand":
             case "Ash":
             case "Squall":
             case "Tornado":
                 constraintLayout.setBackground(getDrawable(R.drawable.windygradient));
                 setToolbarColor(R.color.colorWindyStrong);
                 break;
             case "Clear":
                 constraintLayout.setBackground(getDrawable(R.drawable.sunnygradient));
                 setToolbarColor(R.color.colorSunnyStrong);
                 break;

             case "Clouds":
                 constraintLayout.setBackground(getDrawable(R.drawable.cloduygradient));
                 setToolbarColor(R.color.colorCloudyStrong);
                 break;
                 default:
                     constraintLayout.setBackground(getDrawable(R.drawable.cleargradient));
                     setToolbarColor(R.color.colorClearStrong);
                     break;



         }
    }

    private void setToolbarColor(int colorRainnyStrong) {
        toolbar.setBackgroundColor(getResources().getColor(colorRainnyStrong));

    }

    private void setImage(City city) {
        switch (city.getIcon()){
            case "01d":
                imgView.setImageResource(R.drawable.ic_sunny);
                break;
            case "02d":
            case "02n":
            case "03n":
            case "03d":
                imgView.setImageResource(R.drawable.ic_sun);
                break;
            case  "04d":
            case "04n":
                imgView.setImageResource(R.drawable.ic_cloudy);
                break;
            case "09d":
            case "09n":
            case "10d":
            case "10n":
                imgView.setImageResource(R.drawable.ic_rain);
                break;
            case "11d":
            case "11n":
                imgView.setImageResource(R.drawable.ic_storm);
                break;
            case "13d":
            case "13n":
                imgView.setImageResource(R.drawable.ic_snow);
                break;
            case "50d":
            case "50n":
                imgView.setImageResource(R.drawable.ic_haze);
                break;
                default:
                    imgView.setImageResource(R.drawable.ic_cloudy);
                    break;


        }
    }
}

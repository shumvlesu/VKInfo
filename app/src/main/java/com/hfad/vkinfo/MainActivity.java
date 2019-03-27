package com.hfad.vkinfo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import static com.hfad.vkinfo.utils.NetworkUtils.generateURL;
import static com.hfad.vkinfo.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private TextView result;
    private TextView errorMessage;
    private ProgressBar loadnigIndicator;

    private void showResultTextVew() {
        result.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.INVISIBLE);
    }

    private void showErrorTextView() {
        result.setVisibility(View.INVISIBLE);
        errorMessage.setVisibility(View.VISIBLE);
    }

    //Здесь я создаю отдельный поток для запроса к серверу вк
    //создаю его так как он слишком мнго занимает для программы времени
    class VKQueryTask extends AsyncTask<URL, Void, String> {

        //Этот метод выполняется перед началом выполнения потока
        //Здесь включу прогресс бар. Что бы пользователь видел что началось какоето выполнение.
        //И программа не зависла.
        @Override
        protected void onPreExecute() {
            loadnigIndicator.setVisibility(View.VISIBLE);
        }

        //Этот метод собственно и есть как бы поток.
        @Override
        protected String doInBackground(URL... urls) {
            //Обращаюсь в этом потоке к методу getResponseFromURL
            //Который и обращается к самому сайту ВК
            String responce = null;
            try {
                responce = getResponseFromURL(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return responce;
        }

        //после выполнения doInBackground возвращает в onPostExecute строку
        //Это результат работы потока
        //в моем случае это переменная строкового типа responce с ответом вк в формате json.
        @Override
        protected void onPostExecute(String responce) {
            String firstName = null;
            String lastName = null;
            //Объявляю переменную типа JSON
            JSONObject jsonResponse = null;

            //Если ответ не пустой и не null то обрабатываем ответ
            if (responce != null && !responce.equals("")) {

                try {
                    jsonResponse = new JSONObject(responce);
                    //Это примерный возврат от вк.
                    //{"response":[{"id":1982860,"first_name":"Сергей","last_name":"Шумихин","is_closed":false,"can_access_closed":true}]}
                    //response это массив значений , но у меня в массиве всего один элемент.
                    //Поэтому я объявлю переменную jsonArray с типом массива json и по ключу "response" получу его
                    JSONArray jsonArray = jsonResponse.getJSONArray("response");

                    String resultingString = "";

                    for (int i = 0; i < jsonArray.length(); i++) {
                        //Информация о пользователе можно обратившись к  i индексу массива.
                        JSONObject userInfo = jsonArray.getJSONObject(i);
                        firstName = userInfo.getString("first_name");
                        lastName = userInfo.getString("last_name");

                        //Вывожу результат
                        resultingString += "Имя: " + firstName + "\n" + "Фамилия: " + lastName + "\n\n";

                    }
                    result.setText(resultingString);
                    //Информация о пользователе можно обратившись к  0 индексу массива. Хапись то одна.
                    // JSONObject userInfo = jsonArray.getJSONObject(0);

                    //Ну а теперь получая по ключам json переменные и преобразуя их в строку , кидаю их в переменные строкового типа.


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                showResultTextVew();
            } else {
                showErrorTextView();
            }

            //Неважно вышла ошибка или нет, анимация прогресс бара должна быть выключена.
            loadnigIndicator.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText searchField = findViewById(R.id.et_search_field);
        Button searchButton = findViewById(R.id.b_search_vk);

        result = findViewById(R.id.tv_result);
        errorMessage = findViewById(R.id.tv_error_message);
        loadnigIndicator = findViewById(R.id.pb_loading_indicator);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Здесть я генерирую URL в классе NetworkUtils, метод generateURL
                URL generatedURL = generateURL(searchField.getText().toString());
                //А здесь сформированную URL передаю уже в созданый поток VKQueryTask
                new VKQueryTask().execute(generatedURL);

            }
        };

        searchButton.setOnClickListener(onClickListener);

    }
}

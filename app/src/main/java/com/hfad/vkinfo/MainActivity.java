package com.hfad.vkinfo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import static com.hfad.vkinfo.utils.NetworkUtils.generateURL;
import static com.hfad.vkinfo.utils.NetworkUtils.getResponseFromURL;

public class MainActivity extends AppCompatActivity {

    private TextView result;

    //Здесь я создаю отдельный поток для запроса к серверу вк
    //создаю его так как он слишком мнго занимает для программы времени
    class VKQueryTask extends AsyncTask<URL, Void, String> {

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
            result.setText(responce);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText searchField = findViewById(R.id.et_search_field);
        Button searchButton = findViewById(R.id.b_search_vk);

        result = findViewById(R.id.tv_result);

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

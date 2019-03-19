package com.hfad.vkinfo;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText searchField = findViewById(R.id.et_search_field);
        Button searchButton =  findViewById(R.id.b_search_vk);

        result = findViewById(R.id.tv_result);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL generatedURL = generateURL(searchField.getText().toString());
                String responce = null;
                try {
                    responce = getResponseFromURL(generatedURL);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //result.setText(generatedURL.toString());
                result.setText(responce);
            }
        };

        searchButton.setOnClickListener(onClickListener);

    }
}

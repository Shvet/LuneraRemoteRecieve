package com.luneraremoterecieve;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by shvet on 03/08/2017,LuneraRemoteRecieve
 */

public class SettingActivity extends AppCompatActivity {
    EditText uuid_edit_1, uuid_edit_2;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Button button;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);
        uuid_edit_1 = (EditText) findViewById(R.id.uuid_edit_1);
        uuid_edit_2 = (EditText) findViewById(R.id.uuid_edit_2);
        button = (Button) findViewById(R.id.save);

        preferences = getApplicationContext().getSharedPreferences("Setting", MODE_PRIVATE);
        editor = preferences.edit();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!uuid_edit_1.getText().toString().isEmpty() && !uuid_edit_2.getText().toString().isEmpty()) {
                    editor.putString("UUID1", uuid_edit_1.getText().toString());
                    editor.putString("UUID2", uuid_edit_2.getText().toString());
                    editor.apply();
                    finish();
                }
            }
        });

    }
}

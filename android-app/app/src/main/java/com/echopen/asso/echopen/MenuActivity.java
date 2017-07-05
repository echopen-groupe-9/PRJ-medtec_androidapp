package com.echopen.asso.echopen;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MenuActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_menu);

        Button btn_close = (Button) findViewById(R.id.btn_close_menu);

        btn_close.setOnClickListener(MenuActivity.this);

    }

    @Override
    public void onClick(View v) {
        finish();
    }
}

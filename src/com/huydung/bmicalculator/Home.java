package com.huydung.bmicalculator;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class Home extends Activity {
	
	Button btnCalculate;
	Button btnAbout;
	Button btnMore;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_home);
        
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        btnAbout = (Button) findViewById(R.id.btnAbout);
        btnMore = (Button) findViewById(R.id.btnMore);
        
        btnCalculate.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Home.this, Input.class);
				startActivity(intent);
			}
		});
    }

}

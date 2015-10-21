package com.example.scibdisrat.myapplication2;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

/**
 * @author SCIBBDISRAT
 */
public class  MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"mity1 ...............................");
        final ImageButton imbtn = (ImageButton) findViewById(R.id.imageButton);
        imbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this,SecondActivity.class);


        startActivity(intent);
    }

}




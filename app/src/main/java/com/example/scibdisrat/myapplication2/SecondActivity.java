package com.example.scibdisrat.myapplication2;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * @author SCIBBDISRAT
 */

public class  SecondActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        final Button btn = (Button) findViewById(R.id.button);

        final Button btn2 = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(this);
        btn2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        {

            if (v.getId() == R.id.button) {
                Intent intent = new Intent(this, HospitalActivity.class);
                startActivity(intent);

            } else if (v.getId() == R.id.button2) {
                Intent intent = new Intent(this, RestaurantActivity.class);
                startActivity(intent);
            }

        }
    }

}








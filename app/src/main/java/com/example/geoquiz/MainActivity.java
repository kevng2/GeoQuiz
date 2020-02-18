package com.example.geoquiz;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button mTrueButton;
    private Button mFalseButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* to create a toast, call
                   public static Toast makeText(Context context, int resId, int duration)
                   context is needed to be able to find and use the string's resource ID.
                   Duration specifies how long it should be visible
                 */

                //  Toast.show() displays it on the screen
                Toast.makeText(MainActivity.this, R.string.correct_toast,
                                Toast.LENGTH_SHORT).show();
            }
        });
        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, R.string.incorrect_toast,
                                Toast.LENGTH_SHORT).show();
            }
        });
    }
}

package com.example.geoquiz;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {
    private static final String CHEATER = "cheater";

    private boolean mAnswerIsTrue;
    private int mCheatToken;
    private TextView mAnswerTextView;
    private TextView mAPILevel;
    private Button mShowAnswerButton;

    //key to be put in the putExtra() function
    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.android.geoquiz.answer_is_true";

    // extra key
    private static final String EXTRA_ANSWER_SHOWN =
            "com.android.geoquiz.answer_shown";
    private static final String NUMBER_OF_CHEAT_TOKENS =
            "com.android.geoquiz.num_token";

    public static Intent newIntent(Context packageContext, boolean answerIsTrue, int numToken) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        intent.putExtra(NUMBER_OF_CHEAT_TOKENS, numToken);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    public static int wasIntAnswerShown(Intent result) {
        return result.getIntExtra(NUMBER_OF_CHEAT_TOKENS, 0);
    }

    // will set the intent
    private void setAnswerShownResult(boolean isAnswerShown, int numToken) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        data.putExtra(NUMBER_OF_CHEAT_TOKENS, numToken);

        // when the user presses SHOW ANSWER button, the CheatActivity packages up the result
        // code and the intent in the call to setResult(int, Intent).
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if(savedInstanceState != null) {
            mAnswerIsTrue = savedInstanceState.getBoolean(CHEATER, false);
            mCheatToken = savedInstanceState.getInt(NUMBER_OF_CHEAT_TOKENS);
            setAnswerShownResult(true, mCheatToken);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);
        mCheatToken = getIntent().getIntExtra(NUMBER_OF_CHEAT_TOKENS, 0);

        mAnswerTextView = findViewById(R.id.answer_text_view);

        mAPILevel = findViewById(R.id.api_level_text);
        String API_text = getResources().getString(R.string.api_level_text) + " "
                + Build.VERSION.SDK_INT;
        mAPILevel.setText(API_text);

        mShowAnswerButton = findViewById(R.id.show_answer_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAnswerIsTrue) {
                    mAnswerTextView.setText(R.string.true_button);
                }
                else {
                    mAnswerTextView.setText(R.string.false_button);
                }

                // decrease the cheat token by 1 if button is pressed
                mCheatToken--;

                // calling the function to set the intent
                // put true inside b/c if the user presses the button,
                // then they revealed the answer
                setAnswerShownResult(true, mCheatToken);
                MainActivity.makeButtonInvisible(mShowAnswerButton);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(CHEATER, mAnswerIsTrue);
        savedInstanceState.putInt(NUMBER_OF_CHEAT_TOKENS, mCheatToken);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

package com.example.geoquiz;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String KEY_INDEX = "index";
    private static final String COUNTER = "counter";
    private static final String CHEATER = "cheater";
    private static final int REQUEST_CODE_CHEAT = 0;

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private TextView mQuestionTextView;
    private TextView mScore;
    private TextView mTokenCount;
    private String mGrade;
    private String tokenStr;
    private boolean mIsCheater;
    private int counter = 0;
    private int mCurrentIndex = 0;
    private int cheatIndex;
    private int mCheatToken = 3;

    private Question[] mQuestionBank = new Question[]{
        new Question(R.string.question_australia, true),
        new Question(R.string.question_ocean, true),
        new Question(R.string.question_mideast, false),
        new Question(R.string.question_africa, false),
        new Question(R.string.question_americas, true),
        new Question(R.string.question_asia, true),
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //calling Log.d(...) to log a message
        Log.d(TAG, "onCreate(Bundle) called");

        setContentView(R.layout.activity_main);
        mScore = findViewById(R.id.counter);
        if(savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            counter = savedInstanceState.getInt(COUNTER, 0);
            mScore.setText(Integer.toString(counter));
            mIsCheater = savedInstanceState.getBoolean(CHEATER, false);
        }

        // initializes the text on the screen with a value
        mQuestionTextView = findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();
            }
        });

        mTokenCount = findViewById(R.id.token_count);
        updateToken();

        // gets the current question
        // sets the text into the test view
        updateQuestion();

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* to create a toast, call
                   public static Toast makeText(Context context, int resId, int duration)
                   context is needed to be able to find and use the string's resource ID.
                   Duration specifies how long it should be visible
                 */

                //  Toast.show() displays it on the screen
                if(checkAnswer(true)) {
                    //increments the score
                    mScore.setText(Integer.toString(++counter));
                }
                mIsCheater = false;
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                if(mCurrentIndex == 0) {
                    mGrade = getResources().getString(R.string.score,
                            counter / (double)mQuestionBank.length * 100);
                    Toast.makeText(MainActivity.this, mGrade, Toast.LENGTH_SHORT).show();
                    counter = 0;
                }
                updateQuestion();
                mScore.setText(Integer.toString(counter));
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(checkAnswer(false)) {
                    mScore.setText(Integer.toString(++counter));
                }

                mIsCheater = false;

                // moved this to the outside to prevent user from entering multiple answers
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                if(mCurrentIndex == 0) {
                    mGrade = getResources().getString(R.string.score,
                            counter / (double)mQuestionBank.length * 100);
                    Toast.makeText(MainActivity.this, mGrade, Toast.LENGTH_SHORT).show();
                }
                updateQuestion();
                mScore.setText(Integer.toString(counter));
            }
        });

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // intents are used to switch between the MainActivity and the CheatActivity
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
                Intent intent = CheatActivity.newIntent(MainActivity.this, answerIsTrue, mCheatToken);

                // used to close the next button loophole
                cheatIndex = mCurrentIndex;

                // run the activity
                startActivityForResult(intent, REQUEST_CODE_CHEAT);
                /*
                if(mCheatToken == 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int cx = mCheatButton.getWidth() / 2;
                    int cy = mCheatButton.getHeight() / 2;
                    float radius = mCheatButton.getWidth();
                    Animator anim = ViewAnimationUtils
                            .createCircularReveal(mCheatButton, cx, cy, radius, 0);
                    anim.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            mCheatButton.setVisibility(View.INVISIBLE);
                        }
                    });
                    anim.start();
                }

                 */
            }
        });

        // creating listener for next button
        // finding button from unique name given in xml
        mNextButton = findViewById(R.id.next_button);

        mNextButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // increment index and reset to 0 again if bank reaches max index
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                if(cheatIndex == mCurrentIndex) {
                    mIsCheater = true;
                }
                else
                    mIsCheater = false;
                updateQuestion();
            }
        });

        mPrevButton = findViewById(R.id.prev_button);

        mPrevButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex - 1);
                mIsCheater = false;
                if(mCurrentIndex < 0)
                    mCurrentIndex = mQuestionBank.length - 1;
                updateQuestion();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");

        // saves the current index and puts it into the KEY_INDEX
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putInt(COUNTER, counter);
        savedInstanceState.putBoolean(CHEATER, mIsCheater);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroyed() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private boolean checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();
        boolean isTrue = false;
        int messageResId = 0;
        if(mIsCheater) {
            messageResId = R.string.judgement_toast;
        }
        else {
            if (answerIsTrue == userPressedTrue) {
                messageResId = R.string.correct_toast;
                isTrue = true;
            } else
                messageResId = R.string.incorrect_toast;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        return isTrue;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != Activity.RESULT_OK) {
            return;
        }
        if(requestCode == REQUEST_CODE_CHEAT) {
            if(data == null) {
                return;
            }
            mIsCheater = CheatActivity.wasAnswerShown(data);
            mCheatToken = CheatActivity.wasIntAnswerShown(data);
            updateToken();
            if(mCheatToken == 0) {
                makeButtonInvisible(mCheatButton);
            }
        }
    }

    public void updateToken() {
        tokenStr = getResources().getString(R.string.token_count_text) + " " + mCheatToken;
        mTokenCount.setText(tokenStr);
    }

    public static void makeButtonInvisible(final Button button) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = button.getWidth() / 2;
            int cy = button.getHeight() / 2;
            float radius = button.getWidth();
            Animator anim = ViewAnimationUtils
                    .createCircularReveal(button, cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    button.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        }
        else {
            button.setVisibility(View.INVISIBLE);
        }
    }
}

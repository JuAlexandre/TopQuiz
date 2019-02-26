package io.projet.julien.topquiz.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import io.projet.julien.topquiz.R;
import io.projet.julien.topquiz.model.User;

public class MainActivity extends AppCompatActivity {
    private TextView mGreetingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private User mUser;

    private SharedPreferences mPreferences;

    public static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";
    public static final String PREF_KEY_FIRST_NAME = "PREF_KEY_FIRST_NAME";

    public TextView getGreetingText() {
        return mGreetingText;
    }

    public void setGreetingText(TextView GreetingText) {
        mGreetingText = GreetingText;
    }

    public EditText getNameInput() {
        return mNameInput;
    }

    public void setNameInput(EditText NameInput) {
        mNameInput = NameInput;
    }

    public Button getPlayButton() {
        return mPlayButton;
    }

    public void setPlayButton(Button PlayButton) {
        mPlayButton = PlayButton;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public SharedPreferences getPreferences() {
        return mPreferences;
    }

    public void setPreferences(SharedPreferences preferences) {
        mPreferences = preferences;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            assert data != null;
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);
            getPreferences().edit().putInt(PREF_KEY_SCORE, score).apply();

            greetUser();
        }
    }

    private void greetUser() {
        String firstName = getPreferences().getString(PREF_KEY_FIRST_NAME, null);

        if (firstName != null) {
            int score = getPreferences().getInt(PREF_KEY_SCORE, 0);

            String fullText = "Welcome back, " + firstName
                    + "!\nYour last score was "
                    + score + ", will you do better this time?";

            getGreetingText().setText(fullText);
            getNameInput().setText(firstName);
            getNameInput().setSelection(firstName.length());
            getPlayButton().setEnabled(true);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUser(new User());

        setPreferences(getPreferences(MODE_PRIVATE));

        setGreetingText((TextView) findViewById(R.id.activity_main_greeting_txt));
        setNameInput((EditText) findViewById(R.id.activity_main_name_input));
        setPlayButton((Button) findViewById(R.id.activity_main_play_btn));

        getPlayButton().setEnabled(false);

        greetUser();

        getNameInput().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                getPlayButton().setEnabled(s.toString().length() != 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        getPlayButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = getNameInput().getText().toString();
                getUser().setFirstName(firstName);

                getPreferences().edit().putString(PREF_KEY_FIRST_NAME, getUser().getFirstName()).apply();

                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });
    }
}

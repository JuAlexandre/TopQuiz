package io.projet.julien.topquiz.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

import io.projet.julien.topquiz.R;
import io.projet.julien.topquiz.model.Question;
import io.projet.julien.topquiz.model.QuestionBank;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mQuestionText;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;

    private int mScore;
    private int mNumberOfQuestions;
    private boolean mEnableTouchEvent;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    public static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";

    public TextView getQuestionText() {
        return mQuestionText;
    }

    public void setQuestionText(TextView questionText) {
        mQuestionText = questionText;
    }

    public Button getAnswerButton1() {
        return mAnswerButton1;
    }

    public void setAnswerButton1(Button answerButton1) {
        mAnswerButton1 = answerButton1;
    }

    public Button getAnswerButton2() {
        return mAnswerButton2;
    }

    public void setAnswerButton2(Button answerButton2) {
        mAnswerButton2 = answerButton2;
    }

    public Button getAnswerButton3() {
        return mAnswerButton3;
    }

    public void setAnswerButton3(Button answerButton3) {
        mAnswerButton3 = answerButton3;
    }

    public Button getAnswerButton4() {
        return mAnswerButton4;
    }

    public void setAnswerButton4(Button answerButton4) {
        mAnswerButton4 = answerButton4;
    }

    public int getScore() {
        return mScore;
    }

    public void setScore(int score) {
        mScore = score;
    }

    public int getNumberOfQuestions() {
        return mNumberOfQuestions;
    }

    public void setNumberOfQuestions(int numberOfQuestions) {
        mNumberOfQuestions = numberOfQuestions;
    }

    public QuestionBank getQuestionBank() {
        return mQuestionBank;
    }

    public void setQuestionBank(QuestionBank questionBank) {
        mQuestionBank = questionBank;
    }

    public Question getCurrentQuestion() {
        return mCurrentQuestion;
    }

    public void setCurrentQuestion(Question currentQuestion) {
        mCurrentQuestion = currentQuestion;
    }

    public boolean isEnableTouchEvent() {
        return mEnableTouchEvent;
    }

    public void setEnableTouchEvent(boolean enableTouchEvent) {
        mEnableTouchEvent = enableTouchEvent;
    }

    private QuestionBank generateQuestions() {
        Question question1 = new Question("What is the name of the current french president?",
                Arrays.asList("François Hollande", "Emmanuel Macron", "Jacques Chirac", "François Mitterand"),
                1);

        Question question2 = new Question("How many countries are there in the European Union?",
                Arrays.asList("15", "24", "28", "32"),
                2);

        Question question3 = new Question("Who is the creator of the Android operating system?",
                Arrays.asList("Andy Rubin", "Steve Wozniak", "Jake Wharton", "Paul Smith"),
                0);

        Question question4 = new Question("When did the first man land on the moon?",
                Arrays.asList("1958", "1962", "1967", "1969"),
                3);

        Question question5 = new Question("What is the capital of Romania?",
                Arrays.asList("Bucarest", "Warsaw", "Budapest", "Berlin"),
                0);

        Question question6 = new Question("Who did the Mona Lisa paint?",
                Arrays.asList("Michelangelo", "Leonardo Da Vinci", "Raphael", "Carravagio"),
                1);

        Question question7 = new Question("In which city is the composer Frédéric Chopin buried?",
                Arrays.asList("Strasbourg", "Warsaw", "Paris", "Moscow"),
                2);

        Question question8 = new Question("What is the country top-level domain of Belgium?",
                Arrays.asList(".bg", ".bm", ".bl", ".be"),
                3);

        Question question9 = new Question("What is the house number of The Simpsons?",
                Arrays.asList("42", "101", "666", "742"),
                3);

        return new QuestionBank(
                Arrays.asList(
                    question1,
                    question2,
                    question3,
                    question4,
                    question5,
                    question6,
                    question7,
                    question8,
                    question9
                )
        );
    }

    private void displayQuestion(final Question question) {
        getQuestionText().setText(question.getQuestion());
        getAnswerButton1().setText(question.getChoiceList().get(0));
        getAnswerButton2().setText(question.getChoiceList().get(1));
        getAnswerButton3().setText(question.getChoiceList().get(2));
        getAnswerButton4().setText(question.getChoiceList().get(3));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_STATE_SCORE, getScore());
        outState.putInt(BUNDLE_STATE_QUESTION, getNumberOfQuestions());

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View v) {
        int responseIndex = (int) v.getTag();

        if (responseIndex == getCurrentQuestion().getAnswerIndex()) {
            // Good answer
            mScore++;
            Toast.makeText(this, "Correct", Toast.LENGTH_SHORT).show();
        } else {
            // Wrong answer
            Toast.makeText(this, "Wrong answer!", Toast.LENGTH_SHORT).show();
        }

        setEnableTouchEvent(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setEnableTouchEvent(true);

                // If this is the last question, ends the game.
                if (--mNumberOfQuestions == 0) {
                    // End game
                    endGame();
                } else {
                    // Else, display the next question.
                    setCurrentQuestion(getQuestionBank().getQuestion());
                    displayQuestion(getCurrentQuestion());
                }
            }
        }, 2000);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // Authorize the touch screen
        return mEnableTouchEvent && super.dispatchTouchEvent(ev);
    }

    private void endGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
            .setTitle("Well done!")
            .setMessage("Your score is " + mScore)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // End the activity
                    Intent intent = new Intent();
                    intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                    setResult(RESULT_OK, intent);

                    finish();
                }
            })
            .create()
            .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        setQuestionBank(this.generateQuestions());

        if (savedInstanceState != null) {
            setScore(savedInstanceState.getInt(BUNDLE_STATE_SCORE));
            setNumberOfQuestions(savedInstanceState.getInt(BUNDLE_STATE_QUESTION));
        } else {
            setScore(0);
            setNumberOfQuestions(4);
        }

        setEnableTouchEvent(true);

        setQuestionText((TextView) findViewById(R.id.activity_game_question_text));
        setAnswerButton1((Button) findViewById(R.id.activity_game_answer1_btn));
        setAnswerButton2((Button) findViewById(R.id.activity_game_answer2_btn));
        setAnswerButton3((Button) findViewById(R.id.activity_game_answer3_btn));
        setAnswerButton4((Button) findViewById(R.id.activity_game_answer4_btn));

        // Use the tag property to 'name' the buttons
        getAnswerButton1().setTag(0);
        getAnswerButton2().setTag(1);
        getAnswerButton3().setTag(2);
        getAnswerButton4().setTag(3);

        getAnswerButton1().setOnClickListener(this);
        getAnswerButton2().setOnClickListener(this);
        getAnswerButton3().setOnClickListener(this);
        getAnswerButton4().setOnClickListener(this);

        setCurrentQuestion(getQuestionBank().getQuestion());
        this.displayQuestion(getCurrentQuestion());
    }
}

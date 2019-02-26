package io.projet.julien.topquiz.model;

import java.util.Collections;
import java.util.List;

public class QuestionBank {
    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

    public List<Question> getQuestionList() {
        return mQuestionList;
    }

    public void setQuestionList(List<Question> questionList) {
        mQuestionList = questionList;
    }

    public int getNextQuestionIndex() {
        return mNextQuestionIndex;
    }

    public void setNextQuestionIndex(int nextQuestionIndex) {
        mNextQuestionIndex = nextQuestionIndex;
    }

    public QuestionBank(List<Question> questionList) {
        setQuestionList(questionList);

        // Shuffle the question list
        Collections.shuffle(getQuestionList());

        setNextQuestionIndex(0);
    }

    public Question getQuestion() {
        // Ensure we loop over the questions
        if (getNextQuestionIndex() == getQuestionList().size()) {
            setNextQuestionIndex(0);
        }

        // Please note the post-incrementation
        return getQuestionList().get(mNextQuestionIndex++);
    }
}

package org.mammadli;

import java.util.ArrayList;
import java.util.List;


public class Main {

    public static void main(String[] args) throws Exception {


        DaoQuestion daoQuestion = new DaoQuestion();
        Quiz quiz = new Quiz();
        quiz.setId(1);

        Question question1 = new Question();
        question1.setId(2);
        question1.setContent("What is the capital of France?");
        question1.setTopic("Geography");
        question1.setDifficultyRank(3);
        List<Response> responses = new ArrayList<>();
        responses.add(new Response(4, "Paris", true));
        responses.add(new Response(5, "London", false));
        responses.add(new Response(6, "Rome", false));
        responses.add(new Response(7, "Madrid", false));
        question1.setResponses(responses);


        Question newQuestion = new Question();
        newQuestion.setId(2);
        newQuestion.setContent("What is the capital of Estonia?");
        newQuestion.setTopic("Geography");
        newQuestion.setDifficultyRank(7);
        List<Response> resp = new ArrayList<>();
        resp.add(new Response(4, "Paris", true));
        resp.add(new Response(5, "London", false));
        resp.add(new Response(6, "Rome", false));
        resp.add(new Response(7, "Madrid", false));
        newQuestion.setResponses(resp);

//        daoQuestion.saveQuiz(quiz);
//        daoQuestion.deleteQuestion(4);
//        daoQuestion.saveQuestion(question1, quiz);
//        daoQuestion.searchQuestionsByTopic("Geography");
//        daoQuestion.updateQuestion(2, newQuestion);

    }
}
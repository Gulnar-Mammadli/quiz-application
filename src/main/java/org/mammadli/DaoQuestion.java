package org.mammadli;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DaoQuestion {
    public Connection connect() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/quiz_app";
        String username = "root";
        String password = "123456";
        return DriverManager.getConnection(url, username, password);
    }

    public void saveQuiz(Quiz quiz) {
        try (Connection conn = connect()) {

            PreparedStatement checkStatement = conn.prepareStatement("SELECT id FROM quiz WHERE id = ?");
            checkStatement.setInt(1, quiz.getId());
            ResultSet resultSet = checkStatement.executeQuery();

            if (resultSet.next()) {
                throw new IllegalArgumentException("Quiz with ID " + quiz.getId() + " already exist.");
            }
            String quizQuery = "INSERT INTO quiz (id) VALUES (?)";
            PreparedStatement quizStatement = conn.prepareStatement(quizQuery);
            quizStatement.setInt(1, quiz.getId());
            quizStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveQuestion(Question question, Quiz quiz) {

        try (Connection conn = connect()) {
            PreparedStatement checkStatement = conn.prepareStatement("SELECT id FROM quiz WHERE id = ?");
            checkStatement.setInt(1, quiz.getId());
            ResultSet resultSet = checkStatement.executeQuery();

            if (!resultSet.next()) {
                throw new IllegalArgumentException("Quiz with ID " + quiz.getId() + " does not exist.");
            }

            String query = "INSERT INTO question (id, topic, difficultyRank, content, quiz_id) VALUES (?, ?, ?, ?,?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, question.getId());
            statement.setString(2, question.getTopic());
            statement.setInt(3, question.getDifficultyRank());
            statement.setString(4, question.getContent());
            statement.setInt(5, quiz.getId());
            statement.executeUpdate();

            List<Response> responses = question.getResponses();

            if (!responses.isEmpty()) {
                for (Response response : responses) {
                    String responseQuery = "INSERT INTO response (id, text, correct, question_id) VALUES (?, ?, ?, ?)";
                    PreparedStatement responseStatement = conn.prepareStatement(responseQuery);
                    responseStatement.setInt(1, response.getId());
                    responseStatement.setString(2, response.getText());
                    responseStatement.setBoolean(3, response.isCorrect());
                    responseStatement.setInt(4, question.getId());
                    responseStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void deleteQuestion(int questionId) throws Exception {

        try (Connection conn = connect()) {

            String responseQuery = "DELETE FROM response WHERE question_id = ?";
            PreparedStatement responseStatement = conn.prepareStatement(responseQuery);
            responseStatement.setInt(1, questionId);
            responseStatement.executeUpdate();

            String query = "DELETE FROM question WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, questionId);
            int rowsDeleted = statement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Question with ID " + questionId + " has been deleted successfully.");
            } else {
                System.out.println("Question with ID " + questionId + " does not exist.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Question> searchQuestionsByTopic(String topic) {
        List<Question> questions = new ArrayList<>();
        try (Connection conn = connect()) {
            String query = "SELECT * FROM question WHERE topic = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, topic);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String content = resultSet.getString("content");
                int difficultyRank = resultSet.getInt("difficultyRank");
                List<Response> responses = getResponsesForQuestion(conn, id);

                Question question = new Question();
                question.setId(id);
                question.setContent(content);
                question.setTopic(topic);
                question.setDifficultyRank(difficultyRank);
                question.setResponses(responses);

                questions.add(question);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }

    private List<Response> getResponsesForQuestion(Connection conn, int questionId) throws SQLException {
        List<Response> responses = new ArrayList<>();
        String query = "SELECT * FROM response WHERE question_id = ?";
        PreparedStatement statement = conn.prepareStatement(query);
        statement.setInt(1, questionId);
        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()) {
            int id = resultSet.getInt("id");
            String text = resultSet.getString("text");
            boolean correct = resultSet.getBoolean("correct");

            Response response = new Response();
            response.setId(id);
            response.setText(text);
            response.setCorrect(correct);

            responses.add(response);
        }
        return responses;
    }


    public void updateQuestion(int id, Question newQuestion) {

        try (Connection conn = connect()) {
            String checkQuery = "SELECT id FROM question WHERE id = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
            checkStatement.setInt(1, id);
            ResultSet resultSet = checkStatement.executeQuery();

            if (!resultSet.next()) {
                throw new IllegalArgumentException("Question with ID " + id + " does not exist.");
            }

            String query = "UPDATE question SET topic=?, difficultyRank=?, content=? WHERE id=?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, newQuestion.getTopic());
            statement.setInt(2, newQuestion.getDifficultyRank());
            statement.setString(3, newQuestion.getContent());
            statement.setInt(4, id);
            statement.executeUpdate();

            List<Response> responses = newQuestion.getResponses();
            if (!responses.isEmpty()) {
                String deleteResponseQuery = "DELETE FROM response WHERE question_id=?";
                PreparedStatement deleteResponseStatement = conn.prepareStatement(deleteResponseQuery);
                deleteResponseStatement.setInt(1, id);
                deleteResponseStatement.executeUpdate();

                for (Response response : responses) {
                    String responseQuery = "INSERT INTO response (id, text, correct, question_id) VALUES (?, ?, ?, ?)";
                    PreparedStatement responseStatement = conn.prepareStatement(responseQuery);
                    responseStatement.setInt(1, response.getId());
                    responseStatement.setString(2, response.getText());
                    responseStatement.setBoolean(3, response.isCorrect());
                    responseStatement.setInt(4, id);
                    responseStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

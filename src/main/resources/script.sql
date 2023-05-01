USE quiz_app;

CREATE TABLE quiz (
                      id INT NOT NULL PRIMARY KEY
                    
);

CREATE TABLE question (
                          id INT NOT NULL PRIMARY KEY,
                          topic VARCHAR(255) NOT NULL,
                          difficultyRank INT NOT NULL,
                          content VARCHAR(255) NOT NULL,
                          quiz_id INT NOT NULL,
                          FOREIGN KEY (quiz_id) REFERENCES quiz(id)
);

CREATE TABLE response (
                          id INT NOT NULL PRIMARY KEY,
                          text VARCHAR(255) NOT NULL,
                          correct BOOLEAN NOT NULL,
                          question_id INT NOT NULL,
                          FOREIGN KEY (question_id) REFERENCES question(id)
);
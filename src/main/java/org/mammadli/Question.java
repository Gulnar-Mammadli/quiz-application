package org.mammadli;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    private int id;
    private String topic;
    private int difficultyRank;
    private String content;
    private List<Response> responses;

}

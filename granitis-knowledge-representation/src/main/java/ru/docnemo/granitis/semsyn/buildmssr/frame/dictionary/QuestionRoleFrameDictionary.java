package ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary;

import ru.docnemo.granitis.semsyn.buildmssr.frame.QuestionRoleFrame;

public interface QuestionRoleFrameDictionary {
    QuestionRoleFrame findByPrepositionAndQuestionWord(String preposition, String word);
    QuestionRoleFrame findByQuestionWord(String word);
}

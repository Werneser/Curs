package com.example.curs;

import androidx.room.TypeConverter;

import java.util.ArrayList;
import java.util.List;

public class QuestionConverter {
    private static final String ITEM_SEPARATOR = ";";
    private static final String OPTION_SEPARATOR = ",";
    private static final String QUESTION_SEPARATOR = "|";

    @TypeConverter
    public static List<Question> fromString(String value) {
        if (value == null || value.isEmpty()) {
            return new ArrayList<>();
        }

        List<Question> questions = new ArrayList<>();
        String[] questionStrings = value.split(QUESTION_SEPARATOR);

        for (String questionString : questionStrings) {
            String[] parts = questionString.split(ITEM_SEPARATOR);
            if (parts.length < 2) continue;

            int questionType = Integer.parseInt(parts[0]);
            String questionText = parts[1];
            Question question = new Question(questionType, questionText);

            if (parts.length > 2) {
                String[] options = parts[2].split(OPTION_SEPARATOR);
                for (String option : options) {
                    question.addOption(option, false);
                }
            }

            if (parts.length > 3) {
                String[] correctAnswers = parts[3].split(OPTION_SEPARATOR);
                for (int i = 0; i < correctAnswers.length; i++) {
                    question.correctAnswers.set(i, Boolean.parseBoolean(correctAnswers[i]));
                }
            }

            questions.add(question);
        }

        return questions;
    }

    @TypeConverter
    public static String fromList(List<Question> list) {
        StringBuilder result = new StringBuilder();

        for (Question question : list) {
            if (result.length() > 0) {
                result.append(QUESTION_SEPARATOR);
            }

            result.append(question.questionType)
                    .append(ITEM_SEPARATOR)
                    .append(question.question);

            if (!question.options.isEmpty()) {
                result.append(ITEM_SEPARATOR);
                for (int i = 0; i < question.options.size(); i++) {
                    if (i > 0) {
                        result.append(OPTION_SEPARATOR);
                    }
                    result.append(question.options.get(i));
                }
            }

            if (!question.correctAnswers.isEmpty()) {
                result.append(ITEM_SEPARATOR);
                for (int i = 0; i < question.correctAnswers.size(); i++) {
                    if (i > 0) {
                        result.append(OPTION_SEPARATOR);
                    }
                    result.append(question.correctAnswers.get(i));
                }
            }
        }

        return result.toString();
    }
}

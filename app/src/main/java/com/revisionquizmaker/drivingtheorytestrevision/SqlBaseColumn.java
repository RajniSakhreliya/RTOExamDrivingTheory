package com.revisionquizmaker.drivingtheorytestrevision;

import android.provider.BaseColumns;

public final class SqlBaseColumn {

    public static abstract class C0057a implements BaseColumns {
        public static final String[] a = {"_id", "title", "category_percentage_correct"};
        public static final String[] b = {"_id", "question_number", "question_title", "question_correct_answer", "question_explanation", "questionAnsweredCorrectly", "question_corresponding_quiz", "question_image_path"};
        public static final String[] c = {"_id", "question_number", "question_title", "question_correct_answer", "question_explanation", "questionAnsweredCorrectly", "question_corresponding_quiz"};
        public static final String[] d = {"_id", "answer_text", "ANSWER_TYPE", "ANSWER_IS_CORRECT", "answer_corresponding_question"};
    }
}

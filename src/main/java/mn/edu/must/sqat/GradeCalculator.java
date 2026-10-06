package mn.edu.must.sqat;

import java.math.BigDecimal;

/**
 * Оюутны нийлбэр онооноос үсгэн дүн тооцно (F.CSA313-ийн үнэлгээний бүтэц).
 */
public class GradeCalculator {

    static final double MAX_ATTENDANCE = 10;
    static final double MAX_LAB = 40;
    static final double MAX_QUIZ = 10;
    static final double MAX_EXAM = 30;

    // 90+ -> A, 80-89 -> B, 70-79 -> C, 60-69 -> D, <60 -> F
    // score нь 0-100 хязгаараас гарвал IllegalArgumentException шиднэ
    public String letterGrade(double score) {
        requireInRange("score", score, 100);

        if (score >= 90) {
            return "A";
        }
        if (score >= 80) {
            return "B";
        }
        if (score >= 70) {
            return "C";
        }
        if (score >= 60) {
            return "D";
        }
        return "F";
    }

    // Ирц(10), лаб+бие даалт(40), сорил1(10), сорил2(10), шалгалт(30)-ийн
    // оноонуудаас нийлбэр оноог тооцно. Аль нэг нь СӨРӨГ эсвэл дээд хязгаараасаа хэтэрсэн бол IllegalArgumentException шиднэ.
    public double totalScore(double att, double lab, double quiz1, double quiz2, double exam) {
        requireInRange("att", att, MAX_ATTENDANCE);
        requireInRange("lab", lab, MAX_LAB);
        requireInRange("quiz1", quiz1, MAX_QUIZ);
        requireInRange("quiz2", quiz2, MAX_QUIZ);
        requireInRange("exam", exam, MAX_EXAM);

        // double-ийг шууд нэмбэл 9.3 + 35.3 + 8.3 + 8.9 + 28.2 = 89.99999999999999
        // болж, яг 90 оноотой оюутан B авна. Аравтын бутархайгаар (BigDecimal) нэмнэ.
        return BigDecimal.valueOf(att)
                .add(BigDecimal.valueOf(lab))
                .add(BigDecimal.valueOf(quiz1))
                .add(BigDecimal.valueOf(quiz2))
                .add(BigDecimal.valueOf(exam))
                .doubleValue();
    }

    // NaN-ийг тусад нь шалгана: NaN < 0 ч, NaN > max ч false тул энгийн
    // харьцуулалтаар баригдахгүй, letterGrade-д чимээгүйхэн "F" болж гарна.
    private static void requireInRange(String name, double value, double max) {
        if (Double.isNaN(value) || value < 0 || value > max) {
            throw new IllegalArgumentException(
                    name + " нь 0-" + (int) max + " хооронд байх ёстой, оролт: " + value);
        }
    }
}

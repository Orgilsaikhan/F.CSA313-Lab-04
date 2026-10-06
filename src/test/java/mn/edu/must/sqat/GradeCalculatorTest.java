package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("GradeCalculator — нэгжийн тест")
class GradeCalculatorTest {

    private static final double DELTA = 1e-9;

    // ---------- letterGrade: ердийн утгууд ----------

    @Test
    @DisplayName("95 оноо A дүн байх ёстой")
    void ninetyFiveIsA() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(95.0);             // Act
        assertEquals("A", grade);                          // Assert
    }

    @Test
    @DisplayName("85 оноо B дүн байх ёстой")
    void eightyFiveIsB() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(85.0);             // Act
        assertEquals("B", grade);                          // Assert
    }

    @Test
    @DisplayName("75 оноо C дүн байх ёстой")
    void seventyFiveIsC() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(75.0);             // Act
        assertEquals("C", grade);                          // Assert
    }

    @Test
    @DisplayName("65 оноо D дүн байх ёстой")
    void sixtyFiveIsD() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(65.0);             // Act
        assertEquals("D", grade);                          // Assert
    }

    @Test
    @DisplayName("30 оноо F дүн байх ёстой")
    void thirtyIsF() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(30.0);             // Act
        assertEquals("F", grade);                          // Assert
    }

    // ---------- letterGrade: хязгаарын утгууд ----------

    @Test
    @DisplayName("90 оноо яг A дүн байх ёстой (хязгаарын тохиолдол)")
    void ninetyIsExactlyA() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(90.0);             // Act
        assertEquals("A", grade);                          // Assert
    }

    @Test
    @DisplayName("89.99 оноо A биш, B дүн байх ёстой (хязгаарын доод тал)")
    void justBelowNinetyIsB() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(89.99);            // Act
        assertEquals("B", grade);                          // Assert
    }

    @Test
    @DisplayName("60 оноо яг D дүн буюу тэнцэх доод хязгаар байх ёстой")
    void sixtyIsExactlyD() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(60.0);             // Act
        assertEquals("D", grade);                          // Assert
    }

    @Test
    @DisplayName("59.99 оноо F дүн буюу унасан байх ёстой (хязгаарын доод тал)")
    void justBelowSixtyIsF() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(59.99);            // Act
        assertEquals("F", grade);                          // Assert
    }

    @Test
    @DisplayName("0 оноо (зөвшөөрөгдөх хамгийн бага) F дүн байх ёстой")
    void zeroIsF() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(0.0);              // Act
        assertEquals("F", grade);                          // Assert
    }

    @Test
    @DisplayName("100 оноо (зөвшөөрөгдөх хамгийн их) A дүн байх ёстой")
    void hundredIsA() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(100.0);            // Act
        assertEquals("A", grade);                          // Assert
    }

    // ---------- letterGrade: буруу оролт ----------

    @Test
    @DisplayName("-1 оноо (сөрөг) IllegalArgumentException шидэх ёстой")
    void negativeScoreThrows() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        IllegalArgumentException ex = assertThrows(        // Act + Assert
                IllegalArgumentException.class,
                () -> calc.letterGrade(-1.0));
        assertTrue(ex.getMessage().contains("score"));     // Assert
    }

    @Test
    @DisplayName("101 оноо (100-аас хэтэрсэн) IllegalArgumentException шидэх ёстой")
    void overHundredThrows() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        IllegalArgumentException ex = assertThrows(        // Act + Assert
                IllegalArgumentException.class,
                () -> calc.letterGrade(101.0));
        assertTrue(ex.getMessage().contains("score"));     // Assert
    }

    @Test
    @DisplayName("NaN оноо чимээгүй F болохгүй, IllegalArgumentException шидэх ёстой")
    void nanScoreThrows() {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        assertThrows(IllegalArgumentException.class,       // Act + Assert
                () -> calc.letterGrade(Double.NaN));
    }

    // ---------- totalScore: зөв утгууд ----------

    @Test
    @DisplayName("Бүх хэсэг дээд оноотой (10, 40, 10, 10, 30) бол нийлбэр 100 байх ёстой")
    void allMaxSumsToHundred() {
        GradeCalculator calc = new GradeCalculator();                  // Arrange
        double total = calc.totalScore(10, 40, 10, 10, 30);           // Act
        assertEquals(100.0, total, DELTA);                             // Assert
    }

    @Test
    @DisplayName("Бүх хэсэг 0 оноотой бол нийлбэр 0 байх ёстой (доод хязгаар)")
    void allZeroSumsToZero() {
        GradeCalculator calc = new GradeCalculator();                  // Arrange
        double total = calc.totalScore(0, 0, 0, 0, 0);                // Act
        assertEquals(0.0, total, DELTA);                               // Assert
    }

    @Test
    @DisplayName("Бутархай оноонуудын нийлбэр (0.1 + 0.2) хөвөгч таслалын алдаагүй 0.3 байх ёстой")
    void fractionalScoresSumWithinDelta() {
        GradeCalculator calc = new GradeCalculator();                  // Arrange
        double total = calc.totalScore(0.1, 0.2, 0, 0, 0);            // Act
        assertEquals(0.3, total, DELTA);                               // Assert
    }

    // ---------- totalScore: буруу оролт ----------

    @Test
    @DisplayName("Ирц сөрөг (att = -5) бол IllegalArgumentException шидэх ёстой")
    void negativeAttendanceThrows() {
        GradeCalculator calc = new GradeCalculator();                  // Arrange
        IllegalArgumentException ex = assertThrows(                    // Act + Assert
                IllegalArgumentException.class,
                () -> calc.totalScore(-5, 40, 10, 10, 30));
        assertTrue(ex.getMessage().contains("att"));                   // Assert
    }

    @Test
    @DisplayName("Лаб дээд хязгаараас хэтэрсэн (lab = 41) бол IllegalArgumentException шидэх ёстой")
    void labOverMaxThrows() {
        GradeCalculator calc = new GradeCalculator();                  // Arrange
        IllegalArgumentException ex = assertThrows(                    // Act + Assert
                IllegalArgumentException.class,
                () -> calc.totalScore(10, 41, 10, 10, 30));
        assertTrue(ex.getMessage().contains("lab"));                   // Assert
    }

    // ---------- Parameterized тестүүд ----------

    @ParameterizedTest(name = "[{index}] {0} оноо → {1}")
    @DisplayName("letterGrade: босго бүр болон түүний яг доод талын утга")
    @CsvSource({"95,A", "90,A", "89.99,B", "80,B", "79.99,C", "70,C",
                "69.99,D", "60,D", "59.99,F", "0,F", "100,A"})
    void letterGradeBoundaries(double score, String expected) {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        String grade = calc.letterGrade(score);            // Act
        assertEquals(expected, grade);                     // Assert
    }

    @ParameterizedTest(name = "[{index}] {0} оноо → IllegalArgumentException")
    @DisplayName("letterGrade: 0-100 хязгаараас гадуурх оноо exception шидэх ёстой")
    @ValueSource(doubles = {-1, -0.01, 100.01, 101, Double.NaN,
                            Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void letterGradeRejectsOutOfRange(double score) {
        GradeCalculator calc = new GradeCalculator();      // Arrange
        assertThrows(IllegalArgumentException.class,       // Act + Assert
                () -> calc.letterGrade(score));
    }

    @ParameterizedTest(name = "[{index}] {0} + {1} + {2} + {3} + {4} = {5}")
    @DisplayName("totalScore: хязгаар доторх оноонуудын нийлбэр зөв байх ёстой")
    @CsvSource({
            "10,  40,    10,  10,  30,    100",
            "0,   0,     0,   0,   0,     0",
            "5,   20,    5,   5,   15,    50",
            "8.5, 32.25, 7,   9.5, 22.75, 80",
            "9.3, 35.3,  8.3, 8.9, 28.2,  90",
            "9.3, 35.3,  8.3, 7.1, 0,     60"
    })
    void totalScoreSums(double att, double lab, double quiz1, double quiz2, double exam,
                        double expected) {
        GradeCalculator calc = new GradeCalculator();                  // Arrange
        double total = calc.totalScore(att, lab, quiz1, quiz2, exam);  // Act
        assertEquals(expected, total, DELTA);                          // Assert
    }

    @ParameterizedTest(name = "[{index}] {5}: ({0}, {1}, {2}, {3}, {4}) → IllegalArgumentException")
    @DisplayName("totalScore: аль ч хэсэг сөрөг эсвэл дээд хязгаараас хэтэрвэл exception шидэх ёстой")
    @CsvSource({
            "-0.01, 40,    10, 10, 30, att",
            "10.01, 40,    10, 10, 30, att",
            "10,    -1,    10, 10, 30, lab",
            "10,    40.01, 10, 10, 30, lab",
            "10,    40,    -1, 10, 30, quiz1",
            "10,    40,    11, 10, 30, quiz1",
            "10,    40,    10, -1, 30, quiz2",
            "10,    40,    10, 11, 30, quiz2",
            "10,    40,    10, 10, -1, exam",
            "10,    40,    10, 10, 31, exam"
    })
    void totalScoreRejectsOutOfRange(double att, double lab, double quiz1, double quiz2,
                                     double exam, String field) {
        GradeCalculator calc = new GradeCalculator();                  // Arrange
        IllegalArgumentException ex = assertThrows(                    // Act + Assert
                IllegalArgumentException.class,
                () -> calc.totalScore(att, lab, quiz1, quiz2, exam));
        assertTrue(ex.getMessage().startsWith(field + " "),            // Assert
                "Буруу талбарыг мэдээлсэн: " + ex.getMessage());
    }

    @ParameterizedTest(name = "[{index}] {0} + {1} + {2} + {3} + {4} → {5}")
    @DisplayName("totalScore → letterGrade: нийлбэр яг босго дээр бол тухайн дүнг авах ёстой")
    @CsvSource({
            "10,  40,   10,  10,  30,    A",
            "9.3, 35.3, 8.3, 8.9, 28.2,  A",
            "9.3, 35.3, 8.3, 8.9, 28.19, B",
            "9.1, 38.3, 9.9, 7.1, 15.6,  B",
            "9.3, 35.3, 8.3, 8.9, 8.2,   C",
            "9.3, 35.3, 8.3, 7.1, 0,     D",
            "9.1, 30.9, 8.3, 8.9, 2.8,   D"
    })
    void totalThenLetterGrade(double att, double lab, double quiz1, double quiz2, double exam,
                              String expected) {
        GradeCalculator calc = new GradeCalculator();                  // Arrange
        double total = calc.totalScore(att, lab, quiz1, quiz2, exam);  // Act
        String grade = calc.letterGrade(total);                        // Act
        assertEquals(expected, grade, "нийлбэр = " + total);           // Assert
    }
}

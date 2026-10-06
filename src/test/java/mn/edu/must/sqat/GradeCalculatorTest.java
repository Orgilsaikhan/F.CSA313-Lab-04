package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}

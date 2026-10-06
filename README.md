# Lab 04 - B222270044 - Г. Оргилсайхан

**Сэдэв:** Нэгжийн тестийн эхлэл — JUnit 5 (F.CSA313, 2026)

```
$ java -version
openjdk version "25.0.4.1" 2026-08-18
OpenJDK Runtime Environment (build 25.0.4.1+1-1-26.04.4-Ubuntu)
OpenJDK 64-Bit Server VM (build 25.0.4.1+1-1-26.04.4-Ubuntu, mixed mode, sharing)

$ mvn -version
Apache Maven 3.9.12
Maven home: /usr/share/maven
Java version: 25.0.4.1, vendor: Ubuntu, runtime: /usr/lib/jvm/java-25-openjdk-amd64
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "7.0.0-34-generic", arch: "amd64", family: "unix"
```

Орчин: Ubuntu 26.04.1 LTS · OpenJDK 25 (`maven.compiler.release` = 17) · Maven 3.9.12 · JUnit 5.10.2 · Surefire 3.2.5

## Товч үр дүн

| Үзүүлэлт | Үр дүн |
|---|---|
| Тестийн методын тоо | **24** — 19 `@Test` + 5 `@ParameterizedTest` |
| `results/mvn-test.txt` | **Tests run: 60**, Failures: 0, Errors: 0, Skipped: 0 — BUILD SUCCESS |
| `results/mvn-test-mutant.txt` (`>= 90` → `> 90`) | Tests run: 60, **Failures: 3** — BUILD FAILURE, exit=1 |
| Тестээр илэрсэн жинхэнэ алдаа | `totalScore`-ын floating-point нийлбэр — яг 90 оноотой оюутан B авч байсан |

## Явц

- [x] Алхам 1 — Орчин бэлдэх (OpenJDK 25 JDK, Maven 3.9.12)
- [x] Алхам 2 — Maven төсөл үүсгэх, `pom.xml`-ийг JUnit 5-д тохируулах
- [x] Алхам 3 — `GradeCalculator` бичих
- [x] Алхам 4 — 19 нэгжийн тест (AAA, `@DisplayName`, хязгаарын утга, `assertThrows`)
- [x] Алхам 5 — 5 parameterized тест → floating-point алдаа илэрсэн → `results/mvn-test-float-bug.txt`, засвар
- [x] Алхам 6 — `mvn test` → `results/mvn-test.txt`; мутаци → `results/mvn-test-mutant.txt`
- [x] Дүгнэлт

## Репогийн бүтэц

```
pom.xml                                                  # JUnit 5.10.2, surefire 3.2.5, release 17
src/main/java/mn/edu/must/sqat/GradeCalculator.java      # тестлэгдэх класс
src/test/java/mn/edu/must/sqat/GradeCalculatorTest.java  # 24 тестийн метод
results/mvn-test.txt                                     # Алхам 6 — ногоон гаралт (Tests run: 60)
results/mvn-test-mutant.txt                              # Алхам 6 — >= 90 → > 90 мутацийн гаралт + exit code
results/mvn-test-float-bug.txt                           # Алхам 5 — засварын өмнөх улаан гаралт (floating-point алдаа)
```

Өөрөө ажиллуулж шалгах бол:

```bash
sudo apt install openjdk-25-jdk maven
mkdir -p results && mvn -B test 2>&1 | tee results/mvn-test.txt
```

`-B` (batch mode) нь гаралтад өнгөний ANSI код оруулахгүй тул `results/*.txt` файлууд цэвэр текст хэвээр үлдэнэ.

---

## Алхам 1: Орчин

Машин дээр зөвхөн `openjdk-25-jre` (runtime) суусан байсан тул `javac` ч, Maven ч байгаагүй. Заавар дахь `openjdk-17-jdk`-ийн оронд **`openjdk-25-jdk`** суулгасан: 17-г суулгавал `java` командын default нь 25-ын JRE хэвээр үлдэж, Maven "No compiler is provided in this environment" гэж унах эрсдэлтэй. Заавар "17 буюу түүнээс дээш" гэж зөвшөөрдөг бөгөөд `pom.xml`-д `release 17` тавьсан тул код Java 17-той нийцтэй хэвээр (JDK 25 дээр compile хийхдээ 17-оос хойш нэмэгдсэн API-г ашиглуулахгүй).

```bash
sudo apt update
sudo apt install openjdk-25-jdk maven
```

`mvn` ажиллах бүрт гаралтын эхэнд `WARNING: A terminally deprecated method in sun.misc.Unsafe has been called` гэсэн 4 мөр гардаг. Энэ нь Maven-ийн өөрийнх нь `guava.jar`-аас JDK 24+ дээр гардаг анхааруулга, миний кодтой холбоогүй, build-д нөлөөлөхгүй.

## Алхам 2: Maven төсөл

```bash
mvn archetype:generate -DgroupId=mn.edu.must.sqat \
  -DartifactId=lab04-junit -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.4 -DinteractiveMode=false
```

Үүсгэсэн төслийг `lab04-junit/` дэд хавтсанд биш, репогийн үндсэнд байрлуулсан (Lab 03-тай адил бүтэц). `pom.xml`-д хийсэн өөрчлөлт:

| Юу | Archetype-ийн анхных | Одоо | Яагаад |
|---|---|---|---|
| Java хувилбар | `maven.compiler.source/target` = 1.7 | `maven.compiler.release` = 17 | 1.7 дээр lambda (`assertThrows(..., () -> ...)`) compile хийгдэхгүй, JDK 20+ дээр "Source option 7 is no longer supported" гэж унана |
| Тестийн сан | `junit:junit:4.11` | `org.junit.jupiter:junit-jupiter:5.10.2` | `@DisplayName`, `@ParameterizedTest`, `assertThrows` |
| Surefire | 2.22.1 (`pluginManagement` дотор) | 3.2.5 (тэр мөрийг нь сольсон) | JUnit Platform-ийг найдвартай таних |
| `App.java`, `AppTest.java` | байсан | устгасан | `AppTest` нь JUnit 4 ашигладаг тул junit 4-гүйгээр compile хийгдэхгүй |

`.gitignore`-д `target/`, `.idea/`, `.DS_Store`, `.vscode/`, `*.docx` (багшийн заавар репод орохгүй) нэмсэн.

## Алхам 3: `GradeCalculator`

| Метод | Дүрэм | Буруу оролт |
|---|---|---|
| `letterGrade(score)` | `>= 90` A, `>= 80` B, `>= 70` C, `>= 60` D, бусад F | `score` < 0, > 100 эсвэл NaN → `IllegalArgumentException` |
| `totalScore(att, lab, quiz1, quiz2, exam)` | 5 хэсгийн нийлбэр (10 + 40 + 10 + 10 + 30 = 100) | Аль нэг хэсэг сөрөг, өөрийн дээд хязгаараас хэтэрсэн эсвэл NaN → `IllegalArgumentException` |

Хэрэгжүүлэлтийн онцлог:

- Бүх шалгалтыг нэг `requireInRange(name, value, max)` функцээр хийдэг. Exception-ий мессеж нь аль талбар буруу байсныг нэрээр нь хэлдэг (`lab нь 0-40 хооронд байх ёстой, оролт: 41.0`), тиймээс тест "exception шидсэн" гэдгээс гадна "**зөв** талбар дээр шидсэн" гэдгийг шалгаж чадна.
- **NaN-ийг тусад нь шалгасан.** `NaN < 0` ч, `NaN > 100` ч `false` тул энгийн `score < 0 || score > 100` шалгалтыг NaN давчихна. Дараа нь бүх `>=` харьцуулалт `false` болж, `letterGrade(NaN)` чимээгүйхэн `"F"` буцаана.
- `totalScore` нийлбэрээ `BigDecimal`-аар тооцдог — шалтгааныг Алхам 5-аас харна уу.

## Алхам 4: Нэгжийн тестүүд (19 `@Test`)

Тест бүр AAA бүтэцтэй, `@DisplayName`-ээр монгол нэртэй. Жишээ:

```java
@Test
@DisplayName("89.99 оноо A биш, B дүн байх ёстой (хязгаарын доод тал)")
void justBelowNinetyIsB() {
    GradeCalculator calc = new GradeCalculator();      // Arrange
    String grade = calc.letterGrade(89.99);            // Act
    assertEquals("B", grade);                          // Assert
}
```

| Бүлэг | Тестүүд | Тоо |
|---|---|---|
| `letterGrade` — ердийн утга | 95→A, 85→B, 75→C, 65→D, 30→F | 5 |
| `letterGrade` — хязгаарын утга | 90→A, 89.99→B, 60→D, 59.99→F, 0→F, 100→A | 6 |
| `letterGrade` — буруу оролт (`assertThrows`) | -1, 101, NaN | 3 |
| `totalScore` — зөв оролт | (10, 40, 10, 10, 30) → 100, бүгд 0 → 0, 0.1 + 0.2 → 0.3 | 3 |
| `totalScore` — буруу оролт (`assertThrows`) | att = -5, lab = 41 (мессежид `att` / `lab` байгааг шалгана) | 2 |
| **Нийт** | | **19** |

`double`-ийг `assertEquals(expected, actual, 1e-9)` гэж delta-тай харьцуулсан: Java-д `0.1 + 0.2 = 0.30000000000000004` тул delta-гүй бол `fractionalScoresSumWithinDelta` унана.

## Алхам 5: Parameterized тестүүд (5)

| Метод | Эх сурвалж | Мөр | Юуг шалгадаг |
|---|---|---|---|
| `letterGradeBoundaries` | `@CsvSource` | 11 | Заавар дахь жишээ + 79.99→C, 69.99→D, 100→A — **4 босго бүрийг хоёр талаас нь** |
| `letterGradeRejectsOutOfRange` | `@ValueSource` | 7 | -1, -0.01, 100.01, 101, NaN, +∞, -∞ → exception |
| `totalScoreSums` | `@CsvSource` | 6 | Хязгаар доторх оноонуудын нийлбэр |
| `totalScoreRejectsOutOfRange` | `@CsvSource` | 10 | 5 хэсэг тус бүрийг 0-ээс яг доош, дээд хязгаараас яг дээш; мессеж яг тэр талбарын нэрээр эхлэх ёстой (`quiz1`, `quiz2`-ыг андуурвал унана) |
| `totalThenLetterGrade` | `@CsvSource` | 7 | `totalScore`-ын гаралтыг `letterGrade`-д өгөхөд, нийлбэр яг босго дээр бол зөв дүн гарах |

**Tests run яагаад 60 вэ:** Surefire нь `@CsvSource` / `@ValueSource`-ийн мөр бүрийг тусдаа тест гэж тоолдог → 19 + 11 + 7 + 6 + 10 + 7 = **60**. Тестийн **метод** нь 19 + 5 = **24**.

### Хамгийн сонирхолтой олдвор: яг 90 оноотой оюутан B авч байсан

Эхний хувилбарт `totalScore` нь `return att + lab + quiz1 + quiz2 + exam;` байсан. `totalThenLetterGrade` тестийг нэмэхэд 7 мөрийн 5 нь унасан (`results/mvn-test-float-bug.txt`):

```
[ERROR]   GradeCalculatorTest.totalThenLetterGrade:267 нийлбэр = 89.99999999999999 ==> expected: <A> but was: <B>
[ERROR]   GradeCalculatorTest.totalThenLetterGrade:267 нийлбэр = 79.99999999999999 ==> expected: <B> but was: <C>
[ERROR]   GradeCalculatorTest.totalThenLetterGrade:267 нийлбэр = 69.99999999999999 ==> expected: <C> but was: <D>
[ERROR]   GradeCalculatorTest.totalThenLetterGrade:267 нийлбэр = 59.99999999999999 ==> expected: <D> but was: <F>
[ERROR]   GradeCalculatorTest.totalThenLetterGrade:267 нийлбэр = 59.99999999999999 ==> expected: <D> but was: <F>
[ERROR] Tests run: 60, Failures: 5, Errors: 0, Skipped: 0
```

| Оноо (att, lab, quiz1, quiz2, exam) | Жинхэнэ нийлбэр | `double` нийлбэр | Гарсан | Байх ёстой |
|---|---|---|---|---|
| 9.3, 35.3, 8.3, 8.9, 28.2 | 90 | 89.99999999999999 | B | **A** |
| 9.1, 38.3, 9.9, 7.1, 15.6 | 80 | 79.99999999999999 | C | **B** |
| 9.3, 35.3, 8.3, 8.9, 8.2 | 70 | 69.99999999999999 | D | **C** |
| 9.3, 35.3, 8.3, 7.1, 0 | 60 | 59.99999999999999 | F | **D** |
| 9.1, 30.9, 8.3, 8.9, 2.8 | 60 | 59.99999999999999 | F | **D** |

Сүүлийн хоёр мөр хамгийн аюултай нь — тэнцсэн оюутан **унасан** гэж гарч байна. 9.3, 8.3 гэх мэт аравтын бутархайг `double` яг дүрсэлж чаддаггүй тул нэмэх бүрт жижиг алдаа хуримтлагдаж, нийлбэр босгоос 0.00000000000001-ээр доош ордог.

**Яагаад `totalScoreSums` үүнийг бариагүй вэ?** Яг ижил `9.3, 35.3, 8.3, 8.9, 28.2 → 90` мөр тэнд ч бий, гэхдээ `assertEquals(90, 89.99999999999999, 1e-9)` PASS болно — delta алдааг "уучилсан". Алдаа зөвхөн хоёр методыг **хамт** шалгахад, `letterGrade`-ийн `>=` харьцуулалт delta-гүй ажиллахад л илэрсэн.

**Засвар:** нийлбэрийг `BigDecimal.valueOf(...)`-оор аравтын бутархайгаар тооцдог болгосон. `BigDecimal.valueOf(9.3)` нь `"9.3"` гэсэн мөрөөс үүсдэг тул нийлбэр яг `90` болно. `28.19 → B` мөр нь засвар дугуйлалт хийгээд 89.99-ийг A болгочихоогүйг шалгадаг. Commit-ийн түүхэнд эхлээд улаан тест, дараа нь засвар тусдаа commit-оор харагдана.

## Алхам 6: Тестүүдийг ажиллуулах ба мутаци

### Ногоон гаралт (`results/mvn-test.txt`)

```bash
mkdir -p results && mvn -B test 2>&1 | tee results/mvn-test.txt
```

```
[INFO] Tests run: 60, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

### Мутаци: `>= 90` → `> 90`

```diff
-        if (score >= 90) {
+        if (score > 90) {
```

```bash
mvn -B test 2>&1 | tee results/mvn-test-mutant.txt; echo "exit=${PIPESTATUS[0]}" >> results/mvn-test-mutant.txt
```

Lab 03-аас сурсанчлан `$?` биш `${PIPESTATUS[0]}` ашигласан — `| tee`-ийн дараах `$?` нь tee-ийн 0-ийг буцаадаг.

Үр дүн (`results/mvn-test-mutant.txt`) — **Tests run: 60, Failures: 3**, BUILD FAILURE, `exit=1`:

| Унасан тест | Мессеж |
|---|---|
| `ninetyIsExactlyA` | `expected: <A> but was: <B>` |
| `letterGradeBoundaries` [2] (`90, A`) | `expected: <A> but was: <B>` |
| `totalThenLetterGrade` [2] (`9.3, 35.3, 8.3, 8.9, 28.2 → A`) | `нийлбэр = 90.0 ==> expected: <A> but was: <B>` |

`95→A`, `100→A`, `89.99→B` тестүүд PASS хэвээр үлдсэн — эдгээр нь мутантыг "алж" чадахгүй, учир нь `>=` ба `>` зөвхөн яг 90 дээр л өөр үр дүн өгдөг. Хэрэв би зөвхөн ердийн утгуудыг тестэлсэн бол бүх тест ногоон хэвээр, мутант амьд үлдэх байсан.

Дараа нь `>= 90`-ийг буцааж засаад (`git diff` хоосон) `mvn test`-ийг дахин ажиллуулсан → `results/mvn-test.txt` Tests run: 60, BUILD SUCCESS.

### Нэмэлт: бусад 3 босгод мөн мутаци хийж үзсэн

Ижил аргаар `>= 80`, `>= 70`, `>= 60`-ийг нэг нэгээр нь `>` болгож ажиллуулаад буцааж зассан (гаралтыг хадгалаагүй):

| Мутаци | Failures | Алсан тестүүд |
|---|---|---|
| `>= 80` → `> 80` | 2 | `letterGradeBoundaries` (80, B), `totalThenLetterGrade` (→ 80) |
| `>= 70` → `> 70` | 2 | `letterGradeBoundaries` (70, C), `totalThenLetterGrade` (→ 70) |
| `>= 60` → `> 60` | 4 | `sixtyIsExactlyD`, `letterGradeBoundaries` (60, D), `totalThenLetterGrade` ×2 (→ 60) |

80 ба 70-ын мутантыг ганц ч `@Test` метод бариагүй — заавар дахь заавал шалгах хязгаарын жагсаалтад (90, 89.99, 60, 59.99, 0, 100) 80, 70 байхгүй. Тэдгээрийг зөвхөн parameterized тестүүд алсан.

## Дүгнэлт

Нийт **24 тестийн метод** (19 `@Test` + 5 `@ParameterizedTest`) бичсэн бөгөөд `results/mvn-test.txt`-д **Tests run: 60**, Failures: 0, BUILD SUCCESS гарсан. `letterGrade`-ийн `>= 90`-ийг `> 90` болгосон мутацид `ninetyIsExactlyA`, `letterGradeBoundaries` [90, A], `totalThenLetterGrade` [→ 90] гэсэн 3 тест `expected: <A> but was: <B>` мессежтэй унаж BUILD FAILURE болсон, харин 95 ба 100 оноотой тестүүд ногоон хэвээр үлдсэн нь ийм алдааг зөвхөн яг хязгаарын утга л барьдгийг харуулсан. Мутацийг буцааж засаад `mvn test`-ийг дахин ажиллуулж, ногоон гаралтыг хадгалсан. Хамгийн сонирхолтой алдааг `totalThenLetterGrade` илрүүлсэн: `double`-ийн нийлбэрээс болж 9.3 + 35.3 + 8.3 + 8.9 + 28.2 = 89.99999999999999 гарч, яг 90 оноотой оюутан B, яг 60 оноотой оюутан F авч байсан. Ижил оролттой `totalScoreSums` тест 1e-9 delta-гийн ачаар PASS болж байсан тул метод бүрийг тусад нь тестлэхэд алдаа харагдаагүй, хоёр методыг хамт шалгахад л илэрсэн. Үүнийг `BigDecimal`-аар нэмдэг болгож зассан. Эндээс сурсан зүйл: pass болсон тест зөв тест гэсэн үг биш — delta нь "ойролцоо"-г зөвшөөрдөг ч `>=` харьцуулалт "ойролцоо" гэж юу болохыг мэддэггүй.

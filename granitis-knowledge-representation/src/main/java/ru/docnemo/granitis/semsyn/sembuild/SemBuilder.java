package ru.docnemo.granitis.semsyn.sembuild;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticalProperties;
import ru.docnemo.granitis.semsyn.buildmssr.mssr.Mssr;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.TextType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class SemBuilder {
    private final GrammaticAnalyzer grammaticAnalyzer;
    private Mssr matrix;
    //    private Integer numb1;
//    private int len1;
//    private int setind1;
//    private String beg1;
//    private List<String> sembase;
    private String[] sembase;
    //    private List<String> performers;
    private String[] performers;

    //    private String sem;
//    private List<String> semdes;
    private String[] semdes;
    private List<SitDesCrElement> sitDesCr;
    private int timeVarNumb;

    private int[] used;
    private List<Integer> conj;
    private String semrepr;


    public String buildSem(Mssr matrix) {
        log.debug("Sembuild matrix: {}", matrix);
        this.matrix = matrix;
        sitDesCr = new ArrayList<>();
//        sembase = new ArrayList<>();
        sembase = new String[matrix.size()];
//        performers = new ArrayList<>();
        performers = new String[matrix.size()];
//        semdes = new ArrayList<>();
        semdes = new String[matrix.size()];
//        used = new ArrayList<>();
        used = new int[matrix.size()];
        conj = new ArrayList<>();
        timeVarNumb = 0;
        semrepr = "";

        beginBuildSem();
        defSituation(); // page 78
        finishBuild();
        return semrepr;
    }

    String prepareSemBuild(Mssr matrix) {
        return switch (matrix.getTextType()) {
            case TextType.stat -> "";
            case TextType.imp -> "(Команда(#Оператор#, #Исполнитель#, #сейчас#, e1)";
            case TextType.genqs -> "Вопрос(х1≡Ист-знач(";
            case TextType.specqsRelat1 ->
                    "Вопрос(x1,"; // k1 : = R1 [mainpos, mcoord];  numb := Число ( R2 [ k1, morph])
            case TextType.specqsRelat2 ->
                    "Вопрос(S1,(Кач-cостав(S1,"; // k1 : = R1 [mainpos, mcoord];  numb := Число ( R2 [ k1, morph])
            case TextType.specqsRol -> "Вопрос(";
            case TextType.specqsQuant1 -> "Вопрос(x1,((x1≡Колич(";
            case TextType.specqsQuant2 ->
                    "Вопрос(x1, ((x1≡ Колич( S1)) ∧ Кач-cостав(S1, %s) ∧ Описание(произв %s * (Элемент, S1) : e1,".formatted("сит", "сит");
//            case TextType.specqsQuant2 -> {
//                String situationSort = "cит"; // надо вытягивать этот сорт из лбд как-то или передавать в пареметрах
//                yield "Вопрос(x1, ((x1≡ Колич( S1)) ∧ Кач-cостав(S1, %s) ∧ Описание(произв %s * (Элемент, S1) : e1,".formatted(situationSort, situationSort);
//            }
        };
    }

    String calculationCaseKind(int index, String textUnitClass, String sem) {
        return switch (textUnitClass) {
            case "прилаг" -> "Case1";
            case "конструкт" -> "Case2";
            case "сущ" -> {
                if (Objects.requireNonNullElse(matrix.get(index).getTerms().getFirst().getSubClass(), "").equals("имя")) {
                    yield "Case3";
                } else {
                    int numb1 = Objects.requireNonNullElse(matrix.get(index).getQuantity(), 0); // todo надо разобраться что сюда надо-то, ноль или млжно null
                    String ref = "нек";
                    String[] splitedSem = sem.split(" ");
                    String beg1 = splitedSem[0];
                    int len1 = splitedSem.length;
                    int setind1 = -1;
                    if (len1 >= 2 && splitedSem[1].equals("множ")) {
                        setind1 = 0; // todo передалать в bool. Это признак обозначения индивида, а не множества индивидов
                    }
//                    if ((numb1 == 0 || numb1 == 1) && beg1 == "ref" && setind1 == 0) {
                    if ((numb1 == 0 || numb1 == 1) && beg1 == ref && setind1 == 0) {
                        yield "Case4";
                    }
//                    if (numb1 == 0 && beg1 != "ref" && sem.isNotFunction()) { // не заню как делать проверку на не функцию
                    if (numb1 == 0 && !beg1.equals(ref)) { // sem1 не является обозначением функции из F(B(Cb)
//                        if (Objects.requireNonNullElse(matrix.get(index).getQuantity(), 0) == 1) { // todo надо разобраться что сюда надо-то, ноль или млжно null
                        if (grammaticAnalyzer.getGrammaticalProperties(matrix.get(index).getUnit()).getNumber().equals("ед")) { // todo надо разобраться что сюда надо-то, ноль или млжно null
                            yield "Case5";
                        } else {
                            yield "Case6";
                        }
                    }
//                    if (numb1 == 0 && setind1 == 1) {
                    yield "Case7";
//                    }
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + textUnitClass);
        };
    }

    void buildSemDes1(int k1, String sem1, String caseMark) { // работа с прилагательными
        if (matrix.get(k1 - 1).getAdjectivesNumber() == 0) {
            sembase[k1] = transform1(sem1);
        } else {
            sembase[k1] = sembase[k1 - 1] + transform1(sem1);
        }
    }

    void buildSemDes2(int k1, String sem1, String caseMark) { // работа с конструктами
        sembase[k1] = sem1;
        performers[k1] = matrix.get(k1).getUnit();
    }

    void buildSemDes3(int k1, String sem1, String caseMark) { // работа с названиями
        // Условие вызова: в позиции k1 расположено существительное, в позиции k1 + 1 расположено выражение в кавычках или апострофах.
        String name = matrix.get(k1 + 1).getUnit();
        if (!performers[k1].contains("*")) {
            performers[k1] = performers[k1] + "*(Название, %s)".formatted(name);
        } else {
            performers[k1] = performers[k1] + "(Название, %s)".formatted(name);
        }
    }

    void buildSemDes4(int k1, String sem1, String caseMark) { // работа с сущ собственными
        sembase[k1] = sem1;
        semdes[k1] = sembase[k1];
        String var1 = matrix.get(k1).getMark();
        performers[k1] = semdes[k1] + ":" + var1;
    }

    void buildSemDes5(int k1, String sem1, String caseMark) { // работа с сущ нарицательными
        if (Objects.nonNull(matrix.get(k1).getAdjectivesNumber()) && matrix.get(k1).getAdjectivesNumber() > 0) {
            sembase[k1] = sem1 + "*" + sembase[k1 - 1];
        } else {
            sembase[k1] = sem1;
        }
        String ref = "нек";
        semdes[k1] = "%s %s".formatted(ref, sem1);
        String var = matrix.get(k1).getMark();
        performers[k1] = semdes[k1] + ":" + var;
    }

    void buildSemDes6(int k1, String sem1, String caseMark) { // Обработка сочетаний с существительными, обозначающих множества объектов
        Integer numb1 = matrix.get(k1).getQuantity();
        sembase[k1] = sem1;
//        if (numb1 > 0) {
        if (Objects.nonNull(numb1)) {
            semdes[k1] = "нек множ * (Колич, %d)(Кач-состав, %s)".formatted(numb1, sembase[k1]);
        } else {
            semdes[k1] = "нек множ * (Кач-состав, %s)".formatted(sembase[k1]);
        }
        String beg1 = sem1.split(" ")[0];
        String var1 = matrix.get(k1).getMark();
        String var2 = varsetmember(var1);

        performers[k1] = "произвольн %s * Элем(%s:%s):%s".formatted(
                beg1,
                semdes[k1],
                var1,
                var2
        );
    }

    void buildSemDes7(int k1, String sem1, String caseMark) { // Обработка собирательных существительных
        if (!matrix.get(k1 - 1).getTerms().getFirst().getPartOfSpeech().equals("прилаг")) {
            semdes[k1] = sem1;
        } else {
            semdes[k1] = transform2(semdes[k1 - 1], sem1);
        }
    }

    String varsetmember(String var1) {
        Pattern p = Pattern.compile("^.*(\\d+)$");
        Matcher m = p.matcher(var1);
        if (m.matches()) {
            return "y" + m.group(1);
        }
        throw new RuntimeException("Некорректнное именование переменной");
    }

    String transform1(String s) {
        Pattern p1 = Pattern.compile("(.*)\\((:?.*),\\s(.*)\\)");
        Pattern p2 = Pattern.compile("\\((.*)\\(:?.*\\)\\s≡\\s(.*)\\)");
        Matcher m1 = p1.matcher(s);
        Matcher m2 = p2.matcher(s);
        if (m1.hasMatch()) {
            return "(%s, %s)".formatted(m1.group(1), m1.group(2));
        } else if (m2.hasMatch()) {
            return "(%s, %s)".formatted(m2.group(1), m2.group(2));
        } else {
            throw new RuntimeException("Не преобразуемое значение");
        }
    }

    String transform2(String s, String t) {
        Pattern p = Pattern.compile("(.*Кач-состав,\\s)*(.*)(\\s*\\))");
        Matcher m = p.matcher(t);
        return m.group(1) + m.group(2) + "*" + s + m.group(3);
    }

    int numb(String s) {
        Pattern p = Pattern.compile(".*(\\d+)");
        Matcher m = p.matcher(s);
        if (!m.find()) {
            throw new RuntimeException("Не правильное название переменной");
        }
        return Integer.parseInt(m.group(1));
    }

    String stringVar(String s, int i) {
        return s + i;
    }

    void processSit(int k1) {
        GrammaticalProperties arMorph = grammaticAnalyzer.getGrammaticalProperties(matrix.get(k1).getUnit());
        timeVarNumb++;
        String timeVar = stringVar("t", timeVarNumb);
        String time1 = Objects.requireNonNullElse(arMorph.getTense(), "наст");

        String timeSit = switch (time1) {
            case "прош" -> "(Время, нек мом * (Раньше, #сейчас#) : %s)".formatted(timeVar);
            case "наст" -> "(Время, #сейчас#)";
            case "буд" -> "(Время, нек мом * (Позже, #сейчас#) : %s)".formatted(timeVar);
            default -> throw new RuntimeException("Не опознанное время"); // todo нормальный эксепшн
        };

        String concSit = matrix.get(k1).getTerms().getFirst().getMeaning();
        String var1 = matrix.get(k1).getMark();
        int numbsit = numb(var1);

        if (matrix.getTextType().equals(TextType.imp) && numbsit == 1) {
            sitDesCr.addFirst(SitDesCrElement.builder().expression("Цель (%s, %s*".formatted(var1, concSit)).build());
        } else {
//            sitDesCr.add(numbsit - 1, SitDesCrElement.builder().expression("Ситуация (%s, %s * %s".formatted(var1, concSit, timeSit)).build());
            sitDesCr.add(SitDesCrElement.builder().expression("Ситуация (%s, %s * %s".formatted(var1, concSit, timeSit)).build());
        }
    }

    void beginBuildSem() {
        semrepr = prepareSemBuild(matrix);

        for (int i = 0; i < matrix.size(); i++) {
            String class1 = matrix.get(i).getTerms().getFirst().getPartOfSpeech();

            if (!List.of("конструкт", "имя", "маркер").contains(class1)) {
                String sem1 = matrix.get(i).getTerms().getFirst().getMeaning();
            }
            if (List.of("глаг", "прич").contains(class1)) {
                processSit(i);
            }
            if (List.of("прилаг", "конструкт", "сущ").contains(class1)) {
                String sem1 = matrix.get(i).getTerms().getFirst().getMeaning();
                String caseMark = calculationCaseKind(i, class1, sem1);

                switch (caseMark) {
                    case "Case1" -> buildSemDes1(i, sem1, caseMark);
                    case "Case2" -> buildSemDes2(i, sem1, caseMark);
                    case "Case3" -> buildSemDes3(i, sem1, caseMark);
                    case "Case4" -> buildSemDes4(i, sem1, caseMark);
                    case "Case5" -> buildSemDes5(i, sem1, caseMark);
                    case "Case6" -> buildSemDes6(i, sem1, caseMark);
                    case "Case7" -> buildSemDes7(i, sem1, caseMark);
                }
            }
        }
    }

    String varBuild(String s) {
        if (s.startsWith("S")) {
            return "y" + s.substring(1);
        }
        return s;
    }

    void defSituation() {
        int numbsit = -1;
        String role = null;
        String actant = null;
        for (int m = 0; m < matrix.size(); m++) {
            String class1 = matrix.get(m).getTerms().getFirst().getPartOfSpeech();
            if (List.of("сущ", "конструкт").contains(class1)) {
//                int d = Objects.requireNonNullElse(matrix.get(m).getControl(), 0);  // todo надо разобраться что сюда надо-то, ноль или млжно null
                int d = matrix.get(m).getControlRelations().size();
                for (int q = 0; q < d; q++) {
                    int p1 = matrix.get(m).getControlRelations().get(q).getPosition();
                    String class2 = matrix.get(p1).getTerms().getFirst().getPartOfSpeech();

                    if (List.of("глаг", "прич").contains(class2)) {
                        String var2 = matrix.get(p1).getMark();
                        numbsit = numb(var2);
                        role = matrix.get(m).getControlRelations().getFirst().getRelation();
//                    }

//                    if (class1.equals("сущ")) {
                        if (!List.of(TextType.specqsRelat2, TextType.specqsQuant1).contains(matrix.getTextType())) {
                            if (used[m] == 0) {
                                actant = performers[m];
                            } else {
                                if (class1.equals("сущ")) {
                                    actant = varBuild(matrix.get(m).getMark());
                                }
                                if (class1.equals("конструкт")) {
                                    actant = matrix.get(m).getUnit();
                                }
                            }
                        } else {
                            if (used[m] == 1 || (used[m] == 0 && matrix.get(0).getMark().equals("S1"))) {
                                actant = varBuild(matrix.get(m).getMark());
                            } else {
                                actant = performers[m];
                            }
                        }
//                    }
                        sitDesCr.get(numbsit - 1).setExpression(sitDesCr.get(numbsit - 1).getExpression() + "(%s, %s)".formatted(role, actant));
                    }
                }
            }
        }

        for (int k = 0; k < matrix.size(); k++) {
            String class1 = matrix.get(k).getTerms().getFirst().getPartOfSpeech();

            if (List.of("сущ", "конструкт").contains(class1)) {
                Integer posMaster1 = matrix.get(k).getControlRelations().isEmpty()
                        ? null
                        : matrix.get(k).getControlRelations().getFirst().getPosition();
//                if (posMaster1 < 0) { // по логике здесь должна быть проверка на null
                if (Objects.isNull(posMaster1)) {
                    throw new RuntimeException("Неправильный текст");
                } else {
                    String class2 = matrix.get(posMaster1).getTerms().getFirst().getPartOfSpeech();

                    if (class2.equals("сущ")) {
                        String rel1 = matrix.get(k).getControlRelations().getFirst().getRelation();
                        String varMaster = matrix.get(posMaster1).getMark();

                        String arg2 = null;
                        String descr1 = null;
                        if (class1.equals("конструкт")) {
                            arg2 = sembase[k];
                        }

                        if (class1.equals("сущ")) {
                            String varDep = matrix.get(k).getMark();
                            if (used.length > k && used[k] == 0) {
                                arg2 = performers[k];
                            } else {
                                arg2 = varDep;
                            }
                            char letter = varMaster.charAt(0);
                            if (letter == 'x') {
                                descr1 = "%s(%s, %s)".formatted(rel1, varMaster, arg2);
                            } else {
                                String semhead = sembase[posMaster1].split(" ")[0];
                                descr1 = "%s(произв %s * (Элем, %s), %s)".formatted(rel1, semhead, varMaster, arg2);
                            }
                        }
                        int possit = matrix.get(posMaster1).getControlRelations().getFirst().getPosition();
                        String varsit = matrix.get(possit).getMark();
                        numbsit = numb(varsit);
                        sitDesCr.get(numbsit - 1).setExpression(
                                sitDesCr.get(numbsit - 1).getExpression() + "^" + descr1
                        );
//                        if (conj.get(numbsit - 1) == 0) {
                        conj.add(numbsit - 1, 1);
//                        }
                    }
                }
            }
        }
    }

    int right(int p, String partOfSpeech) {
        for (int i = p; i < matrix.size(); i++) {
            if (
                    partOfSpeech.equals(
                            matrix.get(i).getTerms().getFirst().getPartOfSpeech()
                    )
            ) {
                return i;
            }
        }
        throw new RuntimeException("Справа слово не найдено");
    }


    void finishBuild() {
        String situations = "";
        for (int k = 0; k < sitDesCr.size(); k++) {
            String event = sitDesCr.get(k).getExpression();
//            if (conj.get(k) == 1) {
//                event = "(%s)".formatted(event);
//            }
            if (k == 0) {
                situations = event;
            } else {
                situations = situations + "^" + event;
            }
        }
        if (sitDesCr.size() > 1) {
            situations = "(%s)".formatted(situations);
        }
//        String semrepr = "";
        semrepr = switch (matrix.getTextType()) {
            case TextType.stat -> situations;
            case TextType.imp -> semrepr + "^" + situations + ")";
            case TextType.genqs, TextType.specqsQuant2 -> semrepr + situations + ")))";
            case TextType.specqsRelat1 -> semrepr + situations + ")";
            case TextType.specqsRelat2, TextType.specqsQuant1 -> {
                int mainpos = matrix.getMainPos();
                int posmainnoun = right(mainpos, "сущ");
                String sem1 = sembase[posmainnoun];
                String semhead;
                if (!sem1.contains("*")) {
                    semhead = sem1;
                } else {
                    semhead = matrix.get(posmainnoun).getTerms().getFirst().getMeaning();
                }
                yield semrepr + " %s) ∧ Описание(произв %s*(Элем, S1):y1, %s))".formatted(sem1, semhead, situations);
            }
            case TextType.specqsRol -> {
                String unknowns = "x1";
                for (int i = 2; i < matrix.getQuestionWordsNumber(); i++) {
                    unknowns = unknowns + "^" + stringVar("x", i);
                }
                yield semrepr + "(%s), %s)".formatted(unknowns, situations);
            }
        };
    }

}

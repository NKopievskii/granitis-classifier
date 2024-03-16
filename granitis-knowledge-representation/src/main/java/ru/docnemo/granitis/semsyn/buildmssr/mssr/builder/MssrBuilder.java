package ru.docnemo.granitis.semsyn.buildmssr.mssr.builder;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.docnemo.granitis.semsyn.buildmssr.mssr.element.ControlRelation;
import ru.docnemo.granitis.semsyn.buildmssr.mssr.element.MssrElement;
import ru.docnemo.granitis.semsyn.buildmssr.relation.VerbNounRelation;
import ru.docnemo.granitis.semsyn.buildmssr.relation.finder.VerbNounRelationFinder;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.TextType;
import ru.docnemo.granitis.semsyn.buildmssr.frame.QuestionRoleFrame;
import ru.docnemo.granitis.semsyn.buildmssr.frame.Term;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.LexicalDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.QuestionRoleFrameDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.mssr.Mssr;
import ru.docnemo.granitis.semsyn.buildmssr.relation.TwoNounRelation;
import ru.docnemo.granitis.semsyn.buildmssr.relation.finder.TwoNounRelationFinder;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.Meta;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.identifier.TextTypeIdentifier;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class MssrBuilder {
    private final LexicalDictionary lexicalDictionary;
    private final QuestionRoleFrameDictionary questionRoleFrameDictionary;
    private final TwoNounRelationFinder twoNounRelationFinder;
    private final VerbNounRelationFinder verbNounRelationFinder;
    private final TextTypeIdentifier textTypeIdentifier;
    private final GrammaticAnalyzer grammaticAnalyzer;

    private Mssr mssr;

    private TextType kindText;
    private String leftPreposition;
    private Integer leftNumber;

    private List<Integer> questionWordPositions;
    private Integer depth;

    private List<DepthMeta> positionByDepth;

    record AdjectivePosition(Integer position, String meaning) {
    }

    private List<AdjectivePosition> adjectives;

    private int situationNumber;

    private int entityNumber;

    private int[] controllingUnitNumber;

    public Mssr buildMssr(String[] text) {
        Meta meta = textTypeIdentifier.identifyTextType(text);
        mssr = initMssr(text, meta);
        mssr.setMainPos(meta.getStartPosition()); // какая-та хрень, здесь должен быть mainPos
        kindText = meta.getTextType();
        leftPreposition = meta.getLeftPreposition(); // мб можно все это убрать

        questionWordPositions = new ArrayList<>();
        depth = 0;
        entityNumber = 0;
        adjectives = new ArrayList<>();
        positionByDepth = new ArrayList<>();
        positionByDepth.add(depth, new DepthMeta());
        controllingUnitNumber = new int[mssr.getMatrix().size()];
        situationNumber = 0;

        for (int i = meta.getStartPosition(); i < mssr.size(); i++) {
//            switch (grammaticAnalyzer.getGrammaticalProperties(text[i]).getPartOfSpeech()) {
            log.debug(mssr.get(i).getUnit());
            switch (mssr.get(i).getTerms().getFirst().getPartOfSpeech()) {
                // todo странная хрень, мб надо и не первый, мб надо фильтровать чтобы у всех был одинаковый
                case "предлог" -> processPrepositions(i);
                case "прилаг" -> processAdjective(i);
                case "колич-числит" -> processQuantitativeNumeral(i);
                case "сущ" -> processNoun(i);
                case "местом" -> processPronoun(i);
                case "наречие" -> processAdverb(i);
                case "глаг" -> processVerb(i);
                case "прич" -> processVerbFormParticiple(i);
                case "союз" -> {
                }
//                case "констр" -> {}
//                    processConstruct();
// мб надо перенастроить сюда нахождение отношений между глаголом и констурктом
                case "имя" -> processName(i);
                case "маркер" -> {
                    if (text[i].equals(",")) {
                        processComma(i);
                    }
                }
                default -> {
                    throw new RuntimeException(); // todo Нормальный эксепшн надо
                }

            }
        }
        return mssr;
    }

    Mssr initMssr(String[] text, Meta meta) {
        List<MssrElement> matrix = Arrays
                .stream(text)
                .map(
                        word -> MssrElement
                                .builder()
                                .unit(word)
                                .terms(lexicalDictionary.findTerms(word))
                                .controlRelations(new ArrayList<>())
                                .build()
                )
                .toList();
        log.info("matrix {}", matrix);
        return Mssr.builder().textType(meta.getTextType()).matrix(matrix).build();
    }

    void processPrepositions(int p) {
//        this.leftPreposition = text[p];
        this.leftPreposition = mssr.get(p).getUnit();
    }

    void processQuantitativeNumeral(int p) {
        this.leftNumber = Integer.valueOf(mssr.get(p).getUnit());
    }

    void processName(int p) {
        // где гарантия, что перед ним существительное
        mssr.getMatrix().get(p).getControlRelations().add(new ControlRelation(p - 1, "Название"));
    }

    void processAdjective(int p) {
        adjectives.add(new AdjectivePosition(p, lexicalDictionary.findTerm(mssr.get(p).getUnit()).getMeaning()));
    }

    void processAdverb(int p) {
//        if (getMorhPrisnak(text[p + 1]).getSubClass().equals("вопр-относ-местоим") && depth == 0) {
        if (
                lexicalDictionary
                        .findTerm(
                                mssr.get(p).getUnit()
                        )

                        .getSubClass()
                        .equals("вопр-относ-местоим")
                && depth == 0
        ) {
            leftPreposition = null; // а какого собствена хрена мы его здесь зануляем?
            processRoledQuestionWord(p);
        }
    }

    void processRoledQuestionWord(int p) {
        questionWordPositions.add(p);
        String word = mssr.get(p).getUnit();

//        QuestionRoleFrame frame = getMorhPrisnak(word).getSubClass().equals("вопр-относ-местоим")
        QuestionRoleFrame frame = lexicalDictionary
                .findTerm(word)

                .getSubClass()
                .equals("вопр-относ-местоим")
                ? questionRoleFrameDictionary.findByPrepositionAndQuestionWord(leftPreposition, word)
                : questionRoleFrameDictionary.findByQuestionWord(word);

        mssr
                .get(p)
                .getControlRelations()
                .add(
                        new ControlRelation(
                                null,
                                frame.getFrameMeaning()
                        )
                ); // todo а кто управляет-то??????
        mssr.get(p).setMark("x" + entityNumber);
    }

    void processComma(Integer p) {
        Term nextTerm = lexicalDictionary.findTerm(mssr.get(p + 1).getUnit());
        Term nextNextTerm = lexicalDictionary.findTerm(mssr.get(p + 2).getUnit());
        if (
                !(
                        kindText.equals(TextType.specqsRol)
                        && depth == 0
                        && positionByDepth.get(depth).getVerbPosition() == null
                        && (
                                nextTerm.getSubClass().equals("вопр-относ-местоим")
                                || nextTerm.getSubClass().equals("местоим-наречие")
                                || (
                                        nextTerm.getPartOfSpeech().equals("предлог")
                                        && nextNextTerm.getSubClass().equals("вопр-относ-местоим")
                                )
                        )
                )
        ) {
            if (
                    nextTerm.getPartOfSpeech().equals("причаст")
                    || nextTerm.getSubClass().equals("вопр-относ-местоим")
                    || (
                            nextTerm.getPartOfSpeech().equals("предлог")
                            && nextNextTerm.getSubClass().equals("вопр-относ-местоим")
                    )
            ) {
                depth += 1;
                positionByDepth.add(depth, new DepthMeta());
            }
//            verbposmag[depth] = 0;
//            numbfreedep[depth] = 0;
//            posfreedep[depth] = new Integer[4];
//            positionByDepth.set(depth, DepthMeta.empty());
            positionByDepth.get(depth).getFreeUnitsPositions().clear();
            depth -= 1;
        }
    }

    void processPronoun(int p) {
        Term nextTerm = lexicalDictionary.findTerm(mssr.get(p + 1).getUnit());
        Term previousTerm = lexicalDictionary.findTerm(mssr.get(p - 1).getUnit());
        if (
                nextTerm.getSubClass().equals("вопр-относ-местоим")
                && depth == 1
                && positionByDepth.get(depth).getVerbPosition() == 0
        ) {
            processRoledQuestionWord(p);
        } else if (
                (!mssr.get(p - 1).equals(",") && !previousTerm.getPartOfSpeech().equals("предлог"))
                || (previousTerm.getPartOfSpeech().equals("предлог") && !mssr.get(p - 2).equals(","))) {
            depth++;
            positionByDepth.set(depth, new DepthMeta());
        }
        int controlNoun = findNoun(p);
        if (controlNoun != -1) {
            mssr.get(p).setControl(controlNoun);
        }
        positionByDepth.get(depth).getFreeUnitsPositions().add(p);
    }

    int findNoun(int p) {
//        int verbPosition = verbposmag[depth - 1];
        Integer verbPosition = positionByDepth.get(depth - 1).getVerbPosition();
        for (int i = p; i >= 0; i--) {
            if (
                    lexicalDictionary.findTerm(mssr.get(i).getUnit()).getPartOfSpeech().equals("сущ")
                    && (
                            mssr
                                    .get(i)
                                    .getControlRelations()
                                    .isEmpty()
                            || Objects.equals(
                                    mssr
                                            .get(i)
                                            .getControlRelations()
                                            .get(controllingUnitNumber[i] - 1) //
                                            .getPosition(),
                                    verbPosition
                            )
                    )
//                                       && (
//                            Objects.equals(
//                                    mssr
//                                            .get(i)
//                                            .getControlRelations()
//                                            .get(controllingUnitNumber[i])
//                                            .getPosition(),
//                                    verbPosition
//                            )
//                                    || Objects.equals(
//                                    mssr
//                                            .get(i)
//                                            .getControlRelations()
//                                            .get(controllingUnitNumber[i])
//                                            .getPosition(),
//                                    0
//                            )
//            )
            ) {
                return i;
            }
        }
        return -1;
//        throw new RuntimeException("Не удалось найти существительное"); // todo нормальный ексепшн сделать
    }

    void processVerb(int p) {
        situationNumber++;
        mssr.get(p).setMark("e" + situationNumber);
        positionByDepth.get(depth).setVerbPosition(p);
        if (
                lexicalDictionary.findTerm(
                                mssr
                                        .get(p)
                                        .getUnit()
                        )

                        .getPartOfSpeech()
                        .equals("глаг")
                && depth == 1
                && !questionWordPositions.isEmpty()
        ) {
            for (int p1 : questionWordPositions) {
                // Выражение ниже полное говно, скорее всего надо переделать, чтобы обработка вопросных слов была здесь.
                mssr
                        .get(p1)
                        .getControlRelations()
                        .stream()
                        .filter(
                                controlRelation -> Objects.isNull(
                                        controlRelation.getPosition()
                                )
                        )
                        .forEach(controlRelation -> controlRelation.setPosition(p));
            }
            mssr.setQuestionWordsNumber(questionWordPositions.size());
            questionWordPositions.clear();
        }
        if (!positionByDepth.get(depth).getFreeUnitsPositions().isEmpty()) {
            for (Integer i : positionByDepth.get(depth).getFreeUnitsPositions()) {
                identifySemanticRelationVerbForm(i, p);
                /* Я кажется нахрен понял как работает numbfreedep и pos-free-dep,
                 * в pos-free-dep собираются все элементы без управляющего без привязки к своего номера к id
                 * а в numbfreedep ведется их количество
                 * */
            }
        }
    }

    void processVerbFormParticiple(int p) {
        situationNumber++;
        mssr.get(p).setMark("e" + situationNumber);
//        verbposmag[depth] = p;
        positionByDepth.get(depth).setVerbPosition(p);

        Term term = lexicalDictionary.findTerm(mssr.get(p).getUnit());
//        if (getMorhPrisnak(text[p]).getTClass().equals("прич")) {
        if (term.getPartOfSpeech().equals("прич")) {
//            if (!mssr.get(p - 1).getUnit().equals(",")) {
            depth++;
            // todo надо как-то научиться управлять вложенность автоматически
            positionByDepth.add(depth, new DepthMeta());
            positionByDepth.get(depth).setVerbPosition(p);
//            }
            int nounPosition = findNoun(p); // todo а если не найдено то что???
            if (nounPosition != -1) {
                mssr.get(p).setControl(nounPosition);
//            posVb = p;
//            posdepword = nounPosition;

//            String class1 = "сущ";
//            String subclass = term.getSubClass();
                controllingUnitNumber[nounPosition]++; // todo что это за срань, мб стоит его добавить в мссп
                identifySemanticRelationVerbForm(nounPosition, p);
            }
        }
    }

    private void identifySemanticRelationVerbForm(Integer p, Integer verbPosition) {
        List<VerbNounRelation> relations = findSetTematicRoles(p, verbPosition);

        Integer selectedRelationIndex;

        // имеется ввиду, что если вариант только один, то он и используется
        if (Objects.nonNull(relations) && relations.size() == 1) {
            selectedRelationIndex = 0;
        } else {
            // todo сделтаь нормальный ексепшн
            throw new RuntimeException("Неопределенность глагольной формы или пустой массив отношений");
//            chooseTematicRole();
        }

        // НАдо бы проверить, вообще для глагола это заполняется??????
        mssr.get(verbPosition).getControlRelations().clear(); // убираем все управление от глагольной формы

        mssr
                .get(p)
                .getControlRelations()
                .add(
                        new ControlRelation(
                                verbPosition,
                                relations.get(selectedRelationIndex).getRole()
                        )
                );
    }

    private void chooseTematicRole() {
        // Здесь взаимодейтсвие с пользователем, не реализуется в этой версии
    }

    private List<VerbNounRelation> findSetTematicRoles(Integer posdepword, Integer verbPosition) {
        Term term = lexicalDictionary.findTerm(mssr.get(posdepword).getUnit());
        String class1 = term.getPartOfSpeech();
        String subclass1 = term.getSubClass();
        String preposition = mssr.get(posdepword).getPrep();
        // todo этот метод надо разделить на 2
        if (class1.equals("сущ") || (class1.equals("местоим") && subclass1.equals("вопрос-относ-местоим"))) {
            return verbNounRelationFinder
                    .findRelation(
                            mssr.get(verbPosition).getUnit(),
                            mssr.get(verbPosition).getControl(),
                            mssr.get(posdepword).getUnit(),
                            posdepword,
//                            mssr.get(mssr.get(posdepword).getControl()).getUnit(),
                            mssr.get(verbPosition).getUnit(),
                            preposition
                    );
        }
        if (class1.equals("констр")) {
            return findSetRelVerbConstruct(verbPosition, preposition);
        }
        return null;
    }

    private List<VerbNounRelation> findSetRelVerbConstruct(Integer verbPosition, String preposition) {
//        sort здесь понимается, что это конструкт, в свою очередь конструкт это подкласс
        List<VerbNounRelation> relations = verbNounRelationFinder
                .findRelationForConstruct(
                        mssr.get(verbPosition).getUnit(),
                        preposition
                );
//        leftPreposition = null; // todo так по алгоритму, но мы же его не трогали, нахрена занулять
        return relations;
    }

    void processNoun(int p) {
        // прикрепление числительного
        if (Objects.nonNull(leftNumber)) { // А если прямо число 0 будет указано "сделать 0 раз"
            mssr.get(p).setQuantity(leftNumber);
            leftNumber = null;
        }
        // прикрепеление прилагательных
        if (!adjectives.isEmpty()) {
            for (AdjectivePosition adj : adjectives) {
//                mssr.get(adj.position).getControlRelations().add(1, new ControlRelation(p, adj.meaning));
                mssr.get(adj.position).getControlRelations().add(new ControlRelation(p, adj.meaning));
            }

            mssr.get(p).setAdjectivesNumber(adjectives.size());
            adjectives.clear();
        }
        // прикрепление предлога
        if (Objects.nonNull(leftPreposition)) {
            mssr.get(p).setPrep(leftPreposition);
            leftPreposition = null;
        }

        // является ситуацией?
//        Set<String> sorts = getSorts(text[p]);
        Set<String> sorts = lexicalDictionary.findTerm(mssr.get(p).getUnit()).getSorts();
//        if (!sorts.getFirst().name().equals("ситуация")) {
        if (!sorts.contains("ситуация")) {
            entityNumber++;
        }

//        int gramnumber = getMporphPrisnak(text[p]).getGramNumber();
        String gramnumber = grammaticAnalyzer.getGrammaticalProperties(mssr.get(p).getUnit()).getNumber();

        mssr.get(p).setMark(gramnumber.equals("ед") ? "x" + entityNumber : "S" + entityNumber);

        int leftNounPosition = findLeftNoun(p);
        if (leftNounPosition == 0) { // вообще 0ой существует, нужен null
//            if (positionByDepth.get(depth).getVerbPosition() == 0) {
            if (Objects.isNull(positionByDepth.get(depth).getVerbPosition())) {
                positionByDepth.get(depth).getFreeUnitsPositions().add(p);
            } else {
                Integer verbPosition = positionByDepth.get(depth).getVerbPosition();
                identifySemanticRelationVerbForm(p, verbPosition);
//                identifySemanticRelationVerbForm(posvb, p, mssr); todo так в алгоритме
            }
        } else { // leftNounPosition > 0
            List<TwoNounRelation> twoNounRelations = twoNounRelationFinder
                    .findRelation(
                            mssr.get(leftNounPosition).getUnit(),
                            mssr.get(p).getUnit(),
                            mssr.get(p).getPrep()
                    );

            if (twoNounRelations.isEmpty()) {
                Integer verbPosition = positionByDepth.get(depth).getVerbPosition();
//                if (verbPosition > 0) {
                if (Objects.nonNull(verbPosition)) {
                    identifySemanticRelationVerbForm(leftNounPosition, verbPosition);
                } else { // posVb == 0
                    positionByDepth.get(depth).getFreeUnitsPositions().add(p);
//                    numbfreedep[depth]++;
//                    posfreedep[depth][numbfreedep[depth]] = p;
                }
            }
            if (!twoNounRelations.isEmpty()) {
                Integer verbPosition = positionByDepth.get(depth).getVerbPosition();
                if (Objects.nonNull(verbPosition)) {
//                    List<VerbNounRelation> verbNounRelations = findSetTematicRoles(leftNounPosition, verbPosition);
                    List<VerbNounRelation> verbNounRelations = findSetTematicRoles(p, verbPosition);
                    if (verbNounRelations.isEmpty()) {
                        Integer selectedNounNounRelationIndex;
                        if (twoNounRelations.size() == 1) {
                            selectedNounNounRelationIndex = 0;
                        } else {
//                            selectedNounNounRelationIndex = chooseRelBetweenNoun1Noun2();
//                          todo нормальный эксепшн
                            throw new RuntimeException("Не однозначность отношений между существительными");
                        }
//                        добаить инфу в mssr ??????????????????
                        mssr
                                .get(p)
                                .getControlRelations()
                                .add(
                                        new ControlRelation(
                                                leftNounPosition,
                                                twoNounRelations.get(selectedNounNounRelationIndex).getRelation()
                                        )
                                );
//                                .set(
//                                        1,
//                                        new ControlRelation(
//                                                leftNounPosition,
//                                                twoNounRelations.get(selectedNounNounRelationIndex).getRelation()
//                                        )
//                                );
                    } else { // (!verbNounRelations.isEmpty())// всеь блок определяется пользователем
                        if (!twoNounRelations.isEmpty()) {
                            int selectedTypeControl = chooseControlVerbNoun();
                            Integer selectedVerbNounRelationIndex;
                            if (selectedTypeControl == 1) { // связь с глаголом
                                if (verbNounRelations.size() == 1) {
                                    selectedVerbNounRelationIndex = 1;
                                } else {
//                                    selectedVerbNounRelationIndex = chooseTematicRole(verbNounRelations)
                                    // todo нормальный эксепшн
                                    throw new RuntimeException("Не однозначность отношений между существительными "
                                                               + "и.или глаголом");

                                }
                                controllingUnitNumber[p]++;
                                mssr
                                        .get(p)
                                        .getControlRelations()
                                        .set(
                                                controllingUnitNumber[p],
                                                new ControlRelation(
                                                        verbPosition,
                                                        verbNounRelations.get(selectedVerbNounRelationIndex).getRole()
                                                )
                                        );
                            }
                            int selectedNounNounRelationIndex;
                            if (selectedTypeControl == 2) {
                                if (twoNounRelations.size() == 1) {
                                    selectedNounNounRelationIndex = 1;
                                } else {

//                            selectedNounNounRelationIndex = chooseRelBetweenNoun1Noun2(twoNounRelations);
// todo нормальный эксепшн
                                    throw new RuntimeException("Не однозначность отношений между существительными");

                                }
//                                Matr[posleftnoun, locunit] := arreln1n2 [m2, locn1]; Matr[posleftnoun, nval] :=1;
//                                Matr[pos, locunit] := arreln1n2 [m2, locn2]; Matr[pos, nval] := 1
                                mssr
                                        .get(p)
                                        .getControlRelations()
                                        .set(
                                                1,
                                                new ControlRelation(
                                                        leftNounPosition,
                                                        twoNounRelations
                                                                .get(selectedNounNounRelationIndex)
                                                                .getRelation()
                                                )
                                        );
                            }
                        }
                    }
                    // Это может ппц как не правильное место начала обработки собственнх имен, но вроде все логично
                    Term term = lexicalDictionary.findTerm(mssr.get(p).getUnit());
                    if (p + 1 < mssr.size()) {
                        Term nextTerm = lexicalDictionary.findTerm(mssr.get(p + 1).getUnit());
                        if (nextTerm.getSubClass().equals("сущ-собств") //  todo проблемы !!! не согласовано
                            && grammaticAnalyzer
                                    .getGrammaticalProperties(
                                            mssr.get(p).getUnit()
                                    )
                                    .getCase()
                                    .equals(
                                            grammaticAnalyzer
                                                    .getGrammaticalProperties(
                                                            mssr.get(p + 1).getUnit()
                                                    )
                                                    .getCase()
                                    )
                            && !Collections.disjoint(term.getSorts(), nextTerm.getSorts())) {
                            processProperNoun(p);
                        }
                        if (nextTerm.getSubClass().equals("имя")) { //  todo проблемы !!! не согласовано
                            processName(p + 1);
                        }
                    }
                }
            }
        }
    }

    private int chooseControlVerbNoun() {
        return 0; // заглушка, тут пользака надо справшивать
    }

    void processProperNoun(int p) {
        int i = p + 1;
        Term term = lexicalDictionary.findTerm(mssr.get(i).getUnit());
        while (term.getSubClass().equals("сущ-собств")) {
            mssr.get(i).getControlRelations().set(1, new ControlRelation(p, term.getMeaning()));
            i++;
            term = lexicalDictionary.findTerm(mssr.get(i).getUnit());
        }
    }

    int findLeftNoun(int p) {
        for (int i = p - 1; i >= 0; i--) {
            String class1 = lexicalDictionary.findTerm(mssr.get(i).getUnit()).getPartOfSpeech();
            if (class1.equals("сущ")) {
                return i;
            }
            if (List.of("глаг", "прич", "наречие", "местоим", "констр", "маркер").contains(class1)) {
                return 0;
            }
        }
        return 0;
    }
}

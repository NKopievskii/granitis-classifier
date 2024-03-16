package ru.docnemo.granitis.configuration;

import company.evo.jmorphy2.MorphAnalyzer;
import company.evo.jmorphy2.ResourceFileLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.docnemo.granitis.algsemdef1.AlgSemDef1;
import ru.docnemo.granitis.converter.PrepositionFrameConverter;
import ru.docnemo.granitis.converter.QuestionRoleFrameConverter;
import ru.docnemo.granitis.converter.TermConverter;
import ru.docnemo.granitis.converter.VerbPrepositionFrameConverter;
import ru.docnemo.granitis.core.repository.frame.PrepositionFrameRepository;
import ru.docnemo.granitis.core.repository.frame.QuestionRoleFramesRepository;
import ru.docnemo.granitis.core.repository.frame.VerbPrepositionFrameRepository;
import ru.docnemo.granitis.core.repository.lexical.SortRepository;
import ru.docnemo.granitis.core.repository.lexical.TermRepository;
import ru.docnemo.granitis.dictionary.DefaultLexicalDictionary;
import ru.docnemo.granitis.dictionary.DefaultPrepositionFrameDictionary;
import ru.docnemo.granitis.dictionary.DefaultQuestionRoleFrameDictionary;
import ru.docnemo.granitis.dictionary.DefaultSortDictionary;
import ru.docnemo.granitis.dictionary.DefaultVerbPrepositionFrameDictionary;
import ru.docnemo.granitis.grammatic.analyzer.DefaultGrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.SemSyn;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.LexicalDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.PrepositionFrameDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.QuestionRoleFrameDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.SortDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.frame.dictionary.VerbPrepositionFrameDictionary;
import ru.docnemo.granitis.semsyn.buildmssr.morph.GrammaticAnalyzer;
import ru.docnemo.granitis.semsyn.buildmssr.mssr.builder.MssrBuilder;
import ru.docnemo.granitis.semsyn.buildmssr.relation.finder.TwoNounRelationFinder;
import ru.docnemo.granitis.semsyn.buildmssr.relation.finder.VerbNounRelationFinder;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.Statement;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.Command;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.GenQs;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.SpecqsQuant1;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.SpecqsQuant2;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.SpecqsRelat1;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.SpecqsRelat2;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.SpecqsRolWithPrep;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.SpecqsRolWithoutPrep;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.identifier.TextTypeIdentifier;
import ru.docnemo.granitis.semsyn.sembuild.SemBuilder;
import ru.docnemo.granitis.service.StubService;

import java.io.IOException;
import java.util.List;

@Configuration
public class GranitisConfiguration {
    @Bean
    public StubService stubService(
            TermRepository termRepository
    ) {
        return new StubService(
                termRepository
        );
    }

    @Bean
    public QuestionRoleFrameDictionary questionRoleFrameDictionary(
            QuestionRoleFramesRepository questionRoleFramesRepository
    ) {
        return new DefaultQuestionRoleFrameDictionary(
                questionRoleFramesRepository,
                new QuestionRoleFrameConverter()
        );
    }

    @Bean
    public MorphAnalyzer morphAnalyzer() throws IOException {
        return new MorphAnalyzer.Builder()
                .fileLoader(new ResourceFileLoader("/company/evo/jmorphy2/ru/pymorphy2_dicts"))
                .charSubstitutes(null)
                .build();
    }

    @Bean
    public GrammaticAnalyzer grammaticAnalyzer(
            MorphAnalyzer morphAnalyzer,
            TermRepository termRepository
            ) {
        return new DefaultGrammaticAnalyzer(
                morphAnalyzer,
                termRepository
        );
    }

    @Bean
    public LexicalDictionary lexicalDictionary(
            TermRepository termRepository,
            GrammaticAnalyzer grammaticAnalyzer
    ) {

        return new DefaultLexicalDictionary(
                termRepository,
                grammaticAnalyzer,
                new TermConverter()
        );
    }

    @Bean
    public PrepositionFrameDictionary prepositionFrameDictionary(
            PrepositionFrameRepository prepositionFrameRepository
    ) {
        return new DefaultPrepositionFrameDictionary(
                prepositionFrameRepository,
                new PrepositionFrameConverter()
        );
    }

    @Bean
    public SortDictionary sortDictionary(
            SortRepository sortRepository
    ) {
        return new DefaultSortDictionary(
                sortRepository
        );
    }

    @Bean
    public TwoNounRelationFinder twoNounRelationFinder(
            GrammaticAnalyzer grammaticAnalyzer,
            LexicalDictionary lexicalDictionary,
            PrepositionFrameDictionary prepositionFrameDictionary,
            SortDictionary sortDictionary
    ) {
        return new TwoNounRelationFinder(
                grammaticAnalyzer,
                lexicalDictionary,
                prepositionFrameDictionary,
                sortDictionary
        );
    }

    @Bean
    public VerbPrepositionFrameDictionary verbPrepositionFrameDictionary(
            VerbPrepositionFrameRepository verbPrepositionFrameRepository
    ) {
        return new DefaultVerbPrepositionFrameDictionary(
                verbPrepositionFrameRepository,
                new VerbPrepositionFrameConverter()
        );
    }

    @Bean
    public VerbNounRelationFinder verbNounRelationFinder(
            GrammaticAnalyzer grammaticAnalyzer,
            LexicalDictionary lexicalDictionary,
            VerbPrepositionFrameDictionary verbPrepositionFrameDictionary
    ) {
        return new VerbNounRelationFinder(
                grammaticAnalyzer,
                lexicalDictionary,
                verbPrepositionFrameDictionary
        );
    }

    @Bean
    public TextTypeIdentifier textTypeIdentifier(
            GrammaticAnalyzer grammaticAnalyzer
    ) {
        return new TextTypeIdentifier(
                List.of(
                        new Command(grammaticAnalyzer),
                        new GenQs(grammaticAnalyzer),
                        new SpecqsQuant1(grammaticAnalyzer),
                        new SpecqsQuant2(grammaticAnalyzer),
                        new SpecqsRelat1(grammaticAnalyzer),
                        new SpecqsRelat2(grammaticAnalyzer),
                        new SpecqsRolWithoutPrep(grammaticAnalyzer),
                        new SpecqsRolWithPrep(grammaticAnalyzer)
                ),
                new Statement()
        );
    }

    @Bean
    public MssrBuilder mssrBuilder(
            QuestionRoleFrameDictionary questionRoleFrameDictionary,
            LexicalDictionary lexicalDictionary,
            TwoNounRelationFinder twoNounRelationFinder,
            VerbNounRelationFinder verbNounRelationFinder,
            TextTypeIdentifier textTypeIdentifier,
            GrammaticAnalyzer grammaticAnalyzer
    ) {
        return new MssrBuilder(
                lexicalDictionary,
                questionRoleFrameDictionary,
                twoNounRelationFinder,
                verbNounRelationFinder,
                textTypeIdentifier,
                grammaticAnalyzer
        );
    }

    @Bean
    public SemBuilder semBuilder(
            GrammaticAnalyzer grammaticAnalyzer
    ) {
        return new SemBuilder(
                grammaticAnalyzer
        );
    }

    @Bean
    public SemSyn semSyn(
            MssrBuilder mssrBuilder,
            SemBuilder semBuilder
    ) {
        return new SemSyn(
                mssrBuilder,
                semBuilder
        );
    }

    @Bean
    public AlgSemDef1 algSemDef1(
            GrammaticAnalyzer grammaticAnalyzer,
            LexicalDictionary lexicalDictionary,
            SemSyn semSyn
    ) {
        return new AlgSemDef1(
                grammaticAnalyzer,
                lexicalDictionary,
                semSyn
        );
    }
}

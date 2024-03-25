//package ru.docnemo.granitis.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ru.docnemo.granitis.core.domain.meaning.Meaning;
//import ru.docnemo.granitis.core.domain.meaning.MeaningType;
//import ru.docnemo.granitis.core.domain.meaning.SortDb;
//import ru.docnemo.granitis.core.repository.lexical.MeaningRepository;
//import ru.docnemo.granitis.core.repository.lexical.MeaningTypeRepository;
//import ru.docnemo.granitis.core.repository.lexical.SortRepository;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/meaning")
//@RequiredArgsConstructor
//public class MeaningController {
//    private final MeaningRepository meaningRepository;
//    private final MeaningTypeRepository meaningTypeRepository;
//    private final SortRepository sortRepository;
//
//    @GetMapping("/list")
//    public List<Meaning> getMeaningList() {
//        return meaningRepository.findAll();
//    }
//    @GetMapping("/type/list")
//    public List<MeaningType> getMeaningTypeList() {
//        return meaningTypeRepository.findAll();
//    }
//    @PostMapping
//    public Meaning createMeaning(
//            String meaning,
//            List<String> sorts,
//            String type
//    ) {
//        Meaning newMeanings = new Meaning();
//        newMeanings.setMeaning(meaning);
//        List<SortDb> sortDbs = sorts
//                .stream()
//                .distinct()
//                .map(sort -> sortRepository.findBySort(sort).orElseThrow(() -> new RuntimeException("Не найден сорт " + sort)))
//                        .collect(Collectors.toList());
//        newMeanings.setSorts(sortDbs);
//        newMeanings.setType(meaningTypeRepository.findByType(type).orElseThrow(() -> new RuntimeException("Не найден тип " + type)));
//        return meaningRepository.save(newMeanings);
//    }
//    @GetMapping("/sort/list")
//    public List<SortDb> getSortList() {
//        return sortRepository.findAll();
//    }
//    @PostMapping("/sort")
//    public SortDb createSort(String sort, String hyperonim) {
//        SortDb sortDb = new SortDb();
//        sortDb.setSort(sort);
//        sortDb.setHyperonim(sortRepository.findBySort(hyperonim).orElseThrow(() -> new RuntimeException("Нет такого гиперонима")));
//        return sortRepository.save(sortDb);
//    }
//}

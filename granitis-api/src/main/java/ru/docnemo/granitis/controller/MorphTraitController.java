//package ru.docnemo.granitis.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ru.docnemo.granitis.core.domain.lexical.MorphologicalTrait;
//import ru.docnemo.granitis.core.domain.lexical.MorphologicalTraitType;
//import ru.docnemo.granitis.core.repository.lexical.MorphRepository;
//import ru.docnemo.granitis.core.repository.lexical.MorphTraitTypeRepository;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/morph")
//@RequiredArgsConstructor
//public class MorphTraitController {
//    private final MorphRepository morphRepository;
//    private final MorphTraitTypeRepository morphTraitTypeRepository;
//
//    @GetMapping("/list")
//    public List<MorphologicalTrait> getMorphTraitList() {
//        return morphRepository.findAll();
//    }
//    @GetMapping("/type/list")
//    public List<MorphologicalTraitType> getMorphTraitTypeList() {
//        return morphTraitTypeRepository.findAll();
//    }
//
//    @PostMapping
//    public MorphologicalTrait createMorphTraitList(
//            String value,
//            String traitType
//
//    ) {
//        MorphologicalTrait trait = new MorphologicalTrait();
//        trait.setTrait(value);
//        trait.setType(
//                morphTraitTypeRepository
//                        .findByTraitType(traitType)
//                        .orElse(MorphologicalTraitType.builder().traitType(traitType).build())
//        );
//        return morphRepository.save(trait);
//    }
//}

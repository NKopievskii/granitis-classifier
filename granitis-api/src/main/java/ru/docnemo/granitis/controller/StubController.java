package ru.docnemo.granitis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.docnemo.granitis.algsemdef1.AlgSemDef1;
import ru.docnemo.granitis.dto.request.StubRequest;
import ru.docnemo.granitis.semsyn.SemSyn;

@RestController
@RequestMapping("/stub")
@RequiredArgsConstructor
public class StubController {
    private final SemSyn semSyn;
    private final AlgSemDef1 algSemDef1;

    @PostMapping("/1")
    public String getSemSyn(@RequestBody StubRequest request) {
        return semSyn.getSemRepresentation(request.getMessage());
    }

    @PostMapping("/2")
    public String getAlgSemDef1(@RequestParam String text, @RequestParam String domain) {
        return algSemDef1.getSemRepresentation(text.split(" "), domain);
    }
}

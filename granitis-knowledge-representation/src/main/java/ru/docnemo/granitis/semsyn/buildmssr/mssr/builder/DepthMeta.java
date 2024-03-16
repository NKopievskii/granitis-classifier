package ru.docnemo.granitis.semsyn.buildmssr.mssr.builder;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class DepthMeta {

    private Integer verbPosition;
    private List<Integer> freeUnitsPositions;

    public DepthMeta() {
        this.verbPosition = null;
        this.freeUnitsPositions = new ArrayList<>();
    }
}

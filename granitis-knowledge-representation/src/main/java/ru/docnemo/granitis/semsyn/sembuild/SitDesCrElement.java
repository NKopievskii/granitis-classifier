package ru.docnemo.granitis.semsyn.sembuild;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SitDesCrElement {
    private String mark;
    private String expression;
}

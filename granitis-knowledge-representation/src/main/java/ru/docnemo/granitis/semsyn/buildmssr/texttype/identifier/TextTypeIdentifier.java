package ru.docnemo.granitis.semsyn.buildmssr.texttype.identifier;

import lombok.RequiredArgsConstructor;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.DefaultTextTypeDeterminant;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.Meta;
import ru.docnemo.granitis.semsyn.buildmssr.texttype.determinant.specific.SpecificTextTypeDeterminant;

import java.util.List;

@RequiredArgsConstructor
public class TextTypeIdentifier {
    private final List<SpecificTextTypeDeterminant> specificTextTypeDeterminants;
    private final DefaultTextTypeDeterminant defaultTextTypeDeterminant;

    public Meta identifyTextType(String[] text) {
        List<SpecificTextTypeDeterminant> determinantList = specificTextTypeDeterminants
                .stream()
                .filter(determinant -> determinant.isThis(text))
                .toList();
        if (determinantList.size() > 1) {
            // todo сделать норм эксепшн
            throw new RuntimeException("Не удалось определеить тип текста. Опредено видов: " + determinantList.size());
        }
        if (determinantList.isEmpty()) {
            return defaultTextTypeDeterminant.getMeta(text);
        }
        return determinantList.getFirst().getMeta(text);
    }
}

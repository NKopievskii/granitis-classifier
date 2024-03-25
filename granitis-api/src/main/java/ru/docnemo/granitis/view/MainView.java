package ru.docnemo.granitis.view;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import ru.docnemo.granitis.algsemdef1.AlgSemDef1;

@Route
@UIScope
@PreserveOnRefresh
@SpringComponent
public class MainView extends HorizontalLayout {
    private static final String TEXT_AREA_HEIGHT = "300px";
    private static final String TEXT_AREA_WIDTH = "500px";

    public MainView(@Autowired AlgSemDef1 algSemDef1) {
        setPadding(true);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        TextArea textArea = new TextArea("Введите текст");
        textArea.addClassName("bordered");

        textArea.setMinHeight(TEXT_AREA_HEIGHT);
        textArea.setMinWidth(TEXT_AREA_WIDTH);

        TextArea outputTextArea = new TextArea("Результат");
        outputTextArea.addClassName("bordered");

        outputTextArea.setMinHeight(TEXT_AREA_HEIGHT);
        outputTextArea.setMinWidth(TEXT_AREA_WIDTH);
        outputTextArea.setReadOnly(true);

        Button button = new Button("Построить КП", e -> {
            try {
                outputTextArea.setValue(algSemDef1.getSemRepresentation(textArea.getValue().split(" "), ""));
            } catch (RuntimeException ex) {
                Notification.show(ex.getLocalizedMessage());
            }
        });
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        setAlignSelf(FlexComponent.Alignment.END, button);

        button.addClickShortcut(Key.ENTER);

        addClassName("centered-content");
        add(textArea, button, outputTextArea);
    }
}

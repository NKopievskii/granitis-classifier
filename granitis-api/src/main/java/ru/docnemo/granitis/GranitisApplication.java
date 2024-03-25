package ru.docnemo.granitis;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.PWA;
//import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@PWA(name = "Project Base for Vaadin with Spring", shortName = "Project Base")
//@Theme("my-theme")
@SpringBootApplication
public class GranitisApplication implements AppShellConfigurator {
    public static void main(String[] args) {
        SpringApplication.run(GranitisApplication.class, args);
    }
}

package by.i4t.isdo.export;

import by.i4t.isdo.export.config.RootConfig;
import by.i4t.isdo.export.service.MainService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by roman on 28.11.2016.
 */
@Component
public class Main {

    public static void main(final String[] args) {
        ApplicationContext appContext = new AnnotationConfigApplicationContext(RootConfig.class);
        MainService mainService = appContext.getBean(MainService.class);
    }

}
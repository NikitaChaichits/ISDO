package by.i4t.test;

import by.i4t.service.EduDocExportToGisunValidatingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfig.class})
public class GisunExportValidatingTest {
    @Autowired
    private EduDocExportToGisunValidatingService service;

    @Test
    public void test() {
        service.execute();
    }

}

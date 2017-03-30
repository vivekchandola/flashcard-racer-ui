import org.junit.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

public class TestEditTeacherFrontend {
	@Test
    public void testSimplePage(){
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
                browser.goTo("http://localhost:3333/teacher/edit/1");
                assertTrue(browser.pageSource().contains(""));
            });
}
}

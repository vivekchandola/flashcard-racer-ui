
import static org.junit.Assert.fail;

import javax.persistence.*;

import org.junit.FixMethodOrder;
import org.junit.Test;

import models.Teacher;

import play.mvc.*;
import play.test.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;
import java.util.*;

public class TestTeacherBean {

    @Test
    public void test() {
        running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
                // Mock database
                Teacher t = new Teacher();
                t.setName("Johan G");
                t.setId(42L);
                t.save();             

                Teacher k = Teacher.db().find(Teacher.class).where().eq("id",42L).findUnique();

                assertEquals("Johan G", k.getName());
                assertEquals(42L, k.getId());  		

            });
    }

}

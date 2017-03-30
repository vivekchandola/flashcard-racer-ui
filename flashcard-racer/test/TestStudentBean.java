import static org.junit.Assert.*;
import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import models.Student;

public class TestStudentBean {

	@Test
	public void test() {
		running(testServer(3333, fakeApplication(inMemoryDatabase())), HTMLUNIT, browser -> {
			// Mock database record
			setupStudent();
			
			Student s = Student.db().find(Student.class).where().eq("id", 1L).findUnique();

			
			//Test
			assertEquals("Studentname", s.getName());
			assertEquals(1L, s.getId());
			
			
			assertTrue(Arrays.equals("hash".getBytes(),s.getHash()));
			assertTrue(Arrays.equals("salt".getBytes(),s.getSalt()));
			
			assertTrue("salt".equals(new String(s.getSalt())));
			
			assertEquals(1L, s.getId());
			

		});
	}
    
	private void setupStudent() {
		Student t = new Student();
		t.setName("Studentname");
		
		String hash = "hash";
		String salt = "salt";
		
		byte[] hash_b = hash.getBytes();
		byte[] salt_b = salt.getBytes();
		
		t.setHash(hash_b);
		t.setSalt(salt_b);
		
		t.save();
		
	}
}

package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import com.google.inject.Guice;
import com.google.inject.Inject;

import models.Student;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.mvc.Result;
import play.test.Helpers;
import util.HasherSalter;

@RunWith(PowerMockRunner.class)
@PrepareForTest(HasherSalter.class)
public class TestEditStudentTest {

	@Inject
    Application application;

    private Result makeFakeRequest(String username, String password, String password2) {
    	Map<String,String> data = new HashMap<>();
    	
        data.put("login", username);
        data.put("password", password);
        data.put("verifyPassword", password2);
            
        Long id = 1L;
        String saveUrl = controllers.routes.EditStudentController.edit(id).url();
        return Helpers.route(application, Helpers.fakeRequest().bodyForm(data).method("POST").uri(saveUrl));
    }
    
    @Before
    public void setup() throws NoSuchAlgorithmException {
    	   PowerMockito.mockStatic(HasherSalter.class);
           when(HasherSalter.newSalt()).thenReturn("salted".getBytes());
           when(HasherSalter.hashString(any(byte[].class), any(String.class))).
        		   thenReturn("hashed".getBytes());
           
        GuiceApplicationBuilder builder = new GuiceApplicationLoader()
                .builder(new ApplicationLoader.Context(Environment.simple()));
        
        Guice.createInjector(builder.applicationModule()).injectMembers(this);
        Helpers.start(application);
    }
    
    @After
    public void cleanup(){
    	Helpers.stop(application);	
    }
    
    @Test
    public void testEditOneStudent(){
    	insertOneStudent();
    	
    	Result r = makeFakeRequest("newname", "newpass", "newpass");
    	
    	Student s = getStudent();
    	
    	assertEquals("newname", s.getName());
    	
    	assertHashedAndSalted(s);
    	
    	assertEquals(SEE_OTHER, r.status());
    }

    @Test
    public void testEditOneStudentOnlyPasswords(){
    	insertOneStudent();
    	
    	Result r = makeFakeRequest("", "newpass", "newpass");
    	
    	Student s = getStudent();
   
    	assertEquals("RemoveMe", s.getName());
    	
    	assertHashedAndSalted(s);

    	assertEquals(SEE_OTHER, r.status());
    }

    @Test
    public void testEditOneStudentOnlyName(){
    	insertOneStudent();
    	
    	Result r = makeFakeRequest("newname", "", "");
    	
    	Student s = getStudent();
    	
    	assertEquals("newname", s.getName());
    	
    	assertTrue(Arrays.equals(s.getHash(), "hash".getBytes()));
    	assertTrue(Arrays.equals(s.getSalt(), "salt".getBytes()));
		
    	assertEquals(SEE_OTHER, r.status());
    }
    
    @Test
    public void testEditOneStudentOnlyPassword(){
    	insertOneStudent();
    	
    	Result r = makeFakeRequest("", "newpass", "");
    	
    	assertErrorAndNoChange(r);
    }

	
    
    @Test
    public void testEditOneStudentOnlyVerifyPassword(){
    	insertOneStudent();
    	
    	Result r = makeFakeRequest("", "", "newpass");
    	
    	assertErrorAndNoChange(r);
    }

    @Test
    public void testEditOneStudentOnlyPasswordWithName(){
    	insertOneStudent();
    	
    	Result r = makeFakeRequest("newname", "newpass", "");
    	
    	assertErrorAndNoChange(r);
    }
    
    @Test
    public void testEditOneStudentOnlyVerifyPasswordWithName(){
    	insertOneStudent();
    	
    	Result r = makeFakeRequest("newname", "", "newpass");
    	
    	assertErrorAndNoChange(r);
    }
    
    @Test
    public void testEditOneStudentNonMatchingPasswords(){
    	insertOneStudent();
    	
    	Result r = makeFakeRequest("", "pass", "doesNotMatch");
    	
    	assertErrorAndNoChange(r);
    }
    
    private void assertErrorAndNoChange(Result r) {
		Student s = getStudent();
    	
    	assertStudentIsNotChanged(s);
		
    	assertEquals(BAD_REQUEST, r.status());
	}
    
	private void assertStudentIsNotChanged(Student s) {
		assertEquals("RemoveMe", s.getName());
    	
    	assertTrue(Arrays.equals(s.getHash(), "hash".getBytes()));
    	assertTrue(Arrays.equals(s.getSalt(), "salt".getBytes()));
	}
    
	private void assertHashedAndSalted(Student s) {
		assertTrue(Arrays.equals(s.getHash(), "hashed".getBytes()));
    	assertTrue(Arrays.equals(s.getSalt(), "salted".getBytes()));
	}

	private Student getStudent() {
		Student s = Student.db().find(Student.class).where().eq("id", 1L).findUnique();
		return s;
	}
        
	private void insertOneStudent() {
		Student s = new Student();
    	s.setName("RemoveMe");
    	s.setHash("hash".getBytes());
    	s.setSalt("salt".getBytes());
    	
    	s.save();
	}
    
	private List<Student> fetchAllStudents() {
		List<Student> studentList = Student.db().find(Student.class).findList();
		return studentList;
	}

}

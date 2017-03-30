package controllers;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;

import models.Student;
import models.Teacher;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.mvc.Result;
import play.test.Helpers;

import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.mvc.Http.Status.UNAUTHORIZED;

import static play.test.Helpers.*;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import util.HasherSalter;
import util.StudentAuthenticator;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(HasherSalter.class)

 public class TestCreateStudentController {
    
	@Inject
    Application application;
  
	
    private Result makeFakeRequest(String username, String password, String password2) {
    	Map<String,String> data = new HashMap<>();
        data.put("login", username);
        data.put("password", password);
        data.put("verifyPassword", password2);
            
        String saveUrl = controllers.routes.StudentController.createStudent().url();
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
         //       .overrides(testModule);
        Guice.createInjector(builder.applicationModule()).injectMembers(this);
        Helpers.start(application);
    }
    
    @After
    public void cleanup(){
    	Helpers.stop(application);	
    }
    
    
    @Test
    public void testCreateStudentOk() throws NoSuchAlgorithmException {
    	Result r = makeFakeRequest("validname", "password","password");
    	
    	List<Student> studentList = Student.db().find(Student.class).findList();
    	
    	assertEquals(studentList.size(),1);
    	
    	Student s = studentList.get(0);
    	assertTrue(s.getName().equals("validname"));
    	
    	assertIsHashedAndSalted(s);
    	
    	assertTrue(s.getName().equals("validname"));
    	
    	assertEquals(OK,r.status());
    	
    }
    
    

	private void assertIsHashedAndSalted(Student s) {
		assertTrue(Arrays.equals("hashed".getBytes(), s.getHash()));
    	assertTrue(Arrays.equals("salted".getBytes(), s.getSalt()));
	}
    
    @Test
    public void testCreateStudentPasswordNotEqual(){
    	Result r = makeFakeRequest("validname", "password", "WrongPassword");
    	
    	assertNoStudentCreatedAnd401(r);
    }
    
    @Test
    public void testCreateStudentNoPassword(){
    	Result r = makeFakeRequest("validname", "", "password");
    	
    	assertNoStudentCreatedAnd401(r);
    }

    @Test
    public void testCreateStudentNoVerifyPassword(){
    	Result r = makeFakeRequest("validname", "password", "");
    	
    	assertNoStudentCreatedAnd401(r);
    }
    
    @Test
    public void testCreateStudentNoName(){
    	Result r = makeFakeRequest("", "password", "password");
    	
    	assertNoStudentCreatedAnd401(r);
    }
    
    @Test
    public void testRemoveStudent(){
    	insertOneStudent();
    	
    	assertTrue(fetchAllStudents().size()==1);
    	
    	StudentController controller = new StudentController();
    	
    	Result result = controller.deleteStudent(new Long(1L));
    	
    	assertTrue(fetchAllStudents().isEmpty());
    	
    	assertEquals(SEE_OTHER, result.status());
    }

    @Test public void testRemoveStudentDontExists(){
    	insertOneStudent();
    	
    	assertTrue(fetchAllStudents().size()==1);
    	
    	StudentController controller = new StudentController();
    	Long thisIdDoNotExists = 123L;
    	Result result = controller.deleteStudent(new Long(thisIdDoNotExists));
    	
    	assertTrue(fetchAllStudents().size()==1);
    	
    	assertEquals(SEE_OTHER, result.status());	
    }
    
	private void insertOneStudent() {
		Student s = new Student();
    	s.setName("RemoveMe");
    	s.setHash("hash".getBytes());
    	s.setSalt("salt".getBytes());
    	
    	s.save();
	}
    
	private void assertNoStudentCreatedAnd401(Result r) {
		List<Student> studentList = fetchAllStudents();
    	
    	assertTrue(studentList.isEmpty());
    	assertEquals(BAD_REQUEST, r.status());
	}
    
	private List<Student> fetchAllStudents() {
		List<Student> studentList = Student.db().find(Student.class).findList();
		return studentList;
	}
    
}

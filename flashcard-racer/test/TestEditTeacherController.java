package controllers;

import static play.test.Helpers.HTMLUNIT;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;
import static play.test.Helpers.testServer;

import org.junit.Test;

import static play.test.Helpers.contentAsString;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.*;
import static play.test.Helpers.contentAsString;

import org.junit.runners.MethodSorters;
import org.junit.*;

import play.*;
import play.inject.guice.*;
import play.mvc.*;
import play.test.*;

import java.nio.CharBuffer;
import java.nio.charset.Charset;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;

import org.apache.http.client.methods.RequestBuilder;
import com.google.inject.*;
import java.util.*;

import controllers.TeacherLoginController;
import controllers.EditTeacherController;

import models.Teacher;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class TestEditTeacherController {

	    @Inject
	    Application application;

	    private void createUser() throws NoSuchAlgorithmException {
	    	char[] password = "123456".toCharArray();

	    	SecureRandom random = new SecureRandom();
	    	byte[] salt = new byte[256];
	    	random.nextBytes(salt);

	    	MessageDigest digest = MessageDigest.getInstance("SHA-256");
	    	digest.update(salt);
	    	digest.update(Charset.forName("UTF-8").encode(CharBuffer.wrap(password)).array());
	    	byte[] hash = digest.digest();
    		Teacher teacher = new Teacher();
    		teacher.setName("Johnny");
    		teacher.setSalt(salt);	
    		teacher.setHash(hash);
    		teacher.save();
	    }

	    private Result makeFakeRequest(String id, String username) {
	    	Map<String,String> data = new HashMap<>();
	        data.put("login", username);
	        data.put("password", "dummy");
	        data.put("verifyPassword", "dummy");
	        
	        String saveUrl = controllers.routes.EditTeacherController.edit(Long.parseLong(id)).url();
	        return Helpers.route(application, Helpers.fakeRequest().bodyForm(data).method("POST").uri(saveUrl));
	    }

	    @Before
	    public void setup() throws NoSuchAlgorithmException {

	        GuiceApplicationBuilder builder = new GuiceApplicationLoader()
	                .builder(new ApplicationLoader.Context(Environment.simple()));
	        Guice.createInjector(builder.applicationModule()).injectMembers(this);
	        Helpers.start(application);
	        createUser();
	    }
	    
	    @After
	    public void tearDown(){
	    	Helpers.stop(application);
	    }
	    
	    @Test
	    public void testUpdateName(){
	    	Result r = makeFakeRequest("1", "Benny");
	    	
	        Teacher k = Teacher.db().find(Teacher.class).where().eq("id",1L).findUnique();
	        assertEquals("Benny", k.getName());
            assertEquals(1L, k.getId());  		
            
            assertEquals(SEE_OTHER, r.status());
	    }
	    
}

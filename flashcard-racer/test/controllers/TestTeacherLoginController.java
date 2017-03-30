package controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.mvc.Http.Status.UNAUTHORIZED;
import static play.test.Helpers.contentAsString;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;

import models.Teacher;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.mvc.Result;
import play.test.Helpers;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestTeacherLoginController{

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
	teacher.setId(0L);
	teacher.setName("Johnny");
	teacher.setSalt(salt);
	teacher.setHash(hash);
	teacher.save();
    }

    private Result makeFakeRequest(String username, String password) {
    	Map<String,String> data = new HashMap<>();
        data.put("login", username);
        data.put("password", password);
        
        String saveUrl = controllers.routes.TeacherLoginController.login().url();
        return Helpers.route(application, Helpers.fakeRequest().bodyForm(data).method("POST").uri(saveUrl));
    }

    @Before
    public void setup() throws NoSuchAlgorithmException {
        AbstractModule testModule = new AbstractModule() {
            @Override
            public void configure() {
                // Install custom test binding here
            }
        };

        GuiceApplicationBuilder builder = new GuiceApplicationLoader()
                .builder(new ApplicationLoader.Context(Environment.simple()))
                .overrides(testModule);
        Guice.createInjector(builder.applicationModule()).injectMembers(this);
        Helpers.start(application);
	createUser();
    }
    
    @After
    public void tearDown(){
    	Helpers.stop(application);
    }
    
    @Test
    public void testLoginSucceeded() throws NoSuchAlgorithmException {
	Result result = makeFakeRequest("Johnny", "123456");

    	assertEquals(SEE_OTHER, result.status());
    	//assertTrue(contentAsString(result).contains("Welcome teacher!"));
    }

    @Test
    public void testLoginWrongPassword() throws NoSuchAlgorithmException {
	Result result = makeFakeRequest("Johnny", "12345");
	
    	assertEquals(UNAUTHORIZED, result.status());
    	assertTrue(contentAsString(result).contains("Wrong username or password"));
    }

    @Test
    public void testLoginMissingUser() throws NoSuchAlgorithmException {
	Result result = makeFakeRequest("Tommy", "123456");

    	assertEquals(UNAUTHORIZED, result.status());
    	assertTrue(contentAsString(result).contains("Wrong username or password"));
    }

    @Test
    public void testLoginEmptyFields(){
	Result result = makeFakeRequest("", "");

	assertEquals(BAD_REQUEST, result.status());
	assertTrue(contentAsString(result).contains("This field is required"));
    }

    @Test
    public void testLoginEmptyUsernameField(){
	Result result = makeFakeRequest("", "123456");

	assertEquals(BAD_REQUEST, result.status());
	assertTrue(contentAsString(result).contains("This field is required"));
    }

    @Test
    public void testLoginEmptyPasswordField(){
	Result result = makeFakeRequest("Johnny", "");

	assertEquals(BAD_REQUEST, result.status());
	assertTrue(contentAsString(result).contains("This field is required"));
    }
       
}

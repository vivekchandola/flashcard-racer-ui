package util;
import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;

import models.Student;
import models.Teacher;
import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.test.Helpers;
import util.StudentAuthenticator;

import static org.mockito.Matchers.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doReturn;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(StudentAuthenticator.class)
class TestAuthenticator {
    @Inject
    Application application;
	
	@Before
    public void setup() throws NoSuchAlgorithmException {
		
        GuiceApplicationBuilder builder = new GuiceApplicationLoader()
                .builder(new ApplicationLoader.Context(Environment.simple()));
        Guice.createInjector(builder.applicationModule()).injectMembers(this);
        Helpers.start(application);
    }
	
    @After
    public void tearDown(){
    	Helpers.stop(application);
    }
	
    @Test
	public void testUserDoesntExists() throws NoSuchAlgorithmException{
		assertFalse(StudentAuthenticator.authenticate("dont exists", "dont exists"));
	}
	
	@Test
	public void testUserExistsButWrongHashAndSalt() throws NoSuchAlgorithmException{
		Student s = new Student();
		s.setName("Felix");
		s.setHash("dsa".getBytes());
		s.setSalt("dsa".getBytes());
		
		s.save();
		
		StudentAuthenticator obj = new StudentAuthenticator();
		assertFalse(StudentAuthenticator.authenticate("Felix", "password"));	
	}
	
	@Test
	public void testUserExists() throws NoSuchAlgorithmException{
		Student s = new Student();
		s.setName("Felix");
		s.setHash("hash".getBytes());
		s.setSalt("salt".getBytes());
		s.save();
 	   
		PowerMockito.mockStatic(StudentAuthenticator.class);
 	   
		StudentAuthenticator spy = PowerMockito.spy( new StudentAuthenticator());
				
		doReturn("hash".getBytes()).when(spy).hashString(any(byte[].class),any(String.class));
		spy.authenticate("Felix", "password");
		assertTrue(spy.authenticate("Felix", "password"));	
	}
	
	
	
	
}

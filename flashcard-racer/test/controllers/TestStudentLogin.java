package controllers;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;

import play.Application;
import play.ApplicationLoader;
import play.Environment;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.mvc.Result;
import play.test.Helpers;
import util.HasherSalter;
import util.StudentAuthenticator;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.UNAUTHORIZED;
import static play.mvc.Http.Status.BAD_REQUEST;

import controllers.HomeController;

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest(StudentAuthenticator.class)
@PowerMockIgnore("javax.crypto.*")
public class TestStudentLogin {
	

    @Inject
    Application application;

	private Result makeFakeRequest(String username, String password) {
    	Map<String,String> data = new HashMap<>();
        data.put("login", username);
        data.put("password", password);
        
        
        String saveUrl = controllers.routes.HomeController.login().url();
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
        
        PowerMockito.mockStatic(StudentAuthenticator.class);
        
        Mockito.when(StudentAuthenticator.authenticate("name","password")).thenReturn(true);
        Mockito.when(StudentAuthenticator.authenticate("name2","password2")).thenReturn(false);
        
        //Helpers.start(application);
    
    }
 

    @Test
    public void tearDown(){
    	Helpers.stop(application);
    }
    
	@Test
	public void authenticationOk() throws Exception{
		Result r = makeFakeRequest("name", "password");
    	assertEquals(SEE_OTHER, r.status());
	}
	
	@Test
	public void authenticationUnauthorised() throws Exception{
		Result r = makeFakeRequest("name2", "password2");
    	assertEquals(UNAUTHORIZED, r.status());
	}
	
	@Test
	public void loginWithEmptyPassword() throws Exception{
		Result r = makeFakeRequest("name", "");
    	assertEquals(BAD_REQUEST, r.status());
	}
	
	@Test
	public void loginWithEmptyName() throws Exception{
		Result r = makeFakeRequest("", "password");
    	assertEquals(BAD_REQUEST, r.status());
	}
	
	@Test
	public void loginWithEmptyFields() throws Exception{
		Result r = makeFakeRequest("", "");
    	assertEquals(BAD_REQUEST, r.status());
	}
	
}

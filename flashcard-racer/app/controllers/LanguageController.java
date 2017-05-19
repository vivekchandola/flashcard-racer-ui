package controllers;



import play.i18n.Lang;

import play.mvc.Controller;
import play.mvc.Result;


public class LanguageController extends Controller {

    
    
    public Result index(String language) {
        Controller.changeLang(language);
        
        return redirect("/");
    }

 
}
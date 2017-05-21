package controllers;



import play.i18n.Lang;

import play.mvc.Controller;
import play.mvc.Result;


public class LanguageController extends Controller {

    
    
    public Result index(String language, String address) {
        Controller.changeLang(language);
        if(address.equals("0")){
            return redirect("/");
        }
        
        return redirect("/"+address);
    }

 
}
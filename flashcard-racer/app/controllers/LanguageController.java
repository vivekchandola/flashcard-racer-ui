package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class LanguageController extends Controller {

    /***
     * 
     * Change language on click of language button in the UI (defined in main.scala.html)
     * @param language
     * @param address
     * @return 
     */
    public Result index(String language, String address) {
        Controller.changeLang(language);
        if (address.equals("0")) {
            return redirect("/");
        }

        return redirect("/" + address);
    }

}
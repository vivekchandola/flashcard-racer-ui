package controllers;

import play.mvc.Controller;
import play.mvc.Result;

public class HomeController extends Controller {

    /**
     * 
     * Default home page from index.scala.html file
     * @return
     */
    public Result index() {

        return ok(views.html.index.render());
    }

}

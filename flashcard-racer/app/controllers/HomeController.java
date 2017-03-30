package controllers;

import java.security.NoSuchAlgorithmException;

import com.google.inject.Inject;

import dtos.LoginDTO;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import util.StudentAuthenticator;
import views.html.index;
import views.html.studenthome;

public class HomeController extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result index() {
        String user = session("student");
        if (user != null) {
            return ok(studenthome.render(user));
        } else {
            return ok(index.render(formFactory.form(LoginDTO.class)));
        }
    }

    public Result login() throws NoSuchAlgorithmException {
        Form<LoginDTO> form = formFactory.form(LoginDTO.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest(index.render(form));
        }

        LoginDTO dto = form.get();
        String username = dto.getLogin();
        String password = dto.getPassword();

        if (StudentAuthenticator.authenticate(username, password)) {
            sessionAdd("student", username);
            return redirect(routes.HomeController.index());
        } else {
            flash("errorMsg", "Wrong username or password");
            return unauthorized(index.render(form));
        }
    }

    public Result logout() {
        sessionRemove("student");
        return redirect(routes.HomeController.index());
    }

    protected void sessionAdd(String key, String value) {
        session(key, value);
    }

    protected void sessionRemove(String key) {
        session().remove(key);
    }
}

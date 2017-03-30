package controllers;

import java.security.NoSuchAlgorithmException;
import com.google.inject.Inject;

import util.Authentication;
import dtos.LoginDTO;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.teacherhome;
import views.html.teacherlogin;

public class TeacherLoginController extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result index() {
        String user = session("teacher");
        if (user != null) {
            return ok(teacherhome.render(user));
        } else {
            return ok(teacherlogin.render(formFactory.form(LoginDTO.class)));
        }
    }

    public Result login() throws NoSuchAlgorithmException {
        Form<LoginDTO> form = formFactory.form(LoginDTO.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest(teacherlogin.render(form));
        }

        LoginDTO dto = form.get();
        String username = dto.getLogin();
        String password = dto.getPassword();

        if (Authentication.authenticateTeacher(username, password)) {
            sessionAdd("teacher", username);
            return redirect(routes.TeacherLoginController.index());
        } else {
            flash("errorMsg", "Wrong username or password");
            return unauthorized(teacherlogin.render(form));
        }
    }

    public Result logout() {
        sessionRemove("teacher");
        return redirect(routes.TeacherLoginController.index());
    }

    protected void sessionAdd(String key, String value) {
        // Do more?
        session(key, value);
    }

    protected void sessionRemove(String key) {
        // Do more?
        session().remove(key);
    }
}

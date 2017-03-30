package controllers;

import com.google.inject.Inject;

import util.Authentication;
import dtos.LoginDTO;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.admin;

public class AdminController extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result index() {
        String user = session("admin");
        if (user != null) {
            return redirect(routes.TeacherController.teachers());
        } else {
            return ok(admin.render(formFactory.form(LoginDTO.class)));
        }
    }

    public Result login() {
        Form<LoginDTO> form = formFactory.form(LoginDTO.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest(admin.render(form));
        }

        LoginDTO dto = form.get();
        String username = dto.getLogin();
        String password = dto.getPassword();

        if (Authentication.authenticateAdmin(username, password)) {
            sessionAdd("admin", username);
            return redirect(routes.AdminController.index());
        } else {
            flash("errorMsg", "Wrong username or password");
            return unauthorized(admin.render(form));
        }
    }

    public Result logout() {
        sessionRemove("admin");
        return redirect(routes.AdminController.index());
    }

    protected void sessionAdd(String key, String value) {
        session(key, value);
    }

    protected void sessionRemove(String key) {
        session().remove(key);
    }
}

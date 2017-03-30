package controllers;

import java.security.NoSuchAlgorithmException;

import com.avaje.ebean.Query;
import com.google.inject.Inject;

import dtos.TeacherEditDTO;
import models.Teacher;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import util.Authentication;
import views.html.edit;

public class EditTeacherController extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result index(Long id) {
        Query<Teacher> query = Teacher.db().find(Teacher.class);
        Teacher teacher = query.where().eq("id", id).findUnique();

        Form<TeacherEditDTO> form = formFactory.form(TeacherEditDTO.class);

        return ok(edit.render(form, teacher));
    }

    public Result edit(Long id) throws NoSuchAlgorithmException {
        Query<Teacher> query = Teacher.db().find(Teacher.class);
        Teacher teacher = query.where().eq("id", id).findUnique();
        Form<TeacherEditDTO> form = formFactory.form(TeacherEditDTO.class).bindFromRequest();

        if (form.hasErrors()) {
            return badRequest(edit.render(form, teacher));
        }

        TeacherEditDTO dto = form.get();
        String newLogin = dto.getLogin();
        String newPassword = dto.getPassword();

        if (!newLogin.equals("")) {
            teacher.setName(newLogin);
        }

        if (!newPassword.equals("")) {
            byte[] newSalt = Authentication.newSalt();
            byte[] newHash = Authentication.hashString(newSalt, newPassword);
            teacher.setSalt(newSalt);
            teacher.setHash(newHash);
        }

        teacher.save();

        return redirect("/teachers");
    }

}

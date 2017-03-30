package controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import dtos.TeacherRegisterDTO;
import models.Teacher;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import util.Authentication;
import views.html.teachers;

public class TeacherController extends Controller {

    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @Inject
    private FormFactory formFactory;

    public Result teachers() {
        Form<TeacherRegisterDTO> form = formFactory.form(TeacherRegisterDTO.class);
        List<Teacher> teacherList = Teacher.db().find(Teacher.class).findList();
        return ok(teachers.render(form, teacherList));
    }

    public Result editTeacher(Long id) {
        return Results.TODO;
    }

    public Result deleteTeacher(Long id) {
        Teacher.db().find(Teacher.class).setId(id).findUnique().delete();
        return redirect(routes.TeacherController.teachers());
    }

    public Result createTeacher() throws NoSuchAlgorithmException {
        Form<TeacherRegisterDTO> form = formFactory.form(TeacherRegisterDTO.class).bindFromRequest();
        List<Teacher> teacherList = null;

        if (form.hasErrors()) {
            teacherList = Teacher.db().find(Teacher.class).findList();
            return badRequest(teachers.render(form, teacherList));
        }

        TeacherRegisterDTO teacherDTO = form.get();
        String password = teacherDTO.getPassword();

        byte[] salt = Authentication.newSalt();
        byte[] hash = Authentication.hashString(salt, password);

        Teacher teacher = new Teacher();
        teacher.setName(teacherDTO.getLogin());
        teacher.setSalt(salt);
        teacher.setHash(hash);
        teacher.save();

        form = formFactory.form(TeacherRegisterDTO.class);
        teacherList = Teacher.db().find(Teacher.class).findList();

        return ok(teachers.render(form, teacherList));
    }
}

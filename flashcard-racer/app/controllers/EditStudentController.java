package controllers;

import java.security.NoSuchAlgorithmException;

import com.avaje.ebean.Query;
import com.google.inject.Inject;

import dtos.StudentEditDTO;
import models.Student;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import util.HasherSalter;

public class EditStudentController extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result index(Long id) {
        Query<Student> query = Student.db().find(Student.class);
        Student student = query.where().eq("id", id).findUnique();

        Form<StudentEditDTO> form = formFactory.form(StudentEditDTO.class);

        return ok(views.html.editstudent.render(form, student));
    }

    public Result edit(Long id) throws NoSuchAlgorithmException {
        Form<StudentEditDTO> form = formFactory.form(StudentEditDTO.class).bindFromRequest();
        StudentEditDTO dto = form.get();

        Student s = Student.db().find(Student.class).where().eq("id", id).findUnique();

        String name = dto.getLogin();

        if (passwordsAreNotEqual(dto)) {
            return badRequest("");
        }

        if (invalid(dto)) {
            return badRequest("");
        }

        if (!dto.getLogin().equals("")) {
            s.setName(dto.getLogin());
        }

        if (passwordsAreNotEmpty(dto)) {

            byte[] newSalt = HasherSalter.newSalt();
            byte[] newHash = HasherSalter.hashString(newSalt, dto.getPassword());

            s.setHash(newHash);
            s.setSalt(newSalt);
        }

        s.save();
        return redirect(routes.StudentController.index());
    }

    private boolean passwordsAreNotEqual(StudentEditDTO dto) {
        String password = dto.getPassword();
        String password2 = dto.getVerifyPassword();

        return !password.equals(password2);
    }

    private boolean invalid(StudentEditDTO dto) {
        String name = dto.getLogin();

        String password = dto.getPassword();
        String password2 = dto.getVerifyPassword();

        return (name.equals("") && passwordsAreEmpty(dto)) || (password.equals("") ^ password2.equals(""));

    }

    private boolean passwordsAreEmpty(StudentEditDTO dto) {
        return !passwordsAreNotEmpty(dto);
    }

    private boolean passwordsAreNotEmpty(StudentEditDTO dto) {
        String password = dto.getPassword();
        String password2 = dto.getVerifyPassword();
        return !password.equals("") && !password2.equals("");
    }

}

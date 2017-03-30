package controllers;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import com.google.inject.Inject;

import dtos.StudentRegisterDTO;
import models.Student;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import util.HasherSalter;
import views.html.registerstudent;

public class StudentController extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result index() {
        Form<StudentRegisterDTO> form = retrieveForm();
        List<Student> teacherList = Student.db().find(Student.class).findList();

        return ok(registerstudent.render(form, teacherList));
    }

    public Result createStudent() throws NoSuchAlgorithmException {
        Form<StudentRegisterDTO> form = retrieveForm();

        if (formIsInvalid(form)) {
            return badRequest("");
        } else {
            storeStudent(form);

            return index();
        }

    }

    private boolean formIsInvalid(Form<StudentRegisterDTO> form) {
        return form.hasErrors() || passwordsNotOk(form);
    }

    private Form<StudentRegisterDTO> retrieveForm() {
        Form<StudentRegisterDTO> form = formFactory.form(StudentRegisterDTO.class).bindFromRequest();
        return form;
    }

    private boolean passwordsNotOk(Form<StudentRegisterDTO> form) {
        StudentRegisterDTO dto = form.get();

        String password = dto.getPassword();
        String password2 = dto.getVerifyPassword();

        return !password.equals(password2);
    }

    private void storeStudent(Form<StudentRegisterDTO> form) throws NoSuchAlgorithmException {
        StudentRegisterDTO dto = form.get();

        Student s = hashAndSalt(dto);

        s.setName(form.get().getLogin());
        s.save();
    }

    private Student hashAndSalt(StudentRegisterDTO dto) throws NoSuchAlgorithmException {
        String password = dto.getPassword();

        byte[] salt = HasherSalter.newSalt();
        byte[] hash = HasherSalter.hashString(salt, password);

        Student s = new Student();

        s.setHash(hash);
        s.setSalt(salt);
        return s;
    }

    public Result deleteStudent(Long id) {

        if (studentDontExists(id)) {
            return redirect(routes.StudentController.index());
        } else {
            deleteFromDatabase(id);
            return redirect(routes.StudentController.index());
        }

    }

    private void deleteFromDatabase(Long id) {
        Student.db().find(Student.class).setId(id).findUnique().delete();
    }

    private boolean studentDontExists(Long id) {
        return Student.db().find(Student.class).where().eq("id", id).findUnique() == null;
    }

}

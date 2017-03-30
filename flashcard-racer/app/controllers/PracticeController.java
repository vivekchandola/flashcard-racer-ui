package controllers;

import com.google.inject.Inject;

import dtos.PracticeSession;
import models.enums.Difficulty;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.practice;
import views.html.practicescore;
import views.html.startpractice;

public class PracticeController extends Controller {

    @Inject
    private FormFactory formFactory;

    public Result initializePractice() {
        return ok(startpractice.render());
    }

    public Result startSession() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Difficulty difficulty = Difficulty.valueOf(requestData.get("difficulty"));

        Form<PracticeSession> form = formFactory.form(PracticeSession.class);
        form = form.fill(PracticeSession.startSession(difficulty));

        return ok(practice.render(form));
    }

    public Result submitSession() {
        Form<PracticeSession> form = formFactory.form(PracticeSession.class).bindFromRequest();
        PracticeSession session = form.get();
        PracticeSession.advanceSession(session);

        if (session.getCurrent() < session.getSessionLength()) {
            form = formFactory.form(PracticeSession.class).fill(session);
            return ok(practice.render(form));
        } else {
            return ok(practicescore.render(session));
        }
    }
}

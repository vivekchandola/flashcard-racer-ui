package controllers;

import com.google.inject.Inject;

import dtos.PracticeSession;
import dtos.UserSelectedChoice;
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

    /**
     * 
     * Default page with options
     * startpractice.scala.html
     * @return
     */
    public Result initializePractice() {
        return ok(startpractice.render());
    }

    /**
     * Get the selected values from the options and load the math card with selected options
     * Holds the selected choice in session
     * @return
     */
    public Result startSession() {
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String value = requestData.get("radio1");
        Difficulty difficulty;
        if ("1".equals(value)) {
            difficulty = Difficulty.valueOf(requestData.get("difficulty"));
        } else {
            difficulty = createGameWithOptions(requestData);
        }

        Form<PracticeSession> form = formFactory.form(PracticeSession.class);
        form = form.fill(PracticeSession.startSession(difficulty));

        return ok(practice.render(form));
    }

    /**
     * 
     * Creates card deck with user defined selected options
     * @param requestData
     * @return
     */
    private Difficulty createGameWithOptions(DynamicForm requestData) {
        Difficulty difficulty = Difficulty.CUSTOM;
        String add = requestData.get("add");
        String substract = requestData.get("substract");
        String multiply = requestData.get("multiply");
        String division = requestData.get("division");
        String questions = requestData.get("questions");
        String timelimit = requestData.get("timelimit");
        String maxnumber = requestData.get("maxnumber");
        String minnumber = requestData.get("minnumber");
        UserSelectedChoice userSelectedChoice = new UserSelectedChoice(questions, timelimit, maxnumber, minnumber, add,
                substract, multiply, division);
        difficulty.setListOps(userSelectedChoice.getOperators());
        difficulty.setMaxNumber(userSelectedChoice.getMaximumNumber());
        difficulty.setMinNumber(userSelectedChoice.getMinimumNumber());
        difficulty.setTimer(userSelectedChoice.getTimer());
        difficulty.setNumber(userSelectedChoice.getNumberOfQuestions());
        return difficulty;
    }

    /**
     * 
     * Called while submitting a math card
     * Checks for number of questions answered and redirect either to next question or to final result page
     * @return
     */
    public Result submitSession() {
        Form<PracticeSession> form = formFactory.form(PracticeSession.class).bindFromRequest();
        PracticeSession session = form.get();
        PracticeSession.advanceSession(session);

        if (session.getCurrent() < session.getSessionLength()) {
            form = formFactory.form(PracticeSession.class).fill(session);
            return ok(practice.render(form));
        } else {
            setCarValue(session);
            return ok(practicescore.render(session));
        }
    }

    /**
     * 
     * Load the different object which depends on number of correct answer by the user
     * @param session
     */
    private void setCarValue(PracticeSession session) {
        try {
            if (session.getNumCorrect()  * 100/ session.getSessionLength() > 75) {
                session.setCar("car");
            } else if (session.getNumCorrect()  * 100/ session.getSessionLength() > 45) {
                session.setCar("bike");
            } else {
                session.setCar("cycle");
            }
        } catch (Exception e) {
            session.setCar("car");
        }

    }
    
    /**
     * Loads the help page
     * @return
     */
    public Result initializeHelp() {
        return ok(views.html.help.render());
    }
}

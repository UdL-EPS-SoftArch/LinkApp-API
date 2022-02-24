package cat.udl.eps.softarch.linkapp.steps;

import org.springframework.beans.factory.annotation.Autowired;

public class CreateMessageStepDefs {

    @Autowired
    private StepDefs stepDefs;

    CreateMessageStepDefs(StepDefs stepDefs) {
        this.stepDefs = stepDefs;
    }



}

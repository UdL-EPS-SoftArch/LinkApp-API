package cat.udl.eps.softarch.linkapp.steps;

import cat.udl.eps.softarch.linkapp.repository.PostRepository;
import cat.udl.eps.softarch.linkapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ModifyStepDefsPost {

    @Autowired
    private StepDefs stepDefs;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;
}

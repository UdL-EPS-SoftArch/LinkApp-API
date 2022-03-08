package cat.udl.eps.softarch.linkapp.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNPROCESSABLE_ENTITY)
public class ValidationError extends RuntimeException {

    public ValidationError(String message) {
        super(message);
    }

}

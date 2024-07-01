package registration.uz.hgpuserregistration.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import registration.uz.hgpuserregistration.Exception.UserProfileNotFoundException;

import java.io.IOException;

@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException ex){
        if ("Invalid email".equals(ex.getMessage())) {
            return "Invalid email";
        } else if ("Invalid phone number".equals(ex.getMessage())) {
            return "Invalid phone number";
        } else {
            return "Unknown error";
        }
    }
    @ExceptionHandler(UserProfileNotFoundException.class)
    public String handleUserProfileNotFound(UserProfileNotFoundException e){
        return "user not found";
    }
    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(RuntimeException ex){
        return ex.getMessage();
    }
    @ExceptionHandler(IOException.class)
    public String handleIOException(IOException ex){
        return ex.getMessage();
    }
}

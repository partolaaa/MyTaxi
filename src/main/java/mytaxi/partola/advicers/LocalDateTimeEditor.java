package mytaxi.partola.advicers;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.beans.PropertyEditorSupport;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class LocalDateTimeEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        try {
            LocalDateTime dateTime = LocalDateTime.parse(text, formatter);
            setValue(dateTime);
        } catch (DateTimeParseException e) {
            setValue(null);
        }
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDateTime.class, new LocalDateTimeEditor());
    }
}

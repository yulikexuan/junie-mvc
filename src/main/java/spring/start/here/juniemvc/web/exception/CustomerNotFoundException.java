package spring.start.here.juniemvc.web.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Customer not found")
public class CustomerNotFoundException extends RuntimeException {

    private final Integer customerId;

    public CustomerNotFoundException(Integer customerId) {
        super("Customer not found with id: " + customerId);
        this.customerId = customerId;
    }

    public Integer getCustomerId() {
        return customerId;
    }
}

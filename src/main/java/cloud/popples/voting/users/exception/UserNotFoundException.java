package cloud.popples.voting.users.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "UserInfo cannot be found.")
public class UserNotFoundException extends RuntimeException {
}

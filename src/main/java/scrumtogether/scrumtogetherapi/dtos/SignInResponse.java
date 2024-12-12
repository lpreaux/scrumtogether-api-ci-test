package scrumtogether.scrumtogetherapi.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignInResponse {
    private String token;
    Long expiresIn;
}

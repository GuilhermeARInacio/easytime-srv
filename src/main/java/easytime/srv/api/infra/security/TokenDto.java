package easytime.srv.api.infra.security;

public record TokenDto(
        String token,
        String role
) {

}

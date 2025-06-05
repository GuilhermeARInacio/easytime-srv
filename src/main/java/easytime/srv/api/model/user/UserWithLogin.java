package easytime.srv.api.model.user;

import easytime.srv.api.tables.User;

public record UserWithLogin(
        User user,
        String login
) {
}

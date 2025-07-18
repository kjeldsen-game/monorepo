package com.kjeldsen.auth.authentication;

import com.kjeldsen.auth.application.usecases.*;
import com.kjeldsen.auth.authentication.api.AuthApiDelegate;
import com.kjeldsen.auth.authentication.mappers.ProfileMapper;
import com.kjeldsen.auth.authentication.model.*;
import com.kjeldsen.auth.domain.Profile;
import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.exceptions.NotFoundException;
import com.kjeldsen.lib.clients.TeamClientApi;
import com.kjeldsen.lib.model.team.TeamClient;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationDelegate implements AuthApiDelegate {

    private final GetUserUseCase getUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final GenerateTokenUseCase generateTokenUseCase;
    private final TeamClientApi teamClientApi;
    private final UpdateAvatarUseCase updateAvatarUseCase;
    private final GetProfileUseCase getProfileUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;

    @Override
    public ResponseEntity<ProfileResponse> getProfile() {
        Profile profile = getProfileUseCase.get();
        ProfileResponse response = ProfileMapper.INSTANCE.map(profile);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Void> changePassword(ChangePasswordRequest changePasswordRequest) {
        changePasswordUseCase.changePassword(changePasswordRequest.getOldPassword(),
            changePasswordRequest.getNewPassword(), changePasswordRequest.getConfirmPassword());
        return ResponseEntity.ok().build();    }

    @Override
    public ResponseEntity<UserDetailsResponse> me() {
        User user = getUserUseCase.getCurrent();
        UserDetailsResponse details = buildUserDetailsResponse(user);
        return ResponseEntity.ok(details);
    }

    @Override
    public ResponseEntity<Void> updateAvatar(MultipartFile file) {
        updateAvatarUseCase.updateAvatar(file);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> register(@Valid RegisterRequest request) {
        registerUserUseCase.register(request.getEmail(), request.getPassword(),
            request.getTeamName(), request.getConfirmPassword());
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<TokenResponse> generateToken(TokenRequest tokenRequest) {
        String token = generateTokenUseCase.get(tokenRequest.getEmail(), tokenRequest.getPassword());
        return ResponseEntity.ok(new TokenResponse().accessToken(token));
    }

    private UserDetailsResponse buildUserDetailsResponse(User user) {
        List<TeamClient> clientResponse = teamClientApi.getTeam(null, null, user.getId());
        if (clientResponse.isEmpty()) {
            throw new NotFoundException("Team not found for user id: " + user.getId());
        }
        return new UserDetailsResponse(user.getId(), user.getEmail(), clientResponse.get(0).getId());
    }
}

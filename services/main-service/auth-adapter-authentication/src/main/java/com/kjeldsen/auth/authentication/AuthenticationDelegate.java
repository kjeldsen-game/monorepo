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
    private final GeneratePasswordResetLinkUseCase generatePasswordResetLinkUseCase;
    private final ResetPasswordUseCase resetPasswordUseCase;

    @Override
    public ResponseEntity<ProfileResponse> getProfile() {
        Profile profile = getProfileUseCase.get();
        ProfileResponse response = ProfileMapper.INSTANCE.map(profile);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<SuccessResponse> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        resetPasswordUseCase.resetPassword(resetPasswordRequest.getToken(),
            resetPasswordRequest.getNewPassword(), resetPasswordRequest.getConfirmPassword());
        return ResponseEntity.ok(new SuccessResponse().message("Password was reset successfully!"));
    }

    @Override
    public ResponseEntity<SuccessResponse> requestPasswordResetLink(PasswordResetLinkRequest passwordResetLinkRequest) {
        generatePasswordResetLinkUseCase.generate(passwordResetLinkRequest.getEmail());
        return ResponseEntity.ok(new SuccessResponse().message("Password link was successfully generated and sent!"));
    }

    @Override
    public ResponseEntity<SuccessResponse> changePassword(ChangePasswordRequest changePasswordRequest) {
        changePasswordUseCase.changePassword(changePasswordRequest.getOldPassword(),
            changePasswordRequest.getNewPassword(), changePasswordRequest.getConfirmPassword());
        return ResponseEntity.ok(new SuccessResponse().message("Password was changed successfully!"));
    }

    @Override
    public ResponseEntity<UserDetailsResponse> me() {
        User user = getUserUseCase.getCurrent();
        UserDetailsResponse details = buildUserDetailsResponse(user);
        return ResponseEntity.ok(details);
    }

    @Override
    public ResponseEntity<SuccessResponse> updateAvatar(MultipartFile file) {
        updateAvatarUseCase.updateAvatar(file);
        return ResponseEntity.ok(new SuccessResponse().message("Avatar uploaded successfully!"));
    }

    @Override
    public ResponseEntity<SuccessResponse> register(@Valid RegisterRequest request) {
//        for ( int a = 0; a < 10; a++ ) {
//            registerUserUseCase.register(a+request.getEmail(), request.getPassword(),
//                request.getTeamName()+a, request.getConfirmPassword());
//        }
        registerUserUseCase.register(request.getEmail(), request.getPassword(),
            request.getTeamName(), request.getConfirmPassword());
        return ResponseEntity.ok(new SuccessResponse().message("User registered successfully!"));
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

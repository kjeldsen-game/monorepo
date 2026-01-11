package com.kjeldsen.auth.authentication;

import com.kjeldsen.auth.application.usecases.*;
import com.kjeldsen.auth.authentication.mappers.ProfileMapper;
import com.kjeldsen.auth.domain.models.Profile;
import com.kjeldsen.auth.domain.models.User;

import com.kjeldsen.auth.rest.api.*;
import com.kjeldsen.auth.rest.model.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;


@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationDelegate implements AuthApiDelegate {

    private final GetUserUseCase getUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;
    private final GenerateTokenUseCase generateTokenUseCase;
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
        UserDetailsResponse details = new UserDetailsResponse(user.getId(), user.getEmail(), user.getTeamId());
        return ResponseEntity.ok(details);
    }

    @Override
    public ResponseEntity<SuccessResponse> updateAvatar(MultipartFile file) {
        updateAvatarUseCase.updateAvatar(file);
        return ResponseEntity.ok(new SuccessResponse().message("Avatar uploaded successfully!"));
    }

    @Override
    public ResponseEntity<SuccessResponse> register(@Valid RegisterRequest request) {
        registerUserUseCase.register(request.getEmail(), request.getPassword(),
            request.getTeamName(), request.getConfirmPassword());
        return ResponseEntity.ok(new SuccessResponse().message("User registered successfully!"));
    }

    @Override
    public ResponseEntity<TokenResponse> generateServiceToken(ServiceTokenRequest serviceTokenRequest) {
        log.info("Generating service token endpoint");
        String token = generateTokenUseCase.getServiceToken(serviceTokenRequest.getServiceName(), serviceTokenRequest.getClientSecret(),
            serviceTokenRequest.getAudience());
        return ResponseEntity.ok(new TokenResponse().accessToken(token));
    }

    @Override
    public ResponseEntity<TokenResponse> generateUserToken(UserTokenRequest userTokenRequest) {
        return ResponseEntity.ok(new TokenResponse().accessToken(
            generateTokenUseCase.get(userTokenRequest.getEmail(), userTokenRequest.getPassword())));
    }
}

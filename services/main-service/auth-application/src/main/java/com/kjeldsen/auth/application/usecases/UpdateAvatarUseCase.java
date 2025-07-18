package com.kjeldsen.auth.application.usecases;

import com.kjeldsen.auth.domain.User;
import com.kjeldsen.auth.domain.repositories.UserWriteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateAvatarUseCase {

    private final GetUserUseCase getUserUseCase;
    private final UserWriteRepository userWriteRepository;

    public void updateAvatar(MultipartFile file) {
        try {
            User user = getUserUseCase.getCurrent();
            log.info("Updating avatar for userId={} ", user.getId());

            if (file == null || file.isEmpty()) {
                user.setAvatar(null);
            } else {
                byte[] avatarBytes = file.getBytes();
                user.setAvatar(avatarBytes);
            }

            userWriteRepository.save(user);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update avatar", e);
        }
    }
}

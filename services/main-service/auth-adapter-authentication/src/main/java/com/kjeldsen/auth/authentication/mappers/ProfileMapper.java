package com.kjeldsen.auth.authentication.mappers;

import com.kjeldsen.auth.domain.Profile;
import com.kjeldsen.auth.rest.model.ProfileResponse;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProfileMapper {
    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    ProfileResponse map(Profile profile);
}


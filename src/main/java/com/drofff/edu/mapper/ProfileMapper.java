package com.drofff.edu.mapper;

import com.drofff.edu.dto.ProfileDto;
import com.drofff.edu.entity.Group;
import com.drofff.edu.entity.Profile;
import com.drofff.edu.entity.User;
import com.drofff.edu.service.FilesService;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProfileMapper extends Mapper {

    private final TypeMap<Profile, ProfileDto> toDtoTypeMap;

    private final FilesService filesService;

    @Autowired
    public ProfileMapper(FilesService filesService) {
        this.filesService = filesService;
        this.toDtoTypeMap = super.getModelMapper().createTypeMap(Profile.class, ProfileDto.class)
                                .addMappings(x -> x.using(y -> ((User)y.getSource()).getUsername())
                                                    .map(Profile::getUserId, ProfileDto::setEmail))
                                .addMappings(x -> x.using(y -> ((Group)y.getSource()).getName())
                                                    .map(Profile::getGroup, ProfileDto::setGroupName));
    }

    public ProfileDto toDto(Profile profile) {
        ProfileDto profileDto = toDtoTypeMap.map(profile);
        if(Objects.nonNull(profile.getPhotoUrl())) {
            profileDto.setPhotoUrl(filesService.loadPhoto(profile.getPhotoUrl()));
        }
        return profileDto;
    }

}

package com.edu.EvaluationWeb.mapper;

import com.edu.EvaluationWeb.dto.ProfileDto;
import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.service.FilesService;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
        profileDto.setPhotoUrl(filesService.loadPhoto(profile.getPhotoUrl()));
        return profileDto;
    }

}

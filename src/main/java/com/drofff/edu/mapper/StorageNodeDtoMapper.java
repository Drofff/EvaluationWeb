package com.drofff.edu.mapper;

import com.drofff.edu.dto.StorageNodeDto;
import com.drofff.edu.entity.Profile;
import com.drofff.edu.entity.StorageNode;
import com.drofff.edu.repository.StorageNodeRepository;
import com.drofff.edu.component.UserContext;

import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StorageNodeDtoMapper extends Mapper {

    private final StorageNodeRepository storageNodeRepository;
    private final TypeMap<StorageNode, StorageNodeDto> toDtoTypeMap;
    private final UserContext userContext;

    private static final Integer ONE_MEGABYTE = 1024;

    @Autowired
    public StorageNodeDtoMapper(StorageNodeRepository storageNodeRepository, UserContext userContext) {
        this.storageNodeRepository = storageNodeRepository;
        this.userContext = userContext;
        this.toDtoTypeMap = super.getModelMapper().createTypeMap(StorageNode.class, StorageNodeDto.class)
                                    .addMappings(x -> x.skip(StorageNodeDto::setEmpty))
                                    .addMappings(x -> x.using(y -> {
                                        String [] parts = ((String)y.getSource()).split("/");
                                        return parts[parts.length - 1];
                                    }).map(StorageNode::getName, StorageNodeDto::setName))
                                    .addMappings(x -> x.skip(StorageNodeDto::setSize))
                                    .addMappings(x -> x.skip(StorageNodeDto::setAccess));
    }

    public StorageNodeDto toDto(StorageNode entity) {
        Profile profile = userContext.getCurrentUser().getProfile();
        StorageNodeDto dto = toDtoTypeMap.map(entity);
        dto.setEmpty(storageNodeRepository.countDirNodes(entity) == 0);

        Long size = entity.getSize();

        if(entity.getDir()) {
            Long subFoldersSize = storageNodeRepository.countDirectorySize(profile, entity);
            size = subFoldersSize == null ? (long) 0 : subFoldersSize;
        }

        dto.setSize(size > ONE_MEGABYTE ? String.format("%.1f MB", (double)size / 1024.) : size + " kB");
        return dto;
    }

}

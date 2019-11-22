package com.drofff.edu.controller;

import com.drofff.edu.dto.StorageNodeDto;
import com.drofff.edu.entity.Group;
import com.drofff.edu.entity.StorageNode;
import com.drofff.edu.exception.FileManagementException;
import com.drofff.edu.exception.StorageException;
import com.drofff.edu.mapper.GroupMapper;
import com.drofff.edu.mapper.StorageNodeDtoMapper;
import com.drofff.edu.service.ProfileService;
import com.drofff.edu.service.StorageService;
import com.drofff.edu.utils.PathUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/teacher/storage")
@PreAuthorize("hasAuthority('TEACHER')")
public class MyStorageController {

    private final StorageService storageService;
    private final StorageNodeDtoMapper storageNodeDtoMapper;
    private final ProfileService profileService;
    private final GroupMapper groupMapper;

    private static final String ERROR_MESSAGE_KEY = "errorMessage";
    private static final String MESSAGE_KEY = "message";
    private static final String CURRENT_URI = "/teacher/storage";

    @Autowired
    public MyStorageController(StorageService storageService, StorageNodeDtoMapper storageNodeDtoMapper,
                               ProfileService profileService, GroupMapper groupMapper) {
        this.storageService = storageService;
        this.storageNodeDtoMapper = storageNodeDtoMapper;
        this.profileService = profileService;
        this.groupMapper = groupMapper;
    }

    @GetMapping
    public String openMyStorage(Model model, Integer page, HttpServletRequest request,
                                @RequestParam(name = "error-message", required = false) String errorMessage,
                                @RequestParam(required = false) String message) {

        model.addAttribute(ERROR_MESSAGE_KEY, errorMessage);
        model.addAttribute(MESSAGE_KEY, message);
        model.addAttribute("groups", profileService.getMyGroups().stream()
                                    .map(groupMapper::toDto)
                                    .collect(Collectors.toSet()));

        try {
            page = page == null ? 0 : page;
            Page<StorageNode> nodes = storageService.getNodes(page, "/");
            List<StorageNodeDto> nodeDtos = nodes.getContent().stream()
                    .map(storageNodeDtoMapper::toDto)
                    .collect(Collectors.toList());
            nodeDtos.sort(Comparator.comparingInt(x -> x.getDir() ? 0 : 1));

            model.addAttribute("nodes", nodeDtos);

            if(page < (nodes.getTotalPages() - 1)) {
                model.addAttribute("nextURL", request.getRequestURI() + "?page=" + (page + 1));
            }

            if(page > 0) {
                model.addAttribute("prevURL", request.getRequestURI() + "?page=" + (page - 1));
            }

        } catch (StorageException e) {
            model.addAttribute(ERROR_MESSAGE_KEY, e.getMessage());
        }
        return "myStorage";
    }

    @GetMapping("/**")
    public String openMyStoragePath(Model model, Integer page, HttpServletRequest request,
                                    @RequestParam(name = "error-message", required = false) String errorMessage,
                                    @RequestParam(required = false) String message) {

        String path = PathUtils.extractStoragePath(CURRENT_URI + "/", request.getRequestURI());
        model.addAttribute(ERROR_MESSAGE_KEY, errorMessage);
        model.addAttribute(MESSAGE_KEY, message);
        model.addAttribute("groups", profileService.getMyGroups().stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toSet()));

        try {
            page = page == null ? 0 : page;
            Page<StorageNode> nodes = storageService.getNodes(page, path);
            List<StorageNodeDto> nodeDtos = nodes.getContent().stream()
                    .map(storageNodeDtoMapper::toDto)
                    .collect(Collectors.toList());
            nodeDtos.sort(Comparator.comparingInt(x -> x.getDir() ? 0 : 1));

            model.addAttribute("nodes", nodeDtos);

            Set<Group> permissions = storageService.getPermissions(path);

            if(permissions != null && permissions.size() > 0) {
                model.addAttribute("permissions", permissions
                        .stream()
                        .map(Group::getName)
                        .collect(Collectors.joining(", ")));
                model.addAttribute("permissionsList", permissions.stream().map(Group::getId).collect(Collectors.toSet()));
            }
            model.addAttribute("currentDirectory", PathUtils.extractCurrentDirectory(path));
            model.addAttribute("path", PathUtils.extractNavigationInfo(path, CURRENT_URI));
            model.addAttribute("fullPath", path);
            model.addAttribute("id", storageService.getId(path));
            if(page < (nodes.getTotalPages() - 1)) {
                model.addAttribute("nextURL", request.getRequestURI() + "?page=" + (page + 1));
            }
            if(page > 0) {
                model.addAttribute("prevURL", request.getRequestURI() + "?page=" + (page - 1));
            }
        } catch (StorageException e) {
            model.addAttribute(ERROR_MESSAGE_KEY, e.getMessage());
        }
        return "myStorage";
    }

    @PostMapping("/upload")
    public String uploadFile(MultipartFile file, @RequestParam(required = false) String path,
                             @RequestHeader(required = false) String referer) {
        UriBuilder redirectUriBuilder = UriComponentsBuilder.fromUriString(referer == null ? "/teacher/storage" : referer);
        redirectUriBuilder.replaceQueryParam("message");
        redirectUriBuilder.replaceQueryParam("error-message");
        try {
            storageService.upload(file, path);
        } catch (FileManagementException e) {
            redirectUriBuilder.replaceQueryParam("error-message", e.getMessage());
            return "redirect:" + redirectUriBuilder.build();
        }
        redirectUriBuilder.replaceQueryParam("message", "Successfully uploaded");
        return "redirect:" + redirectUriBuilder.build();
    }

    @GetMapping("/delete/{id}")
    public String deleteFile(@RequestHeader(required = false) String referer, @PathVariable Long id) {
        UriBuilder builder = UriComponentsBuilder.fromUriString(referer == null ? "/teacher/storage" : referer);
        try {
            storageService.delete(id);
        } catch(FileManagementException e) {
            builder.replaceQueryParam("error-message", e.getMessage());
            builder.replaceQueryParam("message");
            return "redirect:" + builder.build();
        }
        builder.replaceQueryParam("message", "Successfully deleted");
        builder.replaceQueryParam("error-message");
        return "redirect:" + builder.build();
    }

    @PostMapping("/create/directory")
    public String createDirectory(@RequestParam(required = false) Long parent, String name,
                                  @RequestParam(required = false) Set<Long> access,
                                  @RequestHeader(required = false) String referer) {
        UriBuilder builder = UriComponentsBuilder.fromUriString(referer == null ? "/teacher/storage" : referer);
        try {
            storageService.createDirectory(name, parent, access);
        } catch(FileManagementException e) {
            builder.replaceQueryParam("error-message", e.getMessage());
            builder.replaceQueryParam("message");
            return "redirect:" + builder.build();
        }
        builder.replaceQueryParam("message", "Successfully created directory with name " + name);
        builder.replaceQueryParam("error-message");
        return "redirect:" + builder.build();
    }

    @PostMapping("/edit/permissions")
    public String editPermissions(@RequestParam(required = false) Set<Long> access, Long id,
                                  @RequestHeader(required = false) String referer) {
        UriBuilder builder = UriComponentsBuilder.fromUriString(referer == null ? "/teacher/storage" : referer);
        builder.replaceQueryParam("message");
        builder.replaceQueryParam("error-message");
        try {
            storageService.editPermissions(access, id);
        } catch(FileManagementException e) {
            builder.replaceQueryParam("error-message", e.getMessage());
            return "redirect:" + builder.build();
        }
        builder.replaceQueryParam("message", "Permissions were successfully changed");
        return "redirect:" + builder.build();
    }

}

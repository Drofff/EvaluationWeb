package com.edu.EvaluationWeb.service;

import com.edu.EvaluationWeb.component.UserContext;
import com.edu.EvaluationWeb.entity.Group;
import com.edu.EvaluationWeb.entity.Profile;
import com.edu.EvaluationWeb.entity.StorageNode;
import com.edu.EvaluationWeb.entity.User;
import com.edu.EvaluationWeb.exception.BaseException;
import com.edu.EvaluationWeb.exception.FileManagementException;
import com.edu.EvaluationWeb.exception.NoSuchDirException;
import com.edu.EvaluationWeb.exception.StorageException;
import com.edu.EvaluationWeb.repository.GroupRepository;
import com.edu.EvaluationWeb.repository.ProfileRepository;
import com.edu.EvaluationWeb.repository.StorageNodeRepository;
import java.util.Base64;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class StorageService {

    private final StorageNodeRepository storageNodeRepository;
    private final UserContext userContext;
    private final GroupRepository groupRepository;
    private final ProfileRepository profileRepository;

    @Value("${storage.dir.path}")
    private String basePath;

    @Value("${storage.file.size.max}")
    private Long maxFileSize;

    private static final Integer PAGE_SIZE = 10;

    public static final String CONTENT_TYPE_KEY = "contentType";
    public static final String DATA_KEY = "data";
    public static final String FILENAME_KEY = "filename";

    private final Tika tika;

    @Autowired
    public StorageService(StorageNodeRepository storageNodeRepository, UserContext userContext,
                          GroupRepository groupRepository, ProfileRepository profileRepository) {
        this.storageNodeRepository = storageNodeRepository;
        this.userContext = userContext;
        this.groupRepository = groupRepository;
        this.profileRepository = profileRepository;
        this.tika = new Tika();
    }

    public Page<StorageNode> getNodes(Integer page, String path) throws StorageException {

        User user = userContext.getCurrentUser();
        Profile profile = user.getProfile();

        if(!path.equals("/")) {
            StorageNode storageNode = storageNodeRepository.findByNameAndOwnerAndIsDir(path, profile, true);
            if(storageNode == null) {
                throw new NoSuchDirException(path);
            }
            return storageNodeRepository.findByOwnerAndParent(profile, storageNode, PageRequest.of(page, PAGE_SIZE));
        }

        return storageNodeRepository.findByOwnerAndParentIsNull(profile, PageRequest.of(page, PAGE_SIZE));
    }

    public List<StorageNode> getPublicNodes(Long teacherId, Integer page, String path) {
        User user = userContext.getCurrentUser();
        Profile currentProfile = user.getProfile();
        Profile teacher = profileRepository.findById(teacherId)
                .orElseThrow(() -> new BaseException("Invalid teacher id"));
        if(Objects.nonNull(path)) {
            StorageNode parentNode = storageNodeRepository.findByNameAndOwner(path, teacher);
            validateNode(parentNode, currentProfile.getGroup());
            return storageNodeRepository.findByOwnerAndParent(teacher, parentNode, PageRequest.of(page, PAGE_SIZE))
                    .stream()
                    .filter(x -> hasPermission(x.getAccess(), currentProfile.getGroup()))
                    .collect(Collectors.toList());
        }
        return storageNodeRepository.findByOwnerAndParentIsNull(teacher, PageRequest.of(page, PAGE_SIZE))
                    .stream()
                    .filter(x -> hasPermission(x.getAccess(), currentProfile.getGroup()))
                    .collect(Collectors.toList());
    }

    private boolean hasPermission(Set<Group> nodePermissions, Group userGroup) {
        return nodePermissions == null || nodePermissions.isEmpty() || nodePermissions.contains(userGroup);
    }

    private void validateNode(StorageNode node, Group access) {
        if(node == null) {
            throw new BaseException("Invalid node path");
        }
        if(!node.getAccess().isEmpty() && !node.getAccess().contains(access)) {
            throw new BaseException("Invalid permissions to view this node");
        }
    }

    public void createDirectory(String name, Long parent, Set<Long> access) {

        if(!name.matches("^\\w+$")) {
            throw new FileManagementException("Invalid directory name, Only A-Z, a-Z, 1-9 and _ is allowed");
        }

        Profile profile = userContext.getCurrentUser().getProfile();

        StorageNode storageNode = new StorageNode();
        storageNode.setDir(true);
        storageNode.setOwner(profile);

        if(access != null) {
            storageNode.setAccess(access.stream()
                    .map(x -> groupRepository.findById(x).orElseGet(null))
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        }

        if(parent != null) {
            Optional<StorageNode> parentOptional = storageNodeRepository.findById(parent);
            if(!parentOptional.isPresent()) {
                throw new FileManagementException("Parent Error: Node with such id do not exists");
            }
            StorageNode parentNode = parentOptional.get();
            if(!parentNode.getOwner().getId().equals(profile.getId())) {
                throw new FileManagementException("Invalid permission");
            }
            name = parentNode.getName() + "/" + name;
            storageNode.setParent(parentNode);
        }

        storageNode.setName(name);
        storageNodeRepository.save(storageNode);
    }

    public Set<Group> getPermissions(String path) {
        Profile profile = userContext.getCurrentUser().getProfile();
        if(path == null || path.isEmpty() || path.equals("/")) {
            return Collections.emptySet();
        }
        StorageNode storageNode = storageNodeRepository.findByNameAndOwnerAndIsDir(path, profile, true);
        if(storageNode == null || storageNode.getAccess() == null) {
            return null;
        }
        return storageNode.getAccess();
     }

     public Long getId(String path) {
        if(path == null || path.isEmpty() || path.equals("/")) {
            return null;
        }
        Profile profile = userContext.getCurrentUser().getProfile();
        StorageNode currentNode = storageNodeRepository.findByNameAndOwner(path, profile);
        if(currentNode == null) {
            return null;
        }
        return currentNode.getId();
     }

     public Map<String, Object> download(Long id) {
        User user = userContext.getCurrentUser();
        Profile profile = user.getProfile();

        Optional<StorageNode> storageNodeOptional = storageNodeRepository.findById(id);

        if(!storageNodeOptional.isPresent()) {
            return null;
        }

        StorageNode node = storageNodeOptional.get();

        if(!node.getOwner().getId().equals(profile.getId()) &&
                node.getAccess().size() > 0 && !node.getAccess().contains(profile.getGroup())) {
            return null;
        }

        Path filePath = Paths.get(basePath + "/" + node.getOwner().getStorageFolderPath() + "/" + node.getFileUrl());
        if(Files.notExists(filePath)) {
            return null;
        }

        try {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTENT_TYPE_KEY, tika.detect(filePath));
            data.put(DATA_KEY, Files.readAllBytes(filePath));
            data.put(FILENAME_KEY, node.getOriginalFilename());
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

     }

     public void upload(MultipartFile file, String path) {
        validateFile(file);
        Profile profile = userContext.getCurrentUser().getProfile();
        StorageNode parent = null;
        if(path != null) {
            parent = storageNodeRepository.findByNameAndOwnerAndIsDir(path, profile, true);
            if (parent == null) {
                throw new FileManagementException("Invalid path specified");
            }
        }

        String filename = path != null ? path + "/" + file.getOriginalFilename() : file.getOriginalFilename();
        if(storageNodeRepository.findByNameAndOwner(filename, profile) != null) {
            throw new FileManagementException("File with such name already exists");
        }
        Path folder = getAndCheckDirectory(profile);
        String generatedName = generatePhysicalName(filename);
        save(folder, generatedName, filename, file, profile, parent);
     }

     public void delete(Long id) {
        Optional<StorageNode> nodeOptional = storageNodeRepository.findById(id);

        if(!nodeOptional.isPresent()) {
            throw new FileManagementException("Node with such id do not exists");
        }

        StorageNode node = nodeOptional.get();
        Profile profile = userContext.getCurrentUser().getProfile();

        if(!node.getOwner().getId().equals(profile.getId())) {
            throw new FileManagementException("Invalid permission");
        }

        if(node.getDir() && storageNodeRepository.countDirNodes(node) > 0) {
            throw new FileManagementException("Sorry, but it is forbidden to delete non empty directory");
        }

        Path filePath = Paths.get(basePath + "/" + profile.getStorageFolderPath() + "/" + node.getFileUrl());

        if(Files.exists(filePath)) {
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                throw new FileManagementException("Error while deleting node");
            }
        }

        storageNodeRepository.delete(node);

     }

     public void editPermissions(Set<Long> access, Long id) {
        Optional<StorageNode> storageNodeOptional = storageNodeRepository.findById(id);
        if(!storageNodeOptional.isPresent()) {
            throw new FileManagementException("Invalid node id");
        }

        StorageNode currentNode = storageNodeOptional.get();
        Profile profile = userContext.getCurrentUser().getProfile();

        if(!currentNode.getOwner().getId().equals(profile.getId())) {
            throw new FileManagementException("Invalid permission");
        }

        if(access == null) {
            currentNode.setAccess(null);
            storageNodeRepository.save(currentNode);
            return;
        }

        Set<Group> groups = access.stream().map(x -> {
            Optional<Group> currentGroupOptional = groupRepository.findById(x);
            if(!currentGroupOptional.isPresent()) {
                throw new FileManagementException("Group with such id do not exists");
            }
            return currentGroupOptional.get();
        }).collect(Collectors.toSet());
        currentNode.setAccess(groups);
        storageNodeRepository.save(currentNode);
     }

     private void validateFile(MultipartFile file) {
         if(file == null || file.isEmpty()) {
             throw new FileManagementException("Provided file is null");
         }
         if(file.getSize() > (maxFileSize * 1024 * 1024)) {
             throw new FileManagementException("Max allowed size is 20 megabytes");
         }
     }

     private void save(Path folder, String physicalName, String logicalName,
                       MultipartFile file, Profile owner, StorageNode parent) {
        try {
             Path filePath = Paths.get(folder.toString() + "/" + physicalName);
             Files.copy(file.getInputStream(), filePath);

             StorageNode storageNode = new StorageNode();

             storageNode.setFileUrl(physicalName);
             storageNode.setName(logicalName);
             storageNode.setOwner(owner);
             storageNode.setSize(toKilobytes(file.getSize()));
             storageNode.setDir(false);

             if(parent != null) {
                 storageNode.setParent(parent);
             }

             storageNodeRepository.save(storageNode);
         } catch (IOException e) {
             e.printStackTrace();
             throw new FileManagementException("Error while saving file");
         }

     }

     private Path getAndCheckDirectory(Profile profile) {
         Path folder = Paths.get(basePath + "/" + profile.getStorageFolderPath());
         if(Files.notExists(folder)) {
             try {
                 Files.createDirectory(folder);
             } catch (IOException e) {
                 e.printStackTrace();
                 throw new FileManagementException("Error with your storage space. Please, contact administration");
             }
         }
         return folder;
     }

     private String generatePhysicalName(String logicalPath) {
         String [] pathSegments = logicalPath.split("\\.");
         String physicalName = new String(Base64.getEncoder().encode(logicalPath.getBytes()));
         return physicalName + "." + pathSegments[pathSegments.length - 1];
     }

     private Long toKilobytes(Long bytes) {
        return bytes / 1024;
     }

}

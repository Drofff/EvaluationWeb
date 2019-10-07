package com.edu.EvaluationWeb.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "storage_node")
public class StorageNode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Long size;

    private Boolean isDir;

    @Column(name = "file_url")
    private String fileUrl;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private StorageNode parent;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Profile owner;

    @ManyToMany
    @JoinTable(name = "node_access",
        joinColumns = { @JoinColumn(name = "node_id") },
        inverseJoinColumns = { @JoinColumn(name = "group_id") }
    )
    private Set<Group> access = new HashSet<>();

    public StorageNode() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        if(this.name == null) {
            return null;
        }
        String [] segments = this.name.split("/");
        return segments[segments.length - 1];
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getDir() {
        return isDir;
    }

    public void setDir(Boolean dir) {
        isDir = dir;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public StorageNode getParent() {
        return parent;
    }

    public void setParent(StorageNode parent) {
        this.parent = parent;
    }

    public Profile getOwner() {
        return owner;
    }

    public void setOwner(Profile owner) {
        this.owner = owner;
    }

    public Set<Group> getAccess() {
        return access;
    }

    public void setAccess(Set<Group> access) {
        this.access = access;
    }
}

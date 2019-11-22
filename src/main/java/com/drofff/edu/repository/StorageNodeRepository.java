package com.drofff.edu.repository;

import com.drofff.edu.entity.Group;
import com.drofff.edu.entity.Profile;
import com.drofff.edu.entity.StorageNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface StorageNodeRepository extends PagingAndSortingRepository<StorageNode, Long> {

    Page<StorageNode> findByOwnerAndParent(Profile owner, StorageNode parent, Pageable pageable);

    Page<StorageNode> findByOwnerAndParentIsNull(Profile owner, Pageable pageable);

    StorageNode findByNameAndOwnerAndIsDir(String name, Profile owner, Boolean isDir);

    StorageNode findByName(String name);

    StorageNode findByNameAndOwner(String name, Profile owner);

    @Query("select sn from StorageNode sn join sn.access g on g in :group where sn.owner = :owner")
    List<StorageNode> findByOwnerAndGroups(Profile owner, Set<Group> group);

    @Query("select count(*) from StorageNode sn where sn.parent = :parent")
    Long countDirNodes(StorageNode parent);

    @Query("select sum(sn.size) from StorageNode sn where owner = :owner and parent = :parent")
    Long countDirectorySize(Profile owner, StorageNode parent);

}

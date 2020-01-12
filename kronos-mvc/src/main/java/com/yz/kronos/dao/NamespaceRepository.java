package com.yz.kronos.dao;


import com.yz.kronos.model.NamespaceInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

public interface NamespaceRepository extends JpaRepository<NamespaceInfoModel,Long> {

    @Query(value = "select distinct ns_name from namespace_info where is_delete = 0",nativeQuery = true)
    List<String> findValidNamespace();

}

package com.yz.meson.dao;


import com.yz.meson.model.NamespaceInfoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface NamespaceRepository extends JpaRepository<NamespaceInfoModel,Long> {

    List<NamespaceInfoModel> findByIdIn(Set<Long> ids);
}

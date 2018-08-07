package com.example.demo.dao;

import com.example.demo.model.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionDao extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findByRoleName(String roleName);
}

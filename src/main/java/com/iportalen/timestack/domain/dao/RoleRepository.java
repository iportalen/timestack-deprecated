package com.iportalen.timestack.domain.dao;

import org.springframework.data.repository.CrudRepository;

import com.iportalen.timestack.domain.model.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

	Role findByAuthority(String authority);
}

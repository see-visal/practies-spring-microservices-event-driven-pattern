package com.see.visal.itp_indentity.features.role;


import com.see.visal.itp_indentity.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

}

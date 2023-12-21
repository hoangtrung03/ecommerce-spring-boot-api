package com.example.ecommerce.bootstrap;

import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.RoleName;
import com.example.ecommerce.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    private final RoleRepository roleRepository;

    @Autowired
    public DataLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * A method that runs the specified Java function.
     *
     * @param  args	an array of command-line arguments
     * @throws Exception if an exception occurs during the execution of the function
     */
    @Override
    public void run(String... args) throws Exception {
        // Create list role form enum
        List<RoleName> roleNames = Arrays.asList(RoleName.USER, RoleName.ADMIN, RoleName.MANAGER);

        // Loop list role and check exist or not
        for (RoleName roleName : roleNames) {
            if (!roleRepository.existsByName(roleName)) {
                // If not exist, create
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }
        }
    }
}


package com.fastcampus.projectboardadmin.repository;

import com.fastcampus.projectboardadmin.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}

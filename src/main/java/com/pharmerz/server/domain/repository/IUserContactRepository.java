package com.pharmerz.server.domain.repository;

import com.pharmerz.server.domain.model.UserContacts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RepositoryRestResource
public interface IUserContactRepository extends JpaRepository<UserContacts,UUID> {
}

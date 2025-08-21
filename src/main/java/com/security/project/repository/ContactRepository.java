package com.security.project.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.security.project.model.Contact;


@Repository
public interface ContactRepository extends CrudRepository<Contact, String> {
	
	
}

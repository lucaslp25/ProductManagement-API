package com.lucasdev.productmanagementapi.repositories;

import com.lucasdev.productmanagementapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}

//the repository do the connection with the database, and with the services layer...important understand this
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.argentinaPrograma.portfolio.security.repository;

import com.argentinaPrograma.portfolio.security.entity.ValidacionAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author nahux
 */
@Repository
public interface ValidacionAuthRepository extends JpaRepository<ValidacionAuth,Integer> {
    
}

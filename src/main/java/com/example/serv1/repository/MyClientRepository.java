package com.example.serv1.repository;

import com.example.serv1.model.MyClient;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface MyClientRepository extends JpaRepository<MyClient,Long> {


    @Query(value = "select c from MyClient c where c.email=?1")
    public List<MyClient> findByEmail(String email);

}

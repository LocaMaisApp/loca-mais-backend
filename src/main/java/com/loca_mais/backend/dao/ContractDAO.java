package com.loca_mais.backend.dao;

import com.loca_mais.backend.model.ContractEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class ContractDAO {

    @Autowired
    private DataSource dataSource;

    public boolean create(ContractEntity contractEntity) {

    }

}

package com.zmijewski.ecommerce.repository;

import com.zmijewski.ecommerce.model.entity.GlobalParameter;
import com.zmijewski.ecommerce.model.enums.GlobalParameterName;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalParameterRepository extends CrudRepository<GlobalParameter, Long> {

    @Query("SELECT g.value from GlobalParameter g where g.name = :name")
    String getValueAsString(@Param("name") GlobalParameterName name);

    @Query("SELECT g.value from GlobalParameter g where g.name = :name")
    Integer getValueAsInteger(@Param("name") GlobalParameterName name);
}

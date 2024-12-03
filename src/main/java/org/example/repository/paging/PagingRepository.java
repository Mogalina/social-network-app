package org.example.repository.paging;

import org.example.models.Entity;
import org.example.repository.Repository;
import org.example.utils.Paging.Page;
import org.example.utils.Paging.Pageable;

import java.sql.SQLException;

public interface PagingRepository<ID, E extends Entity<ID>> extends Repository<ID, E> {

    Page<E> findAllOnPage(Pageable pageable) throws SQLException;
}
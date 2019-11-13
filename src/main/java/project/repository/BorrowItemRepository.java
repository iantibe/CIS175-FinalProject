package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.beans.BorrowItem;

@Repository
public interface BorrowItemRepository extends JpaRepository<BorrowItem, Long> {

}

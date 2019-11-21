package project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.beans.BorrowItem;
import project.beans.User;

@Repository
public interface BorrowItemRepository extends JpaRepository<BorrowItem, Long> {
	List<BorrowItem> findByLender(User l);
}

package project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import project.beans.User;
import project.beans.UserItem;

public interface UserItemRepository extends JpaRepository<UserItem, Long> {
	List<UserItem> findByUser(User u);
}

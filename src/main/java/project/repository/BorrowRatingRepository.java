package project.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import project.beans.BorrowRating;

public interface BorrowRatingRepository extends JpaRepository<BorrowRating, Long> {
	@Query(value="SELECT IFNULL(AVG(r.rating), 0) FROM borrow_rating r\r\n" + 
			"JOIN borrow_item bi ON bi.id = r.borrowitem_id\r\n" + 
			"JOIN user_item ui ON bi.useritem_id = ui.id\r\n" + 
			"JOIN user u ON u.id = ui.userid\r\n" + 
			"WHERE bi.borrower_id = ?1", nativeQuery=true)
	double findByUserID(int id);

}

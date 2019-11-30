package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.beans.BorrowRating;

public interface BorrowRatingRepository extends JpaRepository<BorrowRating, Long> {

}

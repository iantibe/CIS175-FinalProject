package project.beans;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="borrow_rating")
public class BorrowRating {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.EAGER)
    @JoinColumn(name="lender_id")
	private User lender;
	
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.EAGER)
    @JoinColumn(name="borrower_id")
	private User borrower;
	
	@Column(name="rating")
	private int rating;

	public BorrowRating() {
		super();
	}

	public BorrowRating(User lender, User borrower, int rating) {
		super();
		this.lender = lender;
		this.borrower = borrower;
		this.rating = rating;
	}

	public BorrowRating(long id, User lender, User borrower, int rating) {
		super();
		this.id = id;
		this.lender = lender;
		this.borrower = borrower;
		this.rating = rating;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getLender() {
		return lender;
	}

	public void setLender(User lender) {
		this.lender = lender;
	}

	public User getBorrower() {
		return borrower;
	}

	public void setBorrower(User borrower) {
		this.borrower = borrower;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "BorrowRating [id=" + id + ", lender=" + lender + ", borrower=" + borrower + ", rating=" + rating + "]";
	}
}

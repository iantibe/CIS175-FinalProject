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
    @JoinColumn(name="borrowitem_id")
	private BorrowItem borrowItem;
	
	@Column(name="rating")
	private int rating;

	public BorrowRating() {
		super();
	}

	public BorrowRating(BorrowItem bi, int rating) {
		super();
		this.borrowItem = bi;
		this.rating = rating;
	}

	public BorrowRating(long id, BorrowItem bi, int rating) {
		super();
		this.id = id;
		this.borrowItem = bi;
		this.rating = rating;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public BorrowItem getBorrowItem() {
		return this.borrowItem;
	}
	
	public void setBorrowItem(BorrowItem bi) {
		this.borrowItem = bi;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	@Override
	public String toString() {
		return "BorrowRating [id=" + id + ", borrowItem=" + borrowItem + ", rating=" + rating + "]";
	}

	
}

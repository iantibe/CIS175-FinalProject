package project.beans;

import java.time.LocalDate;

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
@Table(name="borrow_item")
public class BorrowItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.EAGER)
    @JoinColumn(name="item_id")
	private Item item;
	
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.EAGER)
    @JoinColumn(name="lender_id")
	private User lender;
	
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.EAGER)
    @JoinColumn(name="borrower_id")
	private User borrower;
	
	@Column(name="borrow_date")
	private LocalDate borrowDate;
	
	@Column(name="due_date")
	private LocalDate dueDate;
	
	@Column(name="return_date")
	private LocalDate returnDate;
	
	// CONSTRUCTORS
	
	public BorrowItem() {
	}
	
	public BorrowItem(long id, Item i, User b, User l, LocalDate bd, LocalDate dd) {
		this.setId(id);
		this.setItem(i);
		this.setBorrower(b);
		this.setLender(l);
		this.setBorrowDate(bd);
		this.setDueDate(dd);
	}
	
	public BorrowItem(Item i, User b, User l, LocalDate bd, LocalDate dd) {
		this.setItem(i);
		this.setBorrower(b);
		this.setLender(l);
		this.setBorrowDate(bd);
		this.setDueDate(dd);
	}
	
	// GETTERS AND SETTERS

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
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

	public LocalDate getBorrowDate() {
		return borrowDate;
	}

	public void setBorrowDate(LocalDate borrowDate) {
		this.borrowDate = borrowDate;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDate getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(LocalDate returnDate) {
		this.returnDate = returnDate;
	}
	
	

}

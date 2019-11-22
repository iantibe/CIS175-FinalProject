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

import org.springframework.beans.factory.annotation.Autowired;

@Entity
@Table(name="borrow_item")
public class BorrowItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private long id;
	
	@Autowired
	@ManyToOne(cascade=CascadeType.MERGE, fetch=FetchType.EAGER)
    @JoinColumn(name="useritem_id")
	private UserItem userItem;
	
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
	
	public BorrowItem(long id, UserItem ui, User b, LocalDate bd, LocalDate dd) {
		this.setId(id);
		this.setUserItem(ui);
		this.setBorrower(b);
		this.setBorrowDate(bd);
		this.setDueDate(dd);
	}
	
	public BorrowItem(UserItem ui, User b, LocalDate bd, LocalDate dd) {
		this.setUserItem(ui);
		this.setBorrower(b);
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
	
	public UserItem getUserItem() {
		return this.userItem;
	}
	
	public void setUserItem(UserItem ui) {
		this.userItem = ui;
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

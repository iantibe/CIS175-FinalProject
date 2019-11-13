DROP TABLE borrow_item;
DROP TABLE borrow_rating;
DROP TABLE item;
DROP TABLE user;

CREATE TABLE user (
    id INT NOT NULL AUTO_INCREMENT,
    username VARCHAR(40) NOT NULL,
    password VARCHAR(40) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE item (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(40),
    type VARCHAR(40),
    PRIMARY KEY(id)
);

CREATE TABLE borrow_item (
    id INT NOT NULL AUTO_INCREMENT,
	item_id INT NOT NULL,
	lender_id INT NOT NULL,
	borrower_id INT NOT NULL,
	borrow_date DATE,
	due_date DATE,
	return_date DATE,
    PRIMARY KEY(id),
    FOREIGN KEY(item_id) REFERENCES item(id),
    FOREIGN KEY(lender_id) REFERENCES user(id),
	FOREIGN KEY(borrower_id) REFERENCES user(id)
);

CREATE TABLE borrow_rating (
    id INT NOT NULL AUTO_INCREMENT,
	lender_id INT NOT NULL,
	borrower_id INT NOT NULL,
	rating INT,
    PRIMARY KEY(id),
    FOREIGN KEY(lender_id) REFERENCES user(id),
	FOREIGN KEY(borrower_id) REFERENCES user(id)
);
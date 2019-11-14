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
	borrower_id INT,
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

INSERT INTO user (username, password)
VALUES ('james_bond','007');
INSERT INTO user (username, password)
VALUES ('SpongeRobertPants','1234');
INSERT INTO user (username, password)
VALUES ('tokoyo_sexwhistle','abcd');

INSERT INTO item (name, type)
VALUES ('Dune', 'Book');
INSERT INTO item (name, type)
VALUES ('Nintendo Switch', 'Game System');
INSERT INTO item (name, type)
VALUES ('Sweet Shirt', 'Shirt');
INSERT INTO item (name, type)
VALUES ('Underpants', 'Clothes');
INSERT INTO item (name, type)
VALUES ('Toothbrush', 'Utensil');
INSERT INTO item (name, type)
VALUES ('Hopes and Dreams', 'Abstract Concept');
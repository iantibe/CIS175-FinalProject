DROP TABLE IF EXISTS borrow_rating;
DROP TABLE IF EXISTS borrow_item;
DROP TABLE IF EXISTS user_item;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS user;

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

CREATE TABLE user_item (
	id INT NOT NULL AUTO_INCREMENT,
	userid INT NOT NULL,
	itemid INT NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY(userid) REFERENCES user(id) ON DELETE CASCADE,
	FOREIGN KEY(itemid) REFERENCES item(id) ON DELETE CASCADE
);

CREATE TABLE borrow_item (
    id INT NOT NULL AUTO_INCREMENT,
	useritem_id INT NOT NULL,
	borrower_id INT NOT NULL,
	borrow_date DATE,
	due_date DATE,
	return_date DATE,
    PRIMARY KEY(id),
    FOREIGN KEY(useritem_id) REFERENCES user_item(id) ON DELETE CASCADE,
    FOREIGN KEY(borrower_id) REFERENCES user(id) ON DELETE CASCADE
);

CREATE TABLE borrow_rating (
    id INT NOT NULL AUTO_INCREMENT,
	borrowitem_id INT NOT NULL,
	rating INT,
    PRIMARY KEY(id),
    FOREIGN KEY(borrowitem_id) REFERENCES borrow_item(id) ON DELETE CASCADE
);

INSERT INTO user (username, password)
VALUES ('james_bond','007');
INSERT INTO user (username, password)
VALUES ('SpongeRobertPants','1234');
INSERT INTO user (username, password)
VALUES ('abe_lincoln','abcd');
INSERT INTO user (username, password)
VALUES ('sk8r_boi23','1234');
INSERT INTO user (username, password)
VALUES ('kewlD00d','abcd');
INSERT INTO user (username, password)
VALUES ('sports_fan','1234');
INSERT INTO user (username, password)
VALUES ('another_user','abcd');

INSERT INTO item (name, type) 
VALUES ('Dune', 'Book');
INSERT INTO item (name, type) 
VALUES ('Jurrasic Park', 'Book');
INSERT INTO item (name, type)
VALUES ('Catcher in the Rye', 'Book');
INSERT INTO item (name, type)
VALUES ('Websters Dictionary', 'Book');
INSERT INTO item (name, type)
VALUES ('Nintendo Switch', 'Game System');
INSERT INTO item (name, type)
VALUES ('Xbox One', 'Game System');
INSERT INTO item (name, type)
VALUES ('Sweet Shirt', 'Clothes');
INSERT INTO item (name, type)
VALUES ('Underpants', 'Clothes');
INSERT INTO item (name, type)
VALUES ('Hoodie', 'Clothes');
INSERT INTO item (name, type)
VALUES ('Toothbrush', 'Utensil');
INSERT INTO item (name, type)
VALUES ('Hopes and Dreams', 'Abstract Concept');

INSERT INTO user_item (userid, itemid)
VALUES (1,1);
INSERT INTO user_item (userid, itemid)
VALUES (1,2);
INSERT INTO user_item (userid, itemid)
VALUES (1,5);
INSERT INTO user_item (userid, itemid)
VALUES (1,8);
INSERT INTO user_item (userid, itemid)
VALUES (3,3);
INSERT INTO user_item (userid, itemid)
VALUES (3,4);
INSERT INTO user_item (userid, itemid)
VALUES (3,7);
INSERT INTO user_item (userid, itemid)
VALUES (3,10);
INSERT INTO user_item (userid, itemid)
VALUES (3,11);

INSERT INTO borrow_item (useritem_id, borrower_id, borrow_date, due_date)
VALUES (1, 4, DATE("2019-01-10"), DATE("2020-03-01"));
INSERT INTO borrow_item (useritem_id, borrower_id, borrow_date, due_date, return_date)
VALUES (2, 5, DATE("2019-03-24"), DATE("2019-05-01"), DATE("2019-04-14"));
INSERT INTO borrow_item (useritem_id, borrower_id, borrow_date, due_date)
VALUES (2, 5, DATE("2019-05-10"), DATE("2019-06-01"));

INSERT INTO borrow_rating (borrowitem_id, rating)
VALUES (2, 5);
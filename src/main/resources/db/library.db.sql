BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "book" (
	"id"	INTEGER NOT NULL UNIQUE,
	"isbn"	TEXT NOT NULL UNIQUE,
	"title"	TEXT NOT NULL,
	"published_year"	TEXT,
	"author"	TEXT,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "book_categories" (
	"id"	INTEGER NOT NULL,
	"book_id"	INTEGER,
	"category_id"	INTEGER,
	PRIMARY KEY("id"),
	CONSTRAINT "FK_Book" FOREIGN KEY("book_id") REFERENCES "book"("id"),
	CONSTRAINT "FK_Category" FOREIGN KEY("category_id") REFERENCES "category"("id")
);
CREATE TABLE IF NOT EXISTS "category" (
	"id"	INTEGER NOT NULL UNIQUE,
	"name"	TEXT NOT NULL,
	PRIMARY KEY("id")
);
CREATE TABLE IF NOT EXISTS "loan" (
	"id"	INTEGER NOT NULL UNIQUE,
	"loan_date"	TEXT NOT NULL,
	"due_date"	TEXT NOT NULL,
	"return_date"	TEXT,
	"member_id"	INTEGER,
	"book_id"	INTEGER,
	PRIMARY KEY("id"),
	CONSTRAINT "FK_Book" FOREIGN KEY("book_id") REFERENCES "book",
	CONSTRAINT "FK_Member" FOREIGN KEY("member_id") REFERENCES "member"("id")
);
CREATE TABLE IF NOT EXISTS "member" (
	"id"	INTEGER NOT NULL UNIQUE,
	"full_name"	TEXT NOT NULL,
	"email"	TEXT,
	PRIMARY KEY("id")
);
INSERT INTO "book" VALUES (1,'123123123','el principito','10-10-2024','Paco');
INSERT INTO "book_categories" VALUES (1,1,1);
INSERT INTO "category" VALUES (1,'Terror');
INSERT INTO "loan" VALUES (1,'12-12-2025','12-01-2026',NULL,1,1);
INSERT INTO "member" VALUES (1,'Joel','jb@jb');
COMMIT;

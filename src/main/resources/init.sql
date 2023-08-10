CREATE TABLE users(id SERIAL PRIMARY KEY, name TEXT NOT NULL, email TEXT NOT NULL, tags JSON NOT NULL);
INSERT INTO users(name, email, tags) values ('Bob', 'keith.hyland@gmail.com', '{"tags":[]}');
SELECT * FROM users;
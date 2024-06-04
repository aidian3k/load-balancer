CREATE TABLE packages (
    id SERIAL PRIMARY KEY,
    city VARCHAR(255),
    owner_name VARCHAR(255)
);

-- Insert data into the packages table
INSERT INTO packages (city, owner_name) VALUES ('New York', 'John Doe');
INSERT INTO packages (city, owner_name) VALUES ('Los Angeles', 'Jane Smith');
INSERT INTO packages (city, owner_name) VALUES ('Chicago', 'Mike Johnson');
INSERT INTO packages (city, owner_name) VALUES ('Warsaw', 'Adrian Nowosielski');
INSERT INTO packages (city, owner_name) VALUES ('Warsaw', 'Cezary Skorupski');
INSERT INTO packages (city, owner_name) VALUES ('Warsaw', 'Dawid Dobosz');
INSERT INTO packages (city, owner_name) VALUES ('Warsaw', 'James Sullivan');
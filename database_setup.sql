CREATE DATABASE course_registration;
USE course_registration;

CREATE TABLE student (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    student_id VARCHAR(20)
);

CREATE TABLE course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    course_code VARCHAR(20),
    course_name VARCHAR(100)
);

CREATE TABLE registration (
    id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    course_id INT,
    FOREIGN KEY (student_id) REFERENCES student(id),
    FOREIGN KEY (course_id) REFERENCES course(id)
);

-- Insert sample courses
INSERT INTO course (course_code, course_name)
VALUES 
('CS101', 'Introduction to Programming'),
('CS102', 'Data Structures'),
('CS103', 'Database Systems'),
('CS104', 'Computer Networks');

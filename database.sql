CREATE DATABASE IF NOT EXISTS college_man_sys;
USE college_man_sys;

-- Admins table
CREATE TABLE IF NOT EXISTS admins (
    UID INT(20) AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    date_joined TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Course table
CREATE TABLE IF NOT EXISTS course (
    ID INT(11) AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Branch table
CREATE TABLE IF NOT EXISTS branch (
    ID INT(11) AUTO_INCREMENT PRIMARY KEY,
    course VARCHAR(255) NOT NULL,
    title VARCHAR(255) NOT NULL,
    initial VARCHAR(20),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Semester table
CREATE TABLE IF NOT EXISTS semester (
    UID INT(11) AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(22) NOT NULL,
    course VARCHAR(100) NOT NULL,
    branch VARCHAR(100) NOT NULL,
    tution_fees DOUBLE,
    hostel_fees DOUBLE,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Subject table
CREATE TABLE IF NOT EXISTS subject (
    UID INT(11) AUTO_INCREMENT PRIMARY KEY,
    subject_code VARCHAR(55) NOT NULL,
    title VARCHAR(100) NOT NULL,
    course VARCHAR(100) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Time Table table
CREATE TABLE IF NOT EXISTS time_table (
    UID INT(11) AUTO_INCREMENT PRIMARY KEY,
    id VARCHAR(100) NOT NULL,
    subject VARCHAR(100) NOT NULL,
    faculty VARCHAR(100) NOT NULL,
    course VARCHAR(100) NOT NULL,
    branch VARCHAR(100) NOT NULL,
    semester VARCHAR(20) NOT NULL,
    section VARCHAR(20) NOT NULL,
    day VARCHAR(20) NOT NULL,
    time VARCHAR(55) NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Accounts table
CREATE TABLE IF NOT EXISTS accounts (
    UID INT(11) AUTO_INCREMENT PRIMARY KEY,
    roll_no VARCHAR(100) NOT NULL,
    total_fees VARCHAR(100),
    paid_fees VARCHAR(100),
    due_fees VARCHAR(100),
    date_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Faculty table
CREATE TABLE IF NOT EXISTS faculty (
    UID INT(11) AUTO_INCREMENT PRIMARY KEY,
    registration_no VARCHAR(100) NOT NULL,
    name VARCHAR(255) NOT NULL,
    father_name VARCHAR(100),
    sex VARCHAR(55),
    dob VARCHAR(55),
    email VARCHAR(100),
    phone VARCHAR(22),
    password VARCHAR(255) NOT NULL,
    address TEXT,
    photo LONGBLOB,
    qualifications VARCHAR(255),
    institution VARCHAR(255),
    designation VARCHAR(100),
    experience INT(55),
    course VARCHAR(100),
    department VARCHAR(100),
    date_joined VARCHAR(100),
    date_updated VARCHAR(100),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Student table
CREATE TABLE IF NOT EXISTS student (
    UID INT(11) AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    roll_no VARCHAR(100),
    application_no VARCHAR(100),
    registration_no VARCHAR(100) NOT NULL,
    mother_name VARCHAR(100),
    mother_occupation VARCHAR(255),
    address TEXT,
    father_name VARCHAR(100),
    father_occupation VARCHAR(255),
    sex VARCHAR(100),
    dob VARCHAR(55),
    phone VARCHAR(22),
    email VARCHAR(255),
    photo LONGBLOB,
    password VARCHAR(255) NOT NULL,
    date_of_application VARCHAR(100),
    course VARCHAR(255),
    branch VARCHAR(255),
    batch YEAR(4),
    semester VARCHAR(55),
    year_of_passing YEAR(4),
    hostel TINYINT(1),
    library TINYINT(1),
    qualification VARCHAR(255),
    university VARCHAR(255),
    quota VARCHAR(100),
    marks DOUBLE,
    status VARCHAR(55),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Hostel table
CREATE TABLE IF NOT EXISTS hostel (
    UID INT(11) AUTO_INCREMENT PRIMARY KEY,
    reg_no VARCHAR(100) NOT NULL,
    name VARCHAR(100),
    hostel_no VARCHAR(55),
    floor_no VARCHAR(55),
    room_no VARCHAR(55),
    room_type VARCHAR(55),
    bed_type VARCHAR(55),
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insert default admin
INSERT INTO admins (username, password) VALUES ('admin', 'admin');

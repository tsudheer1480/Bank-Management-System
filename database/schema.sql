-- Database
CREATE DATABASE IF NOT EXISTS bankmanagementsystem;
USE bankmanagementsystem;

-- User registration (phase 1)
CREATE TABLE signup (
                        formno VARCHAR(20) PRIMARY KEY,
                        name VARCHAR(100),
                        father_name VARCHAR(100),
                        dob DATE,
                        gender VARCHAR(10),
                        email VARCHAR(100),
                        marital_status VARCHAR(20),
                        address VARCHAR(255),
                        city VARCHAR(50),
                        state VARCHAR(50),
                        pin_code VARCHAR(10)
);
-- User registration (phase 2 - additional details)
CREATE TABLE signuptwo (
                           formno VARCHAR(20),
                           religion VARCHAR(50),
                           category VARCHAR(50),
                           income VARCHAR(50),
                           education VARCHAR(50),
                           occupation VARCHAR(50),
                           pan_no VARCHAR(20),
                           aadhar_no VARCHAR(20),
                           senior_citizen VARCHAR(5),   -- Yes/No
                           existing_account VARCHAR(5), -- Yes/No
                           FOREIGN KEY (formno) REFERENCES signup(formno) ON DELETE CASCADE
);
-- User registration (phase 3 - account & PIN info)
CREATE TABLE signupthree (
                             formno VARCHAR(20),
                             account_type VARCHAR(50),
                             card_number VARCHAR(20) UNIQUE,
                             pin VARCHAR(100),   -- ⚠️ Store hash here (BCrypt), not plain text
                             facility VARCHAR(255),
                             FOREIGN KEY (formno) REFERENCES signup(formno) ON DELETE CASCADE
);

-- Transactions
CREATE TABLE transactions (
                              id INT AUTO_INCREMENT PRIMARY KEY,
                              pin VARCHAR(100),   -- reference to signupthree.pin
                              date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              type VARCHAR(20),   -- e.g. 'Deposit', 'Withdrawal'
                              amount DECIMAL(10,2)
);

-- Login table (links card number + pin to a user)
CREATE TABLE login (
                       card_number VARCHAR(20) PRIMARY KEY,
                       pin VARCHAR(100),          -- BCrypt hash instead of plain PIN
                       formno VARCHAR(20),
                       FOREIGN KEY (formno) REFERENCES signup(formno) ON DELETE CASCADE
);


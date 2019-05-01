CREATE TABLE `tutorialdb`.`customers` (
  `customer_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NULL,
  `last_name` VARCHAR(45) NULL,
  `address` VARCHAR(45) NULL,
  `city` VARCHAR(30) NULL,
  `state` VARCHAR(20) NULL,
  `zip_code` VARCHAR(10) NULL,
  `is_active` TINYINT(1) NULL,
  PRIMARY KEY (`customer_id`));
  
  
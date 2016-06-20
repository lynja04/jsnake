-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema jsnake
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `jsnake` ;

-- -----------------------------------------------------
-- Schema jsnake
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `jsnake` DEFAULT CHARACTER SET utf8 ;
USE `jsnake` ;

-- -----------------------------------------------------
-- Table `jsnake`.`highscore`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `jsnake`.`highscore` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(45) NOT NULL,
  `score` INT NOT NULL,
  `difficulty` VARCHAR(6) NOT NULL,
  `timestamp` TIMESTAMP NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `DIFFICULTY_INDEX` USING BTREE (`difficulty` ASC))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

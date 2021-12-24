CREATE TABLE `stock_management`.`company` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(256) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

ALTER TABLE `stock_management`.`item_master` 
ADD COLUMN `company_id` INT NOT NULL DEFAULT 1 AFTER `vendor`,
ADD INDEX `company_fk_idx` (`company_id` ASC);
ALTER TABLE `stock_management`.`item_master` 
ADD CONSTRAINT `company_fk`
  FOREIGN KEY (`company_id`)
  REFERENCES `stock_management`.`company` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `stock_management`.`phy_journal` 
ADD COLUMN `company_id` INT NOT NULL DEFAULT 1 AFTER `location_code`,
ADD INDEX `company_fk_idx` (`company_id` ASC);
ALTER TABLE `stock_management`.`phy_journal` 
ADD CONSTRAINT `company_fk_pj`
  FOREIGN KEY (`company_id`)
  REFERENCES `stock_management`.`company` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `stock_management`.`phy_journal_item` 
ADD COLUMN `company_id` INT NOT NULL DEFAULT 1 AFTER `journal_item_id`,
ADD INDEX `company_fk_idx` (`company_id` ASC);
ALTER TABLE `stock_management`.`phy_journal_item` 
ADD CONSTRAINT `company_fk_pji`
  FOREIGN KEY (`company_id`)
  REFERENCES `stock_management`.`company` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `stock_management`.`ware_house` 
ADD COLUMN `company_id` INT NOT NULL DEFAULT 1 AFTER `name`,
ADD INDEX `company_fk_idx` (`company_id` ASC);
ALTER TABLE `stock_management`.`ware_house` 
ADD CONSTRAINT `company_fk_wh`
  FOREIGN KEY (`company_id`)
  REFERENCES `stock_management`.`company` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `stock_management`.`phy_journal_template` 
ADD COLUMN `company_id` INT NOT NULL DEFAULT 1 AFTER `name`,
ADD INDEX `company_fk_idx` (`company_id` ASC);
ALTER TABLE `stock_management`.`phy_journal_template` 
ADD CONSTRAINT `company_fk_pjt`
  FOREIGN KEY (`company_id`)
  REFERENCES `stock_management`.`company` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `stock_management`.`purchase_orders` 
ADD COLUMN `company_id` INT NOT NULL DEFAULT 1 AFTER `purchase_order_id`,
ADD INDEX `company_fk_idx` (`company_id` ASC);
ALTER TABLE `stock_management`.`purchase_orders` 
ADD CONSTRAINT `company_fk_po`
  FOREIGN KEY (`company_id`)
  REFERENCES `stock_management`.`company` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
ALTER TABLE `stock_management`.`purchase_order_lines` 
ADD COLUMN `company_id` INT NOT NULL DEFAULT 1 AFTER `purchase_order_line_id`,
ADD INDEX `company_fk_idx` (`company_id` ASC);
ALTER TABLE `stock_management`.`purchase_order_lines` 
ADD CONSTRAINT `company_fk_pol`
  FOREIGN KEY (`company_id`)
  REFERENCES `stock_management`.`company` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `stock_management`.`stock_issue` 
ADD COLUMN `company_id` INT NOT NULL DEFAULT 1 AFTER `stock_issue_id`,
ADD INDEX `company_fk_idx` (`company_id` ASC);
ALTER TABLE `stock_management`.`stock_issue` 
ADD CONSTRAINT `company_fk_si`
  FOREIGN KEY (`company_id`)
  REFERENCES `stock_management`.`company` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `stock_management`.`stock_issue_lines` 
ADD COLUMN `company_id` INT NOT NULL DEFAULT 1 AFTER `stock_issue`,
ADD INDEX `company_fk_idx` (`company_id` ASC);
ALTER TABLE `stock_management`.`stock_issue_lines` 
ADD CONSTRAINT `company_fk_sil`
  FOREIGN KEY (`company_id`)
  REFERENCES `stock_management`.`company` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  ALTER TABLE `stock_management`.`user` 
ADD COLUMN `company_id` INT NOT NULL DEFAULT 1 AFTER `updated_on`,
ADD INDEX `company_fk_usr_idx` (`company_id` ASC);
ALTER TABLE `stock_management`.`user` 
ADD CONSTRAINT `company_fk_usr`
  FOREIGN KEY (`company_id`)
  REFERENCES `stock_management`.`company` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `stock_management`.`vendor_list` 
ADD COLUMN `company_id` INT NOT NULL DEFAULT 1 AFTER `language_code`,
ADD INDEX `company_fk_vl_idx` (`company_id` ASC);
ALTER TABLE `stock_management`.`vendor_list` 
ADD CONSTRAINT `company_fk_vl`
  FOREIGN KEY (`company_id`)
  REFERENCES `stock_management`.`company` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `stock_management`.`user` 
DROP COLUMN `phone_number`,
DROP COLUMN `last_name`,
CHANGE COLUMN `company_id` `company_id` INT(11) NOT NULL DEFAULT 1 AFTER `role_id`,
CHANGE COLUMN `first_name` `full_name` VARCHAR(64) NOT NULL ,
ADD COLUMN `user_security_id` VARCHAR(48) NULL AFTER `full_name`,
ADD COLUMN `win_user_name` VARCHAR(48) NULL AFTER `email_address`,
ADD COLUMN `win_security_id` VARCHAR(48) NULL AFTER `win_user_name`,
ADD COLUMN `license_type` VARCHAR(48) NULL AFTER `win_securtiy_id`;

ALTER TABLE `stock_management`.`user` 
DROP FOREIGN KEY `FKdltbr5t0nljpuuo4isxgslt82`;
ALTER TABLE `stock_management`.`user` 
DROP COLUMN `updated_by`,
DROP COLUMN `created_by`,
DROP INDEX `FKdltbr5t0nljpuuo4isxgslt82` ;

ALTER TABLE `stock_management`.`user` 
CHANGE COLUMN `updated_on` `updated_on` TIMESTAMP NULL DEFAULT NULL ;

CREATE TABLE `stock_management`.`user_company` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `company` INT NOT NULL,
  `user` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_fk_uc_idx` (`user` ASC),
  INDEX `company_fk_uc_idx` (`company` ASC),
  CONSTRAINT `user_fk_uc`
    FOREIGN KEY (`user`)
    REFERENCES `stock_management`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `company_fk_uc`
    FOREIGN KEY (`company`)
    REFERENCES `stock_management`.`company` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

  


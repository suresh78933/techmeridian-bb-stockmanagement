ALTER TABLE `stock_management`.`phy_journal_item` 
CHANGE COLUMN `unit_amount` `unit_amount` VARCHAR(16) NULL DEFAULT NULL ,
CHANGE COLUMN `amount` `amount` VARCHAR(16) NULL DEFAULT NULL ;

ALTER TABLE `stock_management`.`purchase_orders_lines` 
ADD COLUMN `change_key` VARCHAR(256) NULL AFTER `updated_on`;


CREATE TABLE `stock_management`.`stock_issue_lines` (
  `stock_issue_line_id` INT NOT NULL AUTO_INCREMENT,
  `issue_document_no` VARCHAR(64) NULL,
  `line_no` INT NULL,
  `type` VARCHAR(32) NULL,
  `no` VARCHAR(16) NULL,
  `part_no` VARCHAR(16) NULL,
  `shelf_no` VARCHAR(16) NULL,
  `description` VARCHAR(128) NULL,
  `hmr_km` VARCHAR(32) NULL,
  `unit_of_measure` VARCHAR(10) NULL,
  `location` VARCHAR(16) NULL,
  `quantity` INT NULL,
  `mobile_nav_quantity` INT NULL,
  `shortcut_dimension_1_code` VARCHAR(16) NULL,
  `shortcut_dimension_2_code` VARCHAR(16) NULL,
  `available_inventory` VARCHAR(16) NULL,
  `quantity_to_issue` VARCHAR(16) NULL,
  `quantity_issued` VARCHAR(16) NULL,
  `outstanding_quantity` VARCHAR(16) NULL,
  `unit_amount` VARCHAR(16) NULL,
  `amount` VARCHAR(16) NULL,
  `qty_per_unit_of_measure` VARCHAR(16) NULL,
  `total_inventory_on_issue` VARCHAR(16) NULL,
  `change_key` VARCHAR(256) NULL,
  PRIMARY KEY (`stock_issue_line_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

ALTER TABLE `stock_management`.`stock_issue_lines` 
ADD COLUMN `stock_issue` INT NULL AFTER `stock_issue_line_id`,
ADD INDEX `stock_issue_fk_idx` (`stock_issue` ASC);
ALTER TABLE `stock_management`.`stock_issue_lines` 
ADD CONSTRAINT `stock_issue_fk`
  FOREIGN KEY (`stock_issue`)
  REFERENCES `stock_management`.`stock_issue` (`stock_issue_id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


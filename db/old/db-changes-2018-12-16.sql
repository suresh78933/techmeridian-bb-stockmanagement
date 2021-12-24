ALTER TABLE `stock_management`.`item_master` 
ADD COLUMN `inventory` VARCHAR(16) NULL  AFTER `company_id`,
ADD COLUMN `part_no` VARCHAR(32) NULL AFTER `inventory`;

ALTER TABLE `stock_management`.`purchase_order_lines` 
ADD COLUMN `part_no` VARCHAR(32) NULL AFTER `change_key`;

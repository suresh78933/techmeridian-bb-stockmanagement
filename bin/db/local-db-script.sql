CREATE DATABASE  IF NOT EXISTS `stock_management` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `stock_management`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: stock_management
-- ------------------------------------------------------
-- Server version	5.6.19-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `credentials`
--

DROP TABLE IF EXISTS `credentials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `credentials` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `password` varchar(512) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_password_idx` (`user_id`),
  CONSTRAINT `user_password` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `item_master`
--

DROP TABLE IF EXISTS `item_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `item_master` (
  `item_id` int(11) NOT NULL AUTO_INCREMENT,
  `item_no` varchar(32) NOT NULL,
  `item_desciption` varchar(64) DEFAULT NULL,
  `base_unit_of_measure` varchar(16) NOT NULL,
  `shelf_no` varchar(8) DEFAULT NULL,
  `standard_cost` double DEFAULT NULL,
  `unit_cost` double DEFAULT NULL,
  `blocked` varchar(16) DEFAULT NULL,
  `purchase_unit_of_measure` varchar(16) DEFAULT NULL,
  `e_tag` varchar(128) DEFAULT NULL,
  `production_bom_number` varchar(16) DEFAULT NULL,
  `routing_no` varchar(16) DEFAULT NULL,
  `vendor` varchar(128) DEFAULT NULL,
  `company_id` int(11) NOT NULL DEFAULT '1',
  `inventory` varchar(16) DEFAULT NULL,
  `part_no` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`item_id`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `company_fk` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1739 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `phy_journal`
--

DROP TABLE IF EXISTS `phy_journal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phy_journal` (
  `phy_journal_id` int(11) NOT NULL AUTO_INCREMENT,
  `phy_journal_template` varchar(64) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `no_series` varchar(64) DEFAULT NULL,
  `location_code` varchar(64) DEFAULT NULL,
  `company_id` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`phy_journal_id`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `company_fk_pj` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `phy_journal_item`
--

DROP TABLE IF EXISTS `phy_journal_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phy_journal_item` (
  `journal_item_id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) NOT NULL DEFAULT '1',
  `journal_template_name` varchar(128) DEFAULT NULL,
  `journal_batch_name` varchar(128) DEFAULT NULL,
  `phy_journal_id` int(11) DEFAULT NULL,
  `line_no` int(11) DEFAULT NULL,
  `posting_date` varchar(16) DEFAULT NULL,
  `document_date` varchar(16) DEFAULT NULL,
  `entry_type` varchar(32) DEFAULT NULL,
  `location_code` varchar(64) DEFAULT NULL,
  `item_no` varchar(16) DEFAULT NULL,
  `item_id` int(11) DEFAULT NULL,
  `description` varchar(128) DEFAULT NULL,
  `gen_prod_posting_group` varchar(16) DEFAULT NULL,
  `qty_calculated` int(11) DEFAULT NULL,
  `qty_phys_inventory` int(11) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `unit_of_measure_code` varchar(16) DEFAULT NULL,
  `unit_amount` varchar(16) DEFAULT NULL,
  `amount` varchar(16) DEFAULT NULL,
  `indirect_cost_percent` varchar(45) DEFAULT NULL,
  `unit_cost` varchar(16) DEFAULT NULL,
  `applies_to_entry` int(11) DEFAULT NULL,
  `phys_inventory` varchar(8) DEFAULT NULL,
  `item_description` varchar(64) DEFAULT NULL,
  `mobile_nav_quantity` int(11) DEFAULT NULL,
  `document_no` varchar(32) DEFAULT NULL,
  `variant_code` varchar(32) DEFAULT NULL,
  `bin_code` varchar(32) DEFAULT NULL,
  `salespers_purch_code` varchar(32) DEFAULT NULL,
  `gen_bus_posting_group` varchar(16) DEFAULT NULL,
  `reason_code` varchar(64) DEFAULT NULL,
  `change_key` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`journal_item_id`),
  KEY `item_fkey_idx` (`item_id`),
  KEY `journal_fkey_idx` (`phy_journal_id`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `company_fk_pji` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `item_fk` FOREIGN KEY (`item_id`) REFERENCES `item_master` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `journal_fk` FOREIGN KEY (`phy_journal_id`) REFERENCES `phy_journal` (`phy_journal_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `phy_journal_template`
--

DROP TABLE IF EXISTS `phy_journal_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phy_journal_template` (
  `phy_journal_template_id` int(11) NOT NULL AUTO_INCREMENT,
  `ware_house_id` int(11) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `company_id` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`phy_journal_template_id`),
  KEY `warehouse_fk_idx` (`ware_house_id`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `company_fk_pjt` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `warehouse_fk` FOREIGN KEY (`ware_house_id`) REFERENCES `ware_house` (`ware_house_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase_order_lines`
--

DROP TABLE IF EXISTS `purchase_order_lines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_order_lines` (
  `purchase_order_line_id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) NOT NULL DEFAULT '1',
  `purchase_order` int(11) NOT NULL,
  `document_type` varchar(16) NOT NULL,
  `document_no` varchar(16) NOT NULL,
  `line_no` varchar(16) DEFAULT NULL,
  `type` varchar(16) DEFAULT NULL,
  `item_no` varchar(16) DEFAULT NULL,
  `item_id` int(11) NOT NULL,
  `variant_code` varchar(16) DEFAULT NULL,
  `service_tax_registration_no` varchar(16) DEFAULT NULL,
  `vat_prod_posting_group` varchar(16) DEFAULT NULL,
  `description` varchar(64) DEFAULT NULL,
  `location_code` varchar(16) DEFAULT NULL,
  `bin_code` varchar(16) DEFAULT NULL,
  `unit_of_measure_code` varchar(16) DEFAULT NULL,
  `unit_of_measure` varchar(16) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `job_remaining_quantity` int(11) DEFAULT NULL,
  `reserved_quantity` int(11) DEFAULT NULL,
  `quantity_to_receive` int(11) DEFAULT NULL,
  `quantity_received` int(11) DEFAULT NULL,
  `quantity_to_invoice` int(11) DEFAULT NULL,
  `quantity_invoiced` int(11) DEFAULT NULL,
  `quantity_to_assign` int(11) DEFAULT NULL,
  `quantity_assigned` int(11) DEFAULT NULL,
  `quantity_mobile_nav_receieved` int(11) DEFAULT NULL,
  `direct_unit_cost` varchar(16) DEFAULT NULL,
  `line_amount` varchar(16) DEFAULT NULL,
  `line_discount_percent` varchar(16) DEFAULT NULL,
  `line_discount_amount` varchar(16) DEFAULT NULL,
  `requested_receipt_date` varchar(32) DEFAULT NULL,
  `promised_receipt_date` varchar(32) DEFAULT NULL,
  `planned_receipt_date` varchar(32) DEFAULT NULL,
  `expected_receipt_date` varchar(32) DEFAULT NULL,
  `order_date` varchar(32) DEFAULT NULL,
  `e_tag` varchar(64) DEFAULT NULL,
  `updated_by` int(11) DEFAULT NULL,
  `updated_on` timestamp NULL DEFAULT NULL,
  `change_key` varchar(256) DEFAULT NULL,
  `part_no` varchar(32) DEFAULT NULL,
  `push_to_nav` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`purchase_order_line_id`),
  KEY `item_mapping_idx` (`item_id`),
  KEY `purchase_order_idx` (`purchase_order`),
  KEY `updated_by_mapping_idx` (`updated_by`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `company_fk_pol` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `item_mapping` FOREIGN KEY (`item_id`) REFERENCES `item_master` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `purchase_order` FOREIGN KEY (`purchase_order`) REFERENCES `purchase_orders` (`purchase_order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `updated_by_mapping` FOREIGN KEY (`updated_by`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=499 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase_orders`
--

DROP TABLE IF EXISTS `purchase_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_orders` (
  `purchase_order_id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) NOT NULL DEFAULT '1',
  `number` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `buy_from_vendor_no` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `buy_from_vendor_name` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `buy_from_city` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `buy_from_address` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `buy_from_address_2` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_to_vendor_no` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_to_name` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_to_city` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_to_address` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `pay_to_address_2` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `order_date` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `document_date` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `expected_receipt_date` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `currency_code` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `purchaser_code` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_name` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_city` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_address` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_address_2` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vendor_order_no` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vendor_shipment_no` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vendor_invoice_no` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`purchase_order_id`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `company_fk_po` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=348 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(45) NOT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'SYSTEM_ADMIN'),(2,'ADMIN'),(3,'MANAGER'),(4,'USER');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `stock_issue`
--

DROP TABLE IF EXISTS `stock_issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_issue` (
  `stock_issue_id` int(11) NOT NULL AUTO_INCREMENT,
  `company_id` int(11) NOT NULL DEFAULT '1',
  `issue_no` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  `issue_description` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `issue_ref_no` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `issue_raise_by` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `issue_raised_on` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `issued_by` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `issued_on` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `issued_to` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `global_dimension_1_code` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `global_dimension_2_code` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`stock_issue_id`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `company_fk_si` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `stock_issue_lines`
--

DROP TABLE IF EXISTS `stock_issue_lines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `stock_issue_lines` (
  `stock_issue_line_id` int(11) NOT NULL AUTO_INCREMENT,
  `stock_issue` int(11) DEFAULT NULL,
  `company_id` int(11) NOT NULL DEFAULT '1',
  `issue_document_no` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `line_no` int(11) DEFAULT NULL,
  `type` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `no` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `part_no` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `shelf_no` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hmr_km` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit_of_measure` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `mobile_nav_quantity` int(11) DEFAULT NULL,
  `shortcut_dimension_1_code` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `shortcut_dimension_2_code` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `available_inventory` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `quantity_to_issue` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `quantity_issued` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `outstanding_quantity` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit_amount` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `amount` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `qty_per_unit_of_measure` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `total_inventory_on_issue` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `change_key` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL,
  `push_to_nav` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`stock_issue_line_id`),
  KEY `stock_issue_fk_idx` (`stock_issue`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `company_fk_sil` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `stock_issue_fk` FOREIGN KEY (`stock_issue`) REFERENCES `stock_issue` (`stock_issue_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `unit_of_measure`
--

DROP TABLE IF EXISTS `unit_of_measure`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `unit_of_measure` (
  `unit_of_measure_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `international_standard_code` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `e_tag` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`unit_of_measure_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NOT NULL,
  `user_name` varchar(64) NOT NULL,
  `full_name` varchar(64) NOT NULL,
  `user_security_id` varchar(48) DEFAULT NULL,
  `email_address` varchar(64) DEFAULT NULL,
  `win_user_name` varchar(48) DEFAULT NULL,
  `win_security_id` varchar(48) DEFAULT NULL,
  `license_type` varchar(48) DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user_role_idx` (`role_id`),
  CONSTRAINT `FKn82ha3ccdebhokx3a8fgdqeyy` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_company`
--

DROP TABLE IF EXISTS `user_company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_company` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `company` int(11) NOT NULL,
  `user` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_fk_uc_idx` (`user`),
  KEY `company_fk_uc_idx` (`company`),
  CONSTRAINT `company_fk_uc` FOREIGN KEY (`company`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_fk_uc` FOREIGN KEY (`user`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vendor_list`
--

DROP TABLE IF EXISTS `vendor_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendor_list` (
  `vendor_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `name` varchar(128) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `responsibility_center` varchar(64) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `location_code` varchar(64) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `post_code` varchar(64) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `country_region_code` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `phone_no` varchar(32) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `fax_no` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `contact` varchar(64) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `purchaser_code` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `vendor_posting_group` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `gen_bus_posting_group` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `vat_bus_posting_group` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `search_name` varchar(128) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `shipment_method_code` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `currency_code` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `language_code` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `company_id` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`vendor_list_id`),
  KEY `company_fk_vl_idx` (`company_id`),
  CONSTRAINT `company_fk_vl` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=749 DEFAULT CHARSET=ucs2 COLLATE=ucs2_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `ware_house`
--

DROP TABLE IF EXISTS `ware_house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ware_house` (
  `ware_house_id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(64) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `company_id` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`ware_house_id`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `company_fk_wh` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=136 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-04-01  7:11:35

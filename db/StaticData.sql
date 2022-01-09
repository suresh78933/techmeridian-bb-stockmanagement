CREATE DATABASE  IF NOT EXISTS `stock_management` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `stock_management`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: stock_management
-- ------------------------------------------------------
-- Server version	5.6.42-log

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (1,'Z_Kagem'),(2,'Z_Gemfields'),(3,'CRONUS Kagem 1');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

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
  CONSTRAINT `FKtjqsv4cbg2uy7s641hf5cqkk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `user_password` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credentials`
--

LOCK TABLES `credentials` WRITE;
/*!40000 ALTER TABLE `credentials` DISABLE KEYS */;
INSERT INTO `credentials` VALUES (1,1,'83fad80d92a01d251d5d6a9e93e37317b825a9f9367c596e8f07c1beb0ddea64a81f583e57f326c3a7a2ffd844263494547dc078a14232a70f2f8eef6d99d602'),(2,2,'83fad80d92a01d251d5d6a9e93e37317b825a9f9367c596e8f07c1beb0ddea64a81f583e57f326c3a7a2ffd844263494547dc078a14232a70f2f8eef6d99d602'),(3,3,'83fad80d92a01d251d5d6a9e93e37317b825a9f9367c596e8f07c1beb0ddea64a81f583e57f326c3a7a2ffd844263494547dc078a14232a70f2f8eef6d99d602');
/*!40000 ALTER TABLE `credentials` ENABLE KEYS */;
UNLOCK TABLES;

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
  CONSTRAINT `FKklmoo0m54gcrsvt2u7jl016vu` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `company_fk` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `item_master`
--

LOCK TABLES `item_master` WRITE;
/*!40000 ALTER TABLE `item_master` DISABLE KEYS */;
/*!40000 ALTER TABLE `item_master` ENABLE KEYS */;
UNLOCK TABLES;

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
  CONSTRAINT `FKr3ujyj5i7ukoeatwlqhk0hhdx` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `company_fk_pj` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phy_journal`
--

LOCK TABLES `phy_journal` WRITE;
/*!40000 ALTER TABLE `phy_journal` DISABLE KEYS */;
/*!40000 ALTER TABLE `phy_journal` ENABLE KEYS */;
UNLOCK TABLES;

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
  `change_key` varchar(3000) DEFAULT NULL,
  PRIMARY KEY (`journal_item_id`),
  KEY `item_fkey_idx` (`item_id`),
  KEY `journal_fkey_idx` (`phy_journal_id`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `FK91qrr6w291ffkn8tjjyhp6yef` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `FKio8gksmgwh1qybg0c18venkp0` FOREIGN KEY (`item_id`) REFERENCES `item_master` (`item_id`),
  CONSTRAINT `FKs62dcjsw6epsy9kfyvbbhf3te` FOREIGN KEY (`phy_journal_id`) REFERENCES `phy_journal` (`phy_journal_id`),
  CONSTRAINT `company_fk_pji` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `item_fk` FOREIGN KEY (`item_id`) REFERENCES `item_master` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `journal_fk` FOREIGN KEY (`phy_journal_id`) REFERENCES `phy_journal` (`phy_journal_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phy_journal_item`
--

LOCK TABLES `phy_journal_item` WRITE;
/*!40000 ALTER TABLE `phy_journal_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `phy_journal_item` ENABLE KEYS */;
UNLOCK TABLES;

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
  CONSTRAINT `FKbcjypwghr94a2oan7u6tq2j4f` FOREIGN KEY (`ware_house_id`) REFERENCES `ware_house` (`ware_house_id`),
  CONSTRAINT `FKdymngap0lsgbeunhqopjm0w3h` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `company_fk_pjt` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `warehouse_fk` FOREIGN KEY (`ware_house_id`) REFERENCES `ware_house` (`ware_house_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `phy_journal_template`
--

LOCK TABLES `phy_journal_template` WRITE;
/*!40000 ALTER TABLE `phy_journal_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `phy_journal_template` ENABLE KEYS */;
UNLOCK TABLES;

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
  KEY `company_fk_idx` (`company_id`),
  KEY `FKa101jjc7rr9dxl0ope1roa7g1_idx` (`updated_by`),
  KEY `FKa101jjc7rr9dxl0ope1roa7g1_idx1` (`updated_by`),
  CONSTRAINT `FKa101jjc7rr9dxl0ope1roa7g1` FOREIGN KEY (`updated_by`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FKdd3e1trw0j2rrjd0l4kvm67v3` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `FKfvv5svv4l95gefdpwshtk3rgg` FOREIGN KEY (`item_id`) REFERENCES `item_master` (`item_id`),
  CONSTRAINT `FKluslbpxfw4nn1jn2wbvlebxxv` FOREIGN KEY (`purchase_order`) REFERENCES `purchase_orders` (`purchase_order_id`),
  CONSTRAINT `company_fk_pol` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `item_mapping` FOREIGN KEY (`item_id`) REFERENCES `item_master` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `purchase_order` FOREIGN KEY (`purchase_order`) REFERENCES `purchase_orders` (`purchase_order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order_lines`
--

LOCK TABLES `purchase_order_lines` WRITE;
/*!40000 ALTER TABLE `purchase_order_lines` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_order_lines` ENABLE KEYS */;
UNLOCK TABLES;

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
  `currency_code` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `purchaser_code` varchar(18) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_name` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_city` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_address` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_address_2` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vendor_order_no` varchar(132) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vendor_shipment_no` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vendor_invoice_no` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `change_key` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL,
  `purchase_orderscol` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`purchase_order_id`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `FK1mjn0q6xnn5b9g6tcov0qh5g4` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `company_fk_po` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_orders`
--

LOCK TABLES `purchase_orders` WRITE;
/*!40000 ALTER TABLE `purchase_orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_orders` ENABLE KEYS */;
UNLOCK TABLES;

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
  `change_key` varchar(3999) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`stock_issue_id`),
  KEY `company_fk_idx` (`company_id`),
  CONSTRAINT `FKfruhb735v3c5yr1ph0toxh27a` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `company_fk_si` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_issue`
--

LOCK TABLES `stock_issue` WRITE;
/*!40000 ALTER TABLE `stock_issue` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock_issue` ENABLE KEYS */;
UNLOCK TABLES;

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
  `part_no` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `shelf_no` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `hmr_km` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `unit_of_measure` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `location` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `mobile_nav_quantity` int(11) DEFAULT NULL,
  `shortcut_dimension_1_code` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `shortcut_dimension_2_code` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
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
  CONSTRAINT `FKpr3txftsy46sk4ybu70j61a7v` FOREIGN KEY (`stock_issue`) REFERENCES `stock_issue` (`stock_issue_id`),
  CONSTRAINT `FKpsdtkoauxgm2ndgmeslq33hw2` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `company_fk_sil` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `stock_issue_fk` FOREIGN KEY (`stock_issue`) REFERENCES `stock_issue` (`stock_issue_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `stock_issue_lines`
--

LOCK TABLES `stock_issue_lines` WRITE;
/*!40000 ALTER TABLE `stock_issue_lines` DISABLE KEYS */;
/*!40000 ALTER TABLE `stock_issue_lines` ENABLE KEYS */;
UNLOCK TABLES;

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `unit_of_measure`
--

LOCK TABLES `unit_of_measure` WRITE;
/*!40000 ALTER TABLE `unit_of_measure` DISABLE KEYS */;
/*!40000 ALTER TABLE `unit_of_measure` ENABLE KEYS */;
UNLOCK TABLES;

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
  `email_address` varchar(64) DEFAULT NULL,
  `user_security_id` varchar(48) DEFAULT NULL,
  `win_user_name` varchar(48) DEFAULT NULL,
  `win_security_id` varchar(48) DEFAULT NULL,
  `license_type` varchar(48) DEFAULT NULL,
  `created_on` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_on` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user_role_idx` (`role_id`),
  CONSTRAINT `FKn82ha3ccdebhokx3a8fgdqeyy` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,1,'systemadmin','system','system',NULL,NULL,NULL,'2021-11-12 08:58:27','2021-11-12 06:58:27',NULL),(2,4,'user1','user1','user1','user1@sm.com','9988998899','1','2021-11-12 08:58:27','2021-11-12 06:58:27',NULL),(3,2,'admin','admin','admin','admin','7854213698','1','2021-11-12 08:58:27','2021-11-12 06:58:27',NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

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
  CONSTRAINT `FKi6q4u3i9d85h47qjn7w7p9687` FOREIGN KEY (`company`) REFERENCES `company` (`id`),
  CONSTRAINT `FKopbeqqs42qr4nhq4ew10dt069` FOREIGN KEY (`user`) REFERENCES `user` (`user_id`),
  CONSTRAINT `company_fk_uc` FOREIGN KEY (`company`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `user_fk_uc` FOREIGN KEY (`user`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_company`
--

LOCK TABLES `user_company` WRITE;
/*!40000 ALTER TABLE `user_company` DISABLE KEYS */;
/*!40000 ALTER TABLE `user_company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vendor_list`
--

DROP TABLE IF EXISTS `vendor_list`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vendor_list` (
  `vendor_list_id` int(11) NOT NULL AUTO_INCREMENT,
  `number` varchar(32) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `name` varchar(128) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `responsibility_center` varchar(64) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `location_code` varchar(64) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `post_code` varchar(64) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `country_region_code` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `phone_no` varchar(32) COLLATE ucs2_unicode_ci DEFAULT NULL,
  `fax_no` varchar(32) COLLATE ucs2_unicode_ci DEFAULT NULL,
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
  CONSTRAINT `FK48cu1xg6ffogrk4xss85dcwlo` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `company_fk_vl` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=ucs2 COLLATE=ucs2_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vendor_list`
--

LOCK TABLES `vendor_list` WRITE;
/*!40000 ALTER TABLE `vendor_list` DISABLE KEYS */;
/*!40000 ALTER TABLE `vendor_list` ENABLE KEYS */;
UNLOCK TABLES;

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
  CONSTRAINT `FKr5pm5f5yoq5emqhebt1k0aooe` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`),
  CONSTRAINT `company_fk_wh` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ware_house`
--

LOCK TABLES `ware_house` WRITE;
/*!40000 ALTER TABLE `ware_house` DISABLE KEYS */;
/*!40000 ALTER TABLE `ware_house` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-11-12 15:54:18
CREATE DATABASE  IF NOT EXISTS `test` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `test`;
-- MySQL dump 10.13  Distrib 5.6.17, for Win32 (x86)
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.6.42-log

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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-11-12 15:54:18

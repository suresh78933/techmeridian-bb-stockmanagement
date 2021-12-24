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
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `credentials`
--

LOCK TABLES `credentials` WRITE;
/*!40000 ALTER TABLE `credentials` DISABLE KEYS */;
INSERT INTO `credentials` VALUES (1,2,'7c1ff6d80671ef1a736aaad4ed63124d1694bd4c4d42f74321c08538c0766f22fd2d236c00ab4483b4d9fff0347a7e68b60c93fef5906628ebf87d11bd67e77c'),(2,3,'aecedd63f02e330d6fb1b78b7e72359d08a4a8604f73b0da2833d157cc76c4cf7888029da54d18ddfbc9317404411ba71666701db21b14ea181455ce02852107'),(3,4,'ffc774c1a195efa52ed4764a11fc259c963ccc3fbf04252f25ab8df38c4182c2457dc123749d4008cc50e5fad92dc83acd7bd99c2adf08e407669ebf7dc71b95'),(4,5,'71a6c21ae8d2b02ebe8e19549521679b6ad702d22ac8eee0f7e818195ca7f0d3a9efac1d5d72f1eff23cd0c9ba1ba4730dcefbf06b4a462fdb96a87175bf37fc'),(5,6,'58d1497e0bdeb4ff9b8de7ee6115fb6afe9e25f23de1b3dda64e36344fa289c95b9007052bb26d62f25be2b361aea0e66cf02856bf32215b3d545ed079a06e86'),(6,7,'83fad80d92a01d251d5d6a9e93e37317b825a9f9367c596e8f07c1beb0ddea64a81f583e57f326c3a7a2ffd844263494547dc078a14232a70f2f8eef6d99d602');
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
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB AUTO_INCREMENT=437 DEFAULT CHARSET=utf8;
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
  PRIMARY KEY (`phy_journal_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `phy_journal_item`
--

DROP TABLE IF EXISTS `phy_journal_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `phy_journal_item` (
  `journal_item_id` int(11) NOT NULL AUTO_INCREMENT,
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
  `unit_amount` varchar(8) DEFAULT NULL,
  `amount` varchar(8) DEFAULT NULL,
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
  CONSTRAINT `item_fk` FOREIGN KEY (`item_id`) REFERENCES `item_master` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `journal_fk` FOREIGN KEY (`phy_journal_id`) REFERENCES `phy_journal` (`phy_journal_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
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
  PRIMARY KEY (`phy_journal_template_id`),
  KEY `warehouse_fk_idx` (`ware_house_id`),
  CONSTRAINT `warehouse_fk` FOREIGN KEY (`ware_house_id`) REFERENCES `ware_house` (`ware_house_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase_order_lines`
--

DROP TABLE IF EXISTS `purchase_order_lines`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_order_lines` (
  `purchase_order_line_id` int(11) NOT NULL AUTO_INCREMENT,
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
  PRIMARY KEY (`purchase_order_line_id`),
  KEY `item_mapping_idx` (`item_id`),
  KEY `purchase_order_idx` (`purchase_order`),
  KEY `updated_by_mapping_idx` (`updated_by`),
  CONSTRAINT `item_mapping` FOREIGN KEY (`item_id`) REFERENCES `item_master` (`item_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `purchase_order` FOREIGN KEY (`purchase_order`) REFERENCES `purchase_orders` (`purchase_order_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `updated_by_mapping` FOREIGN KEY (`updated_by`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `purchase_orders`
--

DROP TABLE IF EXISTS `purchase_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `purchase_orders` (
  `purchase_order_id` int(11) NOT NULL AUTO_INCREMENT,
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
  `purchaser_code` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL,
  `status` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_name` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_city` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_address` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ship_to_address_2` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vendor_order_no` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vendor_shipment_no` varchar(16) COLLATE utf8_unicode_ci DEFAULT NULL,
  `vendor_invoice_no` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`purchase_order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
  PRIMARY KEY (`stock_issue_id`)
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
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
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
  `first_name` varchar(64) NOT NULL,
  `last_name` varchar(64) DEFAULT NULL,
  `email_address` varchar(64) DEFAULT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `created_by` int(11) DEFAULT NULL,
  `created_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` timestamp NULL DEFAULT NULL,
  `updated_on` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  KEY `user_role_idx` (`role_id`),
  KEY `FKdltbr5t0nljpuuo4isxgslt82` (`created_by`),
  CONSTRAINT `FKdltbr5t0nljpuuo4isxgslt82` FOREIGN KEY (`created_by`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKn82ha3ccdebhokx3a8fgdqeyy` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,1,'systemadmin','system','system',NULL,NULL,NULL,'2018-02-21 02:58:26',NULL,NULL),(2,4,'user1','user1','user1','user1@sm.com','9988998899',1,'2018-03-21 17:03:39',NULL,NULL),(3,4,'user2','user2','user2','user2@sm.com','7744774477',1,'2018-03-21 17:02:21',NULL,NULL),(4,4,'user3','user3','user3','user3@sm.com','5263987415',1,'2018-03-21 17:03:00',NULL,NULL),(5,4,'user4','user4','user4','user4@sm.com','7412365489',1,'2018-03-21 17:03:48',NULL,NULL),(6,3,'manager','manager','manager','manager1@sm.com','8523649713',1,'2018-03-21 17:05:24',NULL,NULL),(7,2,'admin','admin','admin','admin@sm.com','7854213698',1,'2018-03-21 17:05:24',NULL,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

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
  `phone_no` varchar(16) COLLATE ucs2_unicode_ci DEFAULT NULL,
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
  PRIMARY KEY (`vendor_list_id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=ucs2 COLLATE=ucs2_unicode_ci;
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
  PRIMARY KEY (`ware_house_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-17 23:48:49

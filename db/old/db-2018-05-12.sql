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
-- Dumping data for table `item_master`
--

LOCK TABLES `item_master` WRITE;
/*!40000 ALTER TABLE `item_master` DISABLE KEYS */;
INSERT INTO `item_master` VALUES (293,'80103','19\" M009 Monitor','PCS','',1460,1460,'false','PCS','','','','Service Electronics Ltd.'),(294,'80220','Screw on Mount CD/Tape Drive','PCS','',0,0,'false','PCS','','','','Service Electronics Ltd.'),(295,'1251','Axle Back Wheel','PCS','A6',0.33,0.33,'false','PCS','','','','Custom Metals Incorporated'),(296,'80005','Computer III 866 MHz','PCS','',730,730,'false','PCS','','','','Service Electronics Ltd.'),(297,'1896-S','ATHENS Desk','PCS','D2',35170,35170,'false','PCS','','','','CoolWood Technologies'),(298,'70101','Paint, yellow','CAN','B2',100,100,'false','CAN','','','','AR Day Property Management'),(299,'1960-S','ROME Guest Chair, green','PCS','D9',6770,6770,'false','PCS','','','','AR Day Property Management'),(300,'LS-150','Loudspeaker, Cherry, 150W','PCS','',72,72,'false','PCS','','','',NULL),(301,'1900','Frame','PCS','A17',15.7,15.7,'false','PCS','','','','Custom Metals Incorporated'),(302,'80205','10MBit Ethernet','PCS','',160,160,'false','PCS','','','','Service Electronics Ltd.'),(303,'80105','24\" Ultrascan','PCS','',1870,1870,'false','PCS','','','','Service Electronics Ltd.'),(304,'1936-S','BERLIN Guest Chair, yellow','PCS','D8',6770,6770,'false','PCS','','','','AR Day Property Management'),(305,'80002','Computer III 600 MHz','PCS','',490,490,'false','PCS','','','','Service Electronics Ltd.'),(306,'80021','10.2 GB ATA-66 IDE','PCS','',490,490,'false','PCS','','','','Service Electronics Ltd.'),(307,'80215','250MB Disks/4pack','PCS','',160,160,'false','PCS','','','','Service Electronics Ltd.'),(308,'1170','Tube','PCS','A5',1.75,1.75,'false','PCS','','','','Custom Metals Incorporated'),(309,'1992-W','ALBERTVILLE Whiteboard, green','PCS','F10',49210,49210,'false','PCS','','','',NULL),(310,'80007','Enterprise Computer 667 MHz','PCS','',8920,8920,'false','PCS','','','','Service Electronics Ltd.'),(311,'1255','Socket Back','PCS','A7',0.9,0.9,'false','PCS','','','','Custom Metals Incorporated'),(312,'1964-W','INNSBRUCK Storage Unit/G.Door','PCS','F4',11900,11900,'false','PCS','','','',NULL),(313,'80103-T','19\" M009 Monitor','PCS','B2-T',840,840,'false','PCS','','','','Delhi Postmaster'),(314,'80218-T','Hard disk Drive','PCS','B5-T',1300,1300,'false','PCS','','','','Delhi Postmaster'),(315,'80011','128 MB PC800 ECC','PCS','',730,730,'false','PCS','','','','Service Electronics Ltd.'),(316,'1151','Axle Front Wheel','PCS','A2',0.45,0.45,'false','PCS','','','','Groene Kater BVBA'),(317,'1850','Saddle','PCS','A16',7.2,7.2,'false','PCS','','','','Custom Metals Incorporated'),(318,'80217','Power Supply Cable','PCS','',20,20,'false','PCS','','','','Service Electronics Ltd.'),(319,'80219','Screw on Hard Drive Mounting','PCS','',0,0,'false','PCS','','','','Service Electronics Ltd.'),(320,'LS-120','Loudspeaker, Black, 120W','PCS','',45,45,'false','PCS','','','',NULL),(321,'80204','Ultra 160/M SCSI Controller','PCS','',240,240,'false','PCS','','','','Service Electronics Ltd.'),(322,'80203','Graphic Card 9400','PCS','',240,240,'false','PCS','','','','Service Electronics Ltd.'),(323,'80211','Quietkey Keyboard','PCS','',200,200,'false','PCS','','','','Service Electronics Ltd.'),(324,'80014','512 MB PC800 ECC','PCS','',1300,1300,'false','PCS','','','','Service Electronics Ltd.'),(325,'1988-S','SEOUL Guest Chair, red','PCS','D14',6770,6770,'false','PCS','','','','AR Day Property Management'),(326,'80213','Drive250','PCS','',320,320,'false','PCS','','','','Service Electronics Ltd.'),(327,'1928-S','AMSTERDAM Lamp','PCS','D7',1930,1930,'false','PCS','','','','Delhi Postmaster'),(328,'70060','Mounting','PCS','A9',460,460,'false','PCS','','','','Groene Kater BVBA'),(329,'80208-T','Microsoft Intellimouse','PCS','B3-T',140,140,'false','PCS','','','','Delhi Postmaster'),(330,'1988-W','CALGARY Whiteboard, yellow','PCS','F9',49210,49210,'false','PCS','','','',NULL),(331,'70000','Side Panel','PCS','A1',1090,1090,'false','PCS','','','','Delhi Postmaster'),(332,'1710','Hand rear wheel Brake','PCS','F3',4.5,4.5,'false','PCS','','','','Groene Kater BVBA'),(333,'1996-S','ATLANTA Whiteboard, base','PCS','D15',49110,49110,'false','PCS','','','','CoolWood Technologies'),(334,'766BC-A','CONTOSO Conference System','PCS','F11',244300,244300,'false','PCS','','','',NULL),(335,'80022','20.4 GB ATA-66 IDE','PCS','',730,730,'false','PCS','','','','Service Electronics Ltd.'),(336,'FF-100','Frequency filter for LS-100','PCS','',15,15,'false','PCS','','','',NULL),(337,'70103','Paint, red','CAN','B4',100,100,'false','CAN','','','','AR Day Property Management'),(338,'8912-W','Computer - Trendy Package','PCS','',5900,0,'false','PCS','','','','Service Electronics Ltd.'),(339,'80001','Computer III 533 MHz','PCS','',410,410,'false','PCS','','','','Service Electronics Ltd.'),(340,'1400','Mudguard front','PCS','A10',3.9,3.9,'false','PCS','','','','Groene Kater BVBA'),(341,'1908-S','LONDON Swivel Chair, blue','PCS','D5',6680,6680,'false','PCS','','','','CoolWood Technologies'),(342,'8908-W','Computer - Highline Package','PCS','',4600,0,'false','PCS','','','','Service Electronics Ltd.'),(343,'1320','Chain Wheel Front','PCS','A8',4.66,4.66,'false','PCS','','','','Groene Kater BVBA'),(344,'LS-100','Loudspeaker 100W OakwoodDeluxe','PCS','',150.884,15,'false','PCS','','LS-100','',NULL),(345,'70102','Paint, blue','CAN','B3',100,100,'false','CAN','','','','AR Day Property Management'),(346,'80102-T','17\" M780 Monitor','PCS','B1-T',440,440,'false','PCS','','','','Delhi Postmaster'),(347,'80202','Chip 32 MB','PCS','',190,190,'false','PCS','','','','Service Electronics Ltd.'),(348,'80102','17\" M780 Monitor','PCS','',1220,1220,'false','PCS','','','','Service Electronics Ltd.'),(349,'LS-MAN-10','Manual for Loudspeakers','PCS','',12,12,'false','PCS','','','',NULL),(350,'1100','Front Wheel','PCS','F6',129.671,129.671,'false','PCS','','1100','','AR Day Property Management'),(351,'80101','15\" 1501 FP Flat Panel','PCS','',810,810,'false','PCS','','','','Service Electronics Ltd.'),(352,'1968-S','MEXICO Swivel Chair, black','PCS','D11',6670,6670,'false','PCS','','','','CoolWood Technologies'),(353,'8916-W','Computer - TURBO Package','PCS','',7450,0,'false','PCS','','','','Service Electronics Ltd.'),(354,'70011','Glass Door','PCS','A6',2560,2560,'false','PCS','','','','Delhi Postmaster'),(355,'1300','Chain Assy','PCS','F8',13.157,13.157,'false','PCS','','1300','',NULL),(356,'LS-10PC','Loudspeakers, White for PC','BOX','',25,25,'false','BOX','','','',NULL),(357,'1906-S','ATHENS Mobile Pedestal','PCS','D4',15240,15240,'false','PCS','','','','CoolWood Technologies'),(358,'80214','250MB Disks/2pack','PCS','',80,80,'false','PCS','','','','Service Electronics Ltd.'),(359,'70010','Wooden Door','PCS','A5',1840,1840,'false','PCS','','','','Delhi Postmaster'),(360,'LS-S15','Stand for Loudspeakers LS-150','PCS','',45,45,'false','PCS','','','',NULL),(361,'1976-W','INNSBRUCK Storage Unit/W.Door','PCS','F7',10460,10460,'false','PCS','','','',NULL),(362,'1900-S','PARIS Guest Chair, black','PCS','D3',6770,6770,'false','PCS','','','','AR Day Property Management'),(363,'80026','18GB Ultra 160/M SCSI','PCS','',730,730,'false','PCS','','','','Service Electronics Ltd.'),(364,'1920-S','ANTWERP Conference Table','PCS','D6',22770,22770,'false','PCS','','','','AR Day Property Management'),(365,'80012','256 MB PC800 ECC','PCS','',970,970,'false','PCS','','','','Service Electronics Ltd.'),(366,'1200','Back Wheel','PCS','F9',129.682,129.682,'false','PCS','','1200','',NULL),(367,'1700','Brake','PCS','F11',9.765,9.765,'false','PCS','','1700','',NULL),(368,'80212','Performance Keyboard','PCS','',320,320,'false','PCS','','','','Service Electronics Ltd.'),(369,'LS-75','Loudspeaker, Cherry, 75W','PCS','',36,36,'false','PALLET','','','',NULL),(370,'LS-2','Cables for Loudspeakers','BOX','',15,15,'false','BOX','','','',NULL),(371,'1500','Lamp','PCS','A12',5.2,5.2,'false','PCS','','','','Lyselette Lamper A/S'),(372,'2000-S','SYDNEY Swivel Chair, green','PCS','D16',6670,6670,'false','PCS','','','','CoolWood Technologies'),(373,'70104','Paint, green','CAN','B5',100,100,'false','CAN','','','','AR Day Property Management'),(374,'1250','Back Hub','PCS','F10',12.452,12.452,'false','PCS','','1250','',NULL),(375,'80100','Printing Paper','BOX','D1',210,215.991,'false','PALLET','','','','Kinnareds Träindustri AB'),(376,'1980-S','MOSCOW Swivel Chair, red','PCS','D13',6670,6670,'false','PCS','','','','CoolWood Technologies'),(377,'1972-W','SAPPORO Whiteboard, black','PCS','F6',49210,49210,'false','PCS','','','',NULL),(378,'LSU-4','Tweeter speaker unit 4\" 100W','PCS','',15.126,15.126,'false','PCS','','','',NULL),(379,'1000','Bicycle','PCS','F4',350.595,350.595,'false','PCS','','1000','',NULL),(380,'80013','384 MB PC800 ECC','PCS','',1220,1220,'false','PCS','','','','Service Electronics Ltd.'),(381,'70201','Doorknob','PCS','A11',40,40,'false','PCS','','','','Delhi Postmaster'),(382,'8904-W','Computer - Basic Package','PCS','',2530,0,'false','PCS','','','','Service Electronics Ltd.'),(383,'1120','Spokes','PCS','A1',2,2,'false','PCS','','','','Custom Metals Incorporated'),(384,'80025','9GB Ultra 160/M SCSI','PCS','',650,650,'false','PCS','','','','Service Electronics Ltd.'),(385,'80218','Hard Disk Drive','PCS','',160,160,'false','PCS','','','','Service Electronics Ltd.'),(386,'1160','Tire','PCS','A4',1.23,1.23,'false','PCS','','','','Custom Metals Incorporated'),(387,'1924-W','CHAMONIX Base Storage Unit','PCS','F1',5670,5670,'false','PCS','','','','AR Day Property Management'),(388,'80216','Ethernet Cable','PCS','',10,10,'false','PCS','','','','Service Electronics Ltd.'),(389,'80210','8x/4x/32x IDE CD Read-Write','PCS','',1620,1620,'false','PCS','','','','Service Electronics Ltd.'),(390,'70040','Drawer','PCS','A7',3830,3830,'false','PCS','','','','Delhi Postmaster'),(391,'80104','21\" UltraScan P1110','PCS','',1620,1620,'false','PCS','','','','Service Electronics Ltd.'),(392,'70200','Hinge','PCS','A10',50,50,'false','PCS','','','','Delhi Postmaster'),(393,'1968-W','GRENOBLE Whiteboard, red','PCS','F5',49210,49210,'false','PCS','','','',NULL),(394,'80003','Computer III 733 MHz','PCS','',570,570,'false','PCS','','','','Service Electronics Ltd.'),(395,'80023','27GB ATA-66 IDE','PCS','',890,890,'false','PCS','','','','Service Electronics Ltd.'),(396,'80209','20/48x IDE CD ROM','PCS','',810,810,'false','PCS','','','','Service Electronics Ltd.'),(397,'80201','GRAPHIC PROGRAM','PCS','',160,160,'false','PCS','','','','Service Electronics Ltd.'),(398,'1330','Chain Wheel Back','PCS','A9',5.88,5.88,'false','PCS','','','','Groene Kater BVBA'),(399,'1800','Handlebars','PCS','A15',2.12,2.12,'false','PCS','','','','Custom Metals Incorporated'),(400,'70041','Shelf','PCS','A8',830,830,'false','PCS','','','','Delhi Postmaster'),(401,'766BC-B','CONTOSO Office System','PCS','F12',86530,86530,'false','PCS','','','',NULL),(402,'SPK-100','Spike for LS-100','PCS','',15,15,'false','PCS','','','',NULL),(403,'1150','Front Hub','PCS','F7',12.441,12.441,'false','PCS','','1150','',NULL),(404,'70001','Base','PCS','A2',1430,1430,'false','PCS','','','','Delhi Postmaster'),(405,'1984-W','SARAJEVO Whiteboard, blue','PCS','F8',49210,49210,'false','PCS','','','',NULL),(406,'766BC-C','CONTOSO Storage System','PCS','F13',42650,42650,'false','PCS','','','',NULL),(407,'80006','Team Work Computer 533 MHz','PCS','',6490,6490,'false','PCS','','','','Service Electronics Ltd.'),(408,'1928-W','ST.MORITZ Storage Unit/Drawers','PCS','F2',13330,13330,'false','PCS','','','',NULL),(409,'HS-100','Housing LS-100,Oakwood 120 lts','PCS','',15,15,'false','PCS','','','',NULL),(410,'1110','Rim','PCS','F1',1.05,1.05,'false','PCS','','','','Custom Metals Incorporated'),(411,'8924-W','Server - Enterprise Package','PCS','',14200,0,'false','PCS','','','','Service Electronics Ltd.'),(412,'80024','40GB ATA-66 IDE','PCS','',1260,1260,'false','PCS','','','','Service Electronics Ltd.'),(413,'1001','Touring Bicycle','PCS','F5',350.595,350.595,'false','PCS','','1000','',NULL),(414,'1155','Socket Front','PCS','A3',0.77,0.77,'false','PCS','','','','Groene Kater BVBA'),(415,'80010','64 MB PC800 ECC','PCS','',410,410,'false','PCS','','','','Service Electronics Ltd.'),(416,'C-100','Cabling for LS-100','PCS','',15.758,15.758,'false','PCS','','','',NULL),(417,'1964-S','TOKYO Guest Chair, blue','PCS','D10',6770,6770,'false','PCS','','','','AR Day Property Management'),(418,'80208','Advanced Mouse','PCS','',70,70,'false','PCS','','','','Service Electronics Ltd.'),(419,'1310','Chain','PCS','F2',1.99,1.99,'false','PCS','','','','Groene Kater BVBA'),(420,'80027','36GB Ultra 160/M SCSI','PCS','',1220,1220,'false','PCS','','','','Service Electronics Ltd.'),(421,'1720','Hand front wheel Brake','PCS','A14',4.8,4.8,'false','PCS','','','','Custom Metals Incorporated'),(422,'70003','Rear Panel','PCS','A4',1050,1050,'false','PCS','','','','Delhi Postmaster'),(423,'LS-81','Loudspeaker, Walnut, 80W','PCS','',36,36,'false','PALLET','','','',NULL),(424,'80207','Basic Mouse','PCS','',80,80,'false','PCS','','','','Service Electronics Ltd.'),(425,'8920-W','Server - Teamwear Package','PCS','',9380,0,'false','PCS','','','','Service Electronics Ltd.'),(426,'1600','Bell','PCS','A13',2.7,2.7,'false','PCS','','','','Groene Kater BVBA'),(427,'80216-T','Ethernet Cable','PCS','B4-T',30,30,'false','PCS','','','','Delhi Postmaster'),(428,'LSU-8','Middletone speaker unit 8\"100W','PCS','',15,15,'false','PCS','','','',NULL),(429,'1450','Mudguard back','PCS','A11',3.9,3.9,'false','PCS','','','','Groene Kater BVBA'),(430,'70002','Top Panel ','PCS','A3',1010,1010,'false','PCS','','','','Delhi Postmaster'),(431,'80206','Webcam','PCS','',50,50,'false','PCS','','','','Service Electronics Ltd.'),(432,'LSU-15','Base speaker unit 15\" 100W','PCS','',15,15,'false','PCS','','','',NULL),(433,'1952-W','OSLO Storage Unit/Shelf','PCS','F3',6500,6500,'false','PCS','','','',NULL),(434,'70100','Paint, black','CAN','B1',100,100,'false','CAN','','','','AR Day Property Management'),(435,'1972-S','MUNICH Swivel Chair, yellow','PCS','D12',6670,6670,'false','PCS','','','','CoolWood Technologies'),(436,'80004','Computer III 800 MHz','PCS','',650,650,'false','PCS','','','','Service Electronics Ltd.');
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
  PRIMARY KEY (`phy_journal_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
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
-- Dumping data for table `unit_of_measure`
--

LOCK TABLES `unit_of_measure` WRITE;
/*!40000 ALTER TABLE `unit_of_measure` DISABLE KEYS */;
INSERT INTO `unit_of_measure` VALUES (1,'KG','Kilo','KGM',''),(2,'BOX','Box','BX',''),(3,'KM','Kilometers','KMT',''),(4,'CAN','Can','CA',''),(5,'HOUR','Hour','HUR',''),(6,'PACK','Pack','PK',''),(7,'PALLET','Pallet','PF',''),(8,'MILES','Miles','1A',''),(9,'DAY','Day','DAY',''),(10,'L','Liter','LTR',''),(11,'GR','Gram','GRM',''),(12,'PCS','Piece','EA','');
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
-- Dumping data for table `vendor_list`
--

LOCK TABLES `vendor_list` WRITE;
/*!40000 ALTER TABLE `vendor_list` DISABLE KEYS */;
INSERT INTO `vendor_list` VALUES (69,'01587796','Custom Metals Incorporated','','','US-AL 35242','US','','','Mr. Peter Houston','RL','FOREIGN','EXPORT','EXPORT','CUSTOM METALS INCORPORATED','CIF','USD','ENU'),(70,'01905382','NewCaSup','','','CA-ON M5E 1G5','CA','','','Mr. Toby Nixon','RL','FOREIGN','EXPORT','EXPORT','NEWCASUP','CIF','CAD','ENC'),(71,'45858585','Busterby Stole og Borde A/S','','','DK-4600','DK','','','Fr. Karen Friske','RL','EU','EU','EU','BUSTERBY STOLE OG BORDE A/S','CIF','DKK','DAN'),(72,'42125678','UP Ostrov s.p.','','','CZ-779 00','CZ','','','Roman Miklus','RL','EU','EU','EU','UP OSTROV S.P.','CIF','CZK','CSY'),(73,'47586622','Monabekken Barnesenger A/S','','','NO-0661','NO','','','Christina Philp','RL','FOREIGN','EXPORT','EXPORT','MONABEKKEN BARNESENGER A/S','CIF','NOK','NOR'),(74,'43258545','Sägewerk Mittersill','','','AT-5730','AT','','','Hr. Christian Kemp','RL','EU','EU','EU','SÄGEWERK MITTERSILL','CIF','EUR','DEA'),(75,'49494949','KKA Büromaschinen Gmbh','','','DE-86899','DE','','','','RL','EU','EU','EU','KKA BÜROMASCHINEN GMBH','CIF','EUR','DEU'),(76,'44756404','Furniture Industries','CHENNAI','BLUE','GB-E12 5TG','GB','','','Mr. Stephen A. Mew','RL','EU','EU','EU','FURNITURE INDUSTRIES','CIF','GBP','ENG'),(77,'27889998','Mountain Fisheries','','','ZA-8000','ZA','','','Mrs. Corinna Bolender','RL','FOREIGN','EXPORT','EXPORT','MOUNTAIN FISHERIES','CIF','ZAR','ENU'),(78,'41568934','Technische Betriebe Rotkreuz','','','CH-6343','CH','','','Herrn Michael Ruggiero','RL','FOREIGN','EXPORT','EXPORT','TECHNISCHE BETRIEBE ROTKREUZ','CIF','CHF','DES'),(79,'20000','AR Day Property Management','DELHI','MUMBAI','400001','','','','Mr. Frank Lee','RL','DOMESTIC','NATIONAL','NATIONAL','AR DAY PROPERTY MANAGEMENT','CIF','',''),(80,'62000','WalkerHolland','DELHI','WHITE','GB-WC1 3DG','IN','','','','RL','DOMESTIC','NATIONAL','NATIONAL','WALKERHOLLAND','CFR','',''),(81,'35741852','Huslagnir','','','IS-101','IS','','','Gudmundur Axel Hansen','RL','FOREIGN','EXPORT','EXPORT','HUSLAGNIR','CIF','ISK','ISL'),(82,'40000','Lewis Home Furniture','','GREEN','GB-IB7 7VN','','','','Mr. Barry Potter','RL','DOMESTIC','NATIONAL','NATIONAL','LEWIS HOME FURNITURE','CIF','',''),(83,'47562214','Stilm¢bler as','','','NO-0552','NO','','','Sisser Wichmann','RL','FOREIGN','EXPORT','EXPORT','STILM¢BLER AS','CIF','NOK','NOR'),(84,'32554455','PURE-LOOK','','','BE-2800','BE','','','Rob Caron','RL','EU','EU','EU','PURE-LOOK','CIF','EUR','NLB'),(85,'01254796','Progressive Home Furnishings','','','US-SC 27136','US','','','Mr. Michael Sean Ray','RL','FOREIGN','EXPORT','EXPORT','PROGRESSIVE HOME FURNISHINGS','CIF','','ENU'),(86,'60000','Grassblue Ltd.','DELHI','WHITE','GB-N12 5XY','','','','','RL','DOMESTIC','NATIONAL','NATIONAL','GRASSBLUE LTD.','CIF','',''),(87,'42784512','TON s.r.o.','','','CZ-697 01','CZ','','','Zuzana Janska','RL','EU','EU','EU','TON S.R.O.','CIF','CZK','CSY'),(88,'43589632','Paul Brettschneider KG','','','AT-8850','AT','','','Hr. Michael L. Rothkugel','RL','EU','EU','EU','PAUL BRETTSCHNEIDER KG','CIF','EUR','DEA'),(89,'45774477','Lyselette Lamper A/S','','','DK-5000','DK','','','Hr. Allan Vinther-Wahl','RL','EU','EU','EU','LYSELETTE LAMPER A/S','CIF','DKK','DAN'),(90,'32456123','Groene Kater BVBA','','','BE-1851','BE','','','Roger Van Houten','RL','EU','EU','EU','GROENE KATER BVBA','CIF','EUR','NLB'),(91,'31568974','Koekamp Leerindustrie','','','NL-5132 EE','NL','','','Anita Langers','RL','EU','EU','EU','KOEKAMP LEERINDUSTRIE','CIF','EUR','NLD'),(92,'38521479','Topol Slovenija d.o.o.','','','SI-4502','SI','','','ga. Tina Gorenc','RL','EU','EU','EU','TOPOL SLOVENIJA D.O.O.','CIF','EUR','SLV'),(93,'43698547','Beschläge Schacherhuber','','','AT-1230','AT','','','Hr. Frank Pellow','RL','EU','EU','EU','BESCHLÄGE SCHACHERHUBER','CIF','EUR','DEA'),(94,'41124089','Kradolf Zimmerdecke AG','','','CH-6405','CH','','','Herrn Dick Dievendorff','RL','FOREIGN','EXPORT','EXPORT','KRADOLF ZIMMERDECKE AG','CIF','CHF','DES'),(95,'50000','Service Electronics Ltd.','','','GB-WD2 4RG','','','','Mr. Marc Zimmerman','RL','DOMESTIC','NATIONAL','NATIONAL','SERVICE ELECTRONICS LTD.','CIF','',''),(96,'IC1030','Cronus Cardoxy Procurement','','','DE-20097','DE','','','','RL','EU','EU','EU','CRONUS CARDOXY PROCUREMENT','CIF','EUR','DEU'),(97,'21201992','Texpro Maroc','','','MA-20000','MA','','','M. Charaf HAMZAOUI','RL','FOREIGN','EXPORT','EXPORT','TEXPRO MAROC','CIF','MAD',''),(98,'10000','Delhi Postmaster','DELHI','DELHI','110001','','','','Mrs. Carol Philips','RL','DOMESTIC','NATIONAL','NATIONAL','DELHI POSTMASTER','CIF','',''),(99,'42895623','Mach & spol. v.o.s.','','','CZ-678 01','CZ','','','Milan Cvrkal','RL','EU','EU','EU','MACH & SPOL. V.O.S.','CIF','CZK','CSY'),(100,'20300190','Malay-Dan Export Unit Sdn Bhd','','YELLOW','MY-50450','MY','','','Mr. Fabrice Perez','RL','FOREIGN','EXPORT','EXPORT','MALAY-DAN EXPORT UNIT SDN BHD','CIF','MYR','ENU'),(101,'20323323','Tengah Butong Sdn Bhd','','','MY-88100','MY','','','Mrs. Anisah Yoosoof','RL','FOREIGN','EXPORT','EXPORT','TENGAH BUTONG SDN BHD','CIF','MYR','ENU'),(102,'32665544','Overschrijd de Grens SA','','','BE-8500','BE','','','Tom Vande Velde','RL','EU','EU','EU','OVERSCHRIJD DE GRENS SA','CIF','EUR','NLB'),(103,'46558855','Kinnareds Träindustri AB','','','SE-521 03','SE','','','','RL','EU','EU','EU','KINNAREDS TRÄINDUSTRI AB','CIF','SEK','SVE'),(104,'46635241','Viksjö Snickerifabrik AB','','','SE-852 33','SE','','','','RL','EU','EU','EU','VIKSJÖ SNICKERIFABRIK AB','CIF','SEK','SVE'),(105,'49454647','VAG - Jürgensen','','','DE-20097','DE','','','','RL','EU','EU','EU','VAG - JÜRGENSEN','CIF','EUR','DEU'),(106,'47521478','M¢belhuset AS','','','NO-1400','NO','','','Bjarke Rust Christensen','RL','FOREIGN','EXPORT','EXPORT','M¢BELHUSET AS','CIF','NOK','NOR'),(107,'38458653','IVERKA POHISTVO d.o.o.','','','SI-4502','SI','','','g. Lojze Dolenc','RL','EU','EU','EU','IVERKA POHISTVO D.O.O.','CIF','EUR','SLV'),(108,'01905283','Mundersand Corporation','','','CA-ON P7A 4K8','CA','','','Mr. Mike Hines','RL','FOREIGN','EXPORT','EXPORT','MUNDERSAND CORPORATION','CIF','CAD','ENC'),(109,'IC1020','Cronus Cardoxy Sales','','','DK-2100','DK','','','','RL','EU','EU','EU','CRONUS CARDOXY SALES','CIF','DKK','DAN'),(110,'33012999','Club Euroamis','','YELLOW','FR-44450','FR','','','M. Francois GERARD','RL','EU','EU','EU','CLUB EUROAMIS','CIF','EUR','FRA'),(111,'20319939','KDHSL99 Sdn Bhd','','','MY-42000','MY','','','Mr. Toh Chin Theng','RL','FOREIGN','EXPORT','EXPORT','KDHSL99 SDN BHD','CIF','MYR','ENU'),(112,'49989898','JB-Spedition','','','DE-80997','DE','','','','RL','EU','EU','EU','JB-SPEDITION','CIF','EUR','DEU'),(113,'34110257','Importaciones S.A.','','','ES-03003','ES','','','Sr. Tomas Navarro','RL','EU','EU','EU','IMPORTACIONES S.A.','CIF','EUR','ESP'),(114,'34151086','Subacqua','','','ES-37001','ES','','','Srta. Pilar Pinilla Gallego','RL','EU','EU','EU','SUBACQUA','CIF','EUR','ESP'),(115,'27833998','Jewel Gold Mine','','','ZA-2000','ZA','','','Mr. Craig Dewer','RL','FOREIGN','EXPORT','EXPORT','JEWEL GOLD MINE','CIF','ZAR','ENU'),(116,'35336699','Hurdir HF','','','IS-108','IS','','','Anna Lisa Sigmundsdottir','RL','FOREIGN','EXPORT','EXPORT','HURDIR HF','CIF','ISK','ISL'),(117,'27299299','Big 5 Video','','','ZA-3900','ZA','','','Mr. Kevin Kennedy','RL','FOREIGN','EXPORT','EXPORT','BIG 5 VIDEO','CIF','ZAR','ENU'),(118,'33299199','Belle et Belle','','','FR-50670','FR','','','Mme. Nicole CARON','RL','EU','EU','EU','BELLE ET BELLE','CIF','EUR','FRA'),(119,'01905777','OakvilleWorld','','','CA-ON L6J 3J3','CA','','','Mr. Sean P. Alexander','RL','FOREIGN','EXPORT','EXPORT','OAKVILLEWORLD','CIF','CAD','ENC'),(120,'44127904','WoodMart Supply Co.','','','GB-SA3 7HI','GB','','','Mr. Joseph Matthews','RL','EU','EU','EU','WOODMART SUPPLY CO.','CIF','GBP','ENG'),(121,'31580305','Beekhuysen BV','','','NL-7321 HE','NL','','','Alex Roland','RL','EU','EU','EU','BEEKHUYSEN BV','CIF','EUR','NLD'),(122,'61000','Electronics Ltd.','DELHI','WHITE','GB-N16 34Z','IN','','','','RL','DOMESTIC','NATIONAL','NATIONAL','ELECTRONICS LTD.','CPT','',''),(123,'46895623','Svensk Möbeltextil AB','','','SE-415 06','SE','','','','RL','EU','EU','EU','SVENSK MÖBELTEXTIL AB','CIF','SEK','SVE'),(124,'33399927','Aranteaux Aliments','','','FR-83250','FR','','','M. Francois AJENSTAT','RL','EU','EU','EU','ARANTEAUX ALIMENTS','CIF','EUR','FRA'),(125,'31147896','Houtindustrie Bruynsma','','','NL-1530 JM','NL','','','Lieve Casteels','RL','EU','EU','EU','HOUTINDUSTRIE BRUYNSMA','CIF','EUR','NLD'),(126,'35225588','Husplast HF','','','IS-112','IS','','','Vilhjalmur Arnason','RL','FOREIGN','EXPORT','EXPORT','HUSPLAST HF','CIF','ISK','ISL'),(127,'01863656','American Wood Exports','','','US-NY 11010','US','','','Mr. Jeff D. Henshaw','RL','FOREIGN','EXPORT','EXPORT','AMERICAN WOOD EXPORTS','CIF','USD','ENU'),(128,'44729910','Boybridge Tool Mart','','','GB-N16 34Z','GB','','','Mr. David Campbell','RL','EU','EU','EU','BOYBRIDGE TOOL MART','CIF','GBP','ENG'),(129,'34280789','Transporte Roas','','','ES-07001','ES','','','Sr. Fabricio Noriega','RL','EU','EU','EU','TRANSPORTE ROAS','CIF','EUR','ESP'),(130,'38654478','POIIORLES d.d.','','','SI-6000','SI','','','ga. Borka Durovic','RL','EU','EU','EU','POIIORLES D.D.','CIF','EUR','SLV'),(131,'41483124','Matter Transporte','','','CH-4133','CH','','','Herrn Michael Pfeiffer','RL','FOREIGN','EXPORT','EXPORT','MATTER TRANSPORTE','CIF','CHF','DES'),(132,'21218838','Top Bureau','','BLUE','MA-90000','MA','','','M. Fadi FAKHOURI','RL','FOREIGN','EXPORT','EXPORT','TOP BUREAU','CIF','MAD',''),(133,'21248839','Comacycle','','','MA-20800','MA','','','','RL','FOREIGN','EXPORT','EXPORT','COMACYCLE','CIF','MAD',''),(134,'30000','CoolWood Technologies','','NOIDA','201301','','','','Mr. Richard Bready','RL','DOMESTIC','NATIONAL','NATIONAL','COOLWOOD TECHNOLOGIES','CIF','',''),(135,'44127914','Mortimor Car Company','','','GB-SA3 7HI','GB','','','Mr. Andrew R. Hill','RL','EU','EU','EU','MORTIMOR CAR COMPANY','CIF','GBP','ENG'),(136,'45868686','Ahornby Hvidevare A/S','','','DK-2100','DK','','','Hr. Allan Benny Guinot','RL','EU','EU','EU','AHORNBY HVIDEVARE A/S','CIF','DKK','DAN');
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
  PRIMARY KEY (`ware_house_id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-05-12 12:08:24

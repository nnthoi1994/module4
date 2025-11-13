-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: localhost    Database: order_meal_db
-- ------------------------------------------------------
-- Server version	8.0.43

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `app_state`
--

DROP TABLE IF EXISTS `app_state`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `app_state` (
  `id` int NOT NULL,
  `is_ordering_locked` bit(1) NOT NULL,
  `has_been_spun` bit(1) NOT NULL,
  `selected_users_json` text,
  `picker_user_id` bigint DEFAULT NULL,
  `bank_account_name` varchar(255) DEFAULT NULL,
  `bank_account_no` varchar(255) DEFAULT NULL,
  `bank_bin` varchar(255) DEFAULT NULL,
  `bank_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKq6obpy5tyi7inh7b76vr3jc0j` (`picker_user_id`),
  CONSTRAINT `FKq6obpy5tyi7inh7b76vr3jc0j` FOREIGN KEY (`picker_user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_state`
--

LOCK TABLES `app_state` WRITE;
/*!40000 ALTER TABLE `app_state` DISABLE KEYS */;
INSERT INTO `app_state` VALUES (1,_binary '\0',_binary '\0',NULL,NULL,'NGUYEN NGOC THOI','0935988224','970422','MB Bank');
/*!40000 ALTER TABLE `app_state` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dishes`
--

DROP TABLE IF EXISTS `dishes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dishes` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `price` decimal(38,2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_g9v3f8f18je2t2ou8fvwse3kq` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dishes`
--

LOCK TABLES `dishes` WRITE;
/*!40000 ALTER TABLE `dishes` DISABLE KEYS */;
INSERT INTO `dishes` VALUES (1,'Cà tím + Cá basa + trứng chả',25000.00),(7,'Sườn + trứng ốp la + kim chi',25000.00),(8,'Cơm đùi gà + đậu nhồi thịt + trứng ốp',30000.00),(9,'Đậu khuôn thường + trứng chả',25000.00),(10,'Đùi gà + khuôn đậu nhồi thịt + bắp cải',25000.00),(11,'Thập cẩm + cà tím',25000.00),(12,'Đùi gà + chả trứng ',25000.00),(13,'Cơm heo quay + đậu khuôn + tôm bột',30000.00),(14,'Sườn non + ốp la',25000.00),(15,'Thịt luộc + trứng ốp la',25000.00),(16,'Xíu mại + Thịt kho + cà tím',25000.00),(17,'Đùi gà + Lá lốt + Đồ xào',25000.00),(18,'Thịt viên + chả lá lốt',25000.00),(19,'Sườn non + đậu khuôn + chả cá',30000.00),(20,'thịt heo quay + trứng ko ',25000.00),(21,'Đùi gà + thịt heo',25000.00),(22,'Sườn non + đậu khuôn + cà tím',25000.00),(23,'Đùi gà+ trứng chả',25000.00),(24,'Cá phèn + trứng chả ',25000.00),(25,'Heo quay + cá phích bột + kim chi',25000.00),(26,'Đùi gà + đậu hũ',25000.00),(27,'Chả cá + xíu mại + giá xào',25000.00),(28,'Cà tím + ram + trứng chả',25000.00),(29,'Sườn non + đậu khuôn + cá phích bột',30000.00),(30,'Thịt xíu + thịt kho + bắp cải xào',30000.00),(31,'Đồ xào + Ram + Sườn non + Lá lốt',30000.00),(32,'Thập cẩm + đồ xào ',25000.00),(33,'Thịt kho ruốc + đậu khuôn + cà tím',25000.00),(34,'Sườn non+đậu khuôn+ tôm bột',30000.00),(35,'Thịt heo quay chả trứng ',25000.00),(37,'Cá kho + trứng chả + kim chi',25000.00),(38,'Đùi gà + đậu nhồi thịt',25000.00),(39,'Sườn non + cá phích bột',25000.00),(40,'Cá phèn + cà tím',25000.00),(41,'Cá basa + xíu mại',25000.00),(42,'Heo quay + đậu khuôn ',25000.00),(43,'Đùi gà chiên mắm+trứng chả',25000.00),(44,'Sườn + đậu khuôn',25000.00),(45,'Cà tím + cá phèn + ram',25000.00),(46,'chả cá chiên + gan rim + kim chi',25000.00),(47,'Xíu mại + đậu khuôn nhồi',25000.00),(48,'Đồ xào thịt luộc',25000.00),(49,'Thịt kho ruốt+ chả cá+ đậu khuôn + măng',25000.00),(50,'Cá chim chiên mắm + kim chi',25000.00),(51,'sườn non + ram + cà tím',25000.00),(52,'Thịt kho + trứng',25000.00),(53,'Đùi gà + chả trứng + kim chi',25000.00),(54,'Sườn non + chả cá+ đậu khuôn',30000.00);
/*!40000 ALTER TABLE `dishes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `price_per_item` decimal(38,2) NOT NULL,
  `quantity` int NOT NULL,
  `dish_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKn06bdypik73hotpxvefsrtn77` (`dish_id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKn06bdypik73hotpxvefsrtn77` FOREIGN KEY (`dish_id`) REFERENCES `dishes` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (1,25000.00,1,1,2),(2,25000.00,1,1,3),(16,25000.00,1,11,40),(17,25000.00,2,11,46),(18,25000.00,2,12,46),(19,25000.00,1,11,52),(20,30000.00,1,8,48),(21,25000.00,1,10,39),(23,25000.00,1,7,59),(24,25000.00,1,9,61),(25,25000.00,1,7,62),(26,30000.00,1,13,41),(27,25000.00,1,14,66),(36,25000.00,1,11,82),(37,25000.00,1,16,83),(38,25000.00,1,14,84),(39,25000.00,1,9,92),(40,30000.00,1,13,93),(41,25000.00,1,17,91),(42,30000.00,1,19,90),(43,25000.00,1,10,99),(44,25000.00,1,20,98),(45,25000.00,1,1,104),(46,25000.00,1,22,100),(47,25000.00,1,23,85),(48,25000.00,1,24,85),(49,25000.00,1,14,107),(50,25000.00,1,14,110),(51,25000.00,1,20,112),(52,25000.00,1,11,81),(53,25000.00,1,23,103),(55,25000.00,1,14,116),(56,25000.00,1,1,116),(87,25000.00,1,23,158),(88,25000.00,1,25,163),(89,25000.00,1,1,159),(90,25000.00,1,11,162),(91,25000.00,1,27,161),(92,25000.00,1,28,157),(93,30000.00,1,29,164),(94,25000.00,1,11,175),(95,30000.00,1,30,167),(96,25000.00,1,27,178),(97,25000.00,1,10,180),(98,25000.00,1,32,179),(99,30000.00,1,31,173),(100,25000.00,1,33,181),(101,25000.00,1,16,187),(103,25000.00,1,1,196),(104,30000.00,1,34,201),(106,25000.00,1,16,206),(108,25000.00,1,11,209),(109,25000.00,1,35,208),(110,25000.00,1,11,212),(113,25000.00,1,37,210),(115,25000.00,1,1,200),(116,25000.00,1,10,222),(117,25000.00,1,39,221),(118,25000.00,1,25,220),(119,25000.00,1,38,225),(120,25000.00,1,40,219),(121,25000.00,1,41,213),(122,25000.00,1,42,216),(123,25000.00,1,42,232),(124,25000.00,1,9,234),(125,25000.00,1,43,236),(126,25000.00,1,1,223),(147,25000.00,1,17,266),(148,25000.00,1,44,267),(149,25000.00,1,45,265),(150,25000.00,1,46,270),(151,25000.00,1,18,275),(152,25000.00,1,47,273),(153,25000.00,1,1,274),(154,25000.00,1,11,275),(155,25000.00,1,39,276),(156,25000.00,1,14,277),(157,25000.00,1,48,275),(158,25000.00,1,49,283),(159,25000.00,1,50,286),(160,25000.00,1,51,282),(161,25000.00,1,52,290),(163,25000.00,1,53,291),(164,25000.00,1,16,294),(165,25000.00,1,51,295),(166,30000.00,1,54,299);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `is_completed` bit(1) NOT NULL,
  `order_date` date NOT NULL,
  `total_amount` decimal(38,2) NOT NULL,
  `user_id` bigint NOT NULL,
  `is_paid` tinyint(1) NOT NULL DEFAULT '0',
  `payment_code` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK32ql8ubntj5uh44ph9659tiih` (`user_id`),
  CONSTRAINT `FK32ql8ubntj5uh44ph9659tiih` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=307 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,_binary '\0','2025-11-04',0.00,1,0,NULL),(2,_binary '','2025-11-04',25000.00,2,0,NULL),(3,_binary '\0','2025-11-04',25000.00,2,0,NULL),(38,_binary '\0','2025-11-05',0.00,1,0,NULL),(39,_binary '','2025-11-05',25000.00,14,0,NULL),(40,_binary '','2025-11-05',25000.00,4,0,NULL),(41,_binary '','2025-11-05',30000.00,3,0,NULL),(42,_binary '\0','2025-11-05',0.00,12,0,NULL),(43,_binary '\0','2025-11-05',0.00,18,0,NULL),(44,_binary '\0','2025-11-05',0.00,22,0,NULL),(45,_binary '\0','2025-11-05',0.00,4,0,NULL),(46,_binary '','2025-11-05',100000.00,13,0,NULL),(47,_binary '\0','2025-11-05',0.00,15,0,NULL),(48,_binary '','2025-11-05',30000.00,6,0,NULL),(49,_binary '\0','2025-11-05',0.00,7,0,NULL),(50,_binary '\0','2025-11-05',0.00,13,0,NULL),(51,_binary '\0','2025-11-05',0.00,8,0,NULL),(52,_binary '','2025-11-05',25000.00,19,0,NULL),(53,_binary '\0','2025-11-05',0.00,23,0,NULL),(55,_binary '\0','2025-11-05',0.00,19,0,NULL),(56,_binary '\0','2025-11-05',0.00,6,0,NULL),(57,_binary '\0','2025-11-05',0.00,14,0,NULL),(58,_binary '\0','2025-11-05',0.00,24,0,NULL),(59,_binary '','2025-11-05',25000.00,10,0,NULL),(60,_binary '\0','2025-11-05',0.00,10,0,NULL),(61,_binary '','2025-11-05',25000.00,9,0,NULL),(62,_binary '','2025-11-05',25000.00,17,0,NULL),(63,_binary '\0','2025-11-05',0.00,17,0,NULL),(64,_binary '\0','2025-11-05',0.00,9,0,NULL),(65,_binary '\0','2025-11-05',0.00,3,0,NULL),(66,_binary '','2025-11-05',25000.00,2,0,NULL),(67,_binary '\0','2025-11-05',0.00,2,0,NULL),(81,_binary '','2025-11-06',25000.00,2,0,NULL),(82,_binary '','2025-11-06',25000.00,11,0,NULL),(83,_binary '','2025-11-06',25000.00,21,0,NULL),(84,_binary '','2025-11-06',25000.00,23,0,NULL),(85,_binary '','2025-11-06',50000.00,11,0,NULL),(86,_binary '\0','2025-11-06',0.00,25,0,NULL),(87,_binary '\0','2025-11-06',0.00,21,0,NULL),(88,_binary '\0','2025-11-06',0.00,23,0,NULL),(89,_binary '\0','2025-11-06',0.00,26,0,NULL),(90,_binary '','2025-11-06',30000.00,3,0,NULL),(91,_binary '','2025-11-06',25000.00,27,0,NULL),(92,_binary '','2025-11-06',25000.00,9,0,NULL),(93,_binary '','2025-11-06',30000.00,16,0,NULL),(94,_binary '\0','2025-11-06',0.00,9,0,NULL),(95,_binary '\0','2025-11-06',0.00,16,0,NULL),(96,_binary '\0','2025-11-06',0.00,27,0,NULL),(97,_binary '\0','2025-11-06',0.00,3,0,NULL),(98,_binary '','2025-11-06',25000.00,24,0,NULL),(99,_binary '','2025-11-06',25000.00,14,0,NULL),(100,_binary '','2025-11-06',25000.00,28,0,NULL),(101,_binary '\0','2025-11-06',0.00,14,0,NULL),(102,_binary '\0','2025-11-06',0.00,24,0,NULL),(103,_binary '','2025-11-06',25000.00,5,0,NULL),(104,_binary '','2025-11-06',25000.00,4,0,NULL),(105,_binary '\0','2025-11-06',0.00,4,0,NULL),(106,_binary '\0','2025-11-06',0.00,28,0,NULL),(107,_binary '','2025-11-06',25000.00,12,0,NULL),(108,_binary '\0','2025-11-06',0.00,11,0,NULL),(109,_binary '\0','2025-11-06',0.00,12,0,NULL),(110,_binary '','2025-11-06',25000.00,10,0,NULL),(111,_binary '\0','2025-11-06',0.00,10,0,NULL),(112,_binary '','2025-11-06',25000.00,17,0,NULL),(113,_binary '\0','2025-11-06',0.00,17,0,NULL),(114,_binary '\0','2025-11-06',0.00,2,0,NULL),(115,_binary '\0','2025-11-06',0.00,5,0,NULL),(116,_binary '','2025-11-06',50000.00,1,0,NULL),(117,_binary '\0','2025-11-06',0.00,1,0,NULL),(156,_binary '\0','2025-11-07',0.00,1,0,NULL),(157,_binary '','2025-11-07',25000.00,2,0,NULL),(158,_binary '','2025-11-07',25000.00,16,0,NULL),(159,_binary '','2025-11-07',25000.00,5,0,NULL),(160,_binary '\0','2025-11-07',0.00,12,0,NULL),(161,_binary '','2025-11-07',25000.00,17,0,NULL),(162,_binary '','2025-11-07',25000.00,4,0,NULL),(163,_binary '','2025-11-07',25000.00,10,0,NULL),(164,_binary '','2025-11-07',30000.00,3,0,NULL),(165,_binary '\0','2025-11-07',0.00,16,0,NULL),(166,_binary '\0','2025-11-07',0.00,9,0,NULL),(167,_binary '','2025-11-07',30000.00,22,0,NULL),(168,_binary '\0','2025-11-07',0.00,15,0,NULL),(169,_binary '\0','2025-11-07',0.00,5,0,NULL),(170,_binary '\0','2025-11-07',0.00,10,0,NULL),(171,_binary '\0','2025-11-07',0.00,17,0,NULL),(172,_binary '\0','2025-11-07',0.00,4,0,NULL),(173,_binary '','2025-11-07',30000.00,27,0,NULL),(174,_binary '\0','2025-11-07',0.00,2,0,NULL),(175,_binary '','2025-11-07',25000.00,3,0,NULL),(176,_binary '\0','2025-11-07',0.00,3,0,NULL),(177,_binary '\0','2025-11-07',0.00,22,0,NULL),(178,_binary '','2025-11-07',25000.00,23,0,NULL),(179,_binary '','2025-11-07',25000.00,24,0,NULL),(180,_binary '','2025-11-07',25000.00,14,0,NULL),(181,_binary '','2025-11-07',25000.00,28,0,NULL),(182,_binary '\0','2025-11-07',0.00,23,0,NULL),(183,_binary '\0','2025-11-07',0.00,14,0,NULL),(184,_binary '\0','2025-11-07',0.00,24,0,NULL),(185,_binary '\0','2025-11-07',0.00,27,0,NULL),(186,_binary '\0','2025-11-07',0.00,28,0,NULL),(187,_binary '','2025-11-07',25000.00,21,0,NULL),(188,_binary '\0','2025-11-07',0.00,21,0,NULL),(189,_binary '\0','2025-11-07',0.00,7,0,NULL),(190,_binary '\0','2025-11-07',0.00,8,0,NULL),(191,_binary '\0','2025-11-07',0.00,18,0,NULL),(195,_binary '\0','2025-11-08',0.00,1,0,NULL),(196,_binary '','2025-11-09',25000.00,2,1,'PAYTHOI0911IWJCXRPAY'),(197,_binary '\0','2025-11-09',0.00,1,0,NULL),(198,_binary '\0','2025-11-09',0.00,2,0,NULL),(199,_binary '\0','2025-11-10',0.00,1,0,NULL),(200,_binary '','2025-11-10',25000.00,2,1,'PAYTHOI101161VAUQPAY'),(201,_binary '','2025-11-10',30000.00,3,1,'PAYDUONGQUOCHUY1011W0T4YPPAY'),(204,_binary '\0','2025-11-10',0.00,3,0,NULL),(206,_binary '','2025-11-10',25000.00,21,1,'PAYQUOCKHANH1011KQ9OMKPAY'),(207,_binary '\0','2025-11-10',0.00,21,0,NULL),(208,_binary '','2025-11-10',25000.00,11,1,'PAYPHAMNGOCKHANH10111VHT5WPAY'),(209,_binary '','2025-11-10',25000.00,8,1,'PAYQUAN1011Z30LGHPAY'),(210,_binary '','2025-11-10',25000.00,5,1,'PAYHOVIETTUNG1011V3L6FWPAY'),(212,_binary '','2025-11-10',25000.00,11,1,'PAYPHAMNGOCKHANH10111VHT5WPAY'),(213,_binary '','2025-11-10',25000.00,17,1,'PAYLEVANTAM1011C5OCP4PAY'),(214,_binary '\0','2025-11-10',0.00,8,0,NULL),(216,_binary '','2025-11-10',25000.00,24,1,'PAYUYEN101179KW53PAY'),(217,_binary '\0','2025-11-10',0.00,11,0,NULL),(218,_binary '\0','2025-11-10',0.00,5,0,NULL),(219,_binary '','2025-11-10',25000.00,4,1,'PAYNGUYENNHATTHANH1011R9R20WPAY'),(220,_binary '','2025-11-10',25000.00,10,1,'PAYVANTHANH10111O0PKMPAY'),(221,_binary '','2025-11-10',25000.00,12,1,'PAYTINH10116ND2TAPAY'),(222,_binary '','2025-11-10',25000.00,14,1,'PAYLEBANHATMINH1011THIN04PAY'),(223,_binary '\0','2025-11-10',25000.00,2,0,NULL),(224,_binary '\0','2025-11-10',0.00,12,0,NULL),(225,_binary '','2025-11-10',25000.00,15,0,'PAYNHON1011WXNIMGPAY'),(226,_binary '\0','2025-11-10',0.00,10,0,NULL),(227,_binary '\0','2025-11-10',0.00,15,0,NULL),(228,_binary '\0','2025-11-10',0.00,4,0,NULL),(229,_binary '\0','2025-11-10',0.00,14,0,NULL),(230,_binary '\0','2025-11-10',0.00,17,0,NULL),(231,_binary '\0','2025-11-10',0.00,24,0,NULL),(232,_binary '','2025-11-10',25000.00,23,1,'PAYTRANLEGIANGUYEN1011I3NGNZPAY'),(233,_binary '\0','2025-11-10',0.00,23,0,NULL),(234,_binary '','2025-11-10',25000.00,9,1,'PAYDOANNHATLONG1011J7KZSDPAY'),(235,_binary '\0','2025-11-10',0.00,9,0,NULL),(236,_binary '','2025-11-10',25000.00,22,1,'PAYCHIHONG1011MT652JPAY'),(237,_binary '\0','2025-11-10',0.00,22,0,NULL),(264,_binary '\0','2025-11-11',0.00,1,0,NULL),(265,_binary '','2025-11-11',25000.00,2,0,NULL),(266,_binary '','2025-11-11',25000.00,27,0,NULL),(267,_binary '','2025-11-11',25000.00,15,0,NULL),(268,_binary '\0','2025-11-11',0.00,27,0,NULL),(269,_binary '\0','2025-11-11',0.00,15,0,NULL),(270,_binary '','2025-11-11',25000.00,17,1,NULL),(271,_binary '\0','2025-11-11',0.00,2,0,NULL),(272,_binary '\0','2025-11-11',0.00,17,0,NULL),(273,_binary '','2025-11-11',25000.00,19,0,NULL),(274,_binary '','2025-11-11',25000.00,29,0,NULL),(275,_binary '','2025-11-11',75000.00,25,0,NULL),(276,_binary '','2025-11-11',25000.00,10,1,NULL),(277,_binary '','2025-11-11',25000.00,14,1,NULL),(278,_binary '\0','2025-11-11',0.00,9,0,NULL),(279,_binary '\0','2025-11-11',0.00,19,0,NULL),(280,_binary '\0','2025-11-11',0.00,29,0,NULL),(281,_binary '\0','2025-11-11',0.00,10,0,NULL),(282,_binary '','2025-11-11',25000.00,4,1,NULL),(283,_binary '','2025-11-11',25000.00,8,0,NULL),(284,_binary '\0','2025-11-11',0.00,25,0,NULL),(285,_binary '\0','2025-11-11',0.00,14,0,NULL),(286,_binary '','2025-11-11',25000.00,22,1,NULL),(287,_binary '\0','2025-11-11',0.00,8,0,NULL),(288,_binary '\0','2025-11-11',0.00,22,0,NULL),(289,_binary '\0','2025-11-11',0.00,4,0,NULL),(290,_binary '','2025-11-11',25000.00,12,1,NULL),(291,_binary '','2025-11-11',25000.00,5,1,NULL),(292,_binary '\0','2025-11-11',0.00,12,0,NULL),(294,_binary '','2025-11-11',25000.00,21,1,NULL),(295,_binary '','2025-11-11',25000.00,23,1,NULL),(296,_binary '\0','2025-11-11',0.00,5,0,NULL),(297,_binary '\0','2025-11-11',0.00,21,0,NULL),(298,_binary '\0','2025-11-11',0.00,23,0,NULL),(299,_binary '','2025-11-11',30000.00,3,1,NULL),(300,_binary '\0','2025-11-11',0.00,3,0,NULL),(305,_binary '\0','2025-11-12',0.00,1,0,'PAYADMINISTRATOR1211IYV2DPPAY'),(306,_binary '\0','2025-11-12',0.00,2,0,'PAYTHOI12119JJWBCPAY');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uploaded_images`
--

DROP TABLE IF EXISTS `uploaded_images`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `uploaded_images` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_path` varchar(255) NOT NULL,
  `upload_time` datetime(6) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uploaded_images`
--

LOCK TABLES `uploaded_images` WRITE;
/*!40000 ALTER TABLE `uploaded_images` DISABLE KEYS */;
INSERT INTO `uploaded_images` VALUES (1,'/uploads/774995c1-0c3b-4084-8249-df6717c50066_474516665_3771939886450122_5757290070174655077_n.jpg','2025-11-05 00:15:25.813825'),(6,'/uploads/01cc4727-a922-44be-b075-f87c04030d7d_IMG_20251105_104718.jpg','2025-11-05 10:49:15.488104'),(7,'/uploads/3f400984-de4e-41f0-ab45-6e3d8b71571b_IMG_20251105_104716.jpg','2025-11-05 10:49:15.502998'),(8,'/uploads/89c3708f-f66a-4a90-8657-22dc29c5f461_IMG_20251105_104715.jpg','2025-11-05 10:49:15.515863'),(9,'/uploads/980e637d-7e07-41d1-b8ac-8b720e5a1990_IMG_20251105_104714.jpg','2025-11-05 10:49:15.526808'),(14,'/uploads/e58ef76b-a0e8-417d-b7d6-03152a196c3c_IMG_20251106_104234.jpg','2025-11-06 10:42:53.696325'),(15,'/uploads/67639462-3781-4f5f-9293-936934da8524_IMG_20251106_104233.jpg','2025-11-06 10:42:53.725612'),(16,'/uploads/3cadbd22-584d-47b1-a7c7-c15030f4f25a_IMG_20251106_104232.jpg','2025-11-06 10:42:53.738199'),(17,'/uploads/222c4db8-2748-47c1-b6bf-d0ee75db3f2f_IMG_20251106_104231.jpg','2025-11-06 10:42:53.749937'),(18,'/uploads/276d34a2-d3bd-482f-9b4a-04e0f04374c9_IMG_1762486957604_1762486988877.jpg','2025-11-07 10:43:33.120110'),(19,'/uploads/95a711aa-d50e-4a50-a6c4-a26f2f6c4e37_IMG_1762486957587_1762486987980.jpg','2025-11-07 10:43:33.139572'),(20,'/uploads/1d847e7f-e5e5-46dd-b371-3c34983bcd51_IMG_1762486957571_1762486987081.jpg','2025-11-07 10:43:33.152561'),(21,'/uploads/4648004a-e4f2-4914-b2e9-0110180d9cd4_IMG_1762486957557_1762486984842.jpg','2025-11-07 10:43:33.163686'),(22,'/uploads/734d7715-d5aa-4058-94dc-75074a3b05d3_IMG_1762486957527_1762486984178.jpg','2025-11-07 10:43:33.178312'),(23,'/uploads/bb33921c-a2fd-40f0-9241-19cc3d770ac0_IMG_20251110_105211.jpg','2025-11-10 10:52:40.192101'),(24,'/uploads/987b495d-9bf8-46a3-b038-05d4399a5511_IMG_20251110_105210.jpg','2025-11-10 10:52:40.208841'),(25,'/uploads/c8ac3c2a-60c1-4cd7-8160-f75ba7068d9c_IMG_20251110_105204.jpg','2025-11-10 10:52:40.221809'),(26,'/uploads/76a97263-8c21-43b0-91c5-abd7be4d058a_IMG_20251110_105203.jpg','2025-11-10 10:52:40.236366'),(27,'/uploads/25e03be7-8f98-4f2a-af6f-f4b8c89b9084_19652772640106571922.jpg','2025-11-11 10:33:31.350048'),(28,'/uploads/1b4e22e8-a675-4e3b-b40c-264a1fda174d_2065657112282502343.jpg','2025-11-11 10:33:31.368127'),(29,'/uploads/f388364f-c006-406c-a209-a68b338fcbfd_4188551498117209084.jpg','2025-11-11 10:33:31.381672'),(30,'/uploads/09c75946-e17a-47ef-997e-0cbf1da5e025_43152695464756260511.jpg','2025-11-11 10:33:31.394915');
/*!40000 ALTER TABLE `uploaded_images` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `full_name` varchar(255) NOT NULL,
  `gender` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UK_r43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'admin@ordermeal.com','Administrator','MALE','admin','ADMIN','admin'),(2,'nnthoi1994@gmail.com','Thới','MALE','admin','CUSTOMER','nnthoi1994'),(3,'huyduong031101@gmail.com','Dương Quốc Huy','MALE','huy123','CUSTOMER','huduong0311'),(4,'johnsmithqthh@gmail.com','Nguyễn Nhật Thành','MALE','123456','CUSTOMER','abcxyz231'),(5,'viettung0203@gmail.com','Ho Viet Tung','MALE','tung0203','CUSTOMER','viettung0203'),(6,'mhuynk1005@gmail.com','Huỳnh Minh Huy','MALE','123123123','CUSTOMER','huypro123'),(7,'hathanh.0113@gmail.com','Phan Thanh Hà','MALE','phanthanhha','CUSTOMER','phanthanhha'),(8,'nguyentienquan12a4@gmail.com','Quân','MALE','tienquan123','CUSTOMER','quanne'),(9,'shenlong0009@gmail.com','Đoàn Nhật Long','MALE','1','CUSTOMER','long'),(10,'vvt1304@gmail.com','Văn Thành','MALE','13042002@A','CUSTOMER','binthanh113'),(11,'ngockhanhpham8a@gmail.com','Phạm Ngọc Khánh','MALE','123456789','CUSTOMER','khanh123'),(12,'hotinh13102@gmail.com','Tịnh','MALE','tinh','CUSTOMER','tinh'),(13,'ducvinhmycanh@gmail.com','Nguyễn Đức Vĩnh','MALE','12052003','CUSTOMER','vinhnd'),(14,'minhpri1@gmail.com','Lê Bá Nhật Minh','MALE','minh1303','CUSTOMER','minhpri1'),(15,'nguyennhon822@gmail.com','Nhon','MALE','123456','CUSTOMER','nguyennhon'),(16,'gbls102@gmail.com','Ngô Văn Vinh','MALE','12345678','CUSTOMER','vinhngo'),(17,'letam.x2004@gmail.com','Lê Văn Tâm','MALE','27032004tam','CUSTOMER','tamlee'),(18,'ocsuek08@gmail.com','Sự Trương','FEMALE','123456','CUSTOMER','sutruong90'),(19,'khanhphan.201175@gmail.com','Phan Tá Anh Vương','MALE','anhvuong','CUSTOMER','anhvuong'),(20,'annguyendang.17.07.2002@gmail.com','Nguyễn Đăng Thiên An','MALE','an','CUSTOMER','an'),(21,'namkimnam2@gmail.com','Quốc Khánh','MALE','0703586224Kh@nh','CUSTOMER','qkanengk'),(22,'nguyenhong6693@gmail.com','chị Hồng','FEMALE','vph20052005','CUSTOMER','hongnguyen'),(23,'tranlegianguyen97dn@gmail.com','Trần Lê Gia Nguyên','MALE','1@Blackpanther','CUSTOMER','jackfly001'),(24,'uyen.tran2@codegym.vn','Uyên','FEMALE','Ien','CUSTOMER','uyen'),(25,'chiendndn@gmail.com','Trần Trung Chiến','MALE','112002','CUSTOMER','chien'),(26,'myvan.0601@gmail.com','Văn Thị Mỹ Vân','FEMALE','myvan1902','CUSTOMER','myvan1902'),(27,'dokhacthanhvinh@gmail.com','Đỗ Thành Vinh','MALE','123123','CUSTOMER','vinh'),(28,'nguyenns6802@gmail.com','Sinh','MALE','123456','CUSTOMER','ngocsinh'),(29,'van.van@codegym.vn','Văn Thị Mỹ Vân','FEMALE','myvan1902','CUSTOMER','myvan');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-11-12  8:51:17

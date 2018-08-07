-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: localhost    Database: final_task_db
-- ------------------------------------------------------
-- Server version	5.7.19-log

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
-- Table structure for table `card`
--

DROP TABLE IF EXISTS `card`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `card` (
  `card_number` varchar(16) NOT NULL,
  `money` decimal(10,0) NOT NULL DEFAULT '0',
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`card_number`),
  KEY `card_user_user_id_fk` (`user_id`),
  CONSTRAINT `card_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Денежные карты пользователей';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `card`
--

LOCK TABLES `card` WRITE;
/*!40000 ALTER TABLE `card` DISABLE KEYS */;
INSERT INTO `card` VALUES ('1010101010101010',911965,1),('1010101011111111',0,23),('1212121212121212',280,13),('1969196919691969',0,17),('2424242424242424',985550,15),('3131313131313131',19105,14),('3434343434343434',0,22),('5656565656565656',0,24),('6565656565656565',0,16),('6666666666666666',666,18),('8181818181818181',810000,20),('8787878787878787',0,21),('9898989898989898',935,19);
/*!40000 ALTER TABLE `card` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_info`
--

DROP TABLE IF EXISTS `order_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_info` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `order_status` enum('pending','confirmed','cancelled','finished') DEFAULT 'pending',
  `order_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `reminded` tinyint(1) DEFAULT '0',
  `order_price` decimal(10,0) NOT NULL DEFAULT '0',
  `paid` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'Оплачен ли заказ',
  PRIMARY KEY (`order_id`),
  KEY `order_info_user_user_id_fk` (`user_id`),
  CONSTRAINT `order_info_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8 COMMENT='Заказы пользователей';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_info`
--

LOCK TABLES `order_info` WRITE;
/*!40000 ALTER TABLE `order_info` DISABLE KEYS */;
INSERT INTO `order_info` VALUES (5,14,'cancelled','2018-07-28 13:12:01',0,0,0),(6,14,'finished','2018-07-29 16:27:42',0,0,0),(7,13,'cancelled','2018-07-29 16:26:47',1,0,0),(8,14,'cancelled','2018-08-01 18:00:00',0,0,0),(9,1,'cancelled','2018-07-30 13:00:00',1,0,0),(10,1,'finished','2018-07-30 12:00:00',1,0,0),(11,14,'cancelled','2018-08-02 08:00:00',0,0,0),(12,13,'cancelled','2018-08-01 12:00:00',1,0,0),(13,13,'cancelled','2018-08-01 03:00:00',0,0,0),(14,1,'cancelled','2018-07-31 12:00:00',1,0,0),(15,1,'cancelled','2018-08-03 03:00:00',0,0,0),(16,1,'cancelled','2018-08-03 12:00:00',0,0,0),(17,1,'cancelled','2018-08-02 18:00:00',0,0,0),(18,1,'cancelled','2018-08-03 06:00:00',1,60,0),(19,14,'cancelled','2018-08-04 08:00:00',0,75,0),(20,14,'cancelled','2018-08-04 14:00:00',0,70,0),(21,14,'cancelled','2018-08-04 12:00:00',0,290,0),(22,14,'cancelled','2018-08-03 14:00:57',0,110,1),(23,14,'cancelled','2018-08-03 14:07:40',0,135,1),(24,14,'cancelled','2018-08-03 14:15:41',0,230,0),(25,1,'cancelled','2018-08-03 14:25:07',1,75,0),(26,14,'cancelled','2018-08-04 12:00:00',0,115,0),(27,13,'cancelled','2018-08-03 14:28:45',0,45,0),(28,15,'cancelled','2018-08-04 20:30:00',0,0,0),(29,15,'cancelled','2018-08-10 12:00:00',0,0,0),(30,15,'cancelled','2018-08-04 09:59:38',0,135,0),(31,14,'cancelled','2018-08-04 10:06:20',0,285,0),(32,14,'cancelled','2018-08-10 06:00:00',0,370,0),(33,15,'cancelled','2018-08-04 11:09:28',1,315,0),(34,15,'confirmed','2018-08-04 11:09:02',1,350,1),(35,14,'cancelled','2018-08-05 04:00:00',0,150,0),(36,1,'cancelled','2018-08-06 08:00:00',0,100,0),(37,1,'confirmed','2018-08-06 12:00:00',1,85,1),(38,1,'confirmed','2018-08-06 03:00:00',1,55,1),(39,1,'confirmed','2018-08-06 06:00:00',1,55,1),(40,1,'pending','2018-08-06 06:00:00',0,150,0),(41,18,'cancelled','2018-08-07 09:00:00',0,250,0),(42,18,'pending','2018-08-08 03:00:00',0,60,0),(43,19,'confirmed','2018-08-07 03:01:00',1,45,1),(44,24,'pending','2018-08-08 12:00:00',0,165,0);
/*!40000 ALTER TABLE `order_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_link`
--

DROP TABLE IF EXISTS `order_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_link` (
  `order_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  KEY `order_link_order_info_order_id_fk` (`order_id`),
  KEY `order_link_service_service_id_fk` (`service_id`),
  CONSTRAINT `order_link_order_info_order_id_fk` FOREIGN KEY (`order_id`) REFERENCES `order_info` (`order_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_link_service_service_id_fk` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Связи между заказами  и услугами';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_link`
--

LOCK TABLES `order_link` WRITE;
/*!40000 ALTER TABLE `order_link` DISABLE KEYS */;
INSERT INTO `order_link` VALUES (5,1),(6,3),(7,1),(7,2),(7,4),(8,1),(8,2),(8,4),(9,2),(9,5),(10,1),(10,3),(10,4),(10,5),(10,6),(10,7),(11,1),(11,4),(11,5),(12,5),(12,6),(12,7),(13,1),(13,5),(13,6),(14,1),(14,4),(14,5),(14,6),(15,2),(15,5),(15,6),(15,7),(16,2),(16,4),(16,5),(16,6),(17,2),(18,2),(19,2),(19,3),(19,4),(20,3),(20,4),(20,5),(21,4),(21,5),(21,6),(21,7),(21,8),(22,2),(22,3),(22,5),(22,7),(23,2),(23,3),(23,4),(23,5),(23,6),(24,2),(24,5),(24,6),(24,8),(25,2),(25,3),(25,4),(26,2),(26,6),(26,7),(27,4),(27,5),(30,2),(30,3),(30,4),(30,5),(30,6),(31,3),(31,4),(31,5),(31,6),(31,9),(32,7),(32,8),(32,9),(33,3),(33,4),(33,5),(33,6),(33,7),(33,8),(34,4),(34,8),(34,9),(35,8),(36,3),(36,4),(36,6),(37,3),(37,5),(37,6),(38,3),(38,4),(39,3),(39,4),(40,3),(40,4),(40,6),(40,7),(41,3),(41,4),(41,6),(41,8),(42,5),(42,6),(43,4),(43,5),(44,3),(44,4),(44,5),(44,6),(44,7);
/*!40000 ALTER TABLE `order_link` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `review`
--

DROP TABLE IF EXISTS `review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `review` (
  `review_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `service_id` int(11) NOT NULL,
  `creation_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `mark` int(11) NOT NULL,
  `message` longtext,
  PRIMARY KEY (`review_id`),
  KEY `review_user_user_id_fk` (`user_id`),
  KEY `review_service_service_id_fk` (`service_id`),
  CONSTRAINT `review_service_service_id_fk` FOREIGN KEY (`service_id`) REFERENCES `service` (`service_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `review_user_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8 COMMENT='Отзывы пользователей на услуги';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `review`
--

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES (4,14,1,'2018-07-27 12:13:05',9,'Вери гуд'),(5,13,1,'2018-07-27 13:07:27',10,'Всё круто!!!'),(8,1,1,'2018-07-30 19:26:56',10,'10\\10 gospodi 10\\10'),(12,1,5,'2018-07-30 19:49:46',4,'blah blah blah'),(13,15,8,'2018-08-04 09:39:09',8,'Слишком дорого'),(16,1,2,'2018-08-04 10:03:40',8,'blah blah blah'),(17,14,9,'2018-08-04 10:08:44',7,'норм'),(18,15,6,'2018-08-04 10:40:08',10,'кул, очень круто'),(36,20,9,'2018-08-06 11:07:39',10,'ахаха оно работает'),(37,20,2,'2018-08-06 11:08:51',9,''),(38,18,2,'2018-08-06 11:34:08',10,'lol adresnaya stroka rulit'),(39,18,4,'2018-08-06 11:36:48',7,'нормально'),(40,19,5,'2018-08-06 14:33:17',2,'sdfsdf'),(41,19,4,'2018-08-06 14:41:42',10,'прикольно');
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `service` (
  `service_id` int(11) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(40) NOT NULL,
  `service_description` longtext,
  `service_price` decimal(10,0) NOT NULL,
  `service_status` enum('available','unavailable') DEFAULT 'available',
  PRIMARY KEY (`service_id`),
  UNIQUE KEY `service_service_name_uindex` (`service_name`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='Оказываемые услуги';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` VALUES (1,'Стрижка когтей','блаблабла\r\n',40,'unavailable'),(2,'Чистка глаз','блабла\r\n',45,'unavailable'),(3,'Уход за лапками','Блаблабла',25,'available'),(4,'Чистка шерсти','лвоавпва',30,'available'),(5,'Чистка ушей','блаблабла',15,'available'),(6,'Уход за зубами','блаблабла',45,'available'),(7,'Стрижка шерсти','блаблабла',50,'available'),(8,'Креативная стрижка','блаблабла',150,'available'),(9,'Окрашивание шерсти','блаблабла',170,'available');
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID пользователя',
  `login` varchar(20) NOT NULL COMMENT 'Логин пользователя',
  `password` char(40) NOT NULL COMMENT 'Пароль пользователя',
  `user_name` varchar(40) DEFAULT NULL COMMENT 'Имя пользователя',
  `user_email` varchar(50) NOT NULL COMMENT 'E-mail пользователя',
  `phone_number` varchar(13) NOT NULL COMMENT 'Номер телефона пользователя',
  `user_status` enum('admin','user','banned') NOT NULL DEFAULT 'user' COMMENT 'Статус пользователя: администратор, обычный пользователь, заблокированный пользователь',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `login_UNIQUE` (`login`),
  UNIQUE KEY `phone_number_UNIQUE` (`phone_number`),
  UNIQUE KEY `user_email_UNIQUE` (`user_email`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8 COMMENT='Зарегистрированные пользователи';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'howtowashacat','3df01f9c843aa5d99c321a6e416d001f494b431e','Karina','zhigimont.k@gmail.com','+375293079903','admin'),(13,'vasya','e98f82c834f972a1ff09d3a2171590e1b42ada5a','Василий','klklkl@mmm.com','+375445555555','banned'),(14,'aliceglass','8dbc42f8c1dc5db72d256bcd2026ee98a40c66fd','','fakeemail@gmail.com','+375291237784','user'),(15,'arcticmonkey','36792abdde55da258e32792789ba766a27aa5622','','yuyuyu@g.com','+375294457701','user'),(16,'kuku','32be1a7829bf9d8d8d974a33322d15b896e73b57','','lolkek@kek.com','+375331444444','banned'),(17,'majortom','a78d350729b677efd3cbd1d6f9e8bd0e797b22ff','salad bowl to major tom','lol@kek.ru','+375291011100','user'),(18,'holyspirithazyboy','516c63bd75102498406fa92242e530ce28c2d57c','','holyspirithazyboy@summer.haze','+375296666666','user'),(19,'debiruman','0623bee3ed69621672e321424fcd011dee846763','DEVILMAN crybaby','crybaby@zz.zz','+375291084765','user'),(20,'FistsOfFury','552b15f501742395ec8541aedbf483f6cd20b2ab','Tony','rollermobster@hm.mi','+375296547878','user'),(21,'seacat','7ba60d08a941d6b70d51f13096f7a2d56caef848','','yaygoldenland@rknjm.jp','+375290410987','user'),(22,'zombienight','0fa59a988f3fb2e9e83b59e3311e06ca8dcbedbb','','zombienight@dwtd.com','+375293333333','user'),(23,'nightwalks','2b0c26dc1b83795c5b08073416fbf22ec0eae6a8','','nightwalks@sw.ru','+375291001010','user'),(24,'varchar','df0e5006b34734de067cf6faa2a4039d263830c4','волчара','varchar@sql.my','+375298738477','user');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-08-07 17:01:45

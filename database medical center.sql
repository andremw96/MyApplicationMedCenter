-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.1.19-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win32
-- HeidiSQL Version:             9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- Dumping database structure for medcenter
CREATE DATABASE IF NOT EXISTS `medcenter` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `medcenter`;

-- Dumping structure for table medcenter.gejala
CREATE TABLE IF NOT EXISTS `gejala` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idPenyakit` int(11) NOT NULL DEFAULT '0',
  `gejala` varchar(250) NOT NULL DEFAULT ' ',
  `user` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `idPenyakit` (`idPenyakit`),
  CONSTRAINT `gejala_ibfk_1` FOREIGN KEY (`idPenyakit`) REFERENCES `transactions` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

-- Dumping data for table medcenter.gejala: ~6 rows (approximately)
/*!40000 ALTER TABLE `gejala` DISABLE KEYS */;
INSERT INTO `gejala` (`id`, `idPenyakit`, `gejala`, `user`) VALUES
	(1, 3, 'pusing', 1),
	(2, 4, 'gak bisa gerak', 1),
	(3, 3, 'puyeng', 1),
	(4, 5, 'batuk berdarah', 1),
	(5, 5, 'batuk berdahak', 1),
	(7, 3, 'trombosit rendah', 1);
/*!40000 ALTER TABLE `gejala` ENABLE KEYS */;

-- Dumping structure for table medcenter.transactions
CREATE TABLE IF NOT EXISTS `transactions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `gejala` varchar(250) DEFAULT NULL,
  `obat` varchar(250) DEFAULT NULL,
  `type` smallint(1) NOT NULL,
  `user` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- Dumping data for table medcenter.transactions: ~3 rows (approximately)
/*!40000 ALTER TABLE `transactions` DISABLE KEYS */;
INSERT INTO `transactions` (`id`, `name`, `description`, `gejala`, `obat`, `type`, `user`) VALUES
	(3, 'Demam Berdarah', 'bukan Demam biasa', 'badan merah, trombosit rendah', 'petromax', 3, 1),
	(4, 'Stroke', 'tidak bisa bergerak', 'badan kaku', 'sirup', 4, 1),
	(5, 'TBC', 'Batuk berdarah darah edit', 'Batuk Berdarah edit', 'OHB Combo edit', 3, 1);
/*!40000 ALTER TABLE `transactions` ENABLE KEYS */;

-- Dumping structure for table medcenter.users
CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(250) NOT NULL,
  `password` varchar(35) NOT NULL,
  `status` smallint(1) NOT NULL DEFAULT '1',
  `token` varchar(35) DEFAULT NULL,
  `role` varchar(35) DEFAULT NULL,
  `expiry_date` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- Dumping data for table medcenter.users: ~3 rows (approximately)
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `email`, `password`, `status`, `token`, `role`, `expiry_date`) VALUES
	(1, 'andre', '827ccb0eea8a706c4c34a16891f84e7b', 1, 'ae2c6126c8910c5304ca7e743b720bfa', 'Dokter', '2017-07-23 14:24:20'),
	(2, 'andre2', '827ccb0eea8a706c4c34a16891f84e7b', 1, '22d87c4ebd8d275751c567db0d6d6208', 'User', '2017-07-23 11:51:20'),
	(3, 'dokter', '827ccb0eea8a706c4c34a16891f84e7b', 1, '646182033d3fd0a256130a0316938b8e', 'Dokter', '2017-07-22 15:40:28');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;

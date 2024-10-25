SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

CREATE TABLE IF NOT EXISTS `configurationvalues` (
    `deathHeight` int(11) NOT NULL DEFAULT -64
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `locations` (
    `locationName` varchar(200) NOT NULL,
    `worldName` varchar(200) NOT NULL,
    `X` double NOT NULL,
    `Y` double NOT NULL,
    `Z` double NOT NULL,
    `yaw` float NOT NULL,
    `pitch` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `stats` (
   `playerUUID` varchar(36) NOT NULL,
   `kills` int(11) NOT NULL DEFAULT 0,
   `deaths` int(11) NOT NULL DEFAULT 0,
   `coins` int(11) NOT NULL DEFAULT 0,
   `tokens` int(11) NOT NULL DEFAULT 100
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

ALTER TABLE `stats`
    ADD PRIMARY KEY (`playerUUID`);
COMMIT;

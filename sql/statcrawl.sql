-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Apr 22, 2011 at 12:12 PM
-- Server version: 5.5.8
-- PHP Version: 5.3.5

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `statcrawl`
--

-- --------------------------------------------------------

--
-- Table structure for table `cities`
--

CREATE TABLE IF NOT EXISTS `cities` (
  `countrycode` varchar(100) NOT NULL,
  `short` varchar(10) NOT NULL,
  `name` varchar(200) NOT NULL,
  `count` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `countries`
--

CREATE TABLE IF NOT EXISTS `countries` (
  `short` varchar(10) NOT NULL,
  `name` varchar(200) NOT NULL,
  `count` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `crawled`
--

CREATE TABLE IF NOT EXISTS `crawled` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `md5` varchar(32) NOT NULL,
  `url` varchar(2000) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `md5` (`md5`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=97 ;

-- --------------------------------------------------------

--
-- Table structure for table `crawling`
--

CREATE TABLE IF NOT EXISTS `crawling` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `md5` varchar(64) NOT NULL,
  `url` varchar(3000) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `md5` (`md5`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=61 ;

-- --------------------------------------------------------

--
-- Table structure for table `errors`
--

CREATE TABLE IF NOT EXISTS `errors` (
  `code` varchar(30) NOT NULL,
  `count` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `hosts`
--

CREATE TABLE IF NOT EXISTS `hosts` (
  `host` varchar(2000) NOT NULL,
  `count` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `images`
--

CREATE TABLE IF NOT EXISTS `images` (
  `count` int(11) NOT NULL,
  `size` int(11) NOT NULL,
  `extension` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `ip`
--

CREATE TABLE IF NOT EXISTS `ip` (
  `ip` varchar(50) NOT NULL,
  `count` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `pending`
--

CREATE TABLE IF NOT EXISTS `pending` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `md5` varchar(32) NOT NULL,
  `url` varchar(2000) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `md5` (`md5`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=12184 ;

-- --------------------------------------------------------

--
-- Table structure for table `regions`
--

CREATE TABLE IF NOT EXISTS `regions` (
  `short` varchar(10) NOT NULL,
  `code` varchar(10) NOT NULL,
  `name` varchar(200) NOT NULL,
  `count` int(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

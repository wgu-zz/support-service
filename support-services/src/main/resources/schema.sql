CREATE TABLE IF NOT EXISTS users(EMAIL VARCHAR(255), IP VARCHAR(255), TIMESTMP TIMESTAMP);

CREATE TABLE IF NOT EXISTS `settings` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `incremental_ticket_start_time` bigint(11) DEFAULT NULL,
  `timeout_no_priority` bigint(11) NOT NULL DEFAULT '0',
  `timeout_email_s1` bigint(11) NOT NULL DEFAULT '0',
  `timeout_pagerduty_s1` bigint(11) NOT NULL DEFAULT '0',
  `timeout_email_s2` bigint(11) NOT NULL DEFAULT '0',
  `timeout_pagerduty_s2` bigint(11) NOT NULL DEFAULT '0',
  `timeout_email_s3` bigint(11) NOT NULL DEFAULT '0',
  `timeout_email_s4` bigint(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `ticket` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `ticket_id` int(11) NOT NULL,
  `title` varchar(1024) NOT NULL DEFAULT '',
  `priority` varchar(128) DEFAULT NULL,
  `status` varchar(128) NOT NULL DEFAULT '',
  `assignee_id` int(11) DEFAULT NULL,
  `url` varchar(2048) NOT NULL,
  `create_datetime` datetime NOT NULL,
  `last_update_datetime` datetime NOT NULL,
  `is_emailed` tinyint(1) NOT NULL DEFAULT '0',
  `is_paged` tinyint(1) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB;
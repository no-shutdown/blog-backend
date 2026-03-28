INSERT INTO `user` (`username`, `password`, `role`)
SELECT 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyZQFsuDK', 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE `username` = 'admin');

INSERT INTO `page` (`slug`, `title`, `content`, `content_type`)
SELECT 'about', 'About', '# About\n\nEdit this page in admin panel.', 'markdown'
WHERE NOT EXISTS (SELECT 1 FROM `page` WHERE `slug` = 'about');

INSERT INTO `user` (`username`, `password`, `role`)
SELECT 'admin', '$2b$10$u4gcDAFLgD6AfwjzScdXS.1uJ9XA3RXKqzHaqcTwETNkAJAc5ARKy', 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE `username` = 'admin');

INSERT INTO `page` (`slug`, `title`, `content`, `content_type`)
SELECT 'about', 'About', '# About\n\nEdit this page in admin panel.', 'markdown'
WHERE NOT EXISTS (SELECT 1 FROM `page` WHERE `slug` = 'about');

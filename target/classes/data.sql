INSERT INTO `user` (`username`, `password`, `role`)
SELECT 'admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBpwTTyZQFsuDK', 'ROLE_ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM `user` WHERE `username` = 'admin');

INSERT INTO `page` (`slug`, `title`, `content`, `content_type`)
SELECT 'about', '关于我', '# 关于我\n\n这里是关于我的内容，请在后台编辑。', 'markdown'
WHERE NOT EXISTS (SELECT 1 FROM `page` WHERE `slug` = 'about');

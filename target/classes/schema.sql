CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role` VARCHAR(20) NOT NULL DEFAULT 'ROLE_ADMIN',
    `avatar` VARCHAR(255),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL UNIQUE,
    `description` VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS `tag` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(50) NOT NULL UNIQUE,
    `color` VARCHAR(20) DEFAULT '#409EFF'
);

CREATE TABLE IF NOT EXISTS `article` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `title` VARCHAR(200) NOT NULL,
    `content` LONGTEXT,
    `content_type` ENUM('markdown', 'html') NOT NULL DEFAULT 'markdown',
    `summary` VARCHAR(500),
    `cover_image` VARCHAR(255),
    `status` ENUM('draft', 'published') NOT NULL DEFAULT 'draft',
    `view_count` INT NOT NULL DEFAULT 0,
    `category_id` BIGINT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS `article_tag` (
    `article_id` BIGINT NOT NULL,
    `tag_id` BIGINT NOT NULL,
    PRIMARY KEY (`article_id`, `tag_id`),
    FOREIGN KEY (`article_id`) REFERENCES `article`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`tag_id`) REFERENCES `tag`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `comment` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `article_id` BIGINT NOT NULL,
    `nickname` VARCHAR(50) NOT NULL,
    `email` VARCHAR(100),
    `content` TEXT NOT NULL,
    `parent_id` BIGINT,
    `status` ENUM('pending', 'approved', 'rejected') NOT NULL DEFAULT 'pending',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (`article_id`) REFERENCES `article`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`parent_id`) REFERENCES `comment`(`id`) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `link` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `url` VARCHAR(255) NOT NULL,
    `avatar` VARCHAR(255),
    `description` VARCHAR(200),
    `status` ENUM('visible', 'hidden') NOT NULL DEFAULT 'visible',
    `sort_order` INT NOT NULL DEFAULT 0
);

CREATE TABLE IF NOT EXISTS `page` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `slug` VARCHAR(50) NOT NULL UNIQUE,
    `title` VARCHAR(100) NOT NULL,
    `content` LONGTEXT,
    `content_type` ENUM('markdown', 'html') NOT NULL DEFAULT 'markdown',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

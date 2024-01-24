ALTER TABLE [User]
ADD PasswordHash varchar(100) NOT NULL DEFAULT('{bcrypt}$2a$10$vI1fqpR1bk9ugZAwcnygTOTSiG3Rte96cfCGl7kUotUIwWoCuTL/C');

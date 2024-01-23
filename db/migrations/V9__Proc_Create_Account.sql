-- creates an account given username, displayname, and the encrypted password
CREATE PROCEDURE CreateAccount
(
	@Username varchar(20),
	@DisplayName varchar(50),
	@PasswordHash varchar(50),
	@ID int OUTPUT
)
AS
BEGIN

-- Validate parameters
IF @Username IS NULL OR @Username = ''
BEGIN
	RAISERROR('Paramter @Username is missing or empty', 14, 1);
	RETURN 1;
END

IF @PasswordHash IS NULL OR @PasswordHash = ''
BEGIN
	RAISERROR('Paramter @PasswordHash is missing or empty', 14, 1);
	RETURN 1;
END

-- Check if this user already exists
IF (SELECT count(*) FROM [User] WHERE Username = @Username) > 1
BEGIN
	RAISERROR('Create account failed, please try again', 14, 1);
	RETURN 2;
END

-- No matching users found, create a new one
INSERT INTO [User] (Username, DisplayName, PasswordHash)
VALUES (@Username, @DisplayName, @PasswordHash)

SET @ID = @@IDENTITY;

END
GO
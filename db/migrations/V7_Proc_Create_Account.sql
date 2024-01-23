-- creates an account given username, displayname, and the encrypted password
CREATE PROCEDURE CreateAccount
(
	@Username varchar(20),
	@DisplayName varchar(50),
	@PasswordSalt varchar(50), --what size is this supposed to be?
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

IF @PasswordSalt IS NULL OR @PasswordSalt = ''
BEGIN
	RAISERROR('Paramter @PasswordSalt is missing or empty', 14, 1);
	RETURN 1;
END

-- Check if this user already exists
IF (SELECT count(*) FROM [User] WHERE Username = @Username) > 1
BEGIN
	RAISERROR('Create account failed, please try again', 14, 1);
	RETURN 2;
END

-- No matching users found, create a new one
INSERT INTO [User] (Username, DisplayName, PasswordSalt)
VALUES (@Username, @DisplayName, @PasswordSalt)

DECLARE @ID as int
SET @ID = @@IDENTITY;

END
GO
CREATE PROCEDURE ImportUser
(
	@Name varchar(50),
	@ID int OUTPUT
)
AS
BEGIN
-- Validate parameters
IF @Name IS NULL OR @Name = ''
BEGIN
	RAISERROR('Paramter @Name is missing or empty', 14, 1);
	RETURN 1;
END

-- Check if this user has already been imported
IF (SELECT count(*) FROM [User] WHERE DisplayName = @Name) > 1
BEGIN
	RAISERROR('Multiple existing users found with matching display name', 14, 1);
	RETURN 2;
END

DECLARE @userID AS int;
SELECT @userID = ID FROM [User] WHERE DisplayName = @Name;

IF @userID IS NULL
BEGIN
	-- No matching users found, create a new one
	INSERT INTO [User] (Username, DisplayName)
	VALUES (
		LOWER(REPLACE(@Name, ' ', '')), -- Automatically generated username 'First Last' -> 'firstlast'
		@Name
	)
	SET @ID = scope_identity();
END
ELSE
BEGIN
	SET @ID = @userID
END

END
GO

CREATE PROCEDURE CreateUserImage
(
	@UploaderID int,
	@URL varchar(512)
)
AS
BEGIN

-- Validate parameters
IF @UploaderID IS NULL
BEGIN
	RAISERROR('Parameter @UploaderID is null', 14, 1);
	RETURN 1;
END

IF @URL IS NULL OR @URL = ''
BEGIN
	RAISERROR('Parameter @URL is missing or empty', 14, 1);
	RETURN 1;
END

-- Check if this image already exists
IF EXISTS (SELECT * FROM UserImage WHERE URL = @URL)
BEGIN
	RAISERROR('This image URL has already been registered', 14, 1);
	RETURN 2;
END

-- Check if the user exists
IF NOT EXISTS (SELECT * FROM [User] WHERE ID = @UploaderID)
BEGIN
	RAISERROR('Invalid @UploaderID', 14, 1);
	RETURN 3;
END


INSERT INTO UserImage(UploaderID, URL)
VALUES(@UploaderID, @URL);

END
GO

GRANT EXECUTE ON CreateUserImage TO AnimalTrackerApp

-- Changes the user's password name
CREATE PROCEDURE ChangePassword
(
	@UserID int,
	@NewPasswordHash varchar(100)
)
AS 
BEGIN
--validate parameters
    IF (@NewPasswordHash IS NULL OR @NewPasswordHash='')
    BEGIN
        RAISERROR('Parameter @NewPasswordHash is missing or empty', 14, 1);
        RETURN 1;
    END

    IF (@UserID IS NULL OR @UserID= 0)
    BEGIN
        RAISERROR('Parameter @UserID is missing or empty', 14, 1);
        RETURN 1;
    END

    IF (NOT EXISTS (SELECT * FROM [User] WHERE ID = @UserID))
    BEGIN
        RAISERROR('No user with that ID exists', 14, 2);
        RETURN 2;
    END

	UPDATE [User]
	SET PasswordHash = @NewPasswordHash
	WHERE ID = @UserID
	RETURN 0;
END
GO

GRANT EXECUTE ON ChangePassword TO AnimalTrackerApp
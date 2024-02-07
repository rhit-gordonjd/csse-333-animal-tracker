-- Changes the user's username name
CREATE PROCEDURE UpdateUsername
(
	@UserID int,
	@NewUsername varchar(20)
)
AS 
BEGIN
--validate parameters
    IF (@NewUsername IS NULL OR @NewUsername= '')
    BEGIN
        RAISERROR('Parameter @NewUsername is missing or empty', 14, 1);
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
	SET Username = @NewUsername
	WHERE ID = @UserID
	RETURN 0;
END
GO

GRANT EXECUTE ON UpdateUsername TO AnimalTrackerApp
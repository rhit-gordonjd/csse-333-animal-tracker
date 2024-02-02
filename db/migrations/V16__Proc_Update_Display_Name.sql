-- Changes the user's display name
CREATE PROCEDURE UpdateDisplayName
(
	@UserID int,
	@NewDisplayName varchar(50)
)
AS 
BEGIN
--validate parameters
    IF (@NewDisplayName IS NULL OR @NewDisplayName= '')
    BEGIN
        RAISERROR('Parameter @NewDisplayName is missing or empty', 14, 1);
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
	SET DisplayName = @NewDisplayName
	WHERE ID = @UserID
	RETURN 0;
END
GO

GRANT EXECUTE ON UpdateDisplayName TO AnimalTrackerApp
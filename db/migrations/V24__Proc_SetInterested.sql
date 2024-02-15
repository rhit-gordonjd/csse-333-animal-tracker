CREATE PROCEDURE SetInterested
(
	@UserID int,
	@ProjectID int,
	@IsInterested bit
)
AS
BEGIN
	IF (NOT EXISTS (SELECT * FROM [User] WHERE ID = @UserID))
	BEGIN
		RAISERROR('No user exists with id @UserID', 14, 2);
		RETURN 1;
	END

	IF (NOT EXISTS (SELECT * FROM Project WHERE ID = @ProjectID))
	BEGIN
		RAISERROR('No project exists with id @ProjectID', 14, 2);
		RETURN 2;
	END

	IF (@IsInterested = 1)
	BEGIN
		IF (NOT EXISTS (SELECT * FROM Interested WHERE UserID = @UserID AND ProjectID = @ProjectID))
		BEGIN
			INSERT INTO Interested(UserID, ProjectID)
			VALUES (@UserID, @ProjectID);
		END
	END
	ELSE
	BEGIN
		DELETE FROM Interested
		WHERE UserID = @UserID AND ProjectID = @ProjectID
	END
END
GO

GRANT EXECUTE ON SetInterested TO AnimalTrackerApp
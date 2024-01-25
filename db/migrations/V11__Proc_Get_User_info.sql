-- gets the username and display name for a user by their id
CREATE PROCEDURE GetUserInfo
(
	@ID int
)
AS
BEGIN
SELECT Username, DisplayName FROM [User] WHERE ID = @ID
END
GO

GRANT EXECUTE ON GetUserInfo TO AnimalTrackerApp

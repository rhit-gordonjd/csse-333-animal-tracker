-- get user info now gets passwordhash too
ALTER PROCEDURE GetUserInfo
(
	@ID int
)
AS
BEGIN
SELECT Username, DisplayName, PasswordHash FROM [User] WHERE ID = @ID
END
GO

GRANT EXECUTE ON GetUserInfo TO AnimalTrackerApp
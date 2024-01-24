-- gets the id, display name, and password hash for the user with the given username
CREATE PROCEDURE GetLoginInfo
(
	@Username varchar(20)
)
AS
BEGIN
SELECT ID, DisplayName, PasswordHash FROM [User] WHERE @Username = Username
END
GO

GRANT EXECUTE ON GetLoginInfo TO AnimalTrackerApp

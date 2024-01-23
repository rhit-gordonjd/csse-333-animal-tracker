-- gets the PasswordSalt for the given Username. The application should then use the PasswordSalt to compare to the password the user typed in
CREATE PROCEDURE GetLoginInfo
(
	@Username varchar(20),
	@PasswordHash varchar(20) OUTPUT
)
AS
BEGIN
SELECT PasswordHash FROM [User] WHERE @Username = Username
END
GO
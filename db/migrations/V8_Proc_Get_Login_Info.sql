-- gets the PasswordSalt for the given Username. The application should then use the PasswordSalt to compare to the password the user typed in
CREATE PROCEDURE GetLoginInfo
(
	@Username varchar(20),
	@PasswordSalt varchar(20) OUTPUT
)
AS
BEGIN
SELECT PasswordSalt FROM [User] WHERE @Username = Username
END
GO
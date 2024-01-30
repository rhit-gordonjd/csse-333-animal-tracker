CREATE PROCEDURE RetrieveUserInterestedProjects (
	@UserID int
)
AS
BEGIN
	SELECT ID, [Name], [Description], CreationTimestamp, ClosedDate
	FROM Project as P 
	INNER JOIN Interested as I on I.UserID = @UserID
	WHERE P.ID = I.ProjectID
END
GO
GRANT EXECUTE ON RetrieveUserInterestedProjects TO AnimalTrackerApp
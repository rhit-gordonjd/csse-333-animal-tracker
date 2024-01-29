CREATE PROCEDURE GetProject (
	@ProjectID int
)
AS
BEGIN
	SELECT ID, [Name], [Description], CreationTimestamp, ClosedDate
	FROM Project as P 
	WHERE P.ID = @ProjectID
END
GO

GRANT EXECUTE ON GetProject TO AnimalTrackerApp

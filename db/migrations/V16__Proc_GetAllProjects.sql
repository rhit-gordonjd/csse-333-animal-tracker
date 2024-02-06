CREATE PROCEDURE GetAllProjects
AS
BEGIN
	SELECT ID, [Name], [Description], CreationTimestamp, ClosedDate
	FROM Project as P 
END
GO

GRANT EXECUTE ON GetAllProjects TO AnimalTrackerApp

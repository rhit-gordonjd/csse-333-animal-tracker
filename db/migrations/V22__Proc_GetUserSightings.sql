CREATE PROCEDURE GetUserSightings
(
	@UserID int
)
AS
BEGIN
	-- Validate parameters
	IF @UserID IS NULL
	BEGIN
		RAISERROR('Parameter @UserID is null', 14, 1);
		RETURN 1;
	END
	
	SELECT P.ID as 'ProjectID', P.Name AS 'ProjectName', S.ID as 'ID', S.Timestamp as 'Timestamp', S.Location.Lat as 'LocationLatitude', S.Location.Long as 'LocationLongitude', O.CommonName as 'CommonName', O.ScientificName as 'ScientificName', U.DisplayName as 'DisplayName'
	FROM Project as P
	JOIN Organism as O on P.ID = O.ProjectID
	JOIN Sighting as S on O.ID = S.OrganismID
	JOIN [User] as U on U.ID = S.UserID
	WHERE U.ID = @UserID
	ORDER BY S.Timestamp DESC
END
GO

GRANT EXECUTE ON GetUserSightings TO AnimalTrackerApp

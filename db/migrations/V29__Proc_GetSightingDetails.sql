CREATE PROCEDURE GetSightingDetails
(
	@SightingID int
)
AS
BEGIN
	SELECT
		p.ID as 'ProjectID', p.Name AS 'ProjectName',
		s.ID, s.Timestamp, s.Location.Lat AS 'LocationLatitude', s.Location.Long AS 'LocationLongitude',
		o.CommonName, o.ScientificName,
		u.ID as 'UserID', u.DisplayName
	FROM Sighting as s
	LEFT JOIN Organism as o on o.ID = s.OrganismID
	LEFT JOIN Project as p on p.ID = o.ProjectID
	LEFT JOIN [User] as u on u.ID = s.UserID
	WHERE s.ID = @SightingID;

	SELECT SightingID, ImageURL
	FROM SightingImage
	WHERE SightingID = @SightingID;
END
GO

GRANT EXECUTE ON GetSightingDetails TO AnimalTrackerApp

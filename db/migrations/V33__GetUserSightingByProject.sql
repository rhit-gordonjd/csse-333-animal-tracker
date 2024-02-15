CREATE PROCEDURE [dbo].[GetUserSightingsByProject]
(
	@UserID int,
	@SortOrder char(1) --sort order will either be A for ascending or D for descending
)
AS
BEGIN
	-- Validate parameters
	IF @UserID IS NULL
	BEGIN
		RAISERROR('Parameter @UserID is null', 14, 1);
		RETURN 1;
	END

	IF @SortOrder <> 'A' AND @SortOrder <> 'D'
	BEGIN
		RAISERROR('Paramenter @SortOrder is incorrect', 14, 1);
		RETURN 2;
	END
	
	-- ascending
	IF @SortOrder = 'A'
	BEGIN
		SELECT P.ID as 'ProjectID', P.Name AS 'ProjectName', S.ID as 'ID', S.Timestamp as 'Timestamp', S.Location.Lat as 'LocationLatitude', S.Location.Long as 'LocationLongitude', O.CommonName as 'CommonName', O.ScientificName as 'ScientificName', U.DisplayName as 'DisplayName'
		FROM Project as P
		JOIN Organism as O on P.ID = O.ProjectID
		JOIN Sighting as S on O.ID = S.OrganismID
		JOIN [User] as U on U.ID = S.UserID
		WHERE U.ID = @UserID
		ORDER BY O.ProjectID
	END

	-- descending
	ELSE
	BEGIN
		SELECT P.ID as 'ProjectID', P.Name AS 'ProjectName', S.ID as 'ID', S.Timestamp as 'Timestamp', S.Location.Lat as 'LocationLatitude', S.Location.Long as 'LocationLongitude', O.CommonName as 'CommonName', O.ScientificName as 'ScientificName', U.DisplayName as 'DisplayName'
		FROM Project as P
		JOIN Organism as O on P.ID = O.ProjectID
		JOIN Sighting as S on O.ID = S.OrganismID
		JOIN [User] as U on U.ID = S.UserID
		WHERE U.ID = @UserID
		ORDER BY O.ProjectID DESC
	END

END
GO

GRANT EXECUTE ON GetUserSightingsByProject TO AnimalTrackerApp
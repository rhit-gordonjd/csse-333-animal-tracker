CREATE PROCEDURE DeleteSighting
(
	@SightingID int
)
AS
BEGIN
	IF (NOT EXISTS (SELECT * FROM Sighting WHERE ID = @SightingID))
	BEGIN
		RAISERROR('This sighting id does not exists', 14, 2);
		RETURN 1;
	END
	ELSE
	BEGIN
		DELETE FROM SightingImage
		WHERE SightingID = @SightingID

		DELETE FROM SightingVerification
		WHERE SightingID = @SightingID

		DELETE FROM Sighting
		WHERE ID = @SightingID
	END
END
GO

GRANT EXECUTE ON DeleteSighting TO AnimalTrackerApp

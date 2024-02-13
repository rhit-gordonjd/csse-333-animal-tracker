CREATE PROCEDURE DeleteSighting
(
	@SightingID int
)
AS
BEGIN
	IF (NOT EXISTS (SELECT * FROM SightingID WHERE ID = @SightingID))
	BEGIN
		RAISERROR('This sighting id does not exsist', 14, 2);
		RETURN 1;
	END
	ELSE
	BEGIN
		DELETE FROM SightingImage
		WHERE SightingID = @SightingID

		DELETE FROM SightingVerification
		WHERE SightingID = @SightingID

		DELETE FROM SightingID
		WHERE ID = @SightingID
	END
END
GO

GRANT EXECUTE ON DeleteSighting TO AnimalTrackerApp
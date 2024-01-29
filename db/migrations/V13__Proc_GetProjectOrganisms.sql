CREATE PROCEDURE GetProjectOrganisms (
	@ProjectID int
)
AS
BEGIN
	SELECT ID, CommonName, ScientificName, Description, IdentificationInstructions
	FROM Organism
	WHERE ProjectID = @ProjectID
	ORDER BY CommonName, ScientificName, ID
END
GO

GRANT EXECUTE ON GetProjectOrganisms TO AnimalTrackerApp

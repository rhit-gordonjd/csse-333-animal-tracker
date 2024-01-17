CREATE PROCEDURE ImportOrganism
(
	@ProjectID int,
	@CommonName varchar(150),
	@ScientificName varchar(50),
	@OrganismID int OUTPUT
)
AS
BEGIN
	IF @ProjectID IS NULL
	BEGIN
		RAISERROR('Parameter @ProjectID is required', 14, 1);
		RETURN 1;
	END

	IF @CommonName IS NULL
	BEGIN
		RAISERROR('Parameter @CommonName is required', 14, 1);
		RETURN 2;
	END

	IF @ScientificName IS NULL
	BEGIN
		RAISERROR('Parameter @ScientificName is required', 14, 1);
		RETURN 3;
	END

	IF NOT EXISTS(SELECT * FROM Project WHERE ID = @ProjectID)
	BEGIN
		RAISERROR('No project exists with ID @ProjectID', 14, 1);
		RETURN 3;
	END


	-- First, try to find an organism with the same scientific name
	SELECT @OrganismID = ID
	FROM Organism
	WHERE ProjectID = @ProjectID
		AND ScientificName = @ScientificName;

	-- Next, try to find an organism with the same common name
	IF @OrganismID IS NULL
	BEGIN
		SELECT @OrganismID = ID
		FROM Organism
		WHERE ProjectID = @ProjectID
			AND CommonName = @CommonName;
	END

	-- Finally, create a new organism
	IF @OrganismID IS NULL
	BEGIN
		INSERT INTO Organism (ProjectID, CommonName, ScientificName)
		VALUES(@ProjectID, @CommonName, @ScientificName);

		SET @OrganismID = scope_identity();
	END
	ELSE
	BEGIN
		-- If we are using an existing organism entry, fill in any missing data
		UPDATE Organism
		SET CommonName = COALESCE(CommonName, @CommonName),
			ScientificName = COALESCE(ScientificName, @ScientificName)
		WHERE ID = @OrganismID;
	END
END

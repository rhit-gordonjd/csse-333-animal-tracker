CREATE PROCEDURE ImportSighting
(
	@Timestamp datetime2,
	@ProjectName varchar(50),
	@Person varchar(50),
	@CommonName varchar(150),
	@ScientificName varchar(50),
	@Latitude float,
	@Longitude float,
	@ImageURL varchar(512)
)
AS
BEGIN
	IF @Timestamp IS NULL
	BEGIN
		RAISERROR('Parameter @Timestamp is required', 14, 1);
		RETURN 1;
	END

	IF @ProjectName IS NULL
	BEGIN
		RAISERROR('Parameter @ProjectName is required', 14, 1);
		RETURN 2;
	END

	IF @Person IS NULL
	BEGIN
		RAISERROR('Parameter @Person is required', 14, 1);
		RETURN 3;
	END

	IF @CommonName IS NULL
	BEGIN
		RAISERROR('Parameter @CommonName is required', 14, 1);
		RETURN 4;
	END

	IF @ScientificName IS NULL
	BEGIN
		RAISERROR('Parameter @ScientificName is required', 14, 1);
		RETURN 5;
	END

	IF @Latitude IS NULL
	BEGIN
		RAISERROR('Parameter @Latitude is required', 14, 1);
		RETURN 6;
	END

	IF @Longitude IS NULL
	BEGIN
		RAISERROR('Parameter @Longitude is required', 14, 1);
		RETURN 7;
	END


	-- Find project with Name @ProjectName
	DECLARE @projectID AS INT;
	SELECT @ProjectID = ID
	FROM Project
	WHERE Name = @ProjectName;

	IF @projectID IS NULL
	BEGIN
		RAISERROR('No project exists with name @ProjectName', 14, 1);
		RETURN 8;
	END

	
	DECLARE @status AS int;
	DECLARE @organismID AS int;

	EXEC @status = ImportOrganism @ProjectID = @projectID, @CommonName = @CommonName, @ScientificName = @ScientificName, @OrganismID = @organismID OUTPUT;
	IF @status <> 0
	BEGIN
		RAISERROR('Failed to import ImportOrganism', 14, 1);
		RETURN 9;
	END

	
	-- Import User
	DECLARE @contributorID AS int;

	EXEC @status = ImportUser @Name = @Person, @IsContributor = 1, @ID = @contributorID OUTPUT;
	IF @status <> 0
	BEGIN
		RAISERROR('Failed to import contributor user', 14, 1);
		RETURN 10;
	END


	IF EXISTS (SELECT * FROM Sighting WHERE OrganismID = @organismID AND ContributorID = @contributorID AND Timestamp = @Timestamp)
	BEGIN
		PRINT('This sighting has already been imported, skipping');
		RETURN 0;
	END

	
	DECLARE @sightingID AS int;
	INSERT INTO Sighting(Timestamp, Location, OrganismID, ContributorID)
	VALUES(
		@Timestamp,
		geography::Point(@Latitude, @Longitude, 4326),
		@organismID,
		@contributorID
	);
	SET @sightingID = scope_identity();

	IF @ImageURL IS NOT NULL AND @ImageURL <> ''
	BEGIN
		INSERT INTO UserImage(URL, UploaderID)
		VALUES(@ImageURL, @contributorID);
		
		INSERT INTO SightingImage(SightingID, ImageURL)
		VALUES(@sightingID, @ImageURL);
	END
END

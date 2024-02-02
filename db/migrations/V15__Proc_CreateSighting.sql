CREATE PROCEDURE CreateSighting
(
	@UserID int,
	@OrganismID int,
	@Timestamp datetime2,
	@Latitude float,
	@Longitude float,
	@ImageURL varchar(512),
	@SightingID int output
)
AS
BEGIN

-- Validate parameters
IF @Timestamp IS NULL
BEGIN
	RAISERROR('Parameter @UserID is null', 14, 1);
	RETURN 1;
END

IF @Latitude IS NULL OR @Longitude IS NULL
BEGIN
	RAISERROR('Parameter @Latitude or @Longitude is null', 14, 1);
	RETURN 2;
END

-- Check if the user exists
IF NOT EXISTS (SELECT * FROM [User] WHERE ID = @UserID)
BEGIN
	RAISERROR('Invalid @UploaderID', 14, 1);
	RETURN 3;
END

-- Check if the organism exists
IF NOT EXISTS (SELECT * FROM Organism WHERE ID = @OrganismID)
BEGIN
	RAISERROR('Invalid @OrganismID', 14, 1);
	RETURN 4;
END

IF @ImageURL IS NOT NULL AND NOT EXISTS (SELECT * FROM UserImage WHERE URL = @ImageURL)
BEGIN
	RAISERROR('Invalid @ImageURL', 14, 1)
END


INSERT INTO Sighting(UserID, OrganismID, Timestamp, Location)
VALUES(@UserID, @OrganismID, @Timestamp, geography::Point(@Latitude, @Longitude, 4326));

SET @SightingID = SCOPE_IDENTITY();

IF @ImageURL IS NOT NULL
BEGIN
	INSERT INTO SightingImage(SightingID, ImageURL)
	VALUES(@SightingID, @ImageURL);
END

END
GO

GRANT EXECUTE ON CreateSighting TO AnimalTrackerApp

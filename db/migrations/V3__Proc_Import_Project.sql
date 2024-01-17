CREATE PROCEDURE ImportProject
(
	@Name varchar(50),
	@Description varchar(250),
	@StartedBy varchar(50),
	@Animals varchar(1000)
)
AS
BEGIN
	-- Validate parameters
	IF @Name IS NULL OR @Name = ''
	BEGIN
		RAISERROR('Parameter @Name is missing or empty', 14, 1);
		RETURN 1;
	END

	IF @Description IS NULL OR @Description = ''
	BEGIN
		RAISERROR('Parameter @Description is missing or empty', 14, 1);
		RETURN 2;
	END

	IF @StartedBy IS NULL OR @StartedBy = ''
	BEGIN
		RAISERROR('Parameter @StartedBy is missing or empty', 14, 1);
		RETURN 3;
	END

	-- Check if this project has already been imported
	IF EXISTS (SELECT * FROM Project WHERE Name = @Name)
	BEGIN
		PRINT('This project has already been imported, skipping');
		RETURN 0;
	END


	-- Import User
	DECLARE @ownerID AS int;
	DECLARE @status AS int;

	EXEC @status = ImportUser @Name = @StartedBy, @ID = @ownerID OUTPUT;
	IF @status <> 0
	BEGIN
		RAISERROR('Failed to import StartedBy user', 14, 1);
		RETURN 5;
	END


	-- Import project
	INSERT INTO Project(Name, Description)
	VALUES (@Name, @Description);
	DECLARE @projectID AS int = scope_identity();


	-- Import project owner
	INSERT INTO ProjectMember(UserID, ProjectID, IsOwner)
	VALUES(@ownerID, @projectID, 1);


	-- Import project animals
	INSERT INTO Organism(CommonName, ProjectID)
	SELECT TRIM(value), @projectID
	FROM STRING_SPLIT(@Animals, ',')
	WHERE TRIM(value) <> ''
END

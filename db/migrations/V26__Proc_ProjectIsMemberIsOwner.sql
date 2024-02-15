ALTER PROCEDURE GetAllProjects
(
	@UserID int = NULL
)
AS
BEGIN
	SELECT
		ID, [Name], [Description], CreationTimestamp, ClosedDate,
		CAST(CASE WHEN (i.ProjectID IS NOT NULL) THEN 1 ELSE 0 END AS BIT) AS IsInterested,
		CAST(CASE WHEN (m.ProjectID IS NOT NULL) THEN 1 ELSE 0 END AS BIT) AS IsMember,
		CAST(CASE WHEN (m.IsOwner IS NOT NULL AND m.IsOwner = 1) THEN 1 ELSE 0 END AS BIT) AS IsOwner
	FROM Project p
	LEFT JOIN Interested i ON i.ProjectID = p.ID AND i.UserID = @UserID
	LEFT JOIN ProjectMember m ON m.ProjectID = p.ID AND m.UserID = @UserID
END
GO

ALTER PROCEDURE RetrieveUserInterestedProjects
(
	@UserID int
)
AS
BEGIN
	SELECT
		ID, [Name], [Description], CreationTimestamp, ClosedDate,
		CAST(1 AS BIT) AS IsInterested,
		CAST(CASE WHEN (m.ProjectID IS NOT NULL) THEN 1 ELSE 0 END AS BIT) AS IsMember,
		CAST(CASE WHEN (m.IsOwner IS NOT NULL AND m.IsOwner = 1) THEN 1 ELSE 0 END AS BIT) AS IsOwner
	FROM Project p
	INNER JOIN Interested i on I.UserID = @UserID
	LEFT JOIN ProjectMember m ON m.ProjectID = p.ID AND m.UserID = @UserID
	WHERE p.ID = i.ProjectID
END
GO

ALTER PROCEDURE GetProject (
	@ProjectID int,
	@UserID int = NULL
)
AS
BEGIN
	SELECT
		ID, [Name], [Description], CreationTimestamp, ClosedDate,
		CAST(CASE WHEN (i.ProjectID IS NOT NULL) THEN 1 ELSE 0 END AS BIT) AS IsInterested,
		CAST(CASE WHEN (m.ProjectID IS NOT NULL) THEN 1 ELSE 0 END AS BIT) AS IsMember,
		CAST(CASE WHEN (m.IsOwner IS NOT NULL AND m.IsOwner = 1) THEN 1 ELSE 0 END AS BIT) AS IsOwner
	FROM Project p
	LEFT JOIN Interested i ON i.ProjectID = p.ID AND i.UserID = @UserID
	LEFT JOIN ProjectMember m ON m.ProjectID = p.ID AND m.UserID = @UserID
	WHERE p.ID = @ProjectID
END
GO

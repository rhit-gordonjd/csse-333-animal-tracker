CREATE PROCEDURE CloseProject
(
  @ProjectID int
)
AS
BEGIN
  IF NOT EXISTS (SELECT * FROM Project WHERE ID = @ProjectID)
  BEGIN
    RAISERROR('Project with ID @ProjectID does not exists', 14, 1);
    RETURN 1;
  END

  IF (SELECT ClosedDate FROM Project WHERE ID = @ProjectID) IS NOT NULL
  BEGIN
    RAISERROR('Project is already closed', 14, 1);
    RETURN 2;
  END

  UPDATE Project
  SET ClosedDate = SYSDATETIME()
  WHERE ID = @ProjectID;
END

GRANT EXECUTE ON CloseProject TO AnimalTrackerApp

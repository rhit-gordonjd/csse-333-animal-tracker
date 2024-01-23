CREATE TABLE [User] (
	ID int IDENTITY(1,1) PRIMARY KEY,
	Username varchar(20) NOT NULL UNIQUE,
	DisplayName varchar(50)
);

CREATE TABLE Project (
	ID int IDENTITY(1,1) PRIMARY KEY, 
	Name varchar(50) NOT NULL, 
	Description varchar(250), 
	CreationTimestamp datetime2 NOT NULL DEFAULT (SYSDATETIME()),
	ClosedDate datetime2
);

CREATE TABLE ProjectMember (
	UserID int REFERENCES [User](ID),
	ProjectID int REFERENCES Project(ID),
	IsOwner bit NOT NULL,
	PRIMARY KEY(UserID, ProjectID)
);

CREATE TABLE Interested (
	UserID int REFERENCES [User](ID),
	ProjectID int REFERENCES Project(ID), 
	PRIMARY KEY(UserID, ProjectID)
);

CREATE TABLE Organism (
	ID int IDENTITY(1,1) PRIMARY KEY, 
	IdentificationInstructions varchar(250),
	Description varchar(250), 
	CommonName varchar(150),
	ScientificName varchar(50), 
	ProjectID int NOT NULL REFERENCES Project(ID)
);

CREATE TABLE UserImage (
	URL varchar(512) PRIMARY KEY, 
	UploaderID int REFERENCES [User](ID)
);

CREATE TABLE Sighting (
	ID int IDENTITY(1,1) PRIMARY KEY NOT NULL,
	Timestamp datetime2 NOT NULL,
	Location geography NOT NULL,
	OrganismID int NOT NULL REFERENCES Organism(ID),
	UserID int NOT NULL REFERENCES [User](ID), 
);

CREATE TABLE SightingImage (
	SightingID int REFERENCES Sighting(ID),
	ImageURL varchar(512) REFERENCES UserImage(URL),
	PRIMARY KEY(SightingID, ImageURL)
);

CREATE TABLE SightingVerification (
	SightingID int REFERENCES Sighting(ID), 
	UserID int REFERENCES [User](ID), 
	Timestamp datetime2 NOT NULL DEFAULT (SYSDATETIME()), 
	PRIMARY KEY(SightingID, UserID)
);

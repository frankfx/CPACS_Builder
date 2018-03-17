create table Tipico (tnr int, team varchar(30), betPrediction varchar(30), winValue float, expenses float, pDate date, success boolean, description varchar(100), Primary Key(tnr)); 
create table TipicoConfig (accountBalance decimal);
create table TipicoSoccerwayIDs (id int, team varchar(30), Primary Key(id));



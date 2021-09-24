drop table Ecrivain;

create table Ecrivain(
  pid int primary key,
  nom varchar(30) not null,
  influence int references Ecrivain -- on en garde 1 seul pour simplifier
);


insert into Ecrivain values (201, 'Amin Maalouf',null);
insert into Ecrivain values (202, 'Marguerite Yourcenar',201);
insert into Ecrivain values (203, 'Albert Camus',201);
insert into Ecrivain values (181, 'Voltaire', 203);
insert into Ecrivain values (200, 'Virginia Woolf', 202);
insert into Ecrivain values (204, 'André Gide', 202);

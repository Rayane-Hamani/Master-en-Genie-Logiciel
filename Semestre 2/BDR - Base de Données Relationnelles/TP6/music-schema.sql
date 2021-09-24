-------------------------------------------
-- schéma du discaire : vente d''albums. --
-------------------------------------------

create table albums(
  al_id Numeric(4) constraint albums_pkey primary key,
  al_titre Varchar(100) not null,
  al_sortie Date
);

create table produits(
  prod_id Numeric(4) constraint produits_pkey primary key,
  prod_al Numeric(4) constraint produit_album_fkey references ALBUMS,
  prod_code_barre_ean Varchar(13) not null,
  prod_type_support Varchar(3) -- par exemple 'CD', 'DVD', ou 'Vi' pour vinyle
);

create table clients(
  cli_id Numeric(4) constraint clients_pkey primary key,
  cli_prenom Varchar(20) not null,
  cli_nom Varchar(20) not null
);

create table factures(
  fac_num Numeric(5) constraint factures_pkey primary key,
  fac_cli Numeric(4) constraint facture_client_fkey references CLIENTS,
  fac_date Date not null,
  fac_montant Numeric(6,2) default 0.0 not null
);


create table lignes_factures(
  lig_produit Numeric(4) constraint ligne_produit_fkey references PRODUITS,
  lig_facture Numeric(5) constraint ligne_facture_fkey references FACTURES,
  lig_prix_vente Numeric(4,2) default 0.0 not null,
  lig_quantite Numeric(2) default 1 not null,
  constraint ligne_facture_pkey primary key(lig_facture, lig_produit)
);

create table employes(
  emp_id Numeric(4) constraint employes_pkey primary key,
  emp_poste Varchar(20) not null,
  emp_prenom Varchar(20) not null,
  emp_nom Varchar(20) not null,
  emp_superieur_hierarchique Numeric(4) constraint employe_sup_fkey references EMPLOYES,
  emp_salaire float not null
);




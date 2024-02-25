# H2 database script

CREATE TABLE cliente (
  id integer,
  limite integer,
  saldo integer
);

INSERT INTO cliente VALUES (1,100000,0);

INSERT INTO cliente VALUES (2,80000,0);

INSERT INTO cliente VALUES (3,1000000,0);

INSERT INTO cliente VALUES (4,10000000,0);

INSERT INTO cliente VALUES (5,500000,0);

CREATE TABLE transacao (
 id integer,
 cliente_id integer,
 valor integer,
 tipo  char(1),
 descricao  char(10),
 realizadaEm char(70)
);

CREATE INDEX TRANSACAO_CLIENTE ON transacao(cliente_id);

CREATE INDEX TRANSACAO_REALIZADA ON transacao(realizadaEm);

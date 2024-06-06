CREATE TABLE permissao
(
	codigo bigint auto_increment,
	nome varchar(255) not null, 
	descricao varchar(255) not null,
	
	primary key(codigo)
)Engine=InnoDB default charset = utf8mb4;
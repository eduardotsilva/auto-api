CREATE TABLE usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    ativo BOOLEAN NOT NULL DEFAULT TRUE,
    data_criacao TIMESTAMP NOT NULL,
    ultimo_acesso TIMESTAMP NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE usuario_authorities (
    usuario_id BIGINT NOT NULL,
    authorities VARCHAR(50) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE veiculo (
    id BIGSERIAL PRIMARY KEY,
    placa VARCHAR(7) NOT NULL UNIQUE,
    chassi VARCHAR(17) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    ano INTEGER NOT NULL,
    usuario_id BIGINT NOT NULL,
    data_criacao TIMESTAMP NOT NULL,
    ultima_atualizacao TIMESTAMP NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE rastreador (
    id BIGSERIAL PRIMARY KEY,
    imei VARCHAR(15) NOT NULL UNIQUE,
    numero_serie VARCHAR(50) NOT NULL UNIQUE,
    modelo VARCHAR(50) NOT NULL,
    telefone VARCHAR(20),
    operadora VARCHAR(50),
    veiculo_id BIGINT NOT NULL UNIQUE,
    data_criacao TIMESTAMP NOT NULL,
    data_ativacao TIMESTAMP,
    ultima_atualizacao TIMESTAMP NOT NULL,
    FOREIGN KEY (veiculo_id) REFERENCES veiculo(id)
);

CREATE TABLE dados_localizacao (
    id BIGSERIAL PRIMARY KEY,
    rastreador_id BIGINT NOT NULL,
    latitude DECIMAL(10,8) NOT NULL,
    longitude DECIMAL(11,8) NOT NULL,
    velocidade DECIMAL(5,2),
    data_hora TIMESTAMP NOT NULL,
    FOREIGN KEY (rastreador_id) REFERENCES rastreador(id)
); 
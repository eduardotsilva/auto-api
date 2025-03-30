INSERT INTO usuario (id, nome, email, senha, ativo, data_criacao, ultimo_acesso, role)
VALUES (
    1,
    'Eduardo Silva',
    'eduardo.tsilva@hotmail.com',
    '$2a$10$Y50UaMFOxteibQEYLrwuHeehHYfcoafCopUazP12.rqB41bsolF5.',
    true,
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP,
    'ADMIN'
);

INSERT INTO usuario_authorities (usuario_id, authorities)
VALUES (1, 'ROLE_ADMIN'); 
#!/bin/bash

# Espera o RabbitMQ iniciar completamente
sleep 15

# Declara a exchange para distribuição das mensagens
rabbitmqadmin declare exchange name=exchange-localizacoes type=direct

# Declara a fila para dados em tempo real (com TTL de 5 minutos)
rabbitmqadmin declare queue name=fila-tempo-real durable=true arguments='{"x-message-ttl":300000}'

# Declara a fila para dados históricos (sem TTL)
rabbitmqadmin declare queue name=fila-historico durable=true

# Cria os bindings
rabbitmqadmin declare binding source=exchange-localizacoes destination=fila-tempo-real routing_key=atualizacao.localizacao
rabbitmqadmin declare binding source=exchange-localizacoes destination=fila-historico routing_key=atualizacao.localizacao

# Configura políticas de retenção
rabbitmqadmin declare policy name=politica-retencao pattern="fila-historico" definition='{"max-length":1000000,"max-length-bytes":1073741824}' apply-to=queues

echo "Configuração do RabbitMQ concluída!" 
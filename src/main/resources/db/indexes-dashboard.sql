CREATE INDEX IF NOT EXISTS idx_pedidos_status_data
    ON pedidos (status, data_pedido);

CREATE INDEX IF NOT EXISTS idx_pedidos_cliente_status_data
    ON pedidos (id_cliente, status, data_pedido);

CREATE INDEX IF NOT EXISTS idx_itens_pedido_pedido
    ON itens_pedido (id_pedido);

CREATE INDEX IF NOT EXISTS idx_itens_pedido_doce
    ON itens_pedido (id_doce);
-- Limpa tudo na ordem certa usando CASCADE para ignorar as dependências
DROP TABLE IF EXISTS tb_meta_projeto CASCADE;
DROP TABLE IF EXISTS tb_transacao CASCADE;
DROP TABLE IF EXISTS tb_orcamento CASCADE;
DROP TABLE IF EXISTS tb_categoria CASCADE;
DROP TABLE IF EXISTS tb_usuario CASCADE;

-- ============================================================
-- TABELA: USUARIO
-- ============================================================
CREATE TABLE tb_usuario (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    saldo_atual NUMERIC(19, 2) NOT NULL DEFAULT 0,
    score_financeiro INT NOT NULL DEFAULT 500,
    tom_ia VARCHAR(20) NOT NULL DEFAULT 'CONSELHEIRO',
    modo_escuro BOOLEAN NOT NULL DEFAULT TRUE,
    tutorial_concluido BOOLEAN NOT NULL DEFAULT FALSE,
    data_cadastro DATE NOT NULL DEFAULT CURRENT_DATE
);

-- ============================================================
-- TABELA: CATEGORIA (O "Grupo Maior" e as "Subcategorias")
-- ============================================================
CREATE TABLE tb_categoria (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    tipo VARCHAR(20) NOT NULL, -- RECEITA, DESPESA ou INVESTIMENTO
    usuario_id BIGINT NOT NULL,
    categoria_pai_id BIGINT NULL, -- Se for nulo, é Categoria Pai. Se tiver número, é Subcategoria!
    CONSTRAINT fk_categoria_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id) ON DELETE CASCADE,
    CONSTRAINT fk_categoria_pai FOREIGN KEY (categoria_pai_id) REFERENCES tb_categoria(id) ON DELETE CASCADE,
    CONSTRAINT uq_categoria_usuario_nome UNIQUE (usuario_id, nome, categoria_pai_id)
);

-- ============================================================
-- TABELA: ORÇAMENTO (O Limite Dinâmico Macro)
-- ============================================================
CREATE TABLE tb_orcamento (
    id BIGSERIAL PRIMARY KEY,
    mes INT NOT NULL CHECK (mes >= 1 AND mes <= 12),
    ano INT NOT NULL,
    valor_planejado NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    categoria_id BIGINT NOT NULL,
    subcategoria_id BIGINT NULL, -- Aponta para tb_categoria (onde estão as subcategorias)
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_orcamento_categoria FOREIGN KEY (categoria_id) REFERENCES tb_categoria(id) ON DELETE CASCADE,
    CONSTRAINT fk_orcamento_subcategoria FOREIGN KEY (subcategoria_id) REFERENCES tb_categoria(id) ON DELETE CASCADE,
    CONSTRAINT fk_orcamento_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id) ON DELETE CASCADE,
    CONSTRAINT uq_orcamento_mes_ano UNIQUE (categoria_id, mes, ano)
);

-- ============================================================
-- TABELA: TRANSACAO (Com Competência, Parcelamento e Grupos)
-- ============================================================
CREATE TABLE tb_transacao (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    valor NUMERIC(15, 2) NOT NULL,
    data_criacao DATE NOT NULL DEFAULT CURRENT_DATE,
    data_vencimento DATE NOT NULL,
    mes_competencia INT NOT NULL CHECK (mes_competencia >= 1 AND mes_competencia <= 12),
    ano_competencia INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDENTE',
    categoria_id BIGINT NOT NULL,
    subcategoria_id BIGINT NULL,  -- Aponta para tb_categoria (onde estão as subcategorias)
    usuario_id BIGINT NOT NULL,

    -- ==========================================
    -- NOVOS CAMPOS DE PARCELAMENTO AQUI!
    -- ==========================================
    parcela_atual INT DEFAULT 1,
    total_parcelas INT DEFAULT 1,
    grupo_id VARCHAR(36) NULL,

    CONSTRAINT fk_transacao_categoria FOREIGN KEY (categoria_id) REFERENCES tb_categoria(id) ON DELETE RESTRICT,
    CONSTRAINT fk_transacao_subcategoria FOREIGN KEY (subcategoria_id) REFERENCES tb_categoria(id) ON DELETE SET NULL,
    CONSTRAINT fk_transacao_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id) ON DELETE CASCADE
);

-- ============================================================
-- TABELA: META PROJETO
-- ============================================================
CREATE TABLE tb_meta_projeto (
    id BIGSERIAL PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    valor_alvo NUMERIC(15,2) NOT NULL,
    valor_atual NUMERIC(15,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(20) NOT NULL DEFAULT 'EM_ANDAMENTO',
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_meta_usuario FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id) ON DELETE CASCADE
);

-- ============================================================
-- ÍNDICES (Performance)
-- ============================================================
CREATE INDEX idx_usuario_email ON tb_usuario (email);
CREATE INDEX idx_categoria_usuario ON tb_categoria (usuario_id);
CREATE INDEX idx_categoria_pai ON tb_categoria (categoria_pai_id);
CREATE INDEX idx_orcamento_usuario ON tb_orcamento (usuario_id, mes, ano);
CREATE INDEX idx_transacao_usuario_competencia ON tb_transacao (usuario_id, ano_competencia, mes_competencia);
CREATE INDEX idx_transacao_categoria ON tb_transacao (categoria_id);
CREATE INDEX idx_meta_usuario ON tb_meta_projeto (usuario_id);

-- NOVO: Índice para a requisição Lazy Loading do Acordeão voar no Back-end!
CREATE INDEX idx_transacao_grupo ON tb_transacao (grupo_id);
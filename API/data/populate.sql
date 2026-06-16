-- ============================================================
-- POPULATE — Assistente Financeiro Pessoal (CPF)
-- Período de dados: 01/05/2026 – 31/07/2026
-- ============================================================
--   Usuário: Júlia Pimentel
--   E-mail:  julia@teste.com
--   Senha:   Teste@123
-- ============================================================

-- 1. Limpando a casa para evitar duplicidade
TRUNCATE TABLE tb_meta_projeto CASCADE;
TRUNCATE TABLE tb_transacao CASCADE;
TRUNCATE TABLE tb_orcamento CASCADE;
TRUNCATE TABLE tb_subcategoria CASCADE;
TRUNCATE TABLE tb_categoria CASCADE;
TRUNCATE TABLE tb_usuario CASCADE;

-- ============================================================
-- USUÁRIO
-- BCrypt de "Teste@123": $2a$10$9CPkJgDmYDysSLiLFaXSi.wk7Ze0UQXd5AZKgJK/w9o3rfujq4Tni
-- ============================================================
INSERT INTO tb_usuario (id, nome, email, senha, saldo_atual, score_financeiro, tom_ia, modo_escuro, tutorial_concluido, data_cadastro)
VALUES
  (1, 'Júlia Pimentel', 'julia@teste.com', '$2a$10$9CPkJgDmYDysSLiLFaXSi.wk7Ze0UQXd5AZKgJK/w9o3rfujq4Tni', 8450.50, 820, 'CONSELHEIRO', true, true, '2026-02-02');

-- ============================================================
-- CATEGORIAS (Grupos Maiores)
-- ============================================================
INSERT INTO tb_categoria (id, nome, tipo, usuario_id) VALUES
(1, 'Receitas', 'RECEITA', 1),
(2, 'Moradia', 'DESPESA', 1),
(3, 'Alimentação', 'DESPESA', 1),
(4, 'Transporte', 'DESPESA', 1),
(5, 'Lazer & Assinaturas', 'DESPESA', 1),
(6, 'Educação & Saúde', 'DESPESA', 1),
(7, 'Investimentos', 'INVESTIMENTO', 1);

-- ============================================================
-- SUBCATEGORIAS (Itens Específicos - Opcionais nas transações)
-- ============================================================
INSERT INTO tb_subcategoria (id, nome, categoria_id) VALUES
(1, 'Salário CWI', 1),
(2, 'Freelance Arquitetura', 1),
(3, 'Aluguel', 2),
(4, 'Internet & Energia', 2),
(5, 'Mercado', 3),
(6, 'iFood / Restaurante', 3),
(7, 'Uber', 4),
(8, 'Jogos Steam', 5),
(9, 'Faculdade (UniFacisa)', 6),
(10, 'Assinatura Dietbox', 6),
(11, 'Reserva Carro Novo', 7);

-- ============================================================
-- ORÇAMENTOS (O Planejamento de JUNHO/2026)
-- ============================================================
INSERT INTO tb_orcamento (id, mes, ano, valor_planejado, categoria_id, usuario_id) VALUES
(1, 6, 2026, 0.00, 1, 1),      -- Receitas (sem teto)
(2, 6, 2026, 2000.00, 2, 1),   -- Moradia
(3, 6, 2026, 1200.00, 3, 1),   -- Alimentação
(4, 6, 2026, 400.00, 4, 1),    -- Transporte
(5, 6, 2026, 600.00, 5, 1),    -- Lazer & Assinaturas
(6, 6, 2026, 800.00, 6, 1),    -- Educação & Saúde
(7, 6, 2026, 1500.00, 7, 1);   -- Investimentos (Meta de guardar pro carro)

-- ============================================================
-- TRANSAÇÕES (Agora com categoria_id e subcategoria_id)
-- ============================================================
INSERT INTO tb_transacao (id, descricao, valor, data_criacao, data_vencimento, mes_competencia, ano_competencia, status, categoria_id, subcategoria_id, usuario_id) VALUES

-- 🔴 MAIO/2026 (Mês passado — Tudo PAGO para compor histórico)
(1, 'Salário CWI', 11300.00, '2026-05-05', '2026-05-05', 5, 2026, 'PAGO', 1, 1, 1),
(2, 'Aluguel Apartamento', 1200.00, '2026-05-10', '2026-05-10', 5, 2026, 'PAGO', 2, 3, 1),
(3, 'Conta de Energia', 180.00, '2026-05-15', '2026-05-15', 5, 2026, 'PAGO', 2, 4, 1),
(4, 'Mercado Mensal', 650.00, '2026-05-12', '2026-05-12', 5, 2026, 'PAGO', 3, 5, 1),
(5, 'Mensalidade ADS', 550.00, '2026-05-20', '2026-05-20', 5, 2026, 'PAGO', 6, 9, 1),
(6, 'Projeto 3D Interiores', 3500.00, '2026-05-25', '2026-05-25', 5, 2026, 'PAGO', 1, 2, 1),

-- 🟢 JUNHO/2026 (Mês Atual — Mix de PAGO e PENDENTE para o Dashboard)
(7, 'Salário CWI', 11300.00, '2026-06-05', '2026-06-05', 6, 2026, 'PAGO', 1, 1, 1),
(8, 'Aluguel Apartamento', 1200.00, '2026-06-10', '2026-06-10', 6, 2026, 'PAGO', 2, 3, 1),
(9, 'Aporte Carro Novo', 1500.00, '2026-06-12', '2026-06-12', 6, 2026, 'PAGO', 7, 11, 1),
(10, 'Promoção Steam', 145.50, '2026-06-14', '2026-06-14', 6, 2026, 'PAGO', 5, 8, 1),
(11, 'Assinatura Dietbox', 49.90, '2026-06-15', '2026-06-15', 6, 2026, 'PAGO', 6, 10, 1),
(12, 'Mensalidade ADS', 550.00, '2026-06-20', '2026-06-20', 6, 2026, 'PENDENTE', 6, 9, 1),
(13, 'Conta de Energia', 210.00, '2026-06-22', '2026-06-22', 6, 2026, 'PENDENTE', 2, 4, 1),
(14, 'Uber Fim de Semana', 85.00, '2026-06-18', '2026-06-18', 6, 2026, 'PENDENTE', 4, 7, 1),
(15, 'iFood Pizza', 95.00, '2026-06-19', '2026-06-19', 6, 2026, 'PENDENTE', 3, 6, 1),
-- Exemplo de transação rápida sem subcategoria (Apenas lanche na rua):
(16, 'Café na Padaria', 15.00, '2026-06-16', '2026-06-16', 6, 2026, 'PAGO', 3, NULL, 1),

-- 🔵 JULHO/2026 (Mês que vem — Tudo PENDENTE)
(17, 'Salário CWI', 11300.00, '2026-07-05', '2026-07-05', 7, 2026, 'PENDENTE', 1, 1, 1),
(18, 'Aluguel Apartamento', 1200.00, '2026-07-10', '2026-07-10', 7, 2026, 'PENDENTE', 2, 3, 1);

-- ============================================================
-- METAS / PROJETOS (O Sonho do Carro)
-- ============================================================
INSERT INTO tb_meta_projeto (id, titulo, valor_alvo, valor_atual, status, usuario_id) VALUES
(1, 'Comprar meu Carro Novo', 55000.00, 18500.00, 'EM_ANDAMENTO', 1),
(2, 'Reserva de Emergência', 10000.00, 10000.00, 'CONCLUIDA', 1);

-- ============================================================
-- RESET SEQUENCES (Para evitar erros na hora de cadastrar novas coisas na tela)
-- ============================================================
SELECT setval('tb_usuario_id_seq', (SELECT MAX(id) FROM tb_usuario));
SELECT setval('tb_categoria_id_seq', (SELECT MAX(id) FROM tb_categoria));
SELECT setval('tb_subcategoria_id_seq', (SELECT MAX(id) FROM tb_subcategoria));
SELECT setval('tb_orcamento_id_seq', (SELECT MAX(id) FROM tb_orcamento));
SELECT setval('tb_transacao_id_seq', (SELECT MAX(id) FROM tb_transacao));
SELECT setval('tb_meta_projeto_id_seq', (SELECT MAX(id) FROM tb_meta_projeto));
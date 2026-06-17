package br.com.ControleFinanceiro.API.service;

import br.com.ControleFinanceiro.API.entity.Transacao;
import br.com.ControleFinanceiro.API.repository.TransacaoRepository;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtratoService {

    private final TransacaoRepository transacaoRepository;

    public byte[] gerarExtratoPdf(Long usuarioId, Integer mes, Integer ano) {
        List<Transacao> transacoes = transacaoRepository
                .findAllByUsuarioIdAndMesCompetenciaAndAnoCompetencia(usuarioId, mes, ano, Pageable.unpaged())
                .getContent();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            Font fonteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph titulo = new Paragraph("Extrato Mensal - " + mes + "/" + ano, fonteTitulo);
            titulo.setAlignment(Element.ALIGN_CENTER);
            titulo.setSpacingAfter(20);
            document.add(titulo);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3f, 2f, 2f, 2f, 2f});

            String[] cabecalhos = {"Descrição", "Tipo", "Categoria", "Data", "Valor (R$)"};
            Font fonteCabecalho = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
            for (String cabecalho : cabecalhos) {
                PdfPCell cell = new PdfPCell(new Phrase(cabecalho, fonteCabecalho));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setPadding(8);
                table.addCell(cell);
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Font fonteDados = FontFactory.getFont(FontFactory.HELVETICA, 10);

            for (Transacao t : transacoes) {
                table.addCell(new Phrase(t.getDescricao(), fonteDados));
                table.addCell(new Phrase(t.getCategoria().getTipo().name(), fonteDados));
                table.addCell(new Phrase(t.getCategoria().getNome(), fonteDados));
                table.addCell(new Phrase(t.getDataCriacao().format(formatter), fonteDados));
                table.addCell(new Phrase(t.getValor().toString(), fonteDados));
            }

            document.add(table);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar o PDF do extrato", e);
        }
    }
}

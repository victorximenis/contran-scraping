package com.victor;

import com.victor.db.DbConnection;
import com.victor.model.Resolucao;
import com.victor.scraping.ResolucaoScrap;

import java.text.SimpleDateFormat;
import java.util.List;

public class App 
{
    private static final String URL_CONTRAN = "https://infraestrutura.gov.br/resolucoes-contran.html";

    public static void main( String[] args )
    {
        List<Resolucao> resolucoes = ResolucaoScrap.buscarResolucoes(URL_CONTRAN);

        salvarResolucoes(resolucoes);
    }

    private static void salvarResolucoes(List<Resolucao> resolucoes) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DbConnection dbc = new DbConnection("PostgreSql","localhost","5432","contran","postgres","1234");

        try {
            dbc.conect();

            for (Resolucao r : resolucoes) {
                String query = "INSERT INTO resolucoes(numero, \"dataResolucao\", \"dataPublicacao\", assunto, situacao)\n VALUES ('?', ?, ?, '?', '?');";
                query = query.replaceFirst("\\?", r.getNumero());
                query = query.replaceFirst("\\?", (r.getDataResolucao() != null) ? "'" + sdf.format(r.getDataResolucao()) + "'" : "null");
                query = query.replaceFirst("\\?", (r.getDataPublicacao() != null) ? "'" + sdf.format(r.getDataPublicacao()) + "'" : "null");
                query = query.replaceFirst("\\?", r.getAssunto());
                query = query.replaceFirst("\\?", r.getSituacao());
                if (!dbc.execute(query)) {
                    System.out.println("INSERIDA RESOLUCAO " + r.getNumero());
                } else {
                    System.out.println("ERRO AO INSERIR RESOLUCAO " + r.getNumero());
                }
            }

        } catch (Exception e) {
            System.out.println("Algo deu errado com banco de dados");
        } finally {
            dbc.disconect();
        }
    }
}

package com.victor;

import com.victor.db.DbConnection;
import com.victor.model.Portaria;
import com.victor.model.Resolucao;
import com.victor.scraping.PortariaScrap;
import com.victor.scraping.ResolucaoScrap;

import java.text.SimpleDateFormat;
import java.util.List;

public class App 
{
    private static final String URL_CONTRAN_RESOLUCOES = "https://infraestrutura.gov.br/resolucoes-contran.html";
    private static final String URL_CONTRAN_PORTARIAS_2013 = "https://infraestrutura.gov.br/component/content/article/115-portal-denatran/8521-portarias-2013-denatran.html";

    public static void main( String[] args )
    {
        List<Resolucao> resolucoes = ResolucaoScrap.buscarResolucoes(URL_CONTRAN_RESOLUCOES);
        List<Portaria> portarias = PortariaScrap.buscarPortarias(URL_CONTRAN_PORTARIAS_2013);

        salvarResolucoes(resolucoes);
        salvarPortarias(portarias);
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

    private static void salvarPortarias(List<Portaria> portarias) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        DbConnection dbc = new DbConnection("PostgreSql","localhost","5432","contran","postgres","1234");

        try {
            dbc.conect();

            for (Portaria p : portarias) {
                String query = "INSERT INTO portarias(numero, \"dataPublicacao\", assunto, observacao)\n VALUES ('?', ?, '?', '?');";
                query = query.replaceFirst("\\?", p.getNumero());
                query = query.replaceFirst("\\?", (p.getDataPublicacao() != null) ? "'" + sdf.format(p.getDataPublicacao()) + "'" : "null");
                query = query.replaceFirst("\\?", p.getAssunto());
                query = query.replaceFirst("\\?", p.getObservacao());
                if (!dbc.execute(query)) {
                    System.out.println("INSERIDA PORTARIA " + p.getNumero());
                } else {
                    System.out.println("ERRO AO INSERIR PORTARIA " + p.getNumero());
                }
            }

        } catch (Exception e) {
            System.out.println("Algo deu errado com banco de dados");
        } finally {
            dbc.disconect();
        }
    }
}

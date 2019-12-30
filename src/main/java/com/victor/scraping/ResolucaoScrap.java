package com.victor.scraping;

import com.victor.model.Resolucao;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ResolucaoScrap {

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static List<Resolucao> buscarResolucoes(String url) {

        List<Resolucao> resolucoes = new ArrayList<Resolucao>();

        try {
            liberarSSL();

            Connection conn = Jsoup.connect(url);
            Document page = conn.get();

            Elements elements = page.getElementsByTag("table");
            Elements rows = elements.get(0).getElementsByTag("tr");

            for (int i = 1; i < rows.size(); i++) {

                Resolucao r = new Resolucao();
                Element row = rows.get(i);
                Elements columns = row.children();
                r.setNumero(columns.get(0).text());

                String strDataResolucao = columns.get(1).text().replaceAll("\\.", "/");
                try {
                    strDataResolucao = trataFormatoData(strDataResolucao);
                    Date dataResolucao = sdf.parse(strDataResolucao);
                    r.setDataResolucao(dataResolucao);
                } catch (ParseException e) {
                    r.setDataResolucao(null);
                }

                String strDataPublicacao = columns.get(2).text().replaceAll("\\.", "/");
                try {
                    strDataPublicacao = trataFormatoData(strDataPublicacao);
                    Date dataPublicacao = sdf.parse(strDataPublicacao);
                    r.setDataPublicacao(dataPublicacao);
                } catch (ParseException e) {
                    r.setDataPublicacao(null);
                }

                r.setAssunto(columns.get(3).text());
                r.setSituacao(columns.get(4).text());

                resolucoes.add(r);
            }

        } catch (IOException e) {
            System.out.println("Algo deu errado");
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Algo deu errado");
            e.printStackTrace();
        } catch (KeyManagementException e) {
            System.out.println("Algo deu errado");
            e.printStackTrace();
        }
        return resolucoes;
    }

    private static String trataFormatoData(String data) {
        if (data.length() >= 8 && data.substring(6,8).startsWith("9")) {
            data = data.substring(0, 6) + "19" + data.substring(6,8);
        } else if (data.length() >= 8 && !data.substring(6,8).startsWith("9")) {
            data = data.substring(0, 6) + "20" + data.substring(6,8);
        }
        return data;
    }

    private static void liberarSSL() throws NoSuchAlgorithmException, KeyManagementException {
        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

}

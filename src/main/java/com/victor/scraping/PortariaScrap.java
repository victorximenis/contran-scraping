package com.victor.scraping;

import com.victor.model.Portaria;
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

public class PortariaScrap {
    private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public static List<Portaria> buscarPortarias(String url) {

        List<Portaria> portarias = new ArrayList<Portaria>();

        try {
            liberarSSL();

            Connection conn = Jsoup.connect(url);
            Document page = conn.get();

            Elements elements = page.getElementsByTag("table");
            Elements rows = elements.get(0).getElementsByTag("tr");

            for (int i = 1; i < rows.size(); i++) {

                Portaria p = new Portaria();
                Element row = rows.get(i);
                Elements columns = row.children();
                p.setNumero(columns.get(0).text());

                String strDataPublicacao = columns.get(1).text().replaceAll("\\.", "/");
                try {
                    Date dataPublicacao = sdf.parse(strDataPublicacao);
                    p.setDataPublicacao(dataPublicacao);
                } catch (ParseException e) {
                    p.setDataPublicacao(null);
                }

                p.setAssunto(columns.get(2).text());
                p.setObservacao(columns.get(3).text());

                portarias.add(p);
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
        return portarias;
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

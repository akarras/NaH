package net.supernoobs.nah.data;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.supernoobs.nah.Nah;
import net.supernoobs.nah.Logger.LogLevel;

public class CardCastService {
	private final String cardCast = "https://api.cardcastgame.com/v1/";
	
	public CardCastDeck DownloadDeck(String code) {
		try {
			URL cardsUrl = new URL(cardCast+"decks/"+code+"/cards");
			URL deckInfoUrl = new URL(cardCast+"decks/"+code);
			/*BufferedReader reader = new BufferedReader(new InputStreamReader(deckUrl.openStream()));
			String deckJson = null;
			while ((deckJson = reader.readLine()) != null) {
				
			}*/
			CardCastDeck deck = new CardCastDeck();
			deck.parseCards(downloadViaStupidHackBecauseCardCast(cardsUrl));
			deck.parseInfo(downloadViaStupidHackBecauseCardCast(deckInfoUrl));
			return deck;
		} catch (Exception e) {
			Nah.plugin.nahLogger.Log(LogLevel.CRITICAL, "Failed to parse deck!");
		}
		return null;
		
	}
	
	private Reader downloadViaStupidHackBecauseCardCast(URL downloadURL) throws Exception {
		//This is absolutely not the right way to do this. And I don't like doing it.
		//However, where this plugin is distributed on many sites, it's not feasible
		//To have users manually add CardCast's cert to their VM
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                return null;
	            }
				@Override
				public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws CertificateException {
					// TODO Auto-generated method stub
					
				}
	        }
	    };
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, new java.security.SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		
		URLConnection con = downloadURL.openConnection();
		Reader reader = new InputStreamReader(con.getInputStream());
		
		return reader;
		
	}
}

package ta4jexamples.scraperPaths;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import eu.verdelhan.ta4j.General;

/**
 * <p>
 * Creates path to read google's current data about the stock.
 * Doesn't contain High/Low/Open prices.
 * @see <a href=http://finance.google.com/finance/info?q=IBM>Example Link Here</a>
 * @see <a href=http://finance.google.com/finance/info?q=IBM,AAPL>Fetch few stocks Example Link Here</a>
 * </p>
 * @deprecated - Doesn't show Open / High / Low prices.
 */
public class GoogleCurrJsonPath implements ScraperPath {

    private String path;

    /**
     * @param market - String - Stock Exchange provider.
     * @param symbol - String - Stock Symbol.
     * @return Path to Google current stock data (JSON parse).
     */
    public GoogleCurrJsonPath(String market , String symbol) {
        // When Market is not specified, send query without it
        if (market.equals(""))
            this.path = "http://finance.google.com/finance/info?q=" + symbol;
        // When Market is specified, send query with market
        else
            this.path = "http://finance.google.com/finance/info?q=" + market +":"+ symbol;
    }
    
    /**
     * @param symbol - String - Stock Symbol.
     * @return Path to Google current stock data (JSON parse).
     */
    public GoogleCurrJsonPath(String symbol) {
        this("" , symbol);
    }

    @Override
    public String getScraperPath() {
        return path;
    }

    //==================================================================================================
    
    public static void main(String args[]) throws IOException
    {
        ScraperPath newPath = new GoogleCurrJsonPath("AAPL");
        String URL_path     = newPath.getScraperPath();
        System.out.println(URL_path);
        
        InputStream stream = new URL(URL_path).openConnection().getInputStream();
        
        General.InputStreamReader(stream);

    }
    
    
}

package ta4jexamples.scraperPaths;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import eu.verdelhan.ta4j.General;

/**
 * <p>
 *  Current stock Data by Yahoo - Delay 15 MIN!                                 <br>
 *                                                                              <br>
 *  d1    - Most Recent Trade Date - MM/dd/yyyy                                 <br>
 *  t1    - Most Recent Trade Time (AM/PM) - hh:mma                             <br>
 *  o     - Most Recent Trade Day Open                                          <br>
 *  h     - Most Recent Trade Day High                                          <br>
 *  g     - Most Recent Trade Day Low                                           <br>
 *  l1    - Most Recent Trade Price                                             <br>
 *  d1    - Most Recent Trade Day Volume                                        <br>
 *  x     - Stock Exchange Provider (doesn't always work according to google)   <br>
 *  c8    - After Hours                                                         <br>
 *
 *
 *  @see <a href=http://finance.yahoo.com/d/quotes.csv?s=MSFT&f=d2t1ohgl1v>Example Link</a>         <br>
 *  @see <a href=http://finance.yahoo.com/d/quotes.csv?s=MSFT,AAPL&f=d2t1ohgl1v>Few Stocks 1 CMDExample Link</a>         <br>
 *  @see <a href=http://finance.yahoo.com/exchanges?bypass=true>Suffix Market Exchange data</a>     <br>
 *  @see <a href=https://greenido.wordpress.com/2009/12/22/work-like-a-pro-with-yahoo-finance-hidden-api>Letters Legend</a>
 * </p>
 */
public class YahooCurrCsvPath implements ScraperPath {

    private String path;

    /**
     * @param googExMark    String - The corresponding exchange market prefix used by google.
     * @return              String - Yahoo exchange market prefix.
     */
    // TODO: Fill the function with other exvhage markets.
    public String yahooExMark(String googExMark) {
        if (googExMark.toUpperCase().equals("TLV"))
            return "TA";
        if (googExMark.toUpperCase().equals("NYSE"))
            return "";
        if (googExMark.toUpperCase().equals("NASDAQ"))
            return "";
        else
            return googExMark;
    }

    /**
     * @param market String - Exchange market in which stock belongs to
     * @param symbol String - Stock Symbol.
     * @return Current data from Yahoo (CSV Parse).
     */
    public YahooCurrCsvPath(String market , String symbol) {
        String yahooMarketPrefix = yahooExMark(market);

        if (yahooMarketPrefix.equals("")) {
            this.path = "http://finance.yahoo.com/d/quotes.csv?s="
                        + symbol
                        + "&f=d1ohgl1vt1b2";
        }
        else {
            this.path =  "http://finance.yahoo.com/d/quotes.csv?s="
                    + symbol +"."+ yahooMarketPrefix
                    + "&f=d1ohgl1vt1b2";
        }
    }

    /**
     * @param symbol String - Stock Symbol.
     * @return Current data from Yahoo (CSV Parse).
     */
    public YahooCurrCsvPath(String symbol) {
         this("", symbol);
    }

    @Override
    public String getScraperPath(){
        return  path;
    }

    //======================================================================

    public static void main(String args[]) throws IOException {
        final String Symbol = "AAPL";
        ScraperPath loko = new YahooCurrCsvPath("NYSE", Symbol);

        System.out.println(loko.getScraperPath());
        String URL_path = loko.getScraperPath();

        InputStream stream = new URL(URL_path).openConnection().getInputStream();

        General.InputStreamReader(stream);
    }


}

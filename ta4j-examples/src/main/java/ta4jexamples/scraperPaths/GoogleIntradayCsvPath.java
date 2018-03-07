package ta4jexamples.scraperPaths;

import eu.verdelhan.ta4j.General;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * An example of the full path:
 * @see <a href=https://www.google.com/finance/getprices?i=60&p=10d&f=d,o,h,l,c,v&df=cpct&x=TLV&q=TEVA>Example Link Here</a>
 */
public class GoogleIntradayCsvPath implements ScraperPath {

    private String path;

    /**
     * Constant that represents the time intervals allowed by google intraday API. <br>
     * function returns 'seconds + 1' in order to get a valid 'Timestamp' from google.
     */
    public enum IntradayTimeInterval{
        OneM(60),
        TenM(600),
        QuarterH(900),
        HalfH(1800),
        OneH(3600),
        OneD(86400);

        private final Integer realSeconds;
        IntradayTimeInterval(Integer seconds) {
            this.realSeconds = seconds + 1;
        }
    }

    /**
     * @param market        String                - Stock Exchange provider
     * @param symbol        String                - Stock Symbol
     * @param periodDays    Integer               - Amount of days in which data will be sampled (maximum 50)
     * @param interval      IntradayTimeInterval  - time interval that suits google intraday API
     * @return Google intraDay data (CSV Parse)
     */
    public GoogleIntradayCsvPath(String market , String symbol , Integer periodDays, IntradayTimeInterval interval) {
        String tmp_path;
        String finalSymbol;
        String symbolUpper = symbol.toUpperCase();

        tmp_path = "https://www.google.com/finance/getprices?";

        // When Market is not specified, send query without it
        if (market.equals("")) {
            finalSymbol = "&q=" + symbolUpper;
        } // When Market is specified, send query with market
        else {
            String marketUpper = market.toUpperCase();
            finalSymbol = "&x=" + marketUpper + "&q=" + symbolUpper;
        }

        this.path = tmp_path
                    + "i="
                    + interval.realSeconds
                    + "&p="
                    + periodDays
                    + "d"     // 30 Days can be changed (maximum is 60)
                    + "&f="
                    + "d,o,h,l,c,v"         // Data is unix timestamp maximum is 50 days. (ignore "a" at the beginning). // date - open - high - low - close - volume
                //   + "&df=cpct"
                    + finalSymbol;
    }

    /**
     * @param symbol        String - Stock Symbol
     * @param periodDays    Integer - Amount of days in which data will be sampled (maximum 50)
     * @param interval      IntradayTimeInterval  - time interval that suits google intraday API
     * @return Google intraDay data (CSV Parse)
     */
    public GoogleIntradayCsvPath(String symbol , Integer periodDays, IntradayTimeInterval interval) {
        this("" ,  symbol , periodDays, interval);
    }

    @Override
    public String getScraperPath() {
        return path;
    }

    //==================================================================================================

    public static void main(String args[]) throws IOException {
        ScraperPath newPath = new GoogleIntradayCsvPath("nyse", "teva" , 5, IntradayTimeInterval.HalfH);
        String URL_path = newPath.getScraperPath();
        InputStream stream = new URL(URL_path).openConnection().getInputStream();
        General.InputStreamReader(stream);
    }

}

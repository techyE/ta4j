package ta4jexamples.scraperPaths;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import org.jfree.data.time.Month;
import org.joda.time.DateTime;

/**
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
 *  @see <a href=https://ichart.finance.yahoo.com/table.csv?s=TEVA&a=01&b=19&c=2010&d=01&e=19&f=2011>Example Link</a><br>
 *  @see <a href=https://chart.finance.yahoo.com/table.csv?s=AAPL&a=3&b=11&c=2016&d=3&e=11&f=2017&g=w&ignore=.csv">Another Example Link</a><br>
 *  @see <a href=https://finance.yahoo.com/exchanges?bypass=true>Suffix Market Exchange data</a>     <br>
 * If given data is in the future, yahoo will return recent data.
 */
public class YahooHistCsvPath implements ScraperPath {

    private String path;

    /**
     *  We use the 'Google Exchange Prefixes' as Yahoo's Exchange prefixes.
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
     * Constant that represents the time intervals allowed by google intraday API. <br>
     * function returns 'seconds + 1' in order to get a valid 'Timestamp' from google.
     */
    public enum YahooHistInterval {
        DAILY("d"),
        WEEKLY("w"),
        MONTHLY("m");
        private final String dataFormat;
        YahooHistInterval(String dataFormat) {
            this.dataFormat = dataFormat;
        }
    }

    /**
     * @param market        String - Exchange market in which stock belongs to
     * @param symbol        String - Stock Symbol
     * @param start_date    DateTime - Start sample date
     * @param end_date      DateTime - End sample date
     * @return Current data from Yahoo (CSV Parse).
     */
    public YahooHistCsvPath(String market , String symbol , DateTime start_date , DateTime end_date , YahooHistInterval interval) {
        //String tmp_path            = "https://ichart.finance.yahoo.com/table.csv?s=" + symbol; // ANOTHER OPTION - WASN'T TESTED ON WEEKLY DATA.
        String tmp_path              = "https://chart.finance.yahoo.com/table.csv?s=" + symbol;
        String yahooMarketPrefix     = yahooExMark(market);

        // Make Sure Dates are arranged in the right order.
        if (start_date.isAfter(end_date))
            throw new IllegalArgumentException("Start Date has to come before end date.");

        if (!yahooMarketPrefix.equals("")) {
            tmp_path = tmp_path +"."+ yahooMarketPrefix;
        }

        this.path = tmp_path
                    + "&a="                             // Start Date
                    + (start_date.monthOfYear().get()-1)
                    + "&b="
                    + start_date.dayOfMonth().get()
                    + "&c="
                    + start_date.year().get()
                    + "&d="                             // End Date
                    + (end_date.monthOfYear().get()-1)
                    + "&e="
                    + end_date.dayOfMonth().get()
                    + "&f="
                    + end_date.year().get()
                    + "&g="
                    + interval.dataFormat
                    + "&ignore=.csv";
    }

    /**
     * @param symbol        String - Stock Symbol.
     * @param start_date    DateTime - Start sample date
     * @param end_date      DateTime - End sample date
     * @return Current data from Yahoo (CSV Parse).
     */
    public YahooHistCsvPath(String symbol , DateTime start_date , DateTime end_date) {
        this("", symbol, start_date, end_date , YahooHistInterval.DAILY);
    }

    /**
     * @param symbol        String - Stock Symbol.
     * @param start_date    DateTime - Start sample date
     * @param end_date      DateTime - End sample date
     * @return Current data from Yahoo (CSV Parse).
     */
    public YahooHistCsvPath(String market, String symbol , DateTime start_date , DateTime end_date) {
        this(market, symbol, start_date, end_date , YahooHistInterval.DAILY);
    }

    /**
     * @param symbol        String - Stock Symbol.
     * @param start_date    DateTime - Start sample date
     * @param end_date      DateTime - End sample date
     * @return Current data from Yahoo (CSV Parse).
     */
    public YahooHistCsvPath(String symbol , DateTime start_date , DateTime end_date, YahooHistInterval interval) {
        this("", symbol, start_date, end_date , interval);
    }

    @Override
    public String getScraperPath(){
        return path;
    }

    //======================================================================

    public static void main(String args[]) throws IOException {
        DateTime start_date = new DateTime(2015 , Month.SEPTEMBER, 5, 0, 0);
        DateTime end_date   = new DateTime(2016 , Month.SEPTEMBER, 17, 0, 0);

        //ScraperPath YahooHist = new YahooHistCsvPath("TLV" , "SOHO" , start_date , new DateTime(), YahooHistInterval.WEEKLY);
        ScraperPath YahooHist = new YahooHistCsvPath("AAPL" , start_date , new DateTime(), YahooHistInterval.WEEKLY);

        String URL_path = YahooHist.getScraperPath();

        System.out.println(URL_path);
        InputStream stream = new URL(URL_path).openConnection().getInputStream();

//        generalFuncPack.InputStreamReader(stream);

        File myfile = new File("mosesShit.txt"); //[""], "filename");
        FileUtils.copyInputStreamToFile(stream, myfile);
    }


}

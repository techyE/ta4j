package ta4jexamples.scraperPaths;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

import org.jfree.data.time.Month;
import org.joda.time.DateTime;

import eu.verdelhan.ta4j.General;


/**
 * @see <a href=https://www.google.com/finance/historical?q=nasdaq:goog&startdate=Jan+1,+2012&enddate=Sep+1,+2013&output=csv>Example Link</a>
 */
public class GoogleHistCsvPath implements ScraperPath {

    private String path;

    /**
     * @param market        String - Stock Exchange provider
     * @param symbol        String - Stock Symbol
     * @param start_date    DateTime
     * @param end_date      DateTime
     * @return Google Historical data (CSV Parse)
     */
    public GoogleHistCsvPath(String market , String symbol , DateTime start_date , DateTime end_date)
    {
        String tmp_path;

        // Make Sure Dates are arranged in the right order.
        if (start_date.isAfter(end_date))
            throw new IllegalArgumentException("Start Date has to come before end date.");

        // When Market is not specified, send query without it
        if (market.equals(""))
            tmp_path = "https://www.google.com/finance/historical?q=" + symbol;
            // When Market is specified, send query with market
        else
            tmp_path = "https://www.google.com/finance/historical?q=" + market +":"+ symbol;

        this.path =  tmp_path
                        + "&startdate="
                        + start_date.monthOfYear().getAsShortText(Locale.ENGLISH)
                        + "+"
                        + start_date.getDayOfMonth()
                        + ",+"
                        + start_date.getYear()
                        + "&enddate="
                        + end_date.monthOfYear().getAsShortText(Locale.ENGLISH)
                        + "+"
                        + end_date.getDayOfMonth()
                        + ",+"
                        + end_date.getYear()
                        + "&output=csv";

    }

    /**
     * @param symbol        - String - Stock Symbol
     * @param start_date    - DateTime
     * @param end_date      - DateTime
     * @return Google Historical data (CSV Parse)
     */
    public GoogleHistCsvPath(String symbol , DateTime start_date , DateTime end_date) {
        this("" ,  symbol ,  start_date ,  end_date);
    }

    @Override
    public String getScraperPath(){
        return path;
    }

    //==================================================================================================

    public static void main(String args[]) throws IOException
    {
        DateTime start_date = new DateTime(2015 , Month.SEPTEMBER, 5, 0, 0);
        DateTime end_date   = new DateTime();

        ScraperPath pathObj = new GoogleHistCsvPath("nasdaq" , "aapl" , start_date , end_date);
        String URL_path = pathObj.getScraperPath();

        InputStream stream = new URL(URL_path).openConnection().getInputStream();

        General.InputStreamReader(stream);

    }

}
